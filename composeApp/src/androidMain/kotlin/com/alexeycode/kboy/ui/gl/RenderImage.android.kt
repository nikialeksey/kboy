package com.alexeycode.kboy.ui.gl

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLES20.GL_RGBA
import android.opengl.GLES20.GL_UNSIGNED_BYTE
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.viewinterop.AndroidView
import com.alexeycode.kboy.gb.ppu.Screen
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


val fragmentShader = """
    precision highp float;

    uniform sampler2D u_Texture;
    varying vec2 v_TexCoord;

    void main(void){
        gl_FragColor = texture2D(u_Texture, v_TexCoord);
    }
""".trimIndent()
val vertexShader = """
    uniform mat4 uVPMatrix;
    attribute vec4 a_Position;
    attribute vec2 a_TexCoord;
    varying vec2 v_TexCoord;

    void main(void)
    {
        gl_Position = uVPMatrix * a_Position;
        v_TexCoord = vec2(a_TexCoord.x, (1.0 - (a_TexCoord.y)));
    }
""".trimIndent()

private const val COORDINATES_PER_VERTEX = 2
private const val VERTEX_STRIDE: Int = COORDINATES_PER_VERTEX * 4

private val QUADRANT_COORDINATES = floatArrayOf(
    //x,    y
    -1f, 1f,
    -1f, -1f,
    1f, -1f,
    1f, 1f,
)

private val TEXTURE_COORDINATES = floatArrayOf(
    //x,    y
    0.0f, 1.0f,
    0.0f, 0.0f,
    1.0f, 0.0f,
    1.0f, 1.0f,
)

private val DRAW_ORDER = shortArrayOf(0, 1, 2, 0, 2, 3)

class ScreenGLRenderer(private val screen: () -> Screen) : GLSurfaceView.Renderer {

    private val TAG = "ScreenGLRenderer"

    private val vPMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)

    private var quadPositionHandle = -1
    private var texPositionHandle = -1
    private var textureUniformHandle: Int = -1
    private var viewProjectionMatrixHandle: Int = -1
    private var program: Int = -1
    private val textureUnit = IntArray(1)

    /**
     * Convert float array to float buffer
     */
    private val quadrantCoordinatesBuffer: FloatBuffer = ByteBuffer.allocateDirect(
        QUADRANT_COORDINATES.size * 4).run {
        order(ByteOrder.nativeOrder())
        asFloatBuffer().apply {
            put(QUADRANT_COORDINATES)
            position(0)
        }
    }

    /**
     * Convert float array to float buffer
     */
    private val textureCoordinatesBuffer: FloatBuffer =
        ByteBuffer.allocateDirect(TEXTURE_COORDINATES.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(TEXTURE_COORDINATES)
                position(0)
            }
        }

    /**
     * Convert short array to short buffer
     */
    private val drawOrderBuffer: ShortBuffer = ByteBuffer.allocateDirect(
        DRAW_ORDER.size * 2).run {
        order(ByteOrder.nativeOrder())
        asShortBuffer().apply {
            put(DRAW_ORDER)
            position(0)
        }
    }

    private lateinit var pixelBuffer: ByteBuffer

    /**
     * Called when surface is created or recreated
     */
    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        // Set GL clear color to white.
        GLES20.glClearColor(255f, 255f, 255f, 1.0f)

        // Prepare the rendering objects. This involves reading shaders, so may throw an IOException.
        try {
            val vertexShader =
                ShaderUtil.loadGLShader(
                    TAG,
                    GLES20.GL_VERTEX_SHADER,
                    vertexShader
                )
            val fragmentShader =
                ShaderUtil.loadGLShader(
                    TAG,
                    GLES20.GL_FRAGMENT_SHADER,
                    fragmentShader
                )

            program = GLES20.glCreateProgram()
            GLES20.glAttachShader(program, vertexShader)
            GLES20.glAttachShader(program, fragmentShader)
            GLES20.glLinkProgram(program)
            GLES20.glUseProgram(program)

            ShaderUtil.checkGLError(TAG, "Program creation")

            //Quadrant position handler
            quadPositionHandle = GLES20.glGetAttribLocation(program, "a_Position")

            //Texture position handler
            texPositionHandle = GLES20.glGetAttribLocation(program, "a_TexCoord")

            //Texture uniform handler
            textureUniformHandle = GLES20.glGetUniformLocation(program, "u_Texture")

            //View projection transformation matrix handler
            viewProjectionMatrixHandle = GLES20.glGetUniformLocation(program, "uVPMatrix")

            //Enable blend
            GLES20.glEnable(GLES20.GL_BLEND)
            //Uses to prevent transparent area to turn in black
            GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA)

            GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
            GLES20.glGenTextures(textureUnit.size, textureUnit, 0)
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureUnit[0])
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
            GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)

            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)
            pixelBuffer = ByteBuffer.allocateDirect(160 * 144 * 4).order(ByteOrder.nativeOrder())

            ShaderUtil.checkGLError(TAG, "Texture loading")
        } catch (e: IOException) {
        }
    }

    /**
     * Called after the surface is crated and whenever surface size changes
     */
    override fun onSurfaceChanged(p0: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        val ratio: Float = width.toFloat() / height.toFloat()

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)
    }

    /**
     * Uses for draw the current frame
     */
    override fun onDrawFrame(p0: GL10?) {
        // Use the GL clear color specified in onSurfaceCreated() to erase the GL surface.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        // Set the camera position (View matrix)
        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, 3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)

        // Calculate the projection and view transformation
        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

        try {
            GLES20.glUseProgram(program)

            // Attach the object texture.
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureUnit[0])
            pixelBuffer.rewind()
            pixelBuffer.put(screen().pixels())
            pixelBuffer.flip()
            GLES20.glTexImage2D(
                GLES20.GL_TEXTURE_2D, 0, GL_RGBA,
                160, 144, 0,
                GL_RGBA, GL_UNSIGNED_BYTE, pixelBuffer
            )
            GLES20.glUniform1i(textureUniformHandle, 0)

            // Pass the projection and view transformation to the shader
            GLES20.glUniformMatrix4fv(viewProjectionMatrixHandle, 1, false, vPMatrix, 0)

            //Pass quadrant position to shader
            GLES20.glVertexAttribPointer(
                quadPositionHandle,
                COORDINATES_PER_VERTEX,
                GLES20.GL_FLOAT,
                false,
                VERTEX_STRIDE,
                quadrantCoordinatesBuffer
            )

            //Pass texture position to shader
            GLES20.glVertexAttribPointer(
                texPositionHandle,
                COORDINATES_PER_VERTEX,
                GLES20.GL_FLOAT,
                false,
                VERTEX_STRIDE,
                textureCoordinatesBuffer
            )

            // Enable attribute handlers
            GLES20.glEnableVertexAttribArray(quadPositionHandle)
            GLES20.glEnableVertexAttribArray(texPositionHandle)

            //Draw shape
            GLES20.glDrawElements(
                GLES20.GL_TRIANGLES,
                DRAW_ORDER.size,
                GLES20.GL_UNSIGNED_SHORT,
                drawOrderBuffer
            )

            // Disable vertex arrays
            GLES20.glDisableVertexAttribArray(quadPositionHandle)
            GLES20.glDisableVertexAttribArray(texPositionHandle)
        } catch (t: Throwable) {
            // Avoid crashing the application due to unhandled exceptions.
        }
    }

}

class ScreenGLView(context: Context, screen: () -> Screen) : GLSurfaceView(context) {
    init {
        keepScreenOn = true // Keep screen awake till ARCore performs detection
        preserveEGLContextOnPause = true
        setEGLContextClientVersion(2)
        setEGLConfigChooser(8, 8, 8, 8, 16, 0)
        setRenderer(ScreenGLRenderer(screen))
        renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
    }
}

@Composable
actual fun RenderScreen(image: Screen) {
    var localImage by remember { mutableStateOf(image) }

    LaunchedEffect(image) {
        localImage = image
    }

    AndroidView(
        factory = { context ->
            ScreenGLView(context, {
                localImage
            })
        }
    )
}

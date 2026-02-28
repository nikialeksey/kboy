package com.alexeycode.kboy.ui.gl

import android.opengl.GLES20
import android.util.Log
import java.io.IOException
import java.util.TreeMap

/**
 * Shader helper functions.
 */
object ShaderUtil {
    @Throws(IOException::class)
    fun loadGLShader(
        tag: String?, type: Int, code: String, defineValuesMap: Map<String, Int>
    ): Int {
        // Load shader source code.

        // Prepend any #define values specified during this run.

        var code = code
        var defines = ""
        for ((key, value) in defineValuesMap) {
            defines += "#define $key $value\n"
        }
        code = defines + code

        // Compiles shader code.
        var shader = GLES20.glCreateShader(type)
        GLES20.glShaderSource(shader, code)
        GLES20.glCompileShader(shader)

        // Get the compilation status.
        val compileStatus = IntArray(1)
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compileStatus, 0)

        // If the compilation failed, delete the shader.
        if (compileStatus[0] == 0) {
            Log.e(
                tag,
                "Error compiling shader: " + GLES20.glGetShaderInfoLog(shader)
            )
            GLES20.glDeleteShader(shader)
            shader = 0
        }

        if (shader == 0) {
            throw RuntimeException("Error creating shader.")
        }

        return shader
    }

    /**
     * Overload of loadGLShader that assumes no additional #define values to add.
     */
    @Throws(IOException::class)
    fun loadGLShader(tag: String?, type: Int, code: String): Int {
        val emptyDefineValuesMap: Map<String, Int> = TreeMap()
        return loadGLShader(tag, type, code, emptyDefineValuesMap)
    }

    /**
     * Checks if we've had an error inside of OpenGL ES, and if so what that error is.
     *
     * @param label Label to report in case of error.
     * @throws RuntimeException If an OpenGL error is detected.
     */
    fun checkGLError(tag: String?, label: String) {
        var lastError = GLES20.GL_NO_ERROR
        // Drain the queue of all errors.
        var error: Int
        while ((GLES20.glGetError()
                .also { error = it }) != GLES20.GL_NO_ERROR
        ) {
            Log.e(tag, "$label: glError $error")
            lastError = error
        }
        if (lastError != GLES20.GL_NO_ERROR) {
            throw RuntimeException("$label: glError $lastError")
        }
    }
}
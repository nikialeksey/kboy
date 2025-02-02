package com.alexeycode.kboy.gl

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import com.alexeycode.kboy.gb.ppu.ImageBitmap
import org.lwjgl.opengl.GL.createCapabilities
import org.lwjgl.opengl.GL33.*
import org.lwjgl.opengl.awt.AWTGLCanvas
import org.lwjgl.opengl.awt.GLData
import org.lwjgl.system.MemoryUtil
import java.nio.ByteBuffer
import javax.swing.SwingUtilities


@Composable
actual fun RenderImage(image: ImageBitmap) {
    var localImage by remember { mutableStateOf(image) }

    LaunchedEffect(image) {
        localImage = image
    }

    SwingPanel(
        modifier = Modifier.fillMaxSize(),
        update = {
            val renderLoop: Runnable = object : Runnable {
                override fun run() {
                    if (!it.isValid) return
                    it.render()
                    SwingUtilities.invokeLater(this)
                }
            }
            SwingUtilities.invokeLater(renderLoop)
        },
        factory = {
            val data = GLData()

            val canvas = object : AWTGLCanvas(data) {
                private var textureId: Int = 0
                private lateinit var textureBuffer: ByteBuffer

                override fun initGL() {
                    createCapabilities()
                    glClearColor(0f, 0.0f, 0.0f, 1f)

                    textureId = glGenTextures()
                    glBindTexture(GL_TEXTURE_2D, textureId)

                    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
                    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)

                    glBindTexture(GL_TEXTURE_2D, 0)

                    textureBuffer = MemoryUtil.memAlloc(160 * 144 * 4)
                }

                override fun paintGL() {
                    glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

                    glEnable(GL_TEXTURE_2D)
                    glBindTexture(GL_TEXTURE_2D, textureId)
                    textureBuffer.rewind()
                    textureBuffer.put(localImage.pixels())
                    textureBuffer.flip()
                    glTexImage2D(
                        GL_TEXTURE_2D, 0, GL_RGBA8,
                        localImage.width(), localImage.height(), 0,
                        GL_RGBA, GL_UNSIGNED_BYTE, textureBuffer
                    )

                    glMatrixMode(GL_PROJECTION)
                    glPushMatrix()
                    glLoadIdentity()
                    glOrtho(0.0, width.toDouble(), 0.0, height.toDouble(), -1.0, 1.0)

                    glBegin(GL_QUADS)
                    glTexCoord2f(0f,0f); glVertex2f(0f,0f)
                    glTexCoord2f(0f,1f); glVertex2f(0f,height.toFloat())
                    glTexCoord2f(1f,1f); glVertex2f(width.toFloat(), height.toFloat())
                    glTexCoord2f(1f,0f); glVertex2f(width.toFloat(),0f)
                    glEnd()

                    glPopMatrix()
                    glBindTexture(GL_TEXTURE_2D, 0)
                    glDisable(GL_TEXTURE_2D)
                    swapBuffers()
                }

                override fun disposeCanvas() {
                    super.disposeCanvas()
                    MemoryUtil.memFree(textureBuffer)
                }
            }

            canvas
        }
    )
}

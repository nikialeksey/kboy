package com.alexeycode.kboy.ui.gl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.alexeycode.kboy.gb.ppu.Screen
import kotlinx.browser.document
import org.khronos.webgl.WebGLRenderingContext.Companion.RENDERER
import org.khronos.webgl.WebGLRenderingContext.Companion.SHADING_LANGUAGE_VERSION
import org.khronos.webgl.WebGLRenderingContext.Companion.VENDOR
import org.khronos.webgl.WebGLRenderingContext.Companion.VERSION
import org.khronos.webgl.WebGLRenderingContextBase
import org.w3c.dom.HTMLCanvasElement

@Composable
actual fun RenderScreen(image: Screen) {
    var localImage by remember { mutableStateOf(image) }

    LaunchedEffect(image) {
        localImage = image
    }

    val canvas by remember { mutableStateOf(document.createElement("canvas") as HTMLCanvasElement) }

    DisposableEffect(Unit) {
        val body = document.body!!
        canvas.width = 0
        canvas.height = 0
        canvas.style.apply {
            position = "absolute"
            left = "0px"
            top = "0px"
            background = "black"
        }
        body.appendChild(canvas)

        val gl2 = canvas.getContext("webgl2")
        if (gl2 == null) {
            println("WebGL2 is not supported in this browser")
        }

        val gl = gl2 as WebGLRenderingContextBase

        println("Renderer: " + gl.getParameter(RENDERER))
        println("Vendor: " + gl.getParameter(VENDOR))
        println("Version: " + gl.getParameter(VERSION))
        println("GLSL version: " + gl.getParameter(SHADING_LANGUAGE_VERSION))
        println("Supported extensions")

        val exts = gl.getSupportedExtensions()!!
        (0 until exts.length).map { exts[it] }.forEach {
            println(" - " + it.toString())
        }

        canvas.addEventListener("webglcontextlost") {
            it.preventDefault()
            println("WebGL context lost !")
        }

        document.addEventListener("keydown") {
            // TODO
        }
        document.addEventListener("keyup") {
            // TODO
        }

        onDispose {
            println("Destroying canvas")
            canvas.remove()
        }
    }
}
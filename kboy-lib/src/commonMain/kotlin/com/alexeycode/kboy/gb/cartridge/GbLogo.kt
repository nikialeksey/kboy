package com.alexeycode.kboy.gb.cartridge

//import com.badlogic.gdx.graphics.Pixmap
//import com.badlogic.gdx.graphics.Texture
//
//class GbLogo(
//    private val data: IntArray
//) : Logo {
//
//    private val pixmap: Pixmap by lazy {
//        Pixmap(96, 16, Pixmap.Format.RGB888).apply {
//            setColor(1f, 1f, 1f, 1f)
//            drawPixel(0, 0)
//            1.shr(2)
//            for (row in 0 until 2) {
//                for (column in 0 until 12) {
//                    val tileByte1 = data[row * 24 + column * 2]
//                    val tileByte2 = data[row * 24 + column * 2 + 1]
//                    drawNibble(this, tileByte1 shr 4, column * 4, row * 4)
//                    drawNibble(this, tileByte1, column * 4, row * 4 + 1)
//                    drawNibble(this, tileByte2 shr 4, column * 4, row * 4 + 2)
//                    drawNibble(this, tileByte2, column * 4, row * 4 + 3)
//                }
//            }
//        }
//    }
//
//    override fun asTexture(): Texture {
//        return Texture(pixmap)
//    }
//
//    private companion object {
//        private fun drawNibble(image: Pixmap, nibble: Int, x: Int, y: Int) {
//            var step = 0
//            for (shift in 3 downTo 0) {
//                if (nibble.and((1 shl shift)) != 0) {
//                    drawPixel(image, x + step, y)
//                }
//                step++
//            }
//        }
//
//        private fun drawPixel(image: Pixmap, x: Int, y: Int) {
//            image.drawPixel(x * 2, y * 2)
//            image.drawPixel(x * 2 + 1, y * 2)
//            image.drawPixel(x * 2, y * 2 + 1)
//            image.drawPixel(x * 2 + 1, y * 2 + 1)
//        }
//    }
//}
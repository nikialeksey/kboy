package com.alexeycode.kboy.gb.ppu

import com.alexeycode.kboy.gb.cpu.interrupts.Interrupts
import com.alexeycode.kboy.gb.mem.Memory
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

const val SCREEN_WIDTH = 160
const val SCREEN_HEIGHT = 144

const val RENDER_CYCLES = 70224
private const val LINE_CYCLES = 456
private const val OAM_SCAN_CYCLES = 80
private const val DRAWING_CYCLES = 172

class GbPpu(
    private val interrupts: Interrupts,
    private val memory: Memory,
    private val lcdStatus: LcdStatus,
    private val lcdControl: LcdControl,
    private val palette: Palette,
    private val background: Background,
    private val window: Window
) : Ppu {

    private val _screen = MutableSharedFlow<Screen>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    private val screen = _screen.asSharedFlow()
    private val gbScreen = GbScreen()
    private var cycles = 0
    private var disabledStateEntered = false

    private val spritesSize = 40 // TODO should be 10
    private val spriteX = IntArray(spritesSize)
    private val spriteY = IntArray(spritesSize)
    private val spriteIndex = IntArray(spritesSize)
    private val spriteAttrs = IntArray(spritesSize)
    private var spriteCount = 0

    private var oamScanStarted = false
    private var lineDrawn = false
    private var hBlankStarted = false
    private var vBlankStarted = false

    override fun tick(clockCycles: Int) {
        if (lcdControl.lcdEnable()) {
            disabledStateEntered = false

            cycles = (cycles + clockCycles) % RENDER_CYCLES
            val previousLy = lcdStatus.ly()
            lcdStatus.updateLy(cycles / LINE_CYCLES)
            if (previousLy == 153 && lcdStatus.ly() == 0) {
                vBlankStarted = false
                requestStatInterruptLycEqualsLy()
                _screen.tryEmit(gbScreen)
            }

            if (lcdStatus.ly() >= SCREEN_HEIGHT) {
                // VBlank
                if (!vBlankStarted) {
                    vBlankStarted = true

                    interrupts.requestVBlank()
                    requestStatInterrupt(4)
                    requestStatInterrupt(5)
                    requestStatInterruptLycEqualsLy()
                }
            } else {
                val lineStep = cycles % LINE_CYCLES
                if (lineStep < OAM_SCAN_CYCLES) {
                    // OAM scan
                    if (!oamScanStarted) {
                        oamScanStarted = true
                        requestStatInterrupt(5)
                        requestStatInterruptLycEqualsLy()
                        prepareSprites()
                    }
                    hBlankStarted = false
                } else if (lineStep >= OAM_SCAN_CYCLES && lineStep < OAM_SCAN_CYCLES + DRAWING_CYCLES) {
                    oamScanStarted = false
                    // drawing
                    if (!lineDrawn) {
                        if (lcdControl.bgAndWindowEnable()) {
                            renderBackground()
                        }

                        if (lcdControl.bgAndWindowEnable() && lcdControl.windowEnable() && lcdStatus.ly() >= window.wy()) {
                            renderWindow()
                        }

                        if (lcdControl.objEnable()) {
                            renderSprites()
                        }

                        lineDrawn = true
                    }
                } else {
                    lineDrawn = false
                    // horizontal blank
                    if (!hBlankStarted) {
                        requestStatInterrupt(3)
                        hBlankStarted = true
                    }
                }
            }
        } else {
            if (!disabledStateEntered) {
                disabledStateEntered = true
                gbScreen.fill(0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte())
                _screen.tryEmit(gbScreen)
            }
        }
    }

    private fun renderBackground() {
        // render background
        // 256 x 256 pixels, 32x32 tiles
        // every tile - 8x8 pixels
        // every tile - 16 bytes

        val y = lcdStatus.ly()
        val tilePixelY = (background.scy() + y) % 256
        val tileY = tilePixelY / 8
        val pixelY = tilePixelY % 8
        for (x in 0 until SCREEN_WIDTH) {
            val tilePixelX = (background.scx() + x) % 256
            val tileX = tilePixelX / 8
            val pixelX = tilePixelX % 8
            val tileAddress = lcdControl.bgTileMapStart() + tileY * 32 + tileX
            val tileNumber = memory.read8(tileAddress)
            val tileDataAddress =
                if (lcdControl.bgAndWindowTileDataSignedAddressing()) {
                    lcdControl.bgAndWindowTileDataStart() + tileNumber.toByte() * 16
                } else {
                    lcdControl.bgAndWindowTileDataStart() + tileNumber * 16
                }

            val lineAddress = tileDataAddress + pixelY * 2

            val pixel = readPixel(lineAddress, pixelX)
            palette.drawBgpPixel(gbScreen, x, y, pixel)
        }
    }

    private fun renderWindow() {
        val y = lcdStatus.ly()
        val tilePixelY = (y - window.wy())
        val tileY = tilePixelY / 8
        val pixelY = tilePixelY % 8

        for (x in 0 until SCREEN_WIDTH) {
            val tilePixelX = (x + 7 - window.wx())
            if (tilePixelX >= 0 && tilePixelX < SCREEN_WIDTH) {
                val tileX = tilePixelX / 8
                val pixelX = tilePixelX % 8

                val tileAddress = lcdControl.windowTileMapStart() + tileY * 32 + tileX
                val tileNumber = memory.read8(tileAddress)

                val tileDataAddress =
                    if (lcdControl.bgAndWindowTileDataSignedAddressing()) {
                        lcdControl.bgAndWindowTileDataStart() + tileNumber.toByte() * 16
                    } else {
                        lcdControl.bgAndWindowTileDataStart() + tileNumber * 16
                    }

                val lineAddress = tileDataAddress + pixelY * 2

                val pixel = readPixel(lineAddress, pixelX)
                palette.drawBgpPixel(gbScreen, x, y, pixel)
            }
        }
    }

    private fun renderSprites() {
        for (i in 0 until spriteCount) {
            val spriteY = spriteY[i]
            val spriteX = spriteX[i]
            val attrs = spriteAttrs[i]

            //
            // y = (sprite position on screen + 16)
            // ly - current y
            //
            // (ly - y) - y position inside sprite
            //
            var yInSprite = if (attrs.and(1.shl(6)) == 0) {
                lcdStatus.ly() + 16 - spriteY
            } else {
                lcdControl.objHeight() - (lcdStatus.ly() + 16 - spriteY) - 1
            }

            val tileNumber = if (lcdControl.objHeight() == 16) {
                if (yInSprite > 8) {
                    yInSprite -= 8
                    (spriteIndex[i].and(0xFE) + 1).and(0xFF)
                } else {
                    spriteIndex[i].and(0xFE)
                }
            } else {
                spriteIndex[i]
            }
            val spriteDataAddress = 0x8000 + tileNumber * 16
            val lineAddress = spriteDataAddress + yInSprite * 2
            val a = memory.read8(lineAddress)
            val b = memory.read8(lineAddress + 1)

            for (x in 0 until 8) {
                val xInSprite = if (attrs.and(1.shl(5)) == 0) {
                    x
                } else {
                    8 - x - 1
                }
                val screenX = spriteX - 8 + x
                if (screenX >= 0 && screenX < SCREEN_WIDTH) {
                    val pixel = readPixel(a, b, xInSprite)

                    if (attrs.and(1.shl(4)) == 0) {
                        palette.drawObp0Pixel(gbScreen, screenX, lcdStatus.ly(), pixel)
                    } else {
                        palette.drawObp1Pixel(gbScreen, screenX, lcdStatus.ly(), pixel)
                    }
                }
            }
        }
    }

    private fun readPixel(lineAddress: Int, pixelX: Int): Int {
        val a = memory.read8(lineAddress)
        val b = memory.read8(lineAddress + 1)

        return readPixel(a, b, pixelX)
    }

    private fun readPixel(a: Int, b: Int, pixelX: Int): Int {
        val bit = (8 - pixelX - 1)
        val pixelLow = if (a.and(1.shl(bit)) != 0) 1 else 0
        val pixelHigh = if (b.and(1.shl(bit)) != 0) 1 else 0
        val pixel = pixelHigh.shl(1) + pixelLow
        return pixel
    }

    private fun prepareSprites() {
        spriteCount = 0
        val oamAddress = 0xFE00

        for (i in 0 until 40) {
            val y = memory.read8(oamAddress + 4 * i)
            val x = memory.read8(oamAddress + 4 * i + 1)
            val index = memory.read8(oamAddress + 4 * i + 2)
            val attrs = memory.read8(oamAddress + 4 * i + 3)
            val yRange = y until (y + lcdControl.objHeight())
            if ((x != 0 && x < 168) && (lcdStatus.ly() + 16) in yRange) {
                spriteX[spriteCount] = x
                spriteY[spriteCount] = y
                spriteIndex[spriteCount] = index
                spriteAttrs[spriteCount] = attrs
                spriteCount++
            }
        }
    }

    private fun requestStatInterruptLycEqualsLy() {
        if (lcdStatus.ly() == lcdStatus.lyc()) {
            requestStatInterrupt(6)
        }
    }

    private fun requestStatInterrupt(statBit: Int) {
        if (lcdStatus.stat().and(1.shl(statBit)) != 0) {
            interrupts.requestStat()
        }
    }

    override fun screen(): SharedFlow<Screen> {
        return screen
    }

}
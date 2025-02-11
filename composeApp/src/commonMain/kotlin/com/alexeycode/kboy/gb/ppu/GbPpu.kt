package com.alexeycode.kboy.gb.ppu

import com.alexeycode.kboy.gb.cpu.interrupts.Interrupts
import com.alexeycode.kboy.gb.mem.Memory
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

const val SCREEN_WIDTH = 160
const val SCREEN_HEIGHT = 144

private const val RENDER_CYCLES = 70224
private const val LINE_CYCLES = 456
private const val OAM_SCAN_CYCLES = 80

class GbPpu(
    private val interrupts: Interrupts,
    private val memory: Memory,
    private val lcdStatus: LcdStatus,
    private val lcdControl: LcdControl,
    private val background: Background,
    private val window: Window
) : Ppu {

    private val _screen = MutableSharedFlow<ImageBitmap>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    private val screen = _screen.asSharedFlow()
    private val gbScreen = GbImageBitmap()
    private var cycles = 0 // 70224 clock cycles
    private var disabledStateEntered = false

    private val spritesSize = 40 // TODO should be 10
    private val spriteX = IntArray(spritesSize)
    private val spriteY = IntArray(spritesSize)
    private val spriteIndex = IntArray(spritesSize)
    private val spriteAttrs = IntArray(spritesSize)
    private var spritePointer = 0

    private var oamScanStarted = false
    private var lineDrawn = false
    private var vBlankStarted = false

    override fun tick(clockCycles: Int) {
        if (lcdControl.lcdEnable()) {
            disabledStateEntered = false

            cycles = (cycles + clockCycles) % RENDER_CYCLES
            val previousLy = lcdStatus.ly()
            lcdStatus.updateLy(cycles / LINE_CYCLES)
            if (previousLy == 153 && lcdStatus.ly() == 0) {
                requestStatInterruptLycEqualsLy()
                _screen.tryEmit(gbScreen)
            }

            if (lcdStatus.ly() >= SCREEN_HEIGHT) {
                // VBlank
                if (!vBlankStarted) {
                    interrupts.requestVBlank()
                    requestStatInterrupt(4)
                    requestStatInterrupt(5)
                    requestStatInterruptLycEqualsLy()
                    vBlankStarted = true
                }
            } else {
                vBlankStarted = false

                val lineStep = cycles % LINE_CYCLES
                if (lineStep < OAM_SCAN_CYCLES) {
                    // OAM scan
                    if (!oamScanStarted) {
                        oamScanStarted = true
                        requestStatInterrupt(5)
                        requestStatInterruptLycEqualsLy()
                        prepareSprites()
                    }
                    lineDrawn = false
                } else {
                    oamScanStarted = false
                    // drawing + horizontal blank
                    if (!lineDrawn) {
                        requestStatInterrupt(3)
                        if (lcdControl.bgAndWindowEnable()) {
                            renderBackground()
                        }

                        lineDrawn = true
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

            val a = memory.read8(lineAddress)
            val b = memory.read8(lineAddress + 1)

            val bit = (8 - pixelX - 1)
            val pixelLow = if (a.and(1.shl(bit)) != 0) 1 else 0
            val pixelHigh = if (b.and(1.shl(bit)) != 0) 1 else 0
            val pixel = pixelHigh.shl(1) + pixelLow
            // 0 1 2 3 4 5 6 7 8 9 A B C D E F
            //     1     1     1     1
            if (pixel == 0) {
                gbScreen.setPixel(
                    x,
                    y,
                    0xEE.toByte(),
                    0xEE.toByte(),
                    0xEE.toByte()
                )
            } else if (pixel == 1) {
                gbScreen.setPixel(
                    x,
                    y,
                    0xAA.toByte(),
                    0xAA.toByte(),
                    0xAA.toByte()
                )
            } else if (pixel == 2) {
                gbScreen.setPixel(
                    x,
                    y,
                    0x66.toByte(),
                    0x66.toByte(),
                    0x66.toByte()
                )
            } else if (pixel == 3) {
                gbScreen.setPixel(
                    x,
                    y,
                    0x22.toByte(),
                    0x22.toByte(),
                    0x22.toByte()
                )
            }
        }
    }

    private fun prepareSprites() {
        spritePointer = 0
        val oamAddress = 0xFE00

        for (i in 0 until 40) {
            val y = memory.read8(oamAddress + 4 * i)
            val x = memory.read8(oamAddress + 4 * i + 1)
            val index = memory.read8(oamAddress + 4 * i + 2)
            val attrs = memory.read8(oamAddress + 4 * i + 3)
            if ((lcdStatus.ly() + 16) in y until (y + lcdControl.objHeight())) {
                spriteX[spritePointer] = x
                spriteY[spritePointer] = y
                spriteIndex[spritePointer] = index
                spriteAttrs[spritePointer] = attrs
                spritePointer++
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

    override fun screen(): SharedFlow<ImageBitmap> {
        return screen
    }

}
package com.alexeycode.kboy.gb.mem

class GbDmaTransfer(
    private val memory: Memory,
    private val dma: Dma
) : DmaTransfer {

    private var isInProgress = false
    private var progressCycles = 0

    override fun tick(clockCycles: Int) {
        if (!isInProgress && dma.isTransferInProgress()) {
            isInProgress = true
            progressCycles = 0
        } else if (isInProgress) {
            progressCycles += clockCycles

            if (progressCycles >= 640) {
                val startAddress = dma.value().shl(8)
                for (i in 0 until 0xA0) {
                    memory.write8(0xFE00 + i, memory.read8(startAddress + i))
                }
                isInProgress = false
                dma.transferFinished()
            }
        }
    }
}
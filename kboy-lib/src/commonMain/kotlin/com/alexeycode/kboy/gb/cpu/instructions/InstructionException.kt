package com.alexeycode.kboy.gb.cpu.instructions

class InstructionException : RuntimeException {

    constructor(message: String, cause: Throwable) : super(message, cause)
}

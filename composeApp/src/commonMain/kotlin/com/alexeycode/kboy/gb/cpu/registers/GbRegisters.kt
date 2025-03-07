package com.alexeycode.kboy.gb.cpu.registers

import com.alexeycode.kboy.gb.cpu.registers.flags.FlagRegister
import com.alexeycode.kboy.gb.cpu.registers.flags.SimpleFlagRegister

class GbRegisters : Registers {

    private val a: Register
    private val f: Register
    private val b: Register
    private val c: Register
    private val d: Register
    private val e: Register
    private val h: Register
    private val l: Register
    private val af: Register
    private val bc: Register
    private val de: Register
    private val hl: Register
    private val sp: Register
    private val pc: Register
    private val flag: FlagRegister

    constructor() : this(
        a = Register8(0x01),
        f = Register8Hi(Register8(0xB0)),
        b = Register8(0x00),
        c = Register8(0x13),
        d = Register8(0x00),
        e = Register8(0xD8),
        h = Register8(0x01),
        l = Register8(0x4D),
        sp = Register16(0xFFFE),
        pc = Register16(0x0100)
    )

    constructor(
        a: Register,
        f: Register,
        b: Register,
        c: Register,
        d: Register,
        e: Register,
        h: Register,
        l: Register,
        sp: Register,
        pc: Register,
    ) : this(
        a = a,
        f = f,
        b = b,
        c = c,
        d = d,
        e = e,
        h = h,
        l = l,
        af = MergedRegister(a, f),
        bc = MergedRegister(b, c),
        de = MergedRegister(d, e),
        hl = MergedRegister(h, l),
        sp = sp,
        pc = pc,
        flag = SimpleFlagRegister(f)
    )

    constructor(
        a: Register,
        f: Register,
        b: Register,
        c: Register,
        d: Register,
        e: Register,
        h: Register,
        l: Register,
        af: Register,
        bc: Register,
        de: Register,
        hl: Register,
        sp: Register,
        pc: Register,
        flag: FlagRegister
    ) {
        this.a = a
        this.f = f
        this.b = b
        this.c = c
        this.d = d
        this.e = e
        this.h = h
        this.l = l
        this.af = af
        this.bc = bc
        this.de = de
        this.hl = hl
        this.sp = sp
        this.pc = pc
        this.flag = flag
    }

    override fun a(): Register {
        return this.a
    }

    override fun f(): Register {
        return this.f
    }

    override fun b(): Register {
        return this.b
    }

    override fun c(): Register {
        return this.c
    }

    override fun d(): Register {
        return this.d
    }

    override fun e(): Register {
        return this.e
    }

    override fun h(): Register {
        return this.h
    }

    override fun l(): Register {
        return this.l
    }

    override fun af(): Register {
        return this.af
    }

    override fun bc(): Register {
        return this.bc
    }

    override fun de(): Register {
        return this.de
    }

    override fun hl(): Register {
        return this.hl
    }

    override fun sp(): Register {
        return this.sp
    }

    override fun pc(): Register {
        return this.pc
    }

    override fun flag(): FlagRegister {
        return this.flag
    }

    override fun byName(name: String): Register {
        val n = name
        return if (n.length == 1) {
            if (n[0] == 'A') {
                a
            } else if (n[0] == 'F') {
                f
            } else if (n[0] == 'B') {
                b
            } else if (n[0] == 'C') {
                c
            } else if (n[0] == 'D') {
                d
            } else if (n[0] == 'E') {
                e
            } else if (n[0] == 'H') {
                h
            } else if (n[0] == 'L') {
                l
            } else {
                throw IllegalArgumentException("Unknown register with name '$name'")
            }
        } else {
            if (n[0] == 'A'/*"AF"*/) {
                af
            } else if (n[0] == 'B'/*"BC"*/) {
                bc
            } else if (n[0] == 'D'/*"DE"*/) {
                de
            } else if (n[0] == 'H'/*"HL"*/) {
                hl
            } else if (n[0] == 'S'/*"SP"*/) {
                sp
            } else if (n[0] == 'P'/*"PC"*/) {
                pc
            } else {
                throw IllegalArgumentException("Unknown register with name '$name'")
            }
        }
    }
}
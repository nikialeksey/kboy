package com.alexeycode.kboy.gb.joypad

interface Joypad {
    fun get(): Int
    fun update(value: Int)

    fun right(): Button
    fun left(): Button
    fun up(): Button
    fun down(): Button

    fun a(): Button
    fun b(): Button
    fun select(): Button
    fun start(): Button
}
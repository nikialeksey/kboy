package com.alexeycode.kboy.log

interface Log {
    fun v(tag: Tag, message: String, vararg args: Any)
    fun d(tag: Tag, message: String, vararg args: Any)
    fun i(tag: Tag, message: String, vararg args: Any)
    fun w(tag: Tag, message: String, vararg args: Any)
    fun w(tag: Tag, throwable: Throwable, message: String, vararg args: Any)
    fun e(tag: Tag, message: String, vararg args: Any)
    fun e(tag: Tag, throwable: Throwable, message: String, vararg args: Any)
}
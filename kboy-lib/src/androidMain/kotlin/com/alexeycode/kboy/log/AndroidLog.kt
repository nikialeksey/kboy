package com.alexeycode.kboy.log

class AndroidLog : Log {
    override fun v(
        tag: Tag,
        message: String,
        vararg args: Any
    ) {
        android.util.Log.v(tag.toString(), message.format(*args))
    }

    override fun d(
        tag: Tag,
        message: String,
        vararg args: Any
    ) {
        android.util.Log.d(tag.toString(), message.format(*args))
    }

    override fun i(
        tag: Tag,
        message: String,
        vararg args: Any
    ) {
        android.util.Log.i(tag.toString(), message.format(*args))
    }

    override fun w(
        tag: Tag,
        message: String,
        vararg args: Any
    ) {
        android.util.Log.w(tag.toString(), message.format(*args))
    }

    override fun w(
        tag: Tag,
        throwable: Throwable,
        message: String,
        vararg args: Any
    ) {
        android.util.Log.w(tag.toString(), message.format(*args), throwable)
    }

    override fun e(
        tag: Tag,
        message: String,
        vararg args: Any
    ) {
        android.util.Log.e(tag.toString(), message.format(*args))
    }

    override fun e(
        tag: Tag,
        throwable: Throwable,
        message: String,
        vararg args: Any
    ) {
        android.util.Log.e(tag.toString(), message.format(*args), throwable)
    }
}

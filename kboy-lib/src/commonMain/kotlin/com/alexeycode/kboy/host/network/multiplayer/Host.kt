package com.alexeycode.kboy.host.network.multiplayer

const val IS_MAIN_QUERY = "IS_MAIN"
const val IS_MAIN_ANSWER_YES = "YES"
const val IS_MAIN_ANSWER_NO = "NO"

const val I_AM_MAIN_QUERY = "I_AM_MAIN"
const val I_AM_MAIN_ANSWER_OK = "OK"

interface Host {

    suspend fun isMain(): Boolean

    suspend fun iAmMain()

    fun close()
}
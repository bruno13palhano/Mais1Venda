package com.bruno13palhano.data.log

internal interface AppLog {
    fun logInfo(tag: String, message: String)
    fun logError(tag: String, message: String)
}

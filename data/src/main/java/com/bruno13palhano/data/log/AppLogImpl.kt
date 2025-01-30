package com.bruno13palhano.data.log

import android.util.Log

internal class AppLogImpl : AppLog {
    override fun logInfo(tag: String, message: String) {
        Log.i(tag, message)
    }

    override fun logError(tag: String, message: String) {
        Log.e(tag, message)
    }
}

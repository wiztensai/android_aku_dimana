package com.jetwiz.akudimana.base

import android.app.Application
import timber.log.Timber

class BaseApp: Application() {

    var indices = mutableListOf<Int>()

    companion object {
        /**
         * $radius$filterType. fungsinya untuk flagging apakah setingan filter di DF_Result sama atau tidak dengan sebelumnya?
         * ini berkaitan dengan algoritma, random no repeat place
         */
        var rKey = ""
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

    fun randomNumberNoRepeat(size:Int):Int {
        // kalo randomnya sudah habis, maka direset
        if (indices.isEmpty()) {
            return initRandom(size)
        }

        val rnd = indices.random()
        indices.remove(rnd)

        return rnd
    }

    fun initRandom(size:Int):Int {
        for (c in 0 until size) {
            indices.add(c)
        }

        val rnd = indices.random()
        indices.remove(rnd)

        return rnd
    }
}
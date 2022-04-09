package com.project.coroutines

import kotlinx.coroutines.delay

class Stub {
    suspend fun count() {
        for (i in 1..10) {
            delay(1000)
            println(i)
        }
    }
}
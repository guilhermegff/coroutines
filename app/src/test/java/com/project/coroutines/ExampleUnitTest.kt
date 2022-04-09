package com.project.coroutines

import kotlinx.coroutines.*
import org.junit.Test

import org.junit.Assert.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    @Test
    fun addition_isCorrect() {
        val stub = Stub()
        runBlocking { stub.count() }
        //println(a)
    }
}
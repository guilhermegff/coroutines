package com.project.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity2 : AppCompatActivity() {
    private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate)
    private val secondCoroutineScope = CoroutineScope(Dispatchers.Default)

    private lateinit var button : Button
    private lateinit var textView : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        button = findViewById(R.id.button)
        textView = findViewById(R.id.textView)
    }

    override fun onResume() {
        super.onResume()
        button.setOnClickListener {
            println("button click")
            button.isEnabled = false
            executeBenchmark()
            button.isEnabled = true
        }
    }

    override fun onStop() {
        super.onStop()
        println("onStop")
    }

    private fun executeBenchmark() {
        val benchmarkDurationSeconds = 5

        updateRemainingTime(benchmarkDurationSeconds)

        coroutineScope.launch(Dispatchers.Default) {
            logThreadInfo("benchmark started")

            val stopTimeNano = System.nanoTime() + benchmarkDurationSeconds * 1_000_000_000L

            var iterationsCount: Long = 0
            while(System.nanoTime() < stopTimeNano) {
                iterationsCount++
            }

            logThreadInfo("benchmark completed")

            //withContext(Dispatchers.Main) {
                Toast.makeText(applicationContext, "$iterationsCount", Toast.LENGTH_SHORT).show()
           // }

        }
    }

    private fun updateRemainingTime(remainingTimeSeconds: Int) {
        logThreadInfo("updateRemainingTime: $remainingTimeSeconds seconds")

        if(remainingTimeSeconds > 0) {
            textView.text = "$remainingTimeSeconds"
            Handler(Looper.getMainLooper()).postDelayed({
                updateRemainingTime(remainingTimeSeconds - 1)
            }, 1000)
        } else {
            textView.text = "Done"
        }
    }

    private fun logThreadInfo(message: String) {
        ThreadInfoLogger.logThreadInfo(message)
    }
}
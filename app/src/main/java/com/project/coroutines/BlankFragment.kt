package com.project.coroutines

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BlankFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BlankFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate)
    private val secondCoroutineScope = CoroutineScope(Dispatchers.Default)

    private var job: Job? = null
    private var jobCounter: Job? = null

    private lateinit var button : Button
    private lateinit var textView : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button = view.findViewById(R.id.button)
        textView = view.findViewById(R.id.textView)
    }

    override fun onResume() {
        super.onResume()
        button.setOnClickListener {
            val benchmarkDurationSeconds = 5
            println("button click")
            jobCounter = coroutineScope.launch {
                updateRemainingTime(benchmarkDurationSeconds)
            }

            job = coroutineScope.launch {
                button.isEnabled = false
                val iterationsCount = executeBenchmark(benchmarkDurationSeconds)
                Toast.makeText(requireContext(), "$iterationsCount", Toast.LENGTH_SHORT).show()
                button.isEnabled = true
            }
        }
    }

    override fun onStop() {
        super.onStop()
        println("onStop")
        job?.cancel()
        jobCounter?.cancel()
    }

    private suspend fun executeBenchmark(benchmarkDurationSeconds: Int) = withContext(Dispatchers.Default) {
        logThreadInfo("benchmark started")

        val stopTimeNano = System.nanoTime() + benchmarkDurationSeconds * 1_000_000_000L

        var iterationsCount: Long = 0
        while (System.nanoTime() < stopTimeNano) {
            iterationsCount++
        }

        logThreadInfo("benchmark completed")

        iterationsCount
    }


    private suspend fun updateRemainingTime(remainingTimeSeconds: Int) {
        for (time in remainingTimeSeconds downTo 0) {
            if (time > 0) {
                logThreadInfo("updateRemainingTime: $time seconds")
                if (remainingTimeSeconds > 0) {
                    textView.text = "$time"
                    delay(1000)
                } else {
                    textView.text = "Done"
                }
            }
        }
    }

    private fun logThreadInfo(message: String) {
        ThreadInfoLogger.logThreadInfo(message)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BlankFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BlankFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
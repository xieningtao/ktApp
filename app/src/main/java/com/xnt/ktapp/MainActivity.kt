package com.xnt.ktapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Range
import androidx.databinding.DataBindingUtil
import com.xnt.ktapp.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        initView()
    }

    private fun initView() {
        corutine_with.setOnClickListener {
            multiWithContext()
        }

        corutine_async.setOnClickListener {
            multiAsync()
        }

        corutine_async_all_await.setOnClickListener {
            multiAsyncAllAwait()
        }

        corutine_async_await.setOnClickListener {
            multiAsyncAwait()
        }

        corutine_launch.setOnClickListener {
            multiLaunch()
        }
        corutine_async_with_suspend.setOnClickListener {
            multiAsyncWithSuspend()
        }
    }

    private fun multiAsyncWithSuspend() {
        Log.i(TAG, "method->multiAsyncAllAwait start")
        GlobalScope.launch {
            Log.i(TAG, "globalScope launch start,curThread ${Thread.currentThread().name}")
            runBackJob()

            withContext(Dispatchers.IO) {
                delay(1000)
                Log.i(TAG, "this is finally context,thread name is ${Thread.currentThread().name}")
            }
        }
    }

    private suspend fun runBackJob() {
        Log.i(TAG, "runBackJob start,curThread ${Thread.currentThread().name}")
        val asyncList = mutableListOf<Deferred<Int>>()
        for (i in 1..10) {
            val deferred = GlobalScope.async {
                delay(1000)
                Log.i(TAG, "run in async with all await,threadName is ${Thread.currentThread().name} index is ${i}")
                return@async i
            }
            asyncList.add(deferred)
        }

        Log.i(TAG, "method->runBackJob result: ${asyncList.awaitAll()}")
    }

    /**
     * wait one by one
     */
    private fun multiAsyncAwait() {
        Log.i(TAG, "method->multiAsyncAwait start")
        GlobalScope.launch {
            Log.i(TAG, "globalScope launch start,curThread ${Thread.currentThread().name}")

            for (i in 1..10) {
                val deferred = async {
                    delay(1000)
                    Log.i(TAG, "run in async with single await,threadName is ${Thread.currentThread().name} index is ${i}")
                    return@async i
                }
                Log.i(TAG, "method->multiAsyncAwait index is ${i} result: ${deferred.await()}")
            }



            withContext(Dispatchers.IO) {
                delay(1000)
                Log.i(TAG, "this is finally context,thread name is ${Thread.currentThread().name}")
            }
        }

    }

    /**
     * collect all defer then do await
     */
    private fun multiAsyncAllAwait() {
        Log.i(TAG, "method->multiAsyncAllAwait start")
        GlobalScope.launch {
            Log.i(TAG, "globalScope launch start,curThread ${Thread.currentThread().name}")

            val asyncList = mutableListOf<Deferred<Int>>()
            for (i in 1..10) {
                val deferred = async {
                    delay(1000)
                    Log.i(TAG, "run in async with all await,threadName is ${Thread.currentThread().name} index is ${i}")
                    return@async i
                }
                asyncList.add(deferred)
            }

            Log.i(TAG, "method->multiAsyncAllAwait result: ${asyncList.awaitAll()}")

            withContext(Dispatchers.IO) {
                delay(1000)
                Log.i(TAG, "this is finally context,thread name is ${Thread.currentThread().name}")
            }
        }

    }

    private fun multiLaunch() {
        Log.i(TAG, "method->multiLaunch start")
        GlobalScope.launch {
            Log.i(TAG, "globalScope launch start,curThread ${Thread.currentThread().name}")

            for (i in 1..10) {
                launch {
                    delay(1000)
                    Log.i(TAG, "run in launch,threadName is ${Thread.currentThread().name} index is ${i}")
                }
            }

            withContext(Dispatchers.IO) {
                delay(1000)
                Log.i(TAG, "this is finally context,thread name is ${Thread.currentThread().name}")
            }
        }
    }

    private fun multiAsync() {
        Log.i(TAG, "method->multiAsync start")
        GlobalScope.launch {
            Log.i(TAG, "globalScope launch start,curThread ${Thread.currentThread().name}")

            for (i in 1..10) {
                async {
                    delay(1000)
                    Log.i(TAG, "run in async,threadName is ${Thread.currentThread().name} index is ${i}")
                }
            }

            withContext(Dispatchers.IO) {
                delay(1000)
                Log.i(TAG, "this is finally context")
            }
        }
    }


    private fun multiWithContext() {
        Log.i(TAG, "method->multiWithContext start")
        GlobalScope.launch {
            Log.i(TAG, "globalScope launch start,curThread ${Thread.currentThread().name}")

            for (i in 1..10) {
                withContext(Dispatchers.IO) {
                    delay(1000)
                    Log.i(TAG, "run in withContext,threadName is ${Thread.currentThread().name} index is ${i}")
                }
            }

            withContext(Dispatchers.IO) {
                delay(1000)
                Log.i(TAG, "this is finally context")
            }

        }
    }
}
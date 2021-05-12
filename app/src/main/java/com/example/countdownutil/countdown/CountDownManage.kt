package com.example.countdownutil.countdown

import kotlinx.coroutines.*

/**
 * author: lishangming
 * time: 2021/05/12
 * desc: 倒计时管理
 * @param mIntervalTime 轮询队列时间间隔
 */
class CountDownManage(maxCount: Int = 10, var mIntervalTime: Long = 1000L) {

    private val mCountDownQueue = CountDownQueue(maxCount)

    @Volatile
    private var mExecuteTime = 0L
    private var mJob: Job? = null
    fun addEvent(event: CountDownEvent) {
        val isRestartLoop = mCountDownQueue.isEmpty()
        mCountDownQueue.add(event)
        if (isRestartLoop || mJob?.isActive == false) {
            loop()
        }
    }

    private fun loop() {
        mJob?.cancel()
        mJob = CoroutineScope(Dispatchers.Default).launch {
            while (isActive && !mCountDownQueue.isEmpty()) {
                val currentTime = System.currentTimeMillis()
                if (mExecuteTime == 0L || mExecuteTime <= currentTime) {
                    mExecuteTime = mCountDownQueue.execute()
                }
                val nextTime = mExecuteTime - currentTime
                delay(if (nextTime in 0 until mIntervalTime) nextTime else mIntervalTime)
            }
        }
    }

    fun cancel() {
        mJob?.cancel()
        mExecuteTime = 0L
        mCountDownQueue.clear()
    }

    companion object {
        private var mCountDownManage: CountDownManage? = null
        fun getDefault(intervalTime: Long = 1000L): CountDownManage {
            val manage = mCountDownManage ?: CountDownManage(mIntervalTime = intervalTime)
            if (mCountDownManage == null) {
                mCountDownManage = manage
            }
            return manage
        }
    }
}
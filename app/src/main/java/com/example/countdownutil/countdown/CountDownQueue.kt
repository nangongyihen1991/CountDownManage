package com.example.countdownutil.countdown

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

/**
 * author: lishangming
 * time: 2021/05/12
 * desc: 倒计时队列
 */
class CountDownQueue(maxCount: Int = 10) {

    private val mQueue =
        PriorityQueue<CountDownEvent>(maxCount) { o1: CountDownEvent?, o2: CountDownEvent? ->
            val o1Priority: Long = o1?.executeTime ?: 0
            val o2Priority: Long = o2?.executeTime ?: 0
            when {
                o1Priority > o2Priority -> 1
                o1Priority < o2Priority -> -1
                else -> 0
            }
        }

    @Synchronized
    fun isEmpty() = mQueue.isEmpty()

    @Synchronized
    fun add(event: CountDownEvent) {
        event.executeTime = System.currentTimeMillis() + event.firstDelayTime
        mQueue.add(event)
    }

    @Synchronized
    fun clear() {
        mQueue.clear()
    }

    @Synchronized
    suspend fun execute(): Long {
        val iterator = mQueue.iterator()
        var nextExecuteTime = 0L
        val survivalList = ArrayList<CountDownEvent>()
        while (iterator.hasNext()) {
            val event = iterator.next()
            if (event.maxExecuteCount != -1 && event.maxExecuteCount <= event.executeCount) {
                iterator.remove()
                continue
            }
            if (event.executeTime <= System.currentTimeMillis()) {
                iterator.remove()
                if (realExecute(event)) {
                    //重置开始时间，并加入队列
                    event.executeTime = System.currentTimeMillis() + event.timeInterval
                    survivalList.add(event)
                }
                continue
            } else {
                nextExecuteTime = event.executeTime
                break
            }
        }
        mQueue.addAll(survivalList)
        return nextExecuteTime
    }

    private suspend fun realExecute(event: CountDownEvent): Boolean {
        val isSuccess = withContext(Dispatchers.Main) {
            try {
                event.action?.invoke(event) ?: true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
        when {
            isSuccess -> { //成功加执行次数
                event.executeCount++
            }
            event.countDownStrategy == CountDownStrategy.DEFAULT -> { //失败默认策略加执行次数
                event.executeCount++
            }
            event.countDownStrategy == CountDownStrategy.REPEAT_WHEN_ERROR -> {
                //该策略失败不加执行次数
            }
        }
        return event.maxExecuteCount == -1 || event.maxExecuteCount > event.executeCount
    }

}
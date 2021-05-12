package com.example.countdownutil.countdown

/**
 * author: lishangming
 * time: 2021/05/12
 * desc: 倒计时策略
 */
enum class CountDownStrategy {
    DEFAULT,//默认策略，无论是成功或失败，达到最大执行次数后移除队列
    REPEAT_WHEN_ERROR,//失败不计入执行次数，当成功执行次数达到最大执行次数后移除队列
}
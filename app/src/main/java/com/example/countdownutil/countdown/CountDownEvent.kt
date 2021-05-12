package com.example.countdownutil.countdown

/**
 * author: lishangming
 * time: 2021/05/12
 * desc: 倒计时事件
 * @param countDownStrategy  倒计时策略
 * @param timeInterval 倒计时时间间隔
 * @param firstDelayTime 倒计时第一次延迟时间
 * @param executeTime 倒计时执行时间(初次或者每次执行后)
 * @param maxExecuteCount 最大执行次数(默认：1 为一次，-1 为无限次)
 * @param executeCount 当前执行次数
 * @param action 执行动作
 */
class CountDownEvent(
        var countDownStrategy: CountDownStrategy = CountDownStrategy.DEFAULT,
        var executeTime: Long = 0,
        var executeCount: Int = 0,
        var firstDelayTime: Long = 0,
        var maxExecuteCount: Int = 1,
        var timeInterval: Long = 1000L,
        var action: ((CountDownEvent) -> Boolean)? = null
)
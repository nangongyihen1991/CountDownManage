package com.example.countdownutil

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.countdownutil.countdown.CountDownEvent
import com.example.countdownutil.countdown.CountDownManage
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "CountDownManage"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            CountDownManage.getDefault().cancel()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_countdown -> {
                with(CountDownEvent()) {
                    firstDelayTime = 100L
                    timeInterval = 1000L
                    maxExecuteCount = 30
                    action = {
                        findViewById<TextView>(R.id.mTv).text = """
                            倒计时
                            执行时间： ${getTimeStr(it.executeTime)}
                            剩余时间: ${it.maxExecuteCount - it.executeCount}
                        """.trimIndent()
                        true
                    }
                    CountDownManage.getDefault().addEvent(this)
                }
                return true
            }
            R.id.action_repeat -> {
                with(CountDownEvent()) {
                    timeInterval = 1000L
                    maxExecuteCount = -1
                    action = {
                        findViewById<TextView>(R.id.mTv).text = """
                            重复操作
                            间隔时间:         ${it.timeInterval}
                            执行时间：    ${getTimeStr(it.executeTime)}
                            当前执行次数: ${it.executeCount + 1}
                        """.trimIndent()
                        true
                    }
                    CountDownManage.getDefault().addEvent(this)
                }
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getTimeStr(time: Long): String {
        return SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(time)
    }
}
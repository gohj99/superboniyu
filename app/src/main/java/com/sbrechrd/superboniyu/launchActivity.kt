package com.sbrechrd.superboniyu

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity

class launchActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launch)

        start_new_activity(ClassicActivity::class.java)

    }

    private fun start_new_activity(activityClass: Class<out Activity>){
        Handler(Looper.getMainLooper()).postDelayed({
            // 跳转逻辑
            startActivity(Intent(this, activityClass))
            finish()
        }, 0) //启动画面显示的时间
    }
}
package com.sbrechrd.superboniyu

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity

class launchActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.launch_screen)

        Handler(Looper.getMainLooper()).postDelayed({
            // 跳转逻辑
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }, 0) //启动画面显示的时间
    }
}
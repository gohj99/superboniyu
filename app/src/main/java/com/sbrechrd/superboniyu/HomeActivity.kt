package com.sbrechrd.superboniyu

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import com.sbrechrd.superboniyu.databinding.HomeActivityBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeActivity : ComponentActivity() {
    private lateinit var binding: HomeActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 使用正确的绑定类名，这个名字基于您的布局文件名
        binding = HomeActivityBinding.inflate(layoutInflater)
        setContentView(binding.root) // 只调用一次 setContentView

        //显示时间
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(object : Runnable {
            override fun run() {
                val currentDateTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(
                    Date()
                )
                binding.time.text = currentDateTime
                handler.postDelayed(this, 10000)
            }
        }, 0)
    }
}
package com.sbrechrd.superboniyu

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.sbrechrd.superboniyu.databinding.ActivityGeneralBinding


class GeneralActivity : ComponentActivity() {
    private lateinit var binding: ActivityGeneralBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 使用正确的绑定类名，这个名字基于您的布局文件名
        binding = ActivityGeneralBinding.inflate(layoutInflater)
        setContentView(binding.root) // 只调用一次 setContentView

    }
}
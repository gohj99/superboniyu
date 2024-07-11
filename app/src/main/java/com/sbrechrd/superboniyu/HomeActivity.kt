package com.sbrechrd.superboniyu

import android.app.Activity
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.widget.Button
import androidx.activity.ComponentActivity
import com.sbrechrd.superboniyu.databinding.ActivityHomeBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeActivity : ComponentActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 使用正确的绑定类名，这个名字基于您的布局文件名
        binding = ActivityHomeBinding.inflate(layoutInflater)
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
        // 为按钮控件设置触摸监听器
        setupButtonTouchListener(binding.HomeButton1, ClassicActivity::class.java)
        setupButtonTouchListener(binding.HomeButton2, FavorActivity::class.java)
        setupButtonTouchListener(binding.HomeButton3, GeneralActivity::class.java)
    }

    private fun setupButtonTouchListener(binding_button: Button, activityClass: Class<out Activity>) {
        binding_button.setOnTouchListener { v, event ->
            // 获取按钮的边界
            val rect = Rect(v.left, v.top, v.right, v.bottom)

            // 处理不同的触摸事件
            when (event.action) {
                // 按钮按下事件
                MotionEvent.ACTION_DOWN -> {
                    // 缩小按钮的动画
                    v.animate()
                        .scaleX(0.9f)
                        .scaleY(0.9f)
                        .setDuration(150)
                        .start()
                    true // 返回true表示事件已被处理
                }
                // 按钮抬起事件
                MotionEvent.ACTION_UP -> {
                    // 放大按钮的动画
                    v.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(150)
                        .withEndAction {
                            // 检查手指是否还在按钮上
                            if (rect.contains(v.left + event.x.toInt(), v.top + event.y.toInt())) {
                                // 手指仍在按钮上，响应事件
                                val context = v.context
                                context.startActivity(Intent(context, activityClass))
                            }
                        }
                        .start()
                    true // 返回true表示事件已被处理
                }
                // 按钮取消事件，例如手指移出屏幕
                MotionEvent.ACTION_CANCEL -> {
                    // 恢复按钮大小的动画
                    v.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(150)
                        .start()
                    true // 返回true表示事件已被处理
                }
                // 按钮移动事件
                MotionEvent.ACTION_MOVE -> {
                    // 检查手指是否移出了按钮的边界
                    if (!rect.contains(v.left + event.x.toInt(), v.top + event.y.toInt())) {
                        // 手指移出了按钮，取消动画并恢复按钮大小
                        v.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(150)
                            .start()
                        v.scaleX = 1f
                        v.scaleY = 1f
                    }
                    true // 返回true表示事件已被处理
                }
                else -> false // 其他事件不处理，返回false
            }
        }
    }
}
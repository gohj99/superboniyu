package com.sbrechrd.superboniyu

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.core.content.ContextCompat
import com.sbrechrd.superboniyu.databinding.ActivityHomeBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeActivity : ComponentActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var sharedPref: SharedPreferences
    private lateinit var pagePref: String

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        // 先让 GestureDetector 检查触摸事件
        if (gestureDetector.onTouchEvent(event)) {
            return true
        }
        // 如果 GestureDetector 没有处理事件，则继续正常的事件分发
        return super.dispatchTouchEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }

    // 定义手势检测器
    private val gestureDetector by lazy {
        GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            private val SWIPE_THRESHOLD = 100
            private val SWIPE_VELOCITY_THRESHOLD = 100

            // 注意：这里的参数没有使用可空类型
            override fun onFling(
                e1: MotionEvent?,
                e2: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                e1 ?: return false

                val diffX = e2.x - e1.x
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        // 检测到左向右滑动
                        onSwipeRight()
                    }
                    return true
                }
                return false
            }
        })
    }

    // 处理左滑事件
    private fun onSwipeRight() {
        // 打开新的 Activity
        val intent = Intent(this, ClassicActivity::class.java)
        startActivity(intent)
        // 结束当前页面
        finish()
        // 设置转场动画
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        // 使用正确的绑定类名，这个名字基于您的布局文件名
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root) // 只调用一次 setContentView

        sharedPref = getSharedPreferences("Pref", Context.MODE_PRIVATE)
        pagePref = sharedPref.getString("PagePref", "ClassicActivity") ?: "ClassicActivity"

        // 修改功德值
        val merit = sharedPref.getInt("meritPref", 0)
        binding.HomeButton1.text = merit.toString()
        binding.HomeButton2.text = merit.toString()
        binding.HomeButton3.text = merit.toString()

        // 显示时间
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

        //显示当前选中木鱼
        when (pagePref){
            "ClassicActivity" -> {
                ContextCompat.getDrawable(this@HomeActivity, resources.getIdentifier("home_button_background_1_select", "drawable", packageName))?.let {
                    binding.HomeButton1.background = it
                }
            }
            "FavorActivity" -> {
                ContextCompat.getDrawable(this@HomeActivity, resources.getIdentifier("home_button_background_2_select", "drawable", packageName))?.let {
                    binding.HomeButton2.background = it
                }
            }
            "GeneralActivity" -> {
                ContextCompat.getDrawable(this@HomeActivity, resources.getIdentifier("home_button_background_3_select", "drawable", packageName))?.let {
                    binding.HomeButton3.background = it
                }
            }
        }

        // 为按钮控件设置触摸监听器
        setupButtonTouchListener(binding.HomeButton1, "1")
        setupButtonTouchListener(binding.HomeButton2, "2")
        setupButtonTouchListener(binding.HomeButton3, "3")
    }

    private fun setupButtonTouchListener(binding_button: Button, button_number: String) {
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
                                Before_recovery()
                                // 获取 SharedPreferences.Editor 对象以进行修改
                                val editor = sharedPref.edit()
                                // 修改相应变量
                                pagePref = Get_save_name(button_number)
                                // 将 merit 的新值写入 "PagePref" 键
                                editor.putString("PagePref", pagePref)
                                // 应用更改
                                editor.apply()
                                ContextCompat.getDrawable(this@HomeActivity, resources.getIdentifier("home_button_background_${button_number}_select", "drawable", packageName))?.let {
                                    binding_button.background = it
                                }
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

    private fun Before_recovery(){
        when (pagePref){
            "ClassicActivity" -> {
                ContextCompat.getDrawable(this@HomeActivity, resources.getIdentifier("home_button_background_1", "drawable", packageName))?.let {
                    binding.HomeButton1.background = it
                }
            }
            "FavorActivity" -> {
                ContextCompat.getDrawable(this@HomeActivity, resources.getIdentifier("home_button_background_2", "drawable", packageName))?.let {
                    binding.HomeButton2.background = it
                }
            }
            "GeneralActivity" -> {
                ContextCompat.getDrawable(this@HomeActivity, resources.getIdentifier("home_button_background_3", "drawable", packageName))?.let {
                    binding.HomeButton3.background = it
                }
            }
        }
    }

    private fun Get_save_name(number: String): String {
        when (number){
            "1" -> return "ClassicActivity"
            "2" -> return "FavorActivity"
            "3" -> return "GeneralActivity"
        }
        return "ClassicActivity"
    }
}
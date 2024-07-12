package com.sbrechrd.superboniyu

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Rect
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.sbrechrd.superboniyu.databinding.ActivityClassicBinding
import com.sbrechrd.superboniyu.databinding.ActivityFavorBinding
import com.sbrechrd.superboniyu.databinding.ActivityGeneralBinding
import kotlin.properties.Delegates


class ClassicActivity : ComponentActivity() {
    private lateinit var binding_Classic: ActivityClassicBinding
    private lateinit var binding_Favor: ActivityFavorBinding
    private lateinit var binding_General: ActivityGeneralBinding
    private lateinit var textView_merit: TextView
    private lateinit var sharedPref: SharedPreferences
    private lateinit var pagePref: String
    private var restart: Int = 0

    override fun onResume() {
        super.onResume()
        // 在这里刷新数据或更新 UI
        // 重新启动当前的 Activity
        if (restart == 0) restart = 1
        else{
            val intent = intent
            finish()
            startActivity(intent)
        }
    }

    // 初始化 merit 变量
    var merit: Int by Delegates.observable(0) { _, old, new ->
        // 当 merit 的值改变时，这个 lambda 表达式会被调用
        if (new != old) {
            // 更新 TextView 的文本
            textView_merit.text = getString(R.string.Merits_accumulated) + new.toString()
            // 获取 SharedPreferences.Editor 对象以进行修改
            val editor = sharedPref.edit()
            // 将 merit 的新值写入 "meritPref" 键
            editor.putInt("meritPref", new)
            // 应用更改
            editor.apply()
        }
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
                e1?.let {
                    val diffX = e2.x - e1.x
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX < 0) {
                            // 检测到从右向左滑动
                            onSwipeLeft()
                        }
                        return true
                    }
                }
                return false // 如果没有检测到滑动手势，返回 false
            }
        })
    }

    // 处理左滑事件
    private fun onSwipeLeft() {
        // 打开新的 Activity
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        // 设置转场动画
        overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 检测主页，并绑定类名
        sharedPref = getSharedPreferences("Pref", Context.MODE_PRIVATE)
        pagePref = sharedPref.getString("PagePref", "ClassicActivity") ?: "ClassicActivity"
        when (pagePref){
            "ClassicActivity" -> {
                binding_Classic = ActivityClassicBinding.inflate(layoutInflater)
                setContentView(binding_Classic.root)
                click_woodenFish(binding_Classic.woodenFish)
                textView_merit = binding_Classic.textViewMerit
            }
            "FavorActivity" -> {
                binding_Favor = ActivityFavorBinding.inflate(layoutInflater)
                setContentView(binding_Favor.root)
                click_woodenFish(binding_Favor.woodenFish)
                textView_merit = binding_Favor.textViewMerit
            }
            "GeneralActivity" -> {
                binding_General = ActivityGeneralBinding.inflate(layoutInflater)
                setContentView(binding_General.root)
                click_woodenFish(binding_General.woodenFish)
                textView_merit = binding_General.textViewMerit
            }
        }
        merit = sharedPref.getInt("meritPref", 0)
        textView_merit.text = getString(R.string.Merits_accumulated) + merit.toString()
    }

    private fun click_woodenFish(woodenFish: View){
        woodenFish.setOnTouchListener { v, event ->
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
                                merit += 1
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

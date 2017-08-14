package slmodule.shenle.com

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.iflytek.sunflower.FlowerCollector


/**
 * Created by shenle on 2017/7/31.
 */
abstract class BaseActivity : RxAppCompatActivity() {
    var is_init = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BaseApplication.getInstance().addActivity(this)
        is_init = getIntent().getBooleanExtra("is_init", false)
        var s: String? = null
        try {
            s = intent.getStringExtra("is_init")
        } catch (e: Exception) {
        }
        if (!is_init && s != null) {
            is_init = java.lang.Boolean.parseBoolean(s)
        }
        setContentView(getRootView())
        initOnCreate(savedInstanceState)
    }

    abstract fun getRootView(): Int

    abstract fun initOnCreate(savedInstanceState: Bundle?)

    fun onBack(view: View) {
        onBackPressed()
    }

    // // 获取点击事件
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val view = currentFocus
            if (isHideInput(view, ev)) {
                HideSoftInput(view.windowToken)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    // 判定是否需要隐藏
    private fun isHideInput(v: View?, ev: MotionEvent): Boolean {
        if (v != null && v is EditText && v.hasFocusable()) {
            val l = intArrayOf(0, 0)
            v.getLocationInWindow(l)
            val left = l[0]
            val top = l[1]
            val bottom = top + v.height
            val right = left + v.width
            if (ev.y > top - 100) {
                // && ev.getY() < bottom+150) {
                return false
            } else {
                return true
            }
        }
        return false
    }

    // 隐藏软键盘
    private fun HideSoftInput(token: IBinder?) {
        if (token != null) {
            val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    /**
     * Reload activity
     */
    fun reload() {
        startActivity(Intent(this, this.localClassName::class.java))
        finish()
    }

    override fun onResume() {
        super.onResume()
        BaseApplication.foregroundActivity = this
        FlowerCollector.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        FlowerCollector.onPause(this)
    }

    override fun onDestroy() {
        BaseApplication.getInstance().removeActivity(this)
        super.onDestroy()
    }
}
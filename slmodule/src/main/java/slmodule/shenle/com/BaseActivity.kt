package slmodule.shenle.com

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.support.v7.widget.Toolbar
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.iflytek.sunflower.FlowerCollector
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import slmodule.shenle.com.data.Constants
import slmodule.shenle.com.dialog.LoadingDialog
import slmodule.shenle.com.utils.SwipeBackHelper


/**
 * Created by shenle on 2017/7/31.
 */
abstract class BaseActivity : RxAppCompatActivity(), SwipeBackHelper.SlideBackManager{
    var is_init = false
    lateinit var dialog: LoadingDialog
    open fun onTest(){

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialog = LoadingDialog(this,R.style.LoadingDialog)
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
        val toolBar = initToolBar()
        setSupportActionBar(toolBar)
        toolBar?.setNavigationOnClickListener { onBack(it) }
        initOnCreate(savedInstanceState)
        if (Constants.isTest){
            onTest()
        }
    }

    abstract fun initToolBar():Toolbar?

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
        if (mSwipeBackHelper == null) {
            mSwipeBackHelper = SwipeBackHelper(this)
        }
        return mSwipeBackHelper!!.processTouchEvent(ev) || super.dispatchTouchEvent(ev)
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
        FlowerCollector.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        FlowerCollector.onPause(this)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
    private val TAG = "SwipeBackActivity"

    private var mSwipeBackHelper: SwipeBackHelper? = null

    override fun getSlideActivity(): Activity {
        return this
    }

    override fun supportSlideBack(): Boolean {
        return true
    }

    override fun canBeSlideBack(): Boolean {
        return true
    }

    override fun finish() {
        if (mSwipeBackHelper != null) {
            mSwipeBackHelper!!.finishSwipeImmediately()
            mSwipeBackHelper = null
        }
        super.finish()
    }
}
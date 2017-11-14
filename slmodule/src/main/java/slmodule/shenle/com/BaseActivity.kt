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
import com.readystatesoftware.systembartint.SystemBarTintManager
import android.view.WindowManager
import android.os.Build
import android.view.ViewGroup


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
        toolbar2Setting(toolBar)
        initOnCreate(savedInstanceState)
        if (Constants.isTest){
            onTest()
        }
        if (toolBar!=null&&Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            //透明导航栏
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            val tintManager = SystemBarTintManager(this)
            // 激活状态栏
            tintManager.isStatusBarTintEnabled = true
            // enable navigation bar tint 激活导航栏
//            tintManager.setNavigationBarTintEnabled(true)
            val config = tintManager.config
//            if (editSettingToolBar()){
//
//            }else {
//                toolBar.setPadding(0, config.getPixelInsetTop(true), config.pixelInsetRight, config.pixelInsetBottom)
//            }
//            toolbar.setPadding(0, UIUtils.getStatusBarHeight(this), 0, 0);
            //设置系统栏设置颜色
//            tintManager.setTintColor(R.color.text_color_9);
            //Apply the specified drawable or color resource to the system navigation bar.
            //给导航栏设置资源
//            tintManager.setNavigationBarTintResource(R.color.text_color_8);
            //给状态栏设置颜色
            tintManager.setStatusBarTintResource(setSystemBarTintColor(tintManager))
        }
    }
    open fun setSystemBarTintColor(tintManager: SystemBarTintManager): Int {
        return R.color.text_color_5
    }

    open fun toolbar2Setting(toolbar: Toolbar?){}
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

    fun getCurrentRootView(): View {
        return (findViewById(android.R.id.content) as ViewGroup).getChildAt(0)
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
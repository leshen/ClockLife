package slmodule.shenle.com.helper

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.View.OnTouchListener
import android.view.WindowManager.LayoutParams
import android.widget.LinearLayout
import android.widget.Toast
import slmodule.shenle.com.R

/**
 * @author:Jack Tony
 *
 * 重要：注意要申请权限！！！！
 *
 * <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"></uses-permission>
 *
 * @tips  :思路：
 * 1.获得一个windowManager类
 * 2.通过wmParams设置好windows的各种参数
 * 3.获得一个视图的容器，找到悬浮窗视图的父控件，比如linearLayout
 * 4.将父控件添加到WindowManager中去
 * 5.通过这个父控件找到要显示的悬浮窗图标，并进行拖动或点击事件的设置
 * @date  :2014-9-25
 */
class FloatingService : Service() {
    /**
     * 定义浮动窗口布局
     */
    internal var mlayout: LinearLayout? = null
    /**
     * 悬浮窗的布局
     */
    internal lateinit var wmParams: WindowManager.LayoutParams
    internal lateinit var inflater: LayoutInflater
    /**
     * 创建浮动窗口设置布局参数的对象
     */
    internal lateinit var mWindowManager: WindowManager

    //触摸监听器
    internal lateinit var mGestureDetector: GestureDetector

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        initWindow()//设置窗口的参数
    }


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        initFloating()//设置悬浮窗图标
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mlayout != null) {
            // 移除悬浮窗口
            mWindowManager.removeView(mlayout)
        }
    }

    ///////////////////////////////////////////////////////////////////////

    /**
     * 初始化windowManager
     */
    private fun initWindow() {
        mWindowManager = application.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wmParams = getParams()//设置好悬浮窗的参数
        // 悬浮窗默认显示以左上角为起始坐标
        wmParams.gravity = Gravity.LEFT or Gravity.TOP
        //悬浮窗的开始位置，因为设置的是从左上角开始，所以屏幕左上角是x=0;y=0
        wmParams.x = 50
        wmParams.y = 50
        //得到容器，通过这个inflater来获得悬浮窗控件
        inflater = LayoutInflater.from(application)
        // 获取浮动窗口视图所在布局
        mlayout = inflater.inflate(R.layout.floating_layout, null) as LinearLayout
        // 添加悬浮窗的视图
        mWindowManager.addView(mlayout, wmParams)
    }

    /** 对windowManager进行设置
     * @param wmParams
     * @return
     */
    fun getParams(): WindowManager.LayoutParams {
        var wmParams = WindowManager.LayoutParams()
        //设置window type 下面变量2002是在屏幕区域显示，2003则可以显示在状态栏之上
//        wmParams.type = LayoutParams.TYPE_PHONE;
        //wmParams.type = LayoutParams.TYPE_SYSTEM_ALERT;
//        wmParams.type = LayoutParams.TYPE_TOAST;
        wmParams.type = LayoutParams.TYPE_SYSTEM_ERROR
        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        //wmParams.flags = LayoutParams.FLAG_NOT_FOCUSABLE;
        //设置可以显示在状态栏上
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR or
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH

        //设置悬浮窗口长宽数据
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT

        return wmParams
    }

    /**
     * 找到悬浮窗的图标，并且设置事件
     * 设置悬浮窗的点击、滑动事件
     */
    private fun initFloating() {
        mGestureDetector = GestureDetector(this, MyOnGestureListener())
        //设置监听器
        mlayout?.setOnTouchListener(FloatingListener())
    }

    //开始触控的坐标，移动时的坐标（相对于屏幕左上角的坐标）
    private var mTouchStartX: Int = 0
    private var mTouchStartY: Int = 0
    private var mTouchCurrentX: Int = 0
    private var mTouchCurrentY: Int = 0
    //开始时的坐标和结束时的坐标（相对于自身控件的坐标）
    private var mStartX: Int = 0
    private var mStartY: Int = 0
    private var mStopX: Int = 0
    private var mStopY: Int = 0
    private var isMove: Boolean = false//判断悬浮窗是否移动

    /**
     * @author:金凯
     * @tips  :自己写的悬浮窗监听器
     * @date  :2014-3-28
     */
    private inner class FloatingListener : OnTouchListener {

        override fun onTouch(arg0: View, event: MotionEvent): Boolean {

            val action = event.action
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    isMove = false
                    mTouchStartX = event.rawX.toInt()
                    mTouchStartY = event.rawY.toInt()
                    mStartX = event.x.toInt()
                    mStartY = event.y.toInt()
                }
                MotionEvent.ACTION_MOVE -> {
                    mTouchCurrentX = event.rawX.toInt()
                    mTouchCurrentY = event.rawY.toInt()
                    wmParams.x += mTouchCurrentX - mTouchStartX
                    wmParams.y += mTouchCurrentY - mTouchStartY
                    mWindowManager.updateViewLayout(mlayout, wmParams)

                    mTouchStartX = mTouchCurrentX
                    mTouchStartY = mTouchCurrentY
                }
                MotionEvent.ACTION_UP -> {
                    mStopX = event.x.toInt()
                    mStopY = event.y.toInt()
                    //System.out.println("|X| = "+ Math.abs(mStartX - mStopX));
                    //System.out.println("|Y| = "+ Math.abs(mStartY - mStopY));
                    if (Math.abs(mStartX - mStopX) >= 1 || Math.abs(mStartY - mStopY) >= 1) {
                        isMove = true
                    }
                }
            }
            return mGestureDetector.onTouchEvent(event)  //此处必须返回false，否则OnClickListener获取不到监听
        }

    }

    /**
     * @author:金凯
     * @tips  :自己定义的手势监听类
     * @date  :2014-3-29
     */
    internal inner class MyOnGestureListener : SimpleOnGestureListener() {
        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            if (!isMove) {
                Toast.makeText(applicationContext, "你点击了悬浮窗", 0).show()
                println("onclick")
            }
            return super.onSingleTapConfirmed(e)
        }
    }


}
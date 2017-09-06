package slmodule.shenle.com.view
import android.content.Context
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import slmodule.shenle.com.BaseApplication

/**
 * 可移动view
 */
class FloatView(context: Context): View(context) {
    private var mTouchStartX: Float = 0.toFloat()
    private var mTouchStartY: Float = 0.toFloat()
    private var x_sl = 0f
    private var y_sl = 0f

    private val wm = getContext().getApplicationContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private val wmParams = (getContext().getApplicationContext() as BaseApplication).getMywmParams()

    override fun onTouchEvent(event: MotionEvent): Boolean {
        //获取相对屏幕的坐标，即以屏幕左上角为原点
        x_sl = event.rawX
        y_sl = event.rawY - 25   //25是系统状态栏的高度
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                //获取相对View的坐标，即以此View左上角为原点
                mTouchStartX = event.x
                mTouchStartY = event.y
            }
            MotionEvent.ACTION_MOVE -> updateViewPosition()

            MotionEvent.ACTION_UP -> {
                updateViewPosition()
                mTouchStartY = 0f
                mTouchStartX = mTouchStartY
            }
        }
        return true
    }

    private fun updateViewPosition() {
        //更新浮动窗口位置参数
        wmParams.x = (x_sl - mTouchStartX).toInt()
        wmParams.y = (y_sl - mTouchStartY).toInt()
        wm.updateViewLayout(this, wmParams)

    }

}
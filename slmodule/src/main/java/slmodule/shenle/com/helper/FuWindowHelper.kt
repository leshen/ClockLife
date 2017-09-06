package slmodule.shenle.com.helper

import android.app.AppOpsManager
import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import com.qihoo360.loader2.PMF.getApplicationContext
import slmodule.shenle.com.BaseApplication
import slmodule.shenle.com.R
import slmodule.shenle.com.view.FloatView
import android.content.Context.WINDOW_SERVICE
import android.graphics.Color
import android.os.Binder
import android.os.Build
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.view.View
import android.widget.Toast
import com.iflytek.cloud.resource.Resource.setText
import android.widget.TextView
import slmodule.shenle.com.utils.UIUtils


/**
 * 悬浮窗帮助类
 * Created by shenle on 2017/9/6.
 */
class FuWindowHelper {
    companion object {
        fun showView(): FloatView {
            val mLayout = FloatView(getApplicationContext())
            mLayout.setBackgroundResource(R.mipmap.ic_launcher)
            //获取WindowManager
            var mWindowManager = getApplicationContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
            //设置LayoutParams(全局变量）相关参数
            var param = (getApplicationContext() as BaseApplication).getMywmParams()

//            param.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT     // 系统提示类型,重要
            param.format = 1
            param.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE // 不能抢占聚焦点
            param.flags = param.flags or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH
            param.flags = param.flags or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS // 排版不受限制

            param.alpha = 1.0f

            param.gravity = Gravity.LEFT or Gravity.TOP   //调整悬浮窗口至左上角
            //以屏幕左上角为原点，设置x、y初始值
            param.x = 0
            param.y = 0

            //设置悬浮窗口长宽数据
            param.width = 140
            param.height = 140

            //显示myFloatView图像
            mWindowManager.addView(mLayout, param)
            return mLayout
        }
        fun showToastFuView(){
            val textView = TextView(getApplicationContext())
            textView.gravity = Gravity.CENTER
            textView.setBackgroundColor(Color.BLACK)
            textView.text = "zhang phil @ csdn"
            textView.textSize = 10f
            textView.setTextColor(Color.RED)

            //类型是TYPE_TOAST，像一个普通的Android Toast一样。这样就不需要申请悬浮窗权限了。
            val params = WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_TOAST)

            //初始化后不首先获得窗口焦点。不妨碍设备上其他部件的点击、触摸事件。
            params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE

            params.width = WindowManager.LayoutParams.MATCH_PARENT
            params.height = 300
            //params.gravity=Gravity.BOTTOM;

            textView.setOnClickListener{
                    UIUtils.showToastSafe("不需要权限的悬浮窗实现")
            }

            val windowManager = UIUtils.getActivity()?.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            windowManager.addView(textView, params)
        }
        /**
         * 销毁悬浮窗口
         */
        fun removeFuView(mLayout: FloatView) {
            (getApplicationContext().getSystemService(Context.WINDOW_SERVICE)as WindowManager).removeView(mLayout);
        }

        /**
         * 更新
         */
        fun updateFuView(mLayout: FloatView, params: ViewGroup.LayoutParams){
            (getApplicationContext().getSystemService(Context.WINDOW_SERVICE)as WindowManager).updateViewLayout(mLayout,params)
        }
        fun checkFloatWindowPermission(): Boolean {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return Settings.canDrawOverlays(getApplicationContext())
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                //AppOpsManager添加于API 19
                return checkOps();
            } else {
                //4.4以下一般都可以直接添加悬浮窗
                return true;
            }
        }
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        fun checkOps() :Boolean{
            try {
                var ob = getApplicationContext().getSystemService(Context.APP_OPS_SERVICE)
                if (ob == null) {
                    return false;
                }

                ob =  ob as Object
                var localClass = ob.getClass()
                val arrayOfClass = arrayOfNulls<Class<*>>(3)
                arrayOfClass[0] = Integer.TYPE
                arrayOfClass[1] = Integer.TYPE
                arrayOfClass[2] = String::class.java
                var method = localClass.getMethod("checkOp", *arrayOfClass);
                if (method == null) {
                    return false;
                }
                val arrayOfObject1 = arrayOfNulls<Any>(3)
                arrayOfObject1[0] = 24
                arrayOfObject1[1] = Binder.getCallingUid();
                arrayOfObject1[2] = getApplicationContext().getPackageName();
                var m = method.invoke(ob, arrayOfObject1);
                //4.4至6.0之间的非国产手机，例如samsung，sony一般都可以直接添加悬浮窗
//                return m == AppOpsManager.MODE_ALLOWED || !RomUtils.isDomesticSpecialRom();
                return m == AppOpsManager.MODE_ALLOWED
            } catch (ignore:Exception) {
            }
            return false;
        }


    }
}
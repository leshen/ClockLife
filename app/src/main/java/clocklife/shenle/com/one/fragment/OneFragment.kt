package clocklife.shenle.com.one.fragment

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import clocklife.shenle.com.R
import clocklife.shenle.com.one.TXActivity
import slmodule.shenle.com.BaseFragment
import com.asha.ChromeLikeSwipeLayout
import com.asha.ChromeLikeSwipeLayout.dp2px
import kotlinx.android.synthetic.main.fragment_one.view.*
import slmodule.shenle.com.helper.FloatingService
import slmodule.shenle.com.helper.FuWindowHelper
import slmodule.shenle.com.utils.UIUtils
import android.widget.Toast
import android.provider.Settings.canDrawOverlays
import android.provider.Settings.ACTION_MANAGE_OVERLAY_PERMISSION
import slmodule.shenle.com.floatwindow.FloatWindowManager


/**
 * Created by shenle on 2017/8/1.
 */
class OneFragment : BaseFragment(), View.OnClickListener {
    override fun getTitle(): CharSequence {
        return "OneFragment"
    }
    override fun initOnCreateView(view: View?, savedInstanceState: Bundle?) {
        ChromeLikeSwipeLayout.makeConfig()
                .addIcon(R.drawable.selector_icon_add)
                .addIcon(R.drawable.selector_icon_refresh)
                .addIcon(R.drawable.selector_icon_close)
                .backgroundColor(R.color.bg_5)
//                .maxHeight(dp2px(40f))
                .radius(dp2px(35f))
                .gap(dp2px(5f))
                .circleColor(0xFF11CCFF.toInt())
                .gummyDuration(1000)
                .rippleDuration(1000)
                .collapseDuration(1000)
                .listenItemSelected({ UIUtils.showToastSafe("onItemSelected:" + it)})
                .setTo(view?.chrome_like_swipe_layout)
        view?.bt_jl?.setOnClickListener(this)
        view?.bt_tx?.setOnClickListener(this)
        view?.bt_rj?.setOnClickListener(this)
        view?.bt_gx?.setOnClickListener(this)
        view?.bt_tbda?.setOnClickListener(this)
        view?.bt_jqbm?.setOnClickListener(this)
    }

    override fun getRootView(): Int {
        return R.layout.fragment_one
    }

    override fun onClick(v: View) {
        when (v.id){
            R.id.bt_jl->{FuWindowHelper.showView()}
            R.id.bt_tx->{
                TXActivity.goHere(0)
            }
            R.id.bt_rj->{
                FloatWindowManager.getInstance().showFloatWindow(activity)}
            R.id.bt_gx->{
                activity.stopService(Intent(activity,FloatingService::class.java))
            }
            R.id.bt_tbda->{FuWindowHelper.showToastFuView()}
            R.id.bt_jqbm->{
                //开启悬浮框前先请求权限
                askForPermission()
            }
        }
    }
    var OVERLAY_PERMISSION_REQ_CODE = 1234
    /**
     * 请求用户给予悬浮窗的权限
     */
    fun askForPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(!Settings.canDrawOverlays(activity)) {
                UIUtils.showToastSafe("当前无权限，请授权！")
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + activity.getPackageName()))
                activity?.startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE)
            } else {
                activity.startService(Intent(activity,FloatingService::class.java))
            }
        } else {
            activity.startService(Intent(activity,FloatingService::class.java))
        }
    }

    /**
     * 用户返回
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(activity)) {
                    UIUtils.showToastSafe("权限授予失败，无法开启悬浮窗！")
                } else {
                    UIUtils.showToastSafe("权限授予成功！")
                    //启动FxService
                    activity.startService(Intent(activity, FloatingService::class.java))
                }
            }
        }
    }
    override fun refresh() {

    }

    companion object {
        fun getInstance(): OneFragment {
            return Holder.instance
        }
    }

    private object Holder {
        val instance = OneFragment()
    }
}

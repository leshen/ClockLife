package clocklife.shenle.com.setting

import android.os.Bundle
import android.support.v7.widget.Toolbar
import clocklife.shenle.com.R
import com.readystatesoftware.systembartint.SystemBarTintManager
import kotlinx.android.synthetic.main.activity_app_setting.*
import kotlinx.android.synthetic.main.base_edit_toolbar.*
import slmodule.shenle.com.BaseActivity
import slmodule.shenle.com.utils.BaseAccessibilityService
import slmodule.shenle.com.utils.UIUtils

/**
 * Created by shenle on 2017/9/12.
 */
class MyAppSetting : BaseActivity() {
    companion object {
        fun goHere() {
            UIUtils.startActivity(MyAppSetting::class.java)
        }
    }
    override fun setSystemBarTintColor(tintManager: SystemBarTintManager): Int {
        return R.color.bg_2
    }
    override fun initToolBar(): Toolbar? {
        toolbar?.title = "设置"
        return toolbar
    }

    override fun getRootView(): Int {
        return R.layout.activity_app_setting
    }

    override fun onResume() {
        super.onResume()
        sw_fz.setChecked(BaseAccessibilityService.checkAccessibilityEnabled(this, "clocklife.shenle.com/.base.receiver.MyAccessibilityService"))
        isDone = false
    }

    private var isDone: Boolean = false

    override fun initOnCreate(savedInstanceState: Bundle?) {
        sw_fz.setChecked(BaseAccessibilityService.checkAccessibilityEnabled(this, "clocklife.shenle.com/.base.receiver.MyAccessibilityService"))
        sw_fz.setOnCheckedChangeListener { buttonView, isChecked ->
            run {
                if (!isDone) {
                    isDone = true
                    BaseAccessibilityService.goAccess(this@MyAppSetting)
                }
            }
        }
    }

}
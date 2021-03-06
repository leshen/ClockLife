package clocklife.shenle.com

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import clocklife.shenle.com.base.BaseAppActivity
import cn.smssdk.EventHandler
import cn.smssdk.SMSSDK
import com.readystatesoftware.systembartint.SystemBarTintManager
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.base_edit_toolbar.*
import slmodule.shenle.com.utils.LogUtils
import slmodule.shenle.com.utils.SmsHelper
import slmodule.shenle.com.utils.UIUtils


class RegisterActivity : BaseAppActivity() {
    override fun initToolBar(): Toolbar? {
        toolbar?.title = "重置密码"
        return toolbar
    }

    override fun setSystemBarTintColor(tintManager: SystemBarTintManager): Int {
        return R.color.bg_2
    }

    private lateinit var observable1: Observable<RegisterSuccess>

    override fun getRootView(): Int {
        observable1 = UIUtils.register(RegisterSuccess::class.java, {
            finish()
        })
//        SMSSDK.setAskPermisionOnReadContact(true)
        return R.layout.activity_register
    }

    private var eventHandler: EventHandler? = null

    fun registSMS() {
        // 创建EventHandler对象
        eventHandler = object : EventHandler() {
            override fun beforeEvent(p0: Int, p1: Any?) {
                LogUtils.e("beforeEvent" + p1.toString())
            }

            override fun onUnregister() {
                LogUtils.e("onUnregister")
            }

            override fun onRegister() {
                LogUtils.e("onRegister")
            }

            override fun afterEvent(event: Int, result: Int, data: Any) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    when (event) {
                        SMSSDK.EVENT_GET_VERIFICATION_CODE -> {//获取验证码成功
                            UIUtils.showToastSafe("验证码已发送,请注意查收")
                        }
                        SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE -> {//提交验证码成功
//                            SetPasswordActivity.goHere(phone,code)
//                            finish()
                        }
                        SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES -> {//返回支持发送验证码的国家列表
                        }
                    }
                } else {
                    (data as Throwable).printStackTrace()
                    UIUtils.showToastSafe(data.message)
                }
            }
        }
        // 注册监听器
        SMSSDK.registerEventHandler(eventHandler)
    }

    override fun onDestroy() {
        super.onDestroy()
        UIUtils.unregister(observable1)
        // 注销监听器
        SMSSDK.unregisterEventHandler(eventHandler)
    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        registSMS()
        bt_code.setOnClickListener {
            //发送验证码
            var phone = et_phone.text.toString().trim()
            if (!UIUtils.isEmpty(phone)) {
                SmsHelper.sendYZM(phone, bt_code)
            } else {
                UIUtils.showToastSafe("手机号不能为空")
            }
        }
    }

    companion object {
        @JvmStatic fun goHere() {
            UIUtils.startActivity(RegisterActivity::class.java)
        }
    }

    private var phone: String = ""
    private var code: String = ""

    fun onSubmit(view: View) {
        phone = et_phone.text.toString().trim()
        code = et_code.text.toString().trim()
        if (UIUtils.isEmpty(code)) {
            UIUtils.showSnackBar(view, "验证码不能为空")
            return
        }
        if (UIUtils.isEmpty(phone)) {
            UIUtils.showSnackBar(view, "手机号不能为空")
            return
        }
        SetPasswordActivity.goHere(phone,code)
//        SmsHelper.onSubmit(view.context, phone, code)
    }
}

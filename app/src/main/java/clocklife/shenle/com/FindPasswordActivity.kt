package clocklife.shenle.com

import android.content.Context
import android.os.Bundle
import android.view.View
import com.mob.mobapi.API
import com.mob.mobapi.APICallback
import com.mob.mobapi.MobAPI
import com.mob.mobapi.apis.UserCenter
import kotlinx.android.synthetic.main.activity_find_password.*
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import slmodule.shenle.com.BaseActivity
import slmodule.shenle.com.utils.UIUtils

class FindPasswordActivity : BaseActivity(){
    override fun getRootView(): Int {
        return R.layout.activity_find_password
    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        userPassword1.hint = UIUtils.getString(R.string.set_password)
        userPassword2.hint = UIUtils.getString(R.string.set_password_again)
        login.setOnClickListener {
            if (checkTrue()) {
                val loginApi = MobAPI.getAPI(UserCenter.NAME) as UserCenter
                loginApi.login(userName.editText?.text.toString().trim(), userPassword.editText?.text.toString().trim(), object : APICallback {
                    override fun onSuccess(p0: API?, p1: Int, p2: MutableMap<String, Any>?) {
                        MainActivity.goHere()
                    }

                    override fun onError(p0: API?, p1: Int, p2: Throwable?) {
                        val jsonObject = JSONObject(p2?.message)
                        UIUtils.showToastSafe(jsonObject.optString("message"))
                    }
                })
            }
        }
    }

    private fun  checkTrue(): Boolean {
        return true
    }


    companion object {
        @JvmStatic fun goHere() {
            UIUtils.startActivity(FindPasswordActivity::class.java)
        }
    }


//    override fun new_initView() {
//        setContentView(R.layout.activity_change_password)
//        tv_code.setOnClickListener {
//            if(UIUtils.isEmpty(mobile)) {
//                mobile = et_phone.getText().toString().trim()
//            }else{
//                val trim = et_phone.getText().toString().trim()
//                if(!trim.contains("*")){
//                    mobile = trim
//                }
//            }
//            MyZYT.setYZM(mobile,tv_code) }
//    }
//
//    private var isBackLogin: Boolean? = false
//
//    override fun new_init(baseBundle: Bundle?) {
//        isBackLogin = baseBundle?.getBoolean("isBackLogin")
//        getHttp(Constants.PHONEAUTH_GETPHONE,null,"phone",this)
//    }
//    private var pwd2: String? = null
//    var mobile = ""
//    fun onSubmit(view: View) {
//        val pwd1 = et_password1.getText().toString().trim()
//        pwd2 = et_password2.getText().toString().trim()
//        var code = et_code.getText().toString().trim()
//        if(UIUtils.isEmpty(mobile)) {
//            mobile = et_phone.getText().toString().trim()
//        }else{
//            val trim = et_phone.getText().toString().trim()
//            if(!trim.contains("*")){
//                mobile = trim
//            }
//        }
//        if (pwd1.length < 8) {
//            UIUtils.showToastSafe(R.string.str_set_password_notice)
//            return
//        }
//        if (pwd1 != pwd2) {
//            UIUtils.showToastSafe("两次密码不一致")
//            return
//        }
//        if (mobile.isEmpty()){
//            UIUtils.showToastSafe("手机号不能为空")
//            return
//        }
//        if (code.isEmpty()){
//            UIUtils.showToastSafe("验证码不能为空")
//            return
//        }
//        val params = RequestParams()
//        params.put("newpasswd", pwd1)
//        params.put("code", code)
//        params.put("mobile", mobile)
//        bt_submit.isEnabled = false
//        postHttp(Constants.SET_NEW_PASSWORD, params,"submit", this)
//    }
}

package clocklife.shenle.com

import android.os.Build
import android.os.Bundle
import android.support.v7.widget.Toolbar
import clocklife.shenle.com.base.BaseAppActivity
import clocklife.shenle.com.base.data.BaseAppState
import clocklife.shenle.com.db.bean.AppUserInfo
import clocklife.shenle.com.db.bean.AppUserInfo_Table
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.api.BasicCallback
import com.mob.ums.OperationCallback
import com.mob.ums.UMSSDK
import com.mob.ums.User
import kotlinx.android.synthetic.main.activity_login.*
import slmodule.shenle.com.BaseActivity
import slmodule.shenle.com.db.DBHelper
import slmodule.shenle.com.utils.UIUtils
import java.util.regex.Pattern


/**
 * 登录
 */
class LoginActivity : BaseAppActivity() {
    override fun initToolBar(): Toolbar? {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(UIUtils.getColor(R.color.bg_20))
        }
        return null
    }

    override fun getRootView(): Int {
        return R.layout.activity_login
    }

    override fun onTest() {
        userName.editText?.setText("15810111946")
        userPassword.editText?.setText("shen1le1")
    }
    override fun initOnCreate(savedInstanceState: Bundle?) {
        userName.hint = UIUtils.getString(R.string.prompt_name)
        userPassword.hint = UIUtils.getString(R.string.prompt_password)
        userPassword.isPasswordVisibilityToggleEnabled = true
        var appUserInfo = DBHelper.querySingle(AppUserInfo::class.java)
        if(appUserInfo!=null){
            userName.editText?.setText(appUserInfo.phone)
            userPassword.editText?.setText(appUserInfo.password)
        }
        login.setOnClickListener{
            if(checkTrue()){
//                val loginApi = MobAPI.getAPI(UserCenter.NAME)as UserCenter
//                loginApi.login(userName.editText?.text.toString().trim(),userPassword.editText?.text.toString().trim(), object : APICallback {
//                    override fun onSuccess(p0: API?, p1: Int, p2: MutableMap<String, Any>?) {
//                        MainActivity.goHere()
//                    }
//
//                    override fun onError(p0: API?, p1: Int, p2: Throwable?) {
//                        val jsonObject = JSONObject(p2?.message)
//                        UIUtils.showToastSafe(jsonObject.optString("msg"))
//                    }
//                })
                dialog.show()
                UMSSDK.loginWithPhoneNumber("86", userName.editText?.text.toString().trim(),userPassword.editText?.text.toString().trim(), object: OperationCallback<User>() {
                    override fun onSuccess(user: User) {
                        // 执行成功的操作
                        dialog.dismiss()
                        val where = AppUserInfo_Table.uid.eq(user.id.get())
                        val appUserInfo = DBHelper.querySingleOrMake(AppUserInfo::class.java,where)
                        appUserInfo.uid = user.id.get()
                        appUserInfo.name = user.nickname.get()
                        appUserInfo.phone = user.phone.get()
                        if (user.gender.get()==null){
                            appUserInfo.gender = 0
                        }else{
                            appUserInfo.gender = user.gender.get().code()
                        }
                        appUserInfo.photo = user.avatar.get().get(0)
                        appUserInfo.hasLogin = true
                        appUserInfo.sign = user.signature.get()
                        appUserInfo.password = userPassword.editText?.text.toString()
                        val token = user.getCustomField("token")
                        if(token!=null&&token.value() is String){
                            appUserInfo.token = token.value() as String
                        }
                        appUserInfo.update()
                        BaseAppState.setData(appUserInfo)
                        JMessageClient.login("sl"+user.phone.get(), userPassword.editText?.text.toString().trim(), object : BasicCallback(){
                            override fun gotResult(code: Int, p1: String?) {
                                if (code==0){
                                    //登录成功
                                    MainActivity.goHere()
                                    UIUtils.finishDelay(2,this@LoginActivity)
                                }else if(code==801003){
                                    JMessageClient.register("sl"+user.phone.get(), userPassword.editText?.text.toString().trim(), object :BasicCallback(){
                                        override fun gotResult(code: Int, p1: String?) {
                                            if (code==0){
                                                //注册成功
                                                JMessageClient.login("sl"+user.phone.get(), userPassword.editText?.text.toString().trim(), object : BasicCallback() {
                                                    override fun gotResult(code: Int, p1: String?) {
                                                        if (code == 0) {
                                                            //登录成功
                                                        }
                                                        MainActivity.goHere()
                                                        UIUtils.finishDelay(2, this@LoginActivity)
                                                        UIUtils.showToastSafe("code=${code}"+p1)
                                                    }
                                                })
                                            }else{
                                                MainActivity.goHere()
                                                UIUtils.finishDelay(2, this@LoginActivity)
                                                UIUtils.showToastSafe("code=${code}"+p1)
                                            }

                                        }})
                                }else{
                                    MainActivity.goHere()
                                    UIUtils.finishDelay(2,this@LoginActivity)
                                }
                                UIUtils.showToastSafe("code=${code}"+p1)
                            }
                        })
                    }

                    override fun  onCancel() {
                        // 执行取消的操作
                        dialog.dismiss()
                    }

                    override fun onFailed(t:Throwable) {
                        // 提示错误信息
                        dialog.dismiss()
                        UIUtils.showToastSafe(t.message)
                    }
                });
            }}
        bt_register.setOnClickListener{
            RegisterActivity.goHere()
        }
        bt_find_password.setOnClickListener{
            FindPasswordActivity.goHere()
        }
    }

    private fun checkTrue():Boolean {
        if(userName.editText?.text.toString().trim().length==0){
            userName.error = "${userName.hint}不能为空"
            return false
        }
        if(userPassword.editText?.text.toString().trim().length==0){
            userPassword.error = "${userPassword.hint}不能为空"
            return false
        }
        if(userPassword.editText?.text.toString().trim().length<8){
            userPassword.error = "${userPassword.hint}不能小于8位"
            return false
        }
        val pattern = Pattern.compile("^[a-zA-Z]\\w{7,17}$")//以字母开头，长度在8~18之间，只能包含字符、数字和下划线。
        val matches = pattern.matcher(userPassword.editText?.text.toString().trim()).matches()
        if(!matches){
            userPassword.error = "${userPassword.hint}只能以字母开头,包含字符、数字和下划线"
            return false
        }
        userName.isErrorEnabled = false
        userPassword.isErrorEnabled = false
        return true
    }

    companion object {
        fun goHere(){
            UIUtils.startActivity(LoginActivity::class.java)
        }
    }
}


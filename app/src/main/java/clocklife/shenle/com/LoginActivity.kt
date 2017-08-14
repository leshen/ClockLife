package clocklife.shenle.com

import android.os.Bundle
import com.mob.mobapi.API
import com.mob.mobapi.APICallback
import com.mob.mobapi.MobAPI
import com.mob.mobapi.apis.UserCenter
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONObject
import slmodule.shenle.com.BaseActivity
import slmodule.shenle.com.utils.UIUtils
import java.util.regex.Pattern


/**
 * 登录
 */
class LoginActivity : BaseActivity() {
    override fun getRootView(): Int {
        return R.layout.activity_login
    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        userName.hint = UIUtils.getString(R.string.prompt_name)
        userPassword.hint = UIUtils.getString(R.string.prompt_password)
        login.setOnClickListener{
            if(checkTrue()){
                val loginApi = MobAPI.getAPI(UserCenter.NAME)as UserCenter
                loginApi.login(userName.editText?.text.toString().trim(),userPassword.editText?.text.toString().trim(), object : APICallback {
                    override fun onSuccess(p0: API?, p1: Int, p2: MutableMap<String, Any>?) {
                        MainActivity.goHere()
                    }

                    override fun onError(p0: API?, p1: Int, p2: Throwable?) {
                        val jsonObject = JSONObject(p2?.message)
                        UIUtils.showToastSafe(jsonObject.optString("msg"))
                    }
                })
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


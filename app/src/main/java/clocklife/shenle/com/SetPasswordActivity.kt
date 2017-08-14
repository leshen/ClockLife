package clocklife.shenle.com

import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import com.mob.mobapi.API
import com.mob.mobapi.APICallback
import com.mob.mobapi.MobAPI
import com.mob.mobapi.apis.UserCenter
import kotlinx.android.synthetic.main.activity_setpassword.*
import slmodule.shenle.com.BaseActivity
import slmodule.shenle.com.utils.UIUtils
import java.util.regex.Pattern


class SetPasswordActivity : BaseActivity() {
    override fun getRootView(): Int {
        return R.layout.activity_setpassword
    }
    override fun initOnCreate(savedInstanceState: Bundle?) {
        val phone = intent?.getStringExtra("phone")
        userPassword1.hint = UIUtils.getString(R.string.set_password)
        userPassword2.hint = UIUtils.getString(R.string.set_password_again)
        bt_register.setOnClickListener { //注册
            if(userPassword1.editText?.text.toString().trim().length==0){
                userPassword1.error = "${userPassword1.hint}不能为空"
                return@setOnClickListener
            }
            if(userPassword1.editText?.text.toString().trim().length<8){
                userPassword1.error = "${userPassword1.hint}不能小于8位"
                return@setOnClickListener
            }
            val pattern = Pattern.compile("^[a-zA-Z]\\w{7,17}$")//以字母开头，长度在8~18之间，只能包含字符、数字和下划线。
            val matches = pattern.matcher(userPassword1.editText?.text.toString().trim()).matches()
            if(!matches){
                userPassword1.error = "${userPassword1.hint}"
                return@setOnClickListener
            }
            if(userPassword1.editText?.text.toString().trim()!=userPassword2.editText?.text.toString().trim()){
                userPassword2.error = "两次密码不一致"
            }
            var userCenter = MobAPI.getAPI(UserCenter.NAME) as UserCenter
            userCenter.register(phone,userPassword1.editText?.text.toString().trim(), null,object:APICallback{
                override fun onError(p0: API?, p1: Int, p2: Throwable?) {
                    UIUtils.showToastSafe(p2?.message)
                }

                override fun onSuccess(p0: API?, p1: Int, p2: MutableMap<String, Any>?) {

                }
            })
        }
    }

    companion object {
        @JvmStatic fun goHere(phone :String) {
            val bundle = Bundle()
            bundle.putString("phone",phone)
            UIUtils.startActivity(RegisterActivity::class.java,bundle)
        }
    }

    // // 获取点击事件
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            return true
        }
        return super.dispatchTouchEvent(ev)
    }
    }

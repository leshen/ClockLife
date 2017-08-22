package clocklife.shenle.com

import android.os.Bundle
import android.support.v7.widget.Toolbar
import clocklife.shenle.com.db.bean.AppUserInfo
import clocklife.shenle.com.db.bean.AppUserInfo_Table
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.api.BasicCallback
import com.mob.ums.OperationCallback
import com.mob.ums.UMSSDK
import com.mob.ums.User
import kotlinx.android.synthetic.main.activity_setpassword.*
import slmodule.shenle.com.BaseActivity
import slmodule.shenle.com.db.DBHelper
import slmodule.shenle.com.utils.RxBus
import slmodule.shenle.com.utils.UIUtils
import java.util.regex.Pattern


class SetPasswordActivity : BaseActivity() {
    override fun initToolBar(): Toolbar? {
        return toolbar
    }

    override fun getRootView(): Int {
        return R.layout.activity_setpassword
    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        val phone = intent?.getStringExtra("phone")
        val code = intent?.getStringExtra("code")
        userPassword1.hint = UIUtils.getString(R.string.set_password)
        userPassword2.hint = UIUtils.getString(R.string.set_password_again)
        toolbar.setNavigationOnClickListener { onBack(it) }
        bt_register.setOnClickListener {
            //注册
            if (userPassword1.editText?.text.toString().trim().length == 0) {
                userPassword1.error = "${userPassword1.hint}不能为空"
                return@setOnClickListener
            }
            if (userPassword1.editText?.text.toString().trim().length < 8) {
                userPassword1.error = "${userPassword1.hint}不能小于8位"
                return@setOnClickListener
            }
            val pattern = Pattern.compile("^[a-zA-Z]\\w{7,17}$")//以字母开头，长度在8~18之间，只能包含字符、数字和下划线。
            val matches = pattern.matcher(userPassword1.editText?.text.toString().trim()).matches()
            if (!matches) {
                userPassword1.error = "${userPassword1.hint}"
                return@setOnClickListener
            }
            if (userPassword1.editText?.text.toString().trim() != userPassword2.editText?.text.toString().trim()) {
                userPassword2.error = "两次密码不一致"
            }

//            var userCenter = MobAPI.getAPI(UserCenter.NAME) as UserCenter
//            userCenter.register(phone, userPassword1.editText?.text.toString().trim(), "leshen_s@qq.com", object : APICallback {
//                override fun onError(p0: API?, p1: Int, p2: Throwable?) {
//                    UIUtils.showToastSafe(p2?.message)
//                }
//
//                override fun onSuccess(p0: API?, p1: Int, p2: MutableMap<String, Any>?) {
//
//                    if(p1==1){
//                        if (p2?.get("retCode") == "200") {
//                            //TODO 保存密码
//                            val token = p2?.get("result")
//                            MainActivity.goHere()
//                        }
//                    }
//                }
//            })
//            String country 国家代码，如：中国对应86
//            String phone 手机号码
//            String vcode 验证码
//            String password 密码
//            User user 一同提交注册的用户资料
//            OperationCallback callback 操作回调
            dialog.show()
            UMSSDK.registerWithPhoneNumber("86",phone,code,userPassword1.editText?.text.toString().trim(),User(),object: OperationCallback<User>() {
                override fun onCancel() {
                    dialog.dismiss()
                }

                override fun onFailed(p0: Throwable) {
                    dialog.dismiss()
                    UIUtils.showToastSafe(p0.message)
                    if (p0.message!!.contains("已经存在")){
                        LoginActivity.goHere()
                        RxBus.get().post(RegisterSuccess())
                        finish()
                    }else if (p0.message!!.contains("验证码")){
                        finish()
                    }
                }

                override fun onSuccess(user: User) {
                    dialog.dismiss()
                    val where = AppUserInfo_Table.uid.eq(user.id.get())
                    var appUserInfo = DBHelper.querySingleOrMake(AppUserInfo::class.java,where)
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
                    appUserInfo.password = userPassword1.editText?.text.toString().trim()
                    appUserInfo.update()
                    JMessageClient.register("sl"+user.nickname.get(), userPassword1.editText?.text.toString().trim(), object :BasicCallback(){
                        override fun gotResult(code: Int, p1: String?) {
                            if (code==0){
                                //注册成功
                            }
                            UIUtils.showToastSafe("code=${code}"+p1)
                            MainActivity.goHere()
                            RxBus.get().post(RegisterSuccess())
                            finish()
                        }
                    })
                }
            })
        }
    }

    companion object {
        @JvmStatic fun goHere(phone: String,code:String) {
            val bundle = Bundle()
            bundle.putString("phone", phone)
            bundle.putString("code", code)
            UIUtils.startActivity(SetPasswordActivity::class.java, bundle)
        }
    }

//    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            return true
//        }
//        return false
//    }
}
class RegisterSuccess


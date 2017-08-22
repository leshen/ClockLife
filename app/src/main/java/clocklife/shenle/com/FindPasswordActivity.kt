package clocklife.shenle.com

import android.os.Bundle
import android.support.v7.widget.Toolbar
import clocklife.shenle.com.db.bean.AppUserInfo
import clocklife.shenle.com.db.bean.AppUserInfo_Table
import com.mob.ums.OperationCallback
import com.mob.ums.UMSSDK
import kotlinx.android.synthetic.main.activity_find_password.*
import slmodule.shenle.com.BaseActivity
import slmodule.shenle.com.db.DBHelper
import slmodule.shenle.com.utils.RxBus
import slmodule.shenle.com.utils.UIUtils

class FindPasswordActivity : BaseActivity(){
    override fun initToolBar(): Toolbar? {
        return toolbar
    }

    override fun getRootView(): Int {
        return R.layout.activity_find_password
    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        userPassword1.hint = UIUtils.getString(R.string.set_new_password)
        userPassword2.hint = UIUtils.getString(R.string.set_password_again)
        UIUtils.onClick(bt_submit,{
            val phone = et_phone.text.toString().trim()
            val code = et_code.text.toString().trim()
            if (UIUtils.isEmpty(code)) {
                UIUtils.showSnackBar(it, "验证码不能为空")
                return@onClick
            }
            if (UIUtils.isEmpty(phone)) {
                UIUtils.showSnackBar(it, "手机号不能为空")
                return@onClick
            }
            dialog.show()
            UMSSDK.resetPasswordWithPhoneNumber("86",phone,code,userPassword1.editText?.text.toString().trim(),object: OperationCallback<Void>() {
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

                override fun onSuccess(p0: Void?) {
                    dialog.dismiss()
                    val user = DBHelper.querySingleOrMake(AppUserInfo::class.java, AppUserInfo_Table.phone.eq(phone))
                    user.phone = phone
                    user.password = userPassword1.editText?.text.toString().trim()
                    user.update()
                    LoginActivity.goHere()
                    finish()
                }
            })
        })
    }

    companion object {
        @JvmStatic fun goHere() {
            UIUtils.startActivity(FindPasswordActivity::class.java)
        }
    }

}

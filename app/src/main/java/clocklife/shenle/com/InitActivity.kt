package clocklife.shenle.com

import android.os.Bundle
import android.support.v7.widget.Toolbar
import clocklife.shenle.com.base.BaseAppActivity
import clocklife.shenle.com.base.dao.DbDao
import clocklife.shenle.com.base.data.BaseAppState
import clocklife.shenle.com.db.bean.AppUserInfo
import clocklife.shenle.com.help.MyUtils
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.api.BasicCallback
import io.reactivex.Observable
import slmodule.shenle.com.helper.DBHelper
import slmodule.shenle.com.utils.UIUtils
import java.util.concurrent.TimeUnit


class InitActivity : BaseAppActivity() {
    override fun initToolBar(): Toolbar? {
        return null
    }


    override fun getRootView(): Int {
        return R.layout.activity_init
    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        val appUserInfo = DbDao.findUserByIsLogin(true)
        if (appUserInfo != null) {
            BaseAppState.userUid = appUserInfo.uid
            BaseAppState.userName = appUserInfo.name
            BaseAppState.userPhone = appUserInfo.phone
            BaseAppState.userPhoto = appUserInfo.photo
            Observable.timer(2, TimeUnit.SECONDS).compose(this.bindToLifecycle()).subscribe {
                if (MyUtils.isLogin()) {
                    JMessageClient.login("sl"+appUserInfo.phone, appUserInfo.password, object : BasicCallback(){
                        override fun gotResult(code: Int, p1: String?) {
                            if (code==0){
                                //登录成功
                                MainActivity.goHere()
                                UIUtils.finishDelay(2,this@InitActivity)
                            }else if(code==801003){
                                JMessageClient.register("sl"+appUserInfo.phone, appUserInfo.password, object : BasicCallback(){
                                    override fun gotResult(code: Int, p1: String?) {
                                        if (code==0){
                                            //注册成功
                                            JMessageClient.login("sl"+appUserInfo.phone, appUserInfo.password, object : BasicCallback() {
                                                override fun gotResult(code: Int, p1: String?) {
                                                    if (code == 0) {
                                                        //登录成功
                                                    }
                                                    MainActivity.goHere()
                                                    UIUtils.finishDelay(2, this@InitActivity)
                                                    UIUtils.showToastSafe("code=${code}"+p1)
                                                }
                                            })
                                        }else{
                                            MainActivity.goHere()
                                            UIUtils.finishDelay(2, this@InitActivity)
                                            UIUtils.showToastSafe("code=${code}"+p1)
                                        }

                                    }})
                            }else{
                                MainActivity.goHere()
                                UIUtils.finishDelay(2,this@InitActivity)
                            }
                            UIUtils.showToastSafe("code=${code}"+p1)
                        }
                    })
                }
                UIUtils.finishDelay(1, this)
            }
        } else {
            Observable.timer(2, TimeUnit.SECONDS).compose(this.bindToLifecycle()).subscribe {
                MyUtils.isLogin()
                UIUtils.finishDelay(1, this)
            }
        }
    }

    companion object {
        fun goHere() {
            UIUtils.startActivity(InitActivity::class.java)
        }
    }
}

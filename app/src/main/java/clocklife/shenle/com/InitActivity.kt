package clocklife.shenle.com

import android.os.Bundle
import android.support.v7.widget.Toolbar
import clocklife.shenle.com.base.BaseAppActivity
import clocklife.shenle.com.base.data.BaseAppState
import clocklife.shenle.com.db.bean.AppUserInfo
import clocklife.shenle.com.db.bean.AppUserInfo_Table
import clocklife.shenle.com.help.MyUtils
import io.reactivex.Observable
import slmodule.shenle.com.BaseActivity
import slmodule.shenle.com.db.DBHelper
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
        var appUserInfo = DBHelper.querySingle(AppUserInfo::class.java, AppUserInfo_Table.hasLogin.eq(true))
        if (appUserInfo != null) {
            BaseAppState.userUid = appUserInfo.uid
            BaseAppState.userName = appUserInfo.name
            BaseAppState.userPhone = appUserInfo.phone
            BaseAppState.userPhoto = appUserInfo.photo
            Observable.timer(2, TimeUnit.SECONDS).compose(this.bindToLifecycle()).subscribe {
                if (MyUtils.isLogin()) {
                    MainActivity.goHere()
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

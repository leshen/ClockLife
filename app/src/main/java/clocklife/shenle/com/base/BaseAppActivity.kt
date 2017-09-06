package clocklife.shenle.com.base

import android.os.Bundle
import clocklife.shenle.com.base.data.BaseAppState
import clocklife.shenle.com.db.bean.AppUserInfo
import clocklife.shenle.com.db.bean.AppUserInfo_Table
import slmodule.shenle.com.BaseActivity
import slmodule.shenle.com.helper.DBHelper

/**
 * Created by shenle on 2017/9/4.
 */
abstract class BaseAppActivity :BaseActivity(){
    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        var appUserInfo = DBHelper.querySingle(AppUserInfo::class.java, AppUserInfo_Table.hasLogin.eq(true))
        BaseAppState.setData(appUserInfo)
    }
}
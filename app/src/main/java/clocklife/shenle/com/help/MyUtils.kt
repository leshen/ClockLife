package clocklife.shenle.com.help

import clocklife.shenle.com.LoginActivity
import clocklife.shenle.com.base.data.BaseAppState
import slmodule.shenle.com.utils.UIUtils

/**
 * Created by shenle on 2017/8/17.
 */
class MyUtils {
    companion object {
        fun isLogin():Boolean{
            if (UIUtils.isEmpty(BaseAppState.userName)){
                LoginActivity.goHere()
                return false
            }
            return true
        }
    }
}
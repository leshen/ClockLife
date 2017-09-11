package clocklife.shenle.com.base.dao

import clocklife.shenle.com.db.bean.AppUserInfo
import slmodule.shenle.com.helper.DBHelper
import clocklife.shenle.com.db.bean.AppUserInfo_Table
/**
 * Created by Administrator on 2017/9/9 0009.
 */
class DbDao {
    companion object {
//        var user = AppUserInfo()
        fun findUserByPhone(phone :String): AppUserInfo {
            var user = DBHelper.querySingleOrMake(AppUserInfo::class.java, AppUserInfo_Table.phone.eq(phone))
            return user
        }
        fun findUserByIsLogin(isLogin :Boolean):AppUserInfo?{
            var user = DBHelper.querySingle(AppUserInfo::class.java, AppUserInfo_Table.hasLogin.eq(isLogin))
            return user
        }
        fun findUserByUserId(userId :String):AppUserInfo{
            val user = DBHelper.querySingleOrMake(AppUserInfo::class.java,AppUserInfo_Table.uid.eq(userId))
            return user
        }
    }
}
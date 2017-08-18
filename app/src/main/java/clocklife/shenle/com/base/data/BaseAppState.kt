package clocklife.shenle.com.base.data

import clocklife.shenle.com.db.bean.AppUserInfo

/**
 * Created by shenle on 2017/8/17.
 */
class BaseAppState {
    companion object {
        @JvmStatic
        var userUid: String? = ""
        @JvmStatic
        var userName: String? = ""
        @JvmStatic
        var userPhone: String? = ""
        @JvmStatic
        var userPhoto: String? = ""

        fun clear() {
            userUid = ""
            userName = ""
            userPhone = ""
            userPhoto = ""
        }

        fun setData(appUserInfo: AppUserInfo?) {
            BaseAppState.userUid = appUserInfo?.uid
            BaseAppState.userName = appUserInfo?.name
            BaseAppState.userPhone = appUserInfo?.phone
            BaseAppState.userPhoto = appUserInfo?.photo
        }
    }
}
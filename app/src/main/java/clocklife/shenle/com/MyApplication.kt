package clocklife.shenle.com

import clocklife.shenle.com.base.receiver.NotificationClickEventReceiver
import cn.jpush.im.android.api.JMessageClient
import slmodule.shenle.com.BaseApplication

/**
 * Created by shenle on 2017/8/10.
 */
class MyApplication :BaseApplication(){
    override fun initLib(){
        super.initLib()
        //设置Notification的模式
        JMessageClient.setNotificationFlag(JMessageClient.FLAG_NOTIFY_WITH_SOUND or JMessageClient.FLAG_NOTIFY_WITH_LED or JMessageClient.FLAG_NOTIFY_WITH_VIBRATE)
        //注册Notification点击的接收器
        NotificationClickEventReceiver(applicationContext)
    }
}
package clocklife.shenle.com.one

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import clocklife.shenle.com.R
import clocklife.shenle.com.base.BaseAppActivity
import clocklife.shenle.com.base.data.BaseAppState
import clocklife.shenle.com.base.message.TXMessage
import clocklife.shenle.com.db.bean.AppUserInfo
import clocklife.shenle.com.db.bean.AppUserInfo_Table
import clocklife.shenle.com.help.MyUtils
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.model.Conversation
import cn.jpush.im.android.api.options.MessageSendingOptions
import cn.jpush.im.api.BasicCallback
import com.readystatesoftware.systembartint.SystemBarTintManager
import kotlinx.android.synthetic.main.base_toolbar.*
import slmodule.shenle.com.BaseActivity
import slmodule.shenle.com.db.DBHelper
import slmodule.shenle.com.utils.TimeUtil
import slmodule.shenle.com.utils.UIUtils

class FaTieTXActivity : BaseAppActivity() {
    override fun initToolBar(): Toolbar? {
        toolbar?.title = "提醒"
        return toolbar
    }

    override fun toolbar2Setting(toolbar: Toolbar?) {

    }

    override fun setSystemBarTintColor(tintManager: SystemBarTintManager): Int {
        return R.color.bg_2
    }

    override fun getRootView(): Int {
        return R.layout.activity_fatie_tx_edit
    }

    private var id: Int? = 0

    override fun initOnCreate(savedInstanceState: Bundle?) {
        id = savedInstanceState?.getInt("id", 0)

    }

    var phone: String? = null//手机号
    var content: String? = null//内容
    var clockTime: Long = 0//提醒时间

    override fun onTest() {
        phone = "18547602110"
        content = "hahaha"
        clockTime = TimeUtil.getStringToDate("2017-09-04 12:39",TimeUtil.PATTERN_ALL_LESS)
    }

    fun onSubmit(view: View) {
        if (MyUtils.isLogin()) {
            //发布提醒
            val msg = TXMessage()
            msg.setStringExtra("content", content)
            msg.setNumberExtra("time", clockTime)
            var conv = JMessageClient.getSingleConversation("sl${phone}")
            if (null == conv) {
                conv = Conversation.createSingleConversation("sl${phone}")
            }
            val createSendMessage = conv.createSendMessage(msg)
            createSendMessage.setOnSendCompleteCallback(object : BasicCallback() {
                override fun gotResult(responseCode: Int, responseDesc: String?) {
                    if (responseCode == 0) {
                        //消息发送成功
                        UIUtils.showToastSafe("消息发送成功")
                    } else {
                        //消息发送失败
                        UIUtils.showToastSafe("消息发送失败")
                    }
                }
            })
            var options = MessageSendingOptions()
            options.setRetainOffline(true)
            JMessageClient.sendMessage(createSendMessage, options)//使用自定义的控制参数发送消息
        }
    }

    companion object {
        fun goHere(id: Int) {
            val bundle = Bundle()
            bundle.putInt("id", id)
            UIUtils.startActivity(FaTieTXActivity::class.java, bundle)
        }
    }
}
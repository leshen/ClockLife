package clocklife.shenle.com.one

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import clocklife.shenle.com.R
import clocklife.shenle.com.base.BaseAppActivity
import clocklife.shenle.com.base.message.TXMessage
import clocklife.shenle.com.help.MyUtils
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.model.Conversation
import cn.jpush.im.android.api.options.MessageSendingOptions
import cn.jpush.im.api.BasicCallback
import com.readystatesoftware.systembartint.SystemBarTintManager
import kotlinx.android.synthetic.main.activity_fatie_tx_edit.*
import kotlinx.android.synthetic.main.base_toolbar.*
import slmodule.shenle.com.utils.TimeUtil
import slmodule.shenle.com.utils.UIUtils
import java.util.*

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

    private var isAble: Boolean = false

    private var datePickerDialog: DatePickerDialog? = null

    override fun initOnCreate(savedInstanceState: Bundle?) {
        id = savedInstanceState?.getInt("id", 0)
        bt_time.setOnClickListener{
            //时间选择器
            if (!isAble) {
                isAble = true
                val calendar = Calendar.getInstance()
                // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
                if (datePickerDialog == null) {
                    // 绑定监听器
                    // 设置初始日期
                    datePickerDialog = DatePickerDialog(
                            this,
                            // 绑定监听器
                            DatePickerDialog.OnDateSetListener { dp, year, month, dayOfMonth ->
                                if (isAble) {
                                    isAble = false
                                    TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                                        bt_time.setText(year.toString() + "-" + (month + 1)
                                                + "-" + dayOfMonth + " " + hourOfDay + ":" + minute)
                                    }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
                                }
                            }, calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH))// 设置初始日期
                    datePickerDialog?.setOnDismissListener{isAble = false}
                }
                datePickerDialog?.show()
            }
        }
    }

    var phone: String? = null//手机号
    var content: String? = null//内容
    var title: String? = null//标题
    var clockTime: Long = 0//提醒时间

    override fun onTest() {
//        phone = "18547602110"
        phone = "15810111946"
        content = "hahaha"
        title = "cs"
//        clockTime = TimeUtil.getStringToDate("2017-09-05 16:15",TimeUtil.PATTERN_ALL_LESS)
        clockTime = System.currentTimeMillis()+1000*5
//        clockTime = SystemClock.elapsedRealtime()+1000*10
        ti_content.editText?.setText(content)
        ti_title.editText?.setText(title)
        ti_user.editText?.setText(phone)
        bt_time.setText(TimeUtil.getDate2String(clockTime,TimeUtil.PATTERN_ALL))
    }

    fun onSubmit(view: View) {
        if (MyUtils.isLogin()) {
            //发布提醒
            content = ti_content.editText.toString()
            title = ti_title.editText.toString()
            phone = ti_user.editText.toString()
            var time_str = bt_time.text.toString()
            if(UIUtils.isEmpty(time_str)){
                UIUtils.showSnackBar(bt_time,"提醒时间不能为空")
                return
            }
            if(UIUtils.isEmpty(content)){
                ti_content.error="提醒内容不能为空"
                return
            }
            if(UIUtils.isEmpty(title)){
                ti_content.error="提醒标题不能为空"
                return
            }
            if(UIUtils.isEmpty(phone)){
                ti_user.error="接收人电话号码不能为空"
                return
            }
            clockTime = TimeUtil.getStringToDate(bt_time.text.toString(),TimeUtil.PATTERN_ALL)
            val content = ti_content.editText.toString()
            val msg = TXMessage()
            msg.setContent(content!!)
            msg.setTime(clockTime!!)
            msg.setType(TXMessage.CLOCK_SELF)
            msg.setTitle(title!!)
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
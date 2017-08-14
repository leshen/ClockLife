package main.src.sms

import android.graphics.Color
import android.os.CountDownTimer
import android.widget.EditText
import android.widget.TextView
import cn.smssdk.SMSSDK
import cn.smssdk.EventHandler
import cn.smssdk.OnSendMessageHandler
import com.mob.mobapi.MobAPI
import com.mob.mobapi.R
import org.json.JSONObject


/**
 * Created by shenle on 2017/8/14.
 */
class SmsHelper {
    companion object {
        var eventHandler: EventHandler? = null
        fun init() {
            // 如果希望在读取通信录的时候提示用户，可以添加下面的代码，并且必须在其他代码调用之前，否则不起作用；如果没这个需求，可以不加这行代码
            SMSSDK.setAskPermisionOnReadContact(true)
        }

        fun registSMS() {
            // 创建EventHandler对象
            eventHandler = object : EventHandler() {
                override fun afterEvent(event: Int, result: Int, data: Any) {
                    if (result === SMSSDK.RESULT_COMPLETE) {
                        //回调完成
                        if (event === SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                            //提交验证码成功
                        } else if (event === SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            //获取验证码成功

                        } else if (event === SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                            //返回支持发送验证码的国家列表
                        }
                    } else {
                        (data as Throwable).printStackTrace()
                    }
                }
            }
            // 注册监听器
            SMSSDK.registerEventHandler(eventHandler)
        }

        fun unregistSMS() {
            // 注销监听器
            SMSSDK.unregisterEventHandler(eventHandler)
            eventHandler=null
        }

        /**
         * 发送验证码

         * @param et_phone
         * *
         * @param_code
         */
        fun sendYZM(phone: String, tv_code: TextView) {
            SMSSDK.getVerificationCode("中国",phone,object:OnSendMessageHandler{
                override fun onSendMessage(p0: String?, p1: String?): Boolean {
                    return true
                }

            })
            /**
             * 倒计时 60秒后可以重新发送验证码
             */
            val mc = MyCountTime((60 * 1000).toLong(), 1000, tv_code)
            mc.start()
        }
        fun onSubmit(phone: String, code:String){
            SMSSDK.submitVerificationCode("中国",phone,code)
        }
    }
    /**
     * 定时发送验证码类

     * @author shenle
     */
    class MyCountTime/* 定义一个倒计时的内部类 */
    (millisInFuture: Long, countDownInterval: Long,
     private val tv_code: TextView) : CountDownTimer(millisInFuture, countDownInterval) {
        private var currentTextColor: Int = 0

        override fun onFinish() {
            tv_code.text = "发验证码"
            tv_code.setTextColor(currentTextColor)
            tv_code.isClickable = true
            tv_code.isFocusable = true
        }

        override fun onTick(millisUntilFinished: Long) {
            tv_code.text = (millisUntilFinished / 1000).toString() + "s后重发"
            currentTextColor = tv_code.currentTextColor
            tv_code.setTextColor(Color.parseColor("#999999"))
            tv_code.isClickable = false
            tv_code.isFocusable = false
        }
    }
}
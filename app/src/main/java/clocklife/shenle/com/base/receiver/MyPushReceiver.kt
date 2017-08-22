package clocklife.shenle.com.base.receiver

import android.content.Context
import android.util.Log
import com.iflytek.pushclient.PushReceiver


class MyPushReceiver : PushReceiver() {
    override fun onTags(p0: Context?, p1: String?, p2: String?, p3: Int) {
    }

    /**
     * 调用PushManager.startWork后，sdk将对 push server发起绑定请求，这个过程是异步
     * 的。绑定请求的结果通过onBind返回。
     */
    override protected fun onBind(context: Context, did: String, appId: String, errorCode: Int) {
        Log.d(TAG, "onBind|did = $did,appId = $appId, errorCode = $errorCode")
    }

    /**
     * 调用PushManager.stopWork解绑回调，sdk将对该应用进行解绑，该应用的消息将被丢弃
     * @param context
     * *
     * @param did
     * *
     * @param appId
     * *
     * @param errorCode
     */
    override protected fun onUnBind(context: Context, did: String, appId: String, errorCode: Int) {
        Log.d(TAG, "onUnBind | did = $did,appId = $appId, errorCode = $errorCode")
    }

    /**
     * 接收透传消息的函数。

     * @param context
     * *
     * @param msgId   消息的id
     * *
     * @param content 透传消息的内容，由各应用自己解析
     */
    override protected fun onMessage(context: Context, msgId: String, content: ByteArray) {
        Log.d(TAG, "onMessage | msgId = " + msgId + ", content = " + String(content))
    }

    /**
     * 接收通知点击的函数。注：推送通知被用户点击前，应用无法通过接口获取通知的内容。

     * @param context
     * *
     * @param messageId
     * *
     * @param title
     * *
     * @param content
     * *
     * @param extraContent
     */
    override protected fun onClickNotification(context: Context, messageId: String, title: String, content: String, extraContent: String) {

    }

    companion object {
        private val TAG = "MyPushReceiver"
    }
}
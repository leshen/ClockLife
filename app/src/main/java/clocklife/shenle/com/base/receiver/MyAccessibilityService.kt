package clocklife.shenle.com.base.receiver

import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import slmodule.shenle.com.utils.BaseAccessibilityService


/**
 * 辅助service
 * Created by shenle on 2017/9/12.
 */
class MyAccessibilityService : BaseAccessibilityService() {
    override fun onInterrupt() {
        //必须。这个在系统想要中断AccessibilityService返给的响应时会调用。在整个生命周期里会被调用多次。
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        //必须。通过这个函数可以接收系统发送来的AccessibilityEvent，接收来的AccessibilityEvent是经过过滤的，过滤是在配置工作时设置的
        val eventType = event?.getEventType()
        when (eventType) {
            AccessibilityEvent.TYPE_VIEW_CLICKED -> {//界面点击
            }
            AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED -> {//界面文字改动
            }
        }
    }

    override fun onServiceConnected() {
        //可选。系统会在成功连接上你的服务的时候调用这个方法，在这个方法里你可以做一下初始化工作，例如设备的声音震动管理，也可以调用setServiceInfo()进行配置工作
            //已经在xml配置,这里可以动态修改
//        val serviceInfo = AccessibilityServiceInfo()
//        serviceInfo.eventTypes = AccessibilityEvent.TYPES_ALL_MASK
//        serviceInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
//        serviceInfo.packageNames = arrayOf("com.tencent.mm")
//        serviceInfo.notificationTimeout = 100
//        setServiceInfo(serviceInfo)
        super.onServiceConnected()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        //可选。在系统将要关闭这个AccessibilityService会被调用。在这个方法中进行一些释放资源的工作。
        return super.onUnbind(intent)
    }
}
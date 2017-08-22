package clocklife.shenle.com.base.receiver

import android.content.Context
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.content.*
import cn.jpush.im.android.api.enums.ContentType
import cn.jpush.im.android.api.enums.ConversationType
import cn.jpush.im.android.api.event.MessageEvent
import cn.jpush.im.android.api.event.NotificationClickEvent
import cn.jpush.im.android.api.model.Conversation


/**
 * Created by shenle on 2017/8/22.
 */
class NotificationClickEventReceiver {
    var mContext: Context

    constructor(context: Context) {
        mContext = context
        //注册接收消息事件
        JMessageClient.registerEventReceiver(this)
    }

    /**
     * 通知点击事件
     * @param notificationClickEvent
     */
    fun onEvent(notificationClickEvent: NotificationClickEvent?) {
        if (null == notificationClickEvent) {
            return
        }
        val msg = notificationClickEvent.message
        if (msg != null) {
            val targetId = msg.targetID
            val appKey = msg.fromAppKey
            val type = msg.targetType
            val conv: Conversation
//            val notificationIntent = Intent(mContext, ChatActivity::class.java)
            if (type == ConversationType.single) {
                conv = JMessageClient.getSingleConversation(targetId, appKey)
//                notificationIntent.putExtra(JGApplication.TARGET_ID, targetId)
//                notificationIntent.putExtra(JGApplication.TARGET_APP_KEY, appKey)
            } else {
                conv = JMessageClient.getGroupConversation(java.lang.Long.parseLong(targetId))
//                notificationIntent.putExtra(JGApplication.GROUP_ID, java.lang.Long.parseLong(targetId))
            }
//            notificationIntent.putExtra(JGApplication.CONV_TITLE, conv.title)
            conv.resetUnreadCount()
//            notificationIntent.putExtra("fromGroup", false)
//            notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
//            mContext.startActivity(notificationIntent)
        }
    }
    /**
     * 收到消息处理
     * @param MessageEvent
     */
    fun onEvent(event: MessageEvent) {
        var msg = event.getMessage()

        when (msg.getContentType()) {
            ContentType.text -> {
                //处理文字消息
                var textContent = msg.getContent() as TextContent
                textContent.getText()
            }
            ContentType.image -> {
                //处理图片消息
                var imageContent = msg.getContent() as ImageContent
                imageContent.getLocalPath()//图片本地地址
                imageContent.getLocalThumbnailPath()//图片对应缩略图的本地地址
            }
            ContentType.voice -> {
                //处理语音消息
                var voiceContent = msg.getContent() as VoiceContent
                voiceContent.getLocalPath();//语音文件本地地址
                voiceContent.getDuration();//语音文件时长
            }
            ContentType.custom -> {
                //处理自定义消息
                var customContent = msg.getContent() as CustomContent
                customContent.getNumberValue("custom_num") //获取自定义的值
                customContent.getBooleanValue("custom_boolean")
                customContent.getStringValue("custom_string")
            }
            ContentType.eventNotification -> {
                //处理事件提醒消息
                var eventNotificationContent = msg.getContent() as EventNotificationContent
                when (eventNotificationContent.getEventNotificationType()) {
                    EventNotificationContent.EventNotificationType.group_member_added -> {
                        //群成员加群事件
                    }
                    EventNotificationContent.EventNotificationType.group_member_removed -> {
                        //群成员被踢事件
                    }
                    EventNotificationContent.EventNotificationType.group_member_exit -> {
                        //群成员退群事件
                    }
                    EventNotificationContent.EventNotificationType.group_info_updated -> {//since 2.2.1
                        //群信息变更事件
                    }
                }
            }
        }
    }
}
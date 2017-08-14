package clocklife.shenle.com.xunfeitongjilib

/**
 * Created by shenle on 2017/8/8.
 */
class XunFeiTongJiHelper {
//    1 开启调试模式
//    FlowerCollector.setDebugMode( Boolean flag)
//    开启后可以在 logcat 中看到相应的日志,默认不开启。
//    5.1.2 设置会话时间,默认 30*1000ms(单位为毫秒)。
//    FlowerCollector.setSessionContinueMillis(long time)
//    5.1.3 开启页面统计模式
//    系统默认通过 OnResume 和 OnPause 统计页面,可以调用 FlowerCollector.openPageMode(true); 开启页面统计模式。开启后,可以通过 onPageStart(String pageName) ,onPageEnd(String pageName) 自定义页面的名称。
//    5.1.4 开启自动获取位置信息
//    FlowerCollector.setAutoLocation(Boolean flag) 开启后会在每次发送日志时获取设备位置信息,默认开启。
//    5.1.5 开启自动捕获异常信息
//    FlowerCollector.setCaptureUncaughtException(Boolean flag) 开启后会在自动捕获程序的异常信息,默认关闭。
//    个性化接口：
//    5.1.6 设置用户性别
//    FlowerCollector.setGender(Context context,Gender gender);
//    5.1.7 设置用户年龄
//    FlowerCollector.setAge(Context context, int age);
//    5.1.8 设置用户 id
//    FlowerCollector.setUserID(Context context, String userId);
//    在希望记录事件发生的地方调用如下方法:
//    FlowerCollector.onEvent(Context context, String eventId)
//    例如：用户点击了一次播放音乐：
//    FlowerCollector.onEvent(Context mContext, "music")
//    如果您需要记录音乐的名称：
//    FlowerCollector.onEvent(Context mContext,"music","爸爸去哪里哇")
//    如果您还需要记录更详细的信息：
//    HashMap map = new HashMap();
//    map.put("song", "爸爸去哪里哇");
//    map.put("type", "pop");
//    map.put("format", "mp3");
//    FlowerCollector.onEvent(this, "music",map);
//    5.2.2 记录事件的持续时间（单位毫秒）
//    1. 自己计算,传入时间
//    FlowerCollector.onEventDuration(Context context, String eventId, long duration)
//    例如：音乐播放了5分钟
//    FlowerCollector.onEventDuration(mContext,"music", 5 * 60 * 1000);
//    2. 通过SDK计算
//    在事件开始时调用FlowerCollector.onEventBegin (Context context, String eventId)
//    在事件结束时调用FlowerCollector.onEventEnd (Context context, String eventId)
//    注意：
//    (1) 请确保事件开始和结束的eventId 一致,在Debug模式下可以看到事件是否添加成功的log。
//    (2) event_id 和 label不能使用特殊字符，且长度不能超过128个字节；map中的key和value 都不能使用特殊字符，key 和value 均不能超过128个字节。
//    5.3 错误信息记录
//    1. 用户可以通过调用OnError(Context context, String msg) 传入程序的错误信息。
//    2. 如果您的应用在使用过程中不幸发生崩溃，我们也会将错误信息自动记录下来以供您进行分析。实现此功能需要您在程序入口处调用setCaptureUncaughtException(true)，开启异常信息自动捕获功能。
//    5.4 立即上传日志
//    调用flush()接口会立即发送一次日志，此功能可用作调试，不建议作为发送策略使用。
}
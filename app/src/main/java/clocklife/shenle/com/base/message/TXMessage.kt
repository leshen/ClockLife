package clocklife.shenle.com.base.message

import cn.jpush.im.android.api.content.CustomContent

/**
 * Created by shenle on 2017/9/1.
 */
class TXMessage : CustomContent() {
    companion object {
        val CLOCK_SELF = 0
        val CLOCK_OTHER = 1
    }
    fun setTime(time :Long){
        setNumberExtra("time",time)
    }
    fun getTime():Long{
        return getNumberValue("time") as Long
    }
    fun setContent(content :String){
        setStringExtra("content",content)
    }
    fun getContent():String{
        return getStringValue("content")
    }
    fun setTitle(content :String){
        setStringExtra("title",content)
    }
    fun getTitle():String{
        return getStringValue("title")
    }
    fun setType(type :Int){
        setNumberExtra("type",type)
    }
    fun getType():Int{
        return getNumberValue("type") as Int
    }
}
package clocklife.shenle.com.one.fragment

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import clocklife.shenle.com.R
import clocklife.shenle.com.base.data.BaseAppState
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback
import cn.jpush.im.android.api.model.Conversation
import cn.jpush.im.android.api.model.GroupInfo
import cn.jpush.im.android.api.model.Message
import cn.jpush.im.android.api.model.UserInfo
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import jp.wasabeef.glide.transformations.CropCircleTransformation
import kotlinx.android.synthetic.main.fragment_tx_edit.view.*
import kotlinx.android.synthetic.main.holder_my_tx.view.*
import slmodule.shenle.com.BaseAdapter
import slmodule.shenle.com.BaseFragment
import slmodule.shenle.com.utils.BitmapUtils
import slmodule.shenle.com.utils.TimeUtil

/**
 * 提醒
 * Created by shenle on 2017/8/23.
 */
class TXSelfFragment : BaseFragment() {
    override fun getTitle(): CharSequence {
        return "自己"
    }

    override fun getRootView(): Int {
        return R.layout.fragment_tx_edit
    }

    private var page: Int = 1

    private lateinit var baseAdapter: BaseAdapter<Message>
    private val list_data = ArrayList<Message>()
    private var conversation: Conversation? = null

    override fun initOnCreateView(view: View?, savedInstanceState: Bundle?) {
        view?.rv?.layoutManager = LinearLayoutManager(activity)
        val conversationList = JMessageClient.getConversationList()
        list_data.clear()
        page = 1
        for (c in conversationList) {
            when (c.targetInfo) {
                is UserInfo -> {
                    conversation = c
                    val userName = (c.targetInfo as UserInfo).userName
                    if (userName == "sl${BaseAppState.userPhone}") {
                        //我自己发给自己的消息
                        val messagesFromNewest = c.getMessagesFromNewest(page, 10)
                        list_data.addAll(messagesFromNewest)
                    } else {
                        //别人发我的消息
//                        val messagesFromNewest = c.getMessagesFromNewest(page*10-9, 10)
//                        list_data.addAll(messagesFromNewest)
                    }
                }
                is GroupInfo -> {
                }
            }
        }
        baseAdapter = BaseAdapter<Message>(R.layout.holder_my_tx, list_data, object : BaseAdapter.BaseAdapterInterface<Message> {
            override fun setData(v: View, item: Message) {
                v.tv_time.text = TimeUtil.getDate2String(item.content.getNumberExtra("time").toLong(),TimeUtil.PATTERN_ALL)
                v.tv_name.text = item.content.getStringExtra("title")
                v.tv_content.text = item.content.getStringExtra("content")
                Glide.with(this@TXSelfFragment).load(item.content.getStringExtra("photo")).bitmapTransform(CropCircleTransformation(activity)).into(v.iv_photo)
//                item.fromUser.getAvatarBitmap(object : GetAvatarBitmapCallback(){
//                    override fun gotResult(p0: Int, p1: String?, p2: Bitmap?) {
//                        v.iv_photo.setImageBitmap(BitmapUtils.makeRoundCorner(p2))
//                    }
//                })
            }

        })
        view?.rv?.adapter = baseAdapter
        baseAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT)
        // 滑动最后一个Item的时候回调onLoadMoreRequested方法
        baseAdapter.setOnLoadMoreListener({
            conversation?.let {
                val messagesFromNewest = conversation!!.getMessagesFromNewest(++page, 10)
                if (messagesFromNewest.size == 0) {
                    page--
                    baseAdapter.loadMoreEnd()
                } else {
                    baseAdapter.addData(messagesFromNewest)
                    baseAdapter.loadMoreComplete()
                }
            }
        }, view?.rv)
    }

    override fun refresh() {
    }

    companion object {
        fun getInstance(): TXSelfFragment {
            return TXSelfFragment()
        }
    }
}
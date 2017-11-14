package clocklife.shenle.com.one.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import clocklife.shenle.com.R
import clocklife.shenle.com.base.data.BaseAppState
import cn.jpush.im.android.api.JMessageClient
import cn.jpush.im.android.api.model.Conversation
import cn.jpush.im.android.api.model.GroupInfo
import cn.jpush.im.android.api.model.Message
import cn.jpush.im.android.api.model.UserInfo
import com.chad.library.adapter.base.BaseQuickAdapter
import kotlinx.android.synthetic.main.fragment_my_tx.view.*
import kotlinx.android.synthetic.main.holder_my_tx.view.*
import slmodule.shenle.com.BaseAdapter
import slmodule.shenle.com.BaseFragment

/**
 * 我的提醒
 * Created by shenle on 2017/8/23.
 */
class MyTxFragment : BaseFragment() {
    override fun getTitle(): CharSequence {
        return "收到"
    }

    override fun getRootView(): Int {
        return R.layout.fragment_my_tx
    }

    private var page: Int = 1

    private lateinit var baseAdapter: BaseAdapter<Message>
    private val list_data = ArrayList<Message>()

    override fun initOnCreateView(view: View?, savedInstanceState: Bundle?) {
        view?.rv?.layoutManager = LinearLayoutManager(activity)
        val conversationList = JMessageClient.getConversationList()
        list_data.clear()
        for (c in conversationList) {
            when (c.targetInfo) {
                is UserInfo -> {
                    val userName = (c.targetInfo as UserInfo).userName
                    if (userName == "sl${BaseAppState.userPhone}") {
                        //我自己发给自己的消息
//                         val messagesFromNewest = c.getMessagesFromNewest(page, 10)
//                         list_data.addAll(messagesFromNewest)
                    } else {
                        //别人发我的消息
                        val messagesFromNewest = c.getMessagesFromNewest(page*10-9, 10)
                        list_data.addAll(messagesFromNewest)
                    }
                }
                is GroupInfo -> {
                }
            }
        }
        baseAdapter = BaseAdapter<Message>(R.layout.holder_my_tx, list_data, object : BaseAdapter.BaseAdapterInterface<Message> {
            override fun setData(v: View, item: Message) {
                v.tv_name.text = item.toString()
            }

        })
        view?.rv?.adapter = baseAdapter
        baseAdapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_RIGHT)
        // 滑动最后一个Item的时候回调onLoadMoreRequested方法
//        baseAdapter.setOnLoadMoreListener({ baseAdapter.addData() }, view?.rv)
    }

    override fun refresh() {
    }

    companion object {
        fun getInstance(): MyTxFragment {
            return MyTxFragment()
        }
    }
}
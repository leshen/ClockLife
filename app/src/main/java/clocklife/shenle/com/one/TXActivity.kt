package clocklife.shenle.com.one

import android.os.Bundle
import clocklife.shenle.com.one.fragment.MyTxFragment
import clocklife.shenle.com.one.fragment.TXEditFragment
import slmodule.shenle.com.BaseFLActivity
import slmodule.shenle.com.BaseFragment
import slmodule.shenle.com.BaseTopVpFragment
import slmodule.shenle.com.BaseVPActivity
import slmodule.shenle.com.utils.UIUtils

/**
 * 提醒:(需求)
 * 两大类(去提醒,我的提醒)
 * 1.去提醒:提醒别人,提醒自己
 * 2.我的提醒:别人提醒自己的(所有人都可以提醒自己,熟人app,
 *      可以设置不接受该条信息,会发给提醒方拒绝提示_该条提醒被拒绝,
 *      设置不接收此人信息,提醒方发送提醒前会被提示_对方拒绝被您提醒,),
 *
 * Created by shenle on 2017/5/25.
 */
class TXActivity : BaseVPActivity() {
    override fun getListFragment(): List<BaseFragment>? {
        return listOf(TXEditFragment.getInstance(),MyTxFragment.getInstance())
    }


    override var titleArr: Array<String>
        get() = arrayOf("提醒生活", "我的提醒")
        set(value) {}

    companion object {
        fun goHere(id :Int) {
            val bundle = Bundle()
            bundle.putInt("id",id)
            UIUtils.startActivity(TXActivity::class.java,bundle)
        }
    }

}
package clocklife.shenle.com.one

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import clocklife.shenle.com.R
import clocklife.shenle.com.one.fragment.MySendTxFragment
import clocklife.shenle.com.one.fragment.MyTxFragment
import clocklife.shenle.com.one.fragment.TXSelfFragment
import com.readystatesoftware.systembartint.SystemBarTintManager
import slmodule.shenle.com.BaseFragment
import slmodule.shenle.com.BaseVPActivity
import slmodule.shenle.com.utils.UIUtils

/**
 * 提醒:(需求)
 * 三大类(去提醒,我的提醒)
 * 1.自己提醒自己的:自己给自己
 * 2.别人发我的提醒:别人提醒自己的(所有人都可以提醒自己,熟人app,
 *      可以设置不接受该条信息,会发给提醒方拒绝提示_该条提醒被拒绝,
 *      设置不接收此人信息,提醒方发送提醒前会被提示_对方拒绝被您提醒,),
 * 3.我发给别人的提醒,
 *
 * Created by shenle on 2017/5/25.
 */
class TXActivity : BaseVPActivity() {
    override fun getListFragment(): List<BaseFragment>? {
        return listOf(TXSelfFragment.getInstance(),MyTxFragment.getInstance(), MySendTxFragment.getInstance())
    }
    override fun setSystemBarTintColor(tintManager: SystemBarTintManager): Int {
        return slmodule.shenle.com.R.color.text_color_5
    }

//    override var titleArr: Array<String>
//        get() = arrayOf("我的提醒", "提醒别人")
//        set(value) {}

    companion object {
        fun goHere(id :Int) {
            val bundle = Bundle()
            bundle.putInt("id",id)
            UIUtils.startActivity(TXActivity::class.java,bundle)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu):Boolean {
        getMenuInflater().inflate(R.menu.menu_tx, menu)
        return true
    }

    override fun toolbar2Setting(toolbar: Toolbar?) {
        toolbar?.setOnMenuItemClickListener({
                when (it.getItemId()) {
                    R.id.action_fatie->{
                        //发帖
                        FaTieTXActivity.goHere(0)
                    }
                }
                true
        })
    }
    override fun initOnCreate(savedInstanceState: Bundle?) {
        super.initOnCreate(savedInstanceState)


    }
}
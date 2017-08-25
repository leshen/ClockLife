package clocklife.shenle.com.one.fragment

import android.os.Bundle
import android.view.View
import clocklife.shenle.com.R
import slmodule.shenle.com.BaseFragment

/**
 * 提醒
 * Created by shenle on 2017/8/23.
 */
class TXSelfFragment :BaseFragment(){
    override fun getTitle(): CharSequence {
        return "自己"
    }

    override fun getRootView(): Int {
        return R.layout.fragment_tx_edit
    }

    override fun initOnCreateView(view: View?, savedInstanceState: Bundle?) {

    }

    override fun refresh() {
    }
    companion object {
        fun getInstance(): TXSelfFragment {
            return TXSelfFragment()
        }
    }
}
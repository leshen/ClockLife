package clocklife.shenle.com.one.fragment

import android.os.Bundle
import android.view.View
import clocklife.shenle.com.R
import slmodule.shenle.com.BaseFragment

/**
 * 我的提醒
 * Created by shenle on 2017/8/23.
 */
class MySendTxFragment : BaseFragment(){
    override fun getTitle(): CharSequence {
        return "发出"
    }
    override fun getRootView(): Int {
        return R.layout.fragment_my_tx
    }

    override fun initOnCreateView(view: View?, savedInstanceState: Bundle?) {

    }

    override fun refresh() {
    }
    companion object {
        fun getInstance(): MySendTxFragment {
            return MySendTxFragment()
        }
    }
}
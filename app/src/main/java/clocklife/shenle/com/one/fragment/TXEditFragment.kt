package clocklife.shenle.com.one.fragment

import android.os.Bundle
import android.view.View
import clocklife.shenle.com.R
import slmodule.shenle.com.BaseFragment

/**
 * 提醒
 * Created by shenle on 2017/8/23.
 */
class TXEditFragment :BaseFragment(){
    override fun getTitle(): CharSequence {
        return "提醒生活"
    }

    override fun getRootView(): Int {
        return R.layout.fragment_tx_edit
    }

    override fun initOnCreateView(view: View?, savedInstanceState: Bundle?) {

    }

    override fun refresh() {
    }
    companion object {
        fun getInstance(): TXEditFragment {
            return TXEditFragment()
        }
    }
}
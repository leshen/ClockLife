package clocklife.shenle.com.one.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import clocklife.shenle.com.R
import slmodule.shenle.com.BaseFragment

/**
 * Created by shenle on 2017/8/1.
 */
class FourFragment :BaseFragment() {
    override fun getTitle(): CharSequence {
        return "FourFragment"
    }
    override fun refresh() {

    }

    companion object {
            fun getInstance(): FourFragment {
                return Holder.instance
            }
        }
        private object Holder {
            val instance = FourFragment()
        }

    override fun getRootView(): Int {
        return R.layout.fragment_four
    }

    override fun initOnCreateView(view: View?, savedInstanceState: Bundle?) {
    }
}
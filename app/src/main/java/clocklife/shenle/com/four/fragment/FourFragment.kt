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

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater?.inflate(R.layout.fragment_four, container, false)
        return view
    }
}
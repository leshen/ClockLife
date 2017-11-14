package clocklife.shenle.com.one.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import clocklife.shenle.com.R
import com.amap.api.maps2d.SupportMapFragment
import slmodule.shenle.com.BaseFragment

/**
 * Created by shenle on 2017/8/1.
 */
class ThreeFragment : BaseFragment() {
    override fun getTitle(): CharSequence {
        return "ThreeFragment"
    }
    override fun getRootView(): Int {
        return R.layout.fragment_three
    }

    override fun initOnCreateView(view: View?, savedInstanceState: Bundle?) {
        val newInstance = SupportMapFragment.newInstance()
        childFragmentManager.beginTransaction().replace(R.id.fl,newInstance).commitAllowingStateLoss()
    }

    override fun refresh() {

    }

    companion object {
        fun getInstance(): ThreeFragment {
            return Holder.instance
        }
    }

    private object Holder {
        val instance = ThreeFragment()
    }
}
package clocklife.shenle.com.one.fragment

import android.os.Bundle
import android.view.View
import clocklife.shenle.com.R
import clocklife.shenle.com.one.FamilyMapActivity
import clocklife.shenle.com.one.TXActivity
import com.asha.ChromeLikeSwipeLayout
import com.asha.ChromeLikeSwipeLayout.dp2px
import kotlinx.android.synthetic.main.fragment_one.view.*
import slmodule.shenle.com.BaseFragment
import slmodule.shenle.com.floatwindow.FloatWindowManager
import slmodule.shenle.com.utils.UIUtils


/**
 * Created by shenle on 2017/8/1.
 */
class OneFragment : BaseFragment(), View.OnClickListener {
    override fun getTitle(): CharSequence {
        return "OneFragment"
    }
    override fun initOnCreateView(view: View?, savedInstanceState: Bundle?) {
        ChromeLikeSwipeLayout.makeConfig()
                .addIcon(R.drawable.selector_icon_add)
                .addIcon(R.drawable.selector_icon_refresh)
                .addIcon(R.drawable.selector_icon_close)
                .backgroundColor(R.color.bg_5)
//                .maxHeight(dp2px(40f))
                .radius(dp2px(35f))
                .gap(dp2px(5f))
                .circleColor(0xFF11CCFF.toInt())
                .gummyDuration(1000)
                .rippleDuration(1000)
                .collapseDuration(1000)
                .listenItemSelected({ UIUtils.showToastSafe("onItemSelected:" + it)})
                .setTo(view?.chrome_like_swipe_layout)
        view?.bt_jl?.setOnClickListener(this)
        view?.bt_tx?.setOnClickListener(this)
        view?.bt_rj?.setOnClickListener(this)
        view?.bt_gx?.setOnClickListener(this)
        view?.bt_tbda?.setOnClickListener(this)
        view?.bt_jqbm?.setOnClickListener(this)
    }

    override fun getRootView(): Int {
        return R.layout.fragment_one
    }

    override fun onClick(v: View) {
        when (v.id){
            R.id.bt_jl->{}
            R.id.bt_tx->{
                TXActivity.goHere(0)
            }
            R.id.bt_rj->{}
            R.id.bt_gx->{
            }
            R.id.bt_tbda->{}
            R.id.bt_jqbm->{
                FamilyMapActivity.goHere()
            }
        }
    }
    override fun refresh() {

    }

    companion object {
        fun getInstance(): OneFragment {
            return Holder.instance
        }
    }

    private object Holder {
        val instance = OneFragment()
    }
}

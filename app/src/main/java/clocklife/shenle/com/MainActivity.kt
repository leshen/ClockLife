package clocklife.shenle.com

import clocklife.shenle.com.one.fragment.*
import slmodule.shenle.com.BaseFragment
import slmodule.shenle.com.BaseMainActivity
import slmodule.shenle.com.utils.UIUtils

class MainActivity : BaseMainActivity() {
    override fun getMenuStrResArr(): IntArray {
        return intArrayOf(R.string.menu_a,R.string.menu_b,R.string.menu_c,R.string.menu_d,R.string.menu_e)
    }

    companion object {
        fun goHere(){
            UIUtils.startActivity(MainActivity::class.java)
        }
    }
    override fun getListFragment(): List<BaseFragment> {
        return listOf(OneFragment.getInstance(),
                TwoFragment.getInstance(),
                ThreeFragment.getInstance(),
                FourFragment.getInstance(),
                FiveFragment.getInstance())
    }
}


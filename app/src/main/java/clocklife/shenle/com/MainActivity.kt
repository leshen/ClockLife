package clocklife.shenle.com

import android.content.Intent
import clocklife.shenle.com.one.fragment.*
import slmodule.shenle.com.BaseActivity
import slmodule.shenle.com.BaseFragment
import slmodule.shenle.com.BaseMainActivity

class MainActivity : BaseMainActivity() {
    override fun getMenuStrResArr(): IntArray {
        return intArrayOf(R.string.menu_a,R.string.menu_a,R.string.menu_a,R.string.menu_a,R.string.menu_a)
    }

    companion object {
        fun goHere(activity: BaseActivity){
//            activity.startActivity(Intent(activity,MainActivity::class.java))
            activity.startActivity(MainActivity::class.java)
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


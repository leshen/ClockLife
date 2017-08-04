package slmodule.shenle.com

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.ViewGroup
import java.util.*

/**
 * 首页加载,不销毁
 */
class BasePagerAdapter(fm: FragmentManager, list: List<BaseFragment>?) : FragmentPagerAdapter(fm) {
    private val list = ArrayList<BaseFragment>()

    init {
        if (list != null) {
            this.list.addAll(list)
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return list[position].getTitle()
    }
    /**
     * 数据改变 刷新适配器

     * @param list
     */
    fun reLoadAdapter(list: List<BaseFragment>?) {
        if (list != null) {
            this.list.clear()
            this.list.addAll(list)
            notifyDataSetChanged()
        }
    }

    override fun getCount(): Int {
        return list.size
    }

//    override fun isViewFromObject(arg0: View?, arg1: Any): Boolean {
//        return arg0 === arg1
//    }

    override fun getItem(position: Int): BaseFragment {
        return list[position]
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        return super.instantiateItem(container, position)
    }

}

package slmodule.shenle.com

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.View
import kotlinx.android.synthetic.main.fragment_base_vp_top.view.*
import slmodule.shenle.com.utils.UIUtils

/**
 * Created by shenle on 2017/8/23.
 */
abstract class BaseTopVpFragment : BaseFragment(){
    override fun getRootView(): Int {
        return R.layout.fragment_base_vp_top
    }

    override fun initOnCreateView(view: View?, savedInstanceState: Bundle?) {
        initTop(view)
    }

    override fun refresh() {

    }
    private fun initTop(view :View?) {
        initVp(view)
        setTabLayoutAtt(view?.tablayout)
        view?.tablayout?.getTabAt(0)?.select()
//        将ViewPager与TabLayout关联
        view?.tablayout?.setupWithViewPager(view.viewpager,true)
    }

    fun setTabLayoutAtt(tablayout: TabLayout?) {
        //设置指示器的颜色
        tablayout?.setSelectedTabIndicatorColor(Color.GREEN)
        tablayout?.tabMode = TabLayout.MODE_FIXED
        tablayout?.setSelectedTabIndicatorColor(R.color.bg_1)
        tablayout?.setSelectedTabIndicatorHeight(2)
        tablayout?.tabGravity = TabLayout.GRAVITY_CENTER
        tablayout?.setBackgroundColor(UIUtils.getColor(R.color.bg_12))
        tablayout?.tabTextColors = activity.resources.getColorStateList(R.color.black_red_color_selector)
    }

    private var  adapter: BasePagerAdapter?=null
    private fun initVp(view:View?) {
        view?.viewpager?.setOffscreenPageLimit(getMenuStrResArr().size)
        adapter = BasePagerAdapter(
                childFragmentManager, getListFragment())
        view?.viewpager?.adapter = adapter
//        view?.viewpager?.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
//            override fun onPageScrollStateChanged(state: Int) {
//
//            }
//
//            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
//                view?.tablayout?.setScrollPosition(position,positionOffset,true)
//            }
//
//            override fun onPageSelected(position: Int) {
//            }
//
//        })
    }

    abstract fun getMenuStrResArr(): Array<String>

    abstract fun getListFragment(): List<BaseFragment>
}
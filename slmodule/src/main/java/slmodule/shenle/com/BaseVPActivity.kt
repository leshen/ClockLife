package slmodule.shenle.com

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v7.widget.Toolbar
import kotlinx.android.synthetic.main.activity_base_vp.*
import slmodule.shenle.com.utils.UIUtils

abstract class BaseVPActivity : BaseActivity() {

    override fun initToolBar(): Toolbar? {
        toolbar.setTitle("")
        return toolbar
    }


    override fun getRootView(): Int {
        return R.layout.activity_base_vp
    }

    private var id: Int = 0

    override fun initOnCreate(savedInstanceState: Bundle?) {
        id= savedInstanceState?.getInt("id",0)?:0
        initTop()
    }
    private fun initTop() {
        initVp()
        setTabLayoutAtt(tablayout)
        tablayout?.getTabAt(id)?.select()
//        将ViewPager与TabLayout关联
        tablayout?.setupWithViewPager(viewpager,true)
    }

    fun setTabLayoutAtt(tablayout: TabLayout?) {
        //设置指示器的颜色
        tablayout?.tabMode = TabLayout.MODE_FIXED
        tablayout?.setSelectedTabIndicatorColor(UIUtils.getColor(R.color.bg_1))
        tablayout?.setSelectedTabIndicatorHeight(2)
        tablayout?.tabGravity = TabLayout.GRAVITY_CENTER
        tablayout?.tabTextColors = resources.getColorStateList(R.color.red_black_color_selector)
    }

    private var  adapter: BasePagerAdapter?=null
    private fun initVp() {
//        viewpager?.setOffscreenPageLimit(titleArr.size)
        adapter = BasePagerAdapter(
                supportFragmentManager, getListFragment())
        viewpager?.adapter = adapter
    }

    abstract fun getListFragment(): List<BaseFragment>?

//    abstract var titleArr:Array<String>
}
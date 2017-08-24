package slmodule.shenle.com

import android.os.Bundle
import android.support.v7.widget.Toolbar
import kotlinx.android.synthetic.main.base_toolbar.*

open abstract class BaseFLActivity : BaseActivity() {

    override fun initToolBar(): Toolbar? {
        toolbar.setTitle(titleArr[checknum])
        return toolbar
    }

    override fun getRootView(): Int {
        return R.layout.activity_base_fl
    }

    private var id: Int = 0

    override fun initOnCreate(savedInstanceState: Bundle?) {
        id= savedInstanceState?.getInt("id",0)?:0
        var fragment = getMainFragment(id)
        supportFragmentManager.beginTransaction().replace(R.id.fl,fragment).commitAllowingStateLoss()
    }

    abstract fun getMainFragment(id :Int): BaseTopVpFragment

    abstract var titleArr:Array<String>
    var checknum = 0

    fun changeChecknum(checkednum: Int) {
        this.checknum = checkednum
        toolbar.setTitle(titleArr[checkednum])
    }
}
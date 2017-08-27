package clocklife.shenle.com.one

import android.os.Bundle
import android.support.v7.widget.Toolbar
import clocklife.shenle.com.R
import com.readystatesoftware.systembartint.SystemBarTintManager
import kotlinx.android.synthetic.main.base_toolbar.*
import slmodule.shenle.com.BaseActivity
import slmodule.shenle.com.utils.UIUtils

class FaTieTXActivity : BaseActivity() {
    override fun initToolBar(): Toolbar? {
        toolbar?.title="提醒"
        return toolbar
    }

    override fun toolbar2Setting(toolbar: Toolbar?) {

    }
    override fun setSystemBarTintColor(tintManager: SystemBarTintManager): Int {
        return R.color.bg_2
    }
    override fun getRootView(): Int {
        return R.layout.activity_fatie_tx_edit
    }

    private var id: Int? = 0

    override fun initOnCreate(savedInstanceState: Bundle?) {
        id = savedInstanceState?.getInt("id", 0)

    }

    companion object {
        fun goHere(id: Int) {
            val bundle = Bundle()
            bundle.putInt("id", id)
            UIUtils.startActivity(FaTieTXActivity::class.java, bundle)
        }
    }
}
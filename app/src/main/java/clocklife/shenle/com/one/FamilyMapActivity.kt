package clocklife.shenle.com.one

import android.os.Bundle
import android.support.v7.widget.Toolbar
import clocklife.shenle.com.R
import clocklife.shenle.com.base.BaseMapActivity
import com.amap.api.maps2d.MapView
import kotlinx.android.synthetic.main.activity_family_map.*
import kotlinx.android.synthetic.main.base_toolbar.*
import slmodule.shenle.com.utils.UIUtils

/**
 * Created by shenle on 2017/9/19.
 */
class FamilyMapActivity :BaseMapActivity() {
    companion object {
        fun goHere(){
            UIUtils.startActivity(FamilyMapActivity::class.java)
        }
    }
    override fun initView(savedInstanceState: Bundle?) {
    }


    override fun getMapView(): MapView {
        return map
    }

    override fun initToolBar(): Toolbar? {
        toolbar.title = "亲友分布"
        return toolbar
    }

    override fun getRootView(): Int {
        return R.layout.activity_family_map
    }
}
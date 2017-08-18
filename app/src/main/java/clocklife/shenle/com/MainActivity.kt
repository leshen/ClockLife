package clocklife.shenle.com

import android.support.design.widget.NavigationView
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import clocklife.shenle.com.base.data.BaseAppState
import clocklife.shenle.com.db.bean.AppUserInfo
import clocklife.shenle.com.db.bean.AppUserInfo_Table
import clocklife.shenle.com.help.MyUtils
import clocklife.shenle.com.one.fragment.*
import com.bumptech.glide.Glide
import com.mob.ums.OperationCallback
import com.mob.ums.UMSSDK
import jp.wasabeef.glide.transformations.CropCircleTransformation
import slmodule.shenle.com.BaseFragment
import slmodule.shenle.com.BaseMainActivity
import slmodule.shenle.com.db.DBHelper
import slmodule.shenle.com.utils.UIUtils



class MainActivity : BaseMainActivity() {
    override fun initNavHeaderView(navigationView: NavigationView) {
        val headerView= navigationView.getHeaderView(0)
        headerView.findViewById(R.id.ll_login).setOnClickListener{
            if (MyUtils.isLogin()){
                //去个人中心
                UIUtils.showSnackBar(it,"个人中心")
            }
        }
        (headerView.findViewById(R.id.tv_name) as TextView).text = BaseAppState.userName
        var appUserInfo = DBHelper.querySingle(AppUserInfo::class.java, AppUserInfo_Table.hasLogin.eq(true))
        if (appUserInfo!=null) {
            (headerView.findViewById(R.id.tv_sign) as TextView).text = appUserInfo.sign
            var ivPhoto = headerView.findViewById(R.id.iv_photo) as ImageView
            Glide.with(this).load(appUserInfo.photo).bitmapTransform(CropCircleTransformation(this)).into(ivPhoto)
        }
    }

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

    override fun on2NavigationItemSelected(item: MenuItem) {
        // Handle navigation view item clicks here.
        val id = item.itemId

        if (id == slmodule.shenle.com.R.id.nav_camera) {
            // Handle the camera action
        } else if (id == slmodule.shenle.com.R.id.nav_gallery) {

        } else if (id == slmodule.shenle.com.R.id.nav_slideshow) {

        } else if (id == slmodule.shenle.com.R.id.nav_manage) {

        } else if (id == slmodule.shenle.com.R.id.nav_share) {

        } else if (id == slmodule.shenle.com.R.id.nav_login_out) {
            //注销
            dialog.show()
            UMSSDK.logout(object : OperationCallback<Void>(){
                override fun onSuccess(p0: Void?) {
                    dialog.dismiss()
                    var appUserInfo = DBHelper.querySingle(AppUserInfo::class.java,AppUserInfo_Table.hasLogin.eq(true))
                    appUserInfo?.hasLogin = false
                    appUserInfo.update()
                    BaseAppState.clear()
                    InitActivity.goHere()
                    UIUtils.finishDelay(2,this@MainActivity)
                }

                override fun onFailed(p0: Throwable?) {
                    dialog.dismiss()
                    UIUtils.showToastSafe(p0?.message)
                }

                override fun onCancel() {
                    dialog.dismiss()
                }
            })
        }
    }
}


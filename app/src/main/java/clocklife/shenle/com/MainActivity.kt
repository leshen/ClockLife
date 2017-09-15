package clocklife.shenle.com

import android.app.Activity
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.Toolbar
import android.view.KeyEvent
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import clocklife.shenle.com.base.dao.DbDao
import clocklife.shenle.com.base.data.BaseAppState
import clocklife.shenle.com.db.bean.AppUserInfo
import clocklife.shenle.com.help.MyUtils
import clocklife.shenle.com.one.fragment.*
import clocklife.shenle.com.setting.MyAppSetting
import cn.jpush.im.android.api.JMessageClient
import com.bumptech.glide.Glide
import com.mob.ums.OperationCallback
import com.mob.ums.UMSSDK
import jp.wasabeef.glide.transformations.CropCircleTransformation
import slmodule.shenle.com.BaseFragment
import slmodule.shenle.com.BaseMainActivity
import slmodule.shenle.com.utils.UIUtils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.base_toolbar.*
import slmodule.shenle.com.utils.BitmapUtils


class MainActivity : BaseMainActivity() {
    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        var appUserInfo = DbDao.findUserByIsLogin(true)
        BaseAppState.setData(appUserInfo)
    }
    override fun initToolBar(): Toolbar? {
        toolbar?.setTitle("首页")
        return toolbar
    }
    override fun toolbar2Setting(toolbar: Toolbar?) {
        toolbar?.setBackgroundColor(UIUtils.getColor(R.color.bg_1))
        toolbar?.setTitleTextColor(UIUtils.getColor(R.color.text_color_5))
        Observable.create<Drawable> {
            var bitmapDrawable: Drawable
            if (!UIUtils.isEmpty(BaseAppState.userPhoto)) {
                val bmp = Glide.with(this).load(BaseAppState.userPhoto).asBitmap().into(150, 150).get() as Bitmap
                bitmapDrawable = BitmapDrawable(BitmapUtils.makeRoundCorner(bmp))
            } else {
                bitmapDrawable = UIUtils.getDrawable(R.mipmap.login)
            }
            it.onNext(bitmapDrawable)
            it.onComplete()
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.bindToLifecycle())
                .subscribe({
                    toolbar?.navigationIcon = it
                })
    }

    override fun initNavHeaderView(navigationView: NavigationView) {
        val headerView = navigationView.getHeaderView(0)
        headerView.findViewById(R.id.ll_login).setOnClickListener {
            if (MyUtils.isLogin()) {
                //去个人中心
                UIUtils.showSnackBar(it, "个人中心")
            }
        }
        (headerView.findViewById(R.id.tv_name) as TextView).text = BaseAppState.userName
        var appUserInfo = DbDao.findUserByIsLogin(true)
        if (appUserInfo != null) {
            (headerView.findViewById(R.id.tv_sign) as TextView).text = appUserInfo.sign
            var ivPhoto = headerView.findViewById(R.id.iv_photo) as ImageView
            Glide.with(this).load(appUserInfo.photo).bitmapTransform(CropCircleTransformation(this)).into(ivPhoto)
        }
    }

    override fun getMenuStrResArr(): IntArray {
        return intArrayOf(R.string.menu_a, R.string.menu_b, R.string.menu_c, R.string.menu_d, R.string.menu_e)
    }

    companion object {
        fun goHere(activity : Activity) {
            activity.startActivity(Intent(activity,MainActivity::class.java))
        }
    }

    override fun getListFragment(): List<BaseFragment> {
        return listOf(OneFragment.getInstance(),
                TwoFragment.getInstance(),
                ThreeFragment.getInstance(),
                FourFragment.getInstance(),
                FiveFragment.getInstance())
    }

    override fun changePage(position: Int, toolbar: Toolbar) {
        when (position) {
            0 -> toolbar.setTitle("首页")
            1 -> toolbar.setTitle("2")
            2 -> toolbar.setTitle("3")
            3 -> toolbar.setTitle("4")
            4 -> toolbar.setTitle("5")
        }
    }

    override fun on2NavigationItemSelected(item: MenuItem) {
        // Handle navigation view item clicks here.
        val id = item.itemId

        if (id == slmodule.shenle.com.R.id.nav_camera) {
            // Handle the camera action
        } else if (id == slmodule.shenle.com.R.id.nav_gallery) {

        } else if (id == slmodule.shenle.com.R.id.nav_slideshow) {

        } else if (id == slmodule.shenle.com.R.id.nav_manage) {

        } else if (id == slmodule.shenle.com.R.id.nav_setting) {
            //设置
            MyAppSetting.goHere()

        } else if (id == slmodule.shenle.com.R.id.nav_login_out) {
            //注销
            dialog.show()
            UMSSDK.logout(object : OperationCallback<Void>() {
                override fun onSuccess(p0: Void?) {
                    dialog.dismiss()
                    var appUserInfo = DbDao.findUserByIsLogin(true)
                    appUserInfo?.hasLogin = false
                    appUserInfo?.update()
                    BaseAppState.clear()
                    JMessageClient.logout()
                    InitActivity.goHere()
                    UIUtils.finishDelay(2, this@MainActivity)
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

//    private var mIsExit: Boolean = false
    /**
     * 双击返回键退出
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val drawer_layout = findViewById(slmodule.shenle.com.R.id.drawer_layout) as DrawerLayout
            if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
                drawer_layout.closeDrawer(GravityCompat.START)
                return true
            }
            moveTaskToBack(false)
//            if (mIsExit) {
//                this.finish()
//            } else {
//                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show()
//                mIsExit = true
//                Handler().postDelayed({ mIsExit = false }, 2000)
//            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}


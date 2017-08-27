package slmodule.shenle.com

import android.animation.Animator
import android.content.Context
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v4.view.animation.LinearOutSlowInInterpolator
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.OvershootInterpolator
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_base_main.*
import kotlinx.android.synthetic.main.base_toolbar.*
import kotlinx.android.synthetic.main.content_base_main.*


abstract class BaseMainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    override fun initToolBar(): Toolbar? {
        return toolbar
    }

    override fun supportSlideBack(): Boolean {
        return false
    }
    override fun getRootView(): Int{
        val enabledTranslucentNavigation = getSharedPreferences("shared", Context.MODE_PRIVATE)
                .getBoolean("translucentNavigation", false)
//        setTheme(if (enabledTranslucentNavigation) R.style.AppTheme_TranslucentNavigation else R.style.AppTheme)
        return R.layout.activity_base_main
    }
    override fun initOnCreate(savedInstanceState: Bundle?) {
//        setSupportActionBar(toolbar)
        fab.setOnClickListener{
            Snackbar.make(it, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
        //关闭手势滑动
//        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById(R.id.nav_view) as NavigationView
        initNavHeaderView(navigationView)
        navigationView.setNavigationItemSelectedListener(this)
        initBottom()
    }

    abstract fun initNavHeaderView(navigationView: NavigationView)

    var currentFragment:BaseFragment?=null
    var oldFragment:BaseFragment?=null
    private var isMakeScrollow: Int=0

    private fun initBottom() {
        initVp()
        val imageResources = intArrayOf(R.drawable.menu_a, R.drawable.menu_b,
                            R.drawable.menu_c, R.drawable.menu_d, R.drawable.menu_e)
        val color = R.color.white
        val colorResources = intArrayOf(color, color,color,color,color)
        val strArr = getMenuStrResArr()
        Observable.just(0,1,2,3,4).subscribe { bottomNavigation.addItem(AHBottomNavigationItem(strArr[it], imageResources[it], colorResources[it])) }
// Set background color
        bottomNavigation.defaultBackgroundColor = ContextCompat.getColor(this, R.color.white)
// Disable the translation inside the CoordinatorLayout
        bottomNavigation.isBehaviorTranslationEnabled = true
// Enable the translation of the FloatingActionButton
        bottomNavigation.manageFloatingActionButtonBehavior(fab)
// Force to tint the drawable (useful for font with icon for example)控制是否自己设颜色
        bottomNavigation.isForceTint = true
        bottomNavigation.isTranslucentNavigationEnabled = true
// Manage titles
        bottomNavigation.titleState = AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE
// Use colored navigation with circle reveal effect
        bottomNavigation.isColored = true//默认false 设置图标颜色
        bottomNavigation.accentColor = ContextCompat.getColor(this, R.color.text_color_2)
        bottomNavigation.inactiveColor = ContextCompat.getColor(this, R.color.text_color_1)
// Customize notification (title, background, typeface)设置红点
        bottomNavigation.setNotificationBackgroundColor(ContextCompat.getColor(this, R.color.text_color_2))
// Add or remove notification for each item
        bottomNavigation.setNotification("", 1)
// OR
//        val notification = AHNotification.Builder()
//                .setText("1")
//                .setBackgroundColor(ContextCompat.getColor(this, R.color.text_color_2))
//                .setTextColor(ContextCompat.getColor(this, R.color.text_color_5))
//                .build()
//        bottomNavigation.setNotification(notification, 1)
// Set listeners
        bottomNavigation.setOnTabSelectedListener { position, wasSelected ->
            if (isMakeScrollow==0||isMakeScrollow==3){
                isMakeScrollow=1
            }
//            if (currentFragment == null) {
//                currentFragment = adapter?.getCurrentFragment()
//            }

            if (wasSelected) {
                currentFragment?.refresh()
                isMakeScrollow=0
                return@setOnTabSelectedListener true
            }
            container.setCurrentItem(position, false)
            if (oldFragment != null&&isMakeScrollow==1) {
                oldFragment?.willBeHidden()
            }else if (oldFragment != null&&isMakeScrollow==2){
                oldFragment?.clearAni()
            }
            currentFragment = adapter?.getItem(position)
            if (isMakeScrollow==1) {
                currentFragment?.willBeDisplayed()
            }else if (isMakeScrollow==2){
                currentFragment?.clearAni()
            }
            bottomNavigation.setNotification("", position)
            if (position == 1) {
                fab.setVisibility(View.VISIBLE)
                fab.setAlpha(0f)
                fab.setScaleX(0f)
                fab.setScaleY(0f)
                fab.animate()
                        .alpha(1f)
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(300)
                        .setInterpolator(OvershootInterpolator())
                        .setListener(object : Animator.AnimatorListener {
                            override fun onAnimationStart(animation: Animator) {

                            }

                            override fun onAnimationEnd(animation: Animator) {
//                                fab.animate()
//                                        .setInterpolator(LinearOutSlowInInterpolator())
//                                        .start()
                            }

                            override fun onAnimationCancel(animation: Animator) {

                            }

                            override fun onAnimationRepeat(animation: Animator) {

                            }
                        })
                        .start()

            } else {
                if (fab.getVisibility() == View.VISIBLE) {
                    fab.animate()
                            .alpha(0f)
                            .scaleX(0f)
                            .scaleY(0f)
                            .setDuration(300)
                            .setInterpolator(LinearOutSlowInInterpolator())
                            .setListener(object : Animator.AnimatorListener {
                                override fun onAnimationStart(animation: Animator) {

                                }

                                override fun onAnimationEnd(animation: Animator) {
                                    fab.setVisibility(View.GONE)
                                }

                                override fun onAnimationCancel(animation: Animator) {
                                    fab.setVisibility(View.GONE)
                                }

                                override fun onAnimationRepeat(animation: Animator) {

                                }
                            })
                            .start()
                }
            }
            isMakeScrollow = 0
            true
        }
//        bottomNavigation.setOnNavigationPositionListener {
//            // Manage the new y position
//        }
        // Set current item programmatically
        bottomNavigation.currentItem = 0
    }

    private var  adapter: BasePagerAdapter?=null

    private fun initVp() {
        container.setOffscreenPageLimit(5)
        container.setPagingEnabled(true)
        adapter = BasePagerAdapter(
                supportFragmentManager, getListFragment())
        container.adapter = adapter
        currentFragment = adapter?.getItem(0)
        container.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                when(isMakeScrollow){
                    0-> {
                        isMakeScrollow = 2
                        oldFragment = currentFragment
                    }
                    1->isMakeScrollow = 3
                }
                bottomNavigation.setCurrentItem(position,true)
                changePage(position,toolbar)
            }

        })
    }

    open fun changePage(position: Int, toolbar: Toolbar) {
    }

    abstract fun getMenuStrResArr(): IntArray

    abstract fun getListFragment(): List<BaseFragment>

    override fun onBackPressed() {
        val drawer_layout = findViewById(R.id.drawer_layout) as DrawerLayout
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.base_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

//
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        on2NavigationItemSelected(item)
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    abstract fun on2NavigationItemSelected(item: MenuItem)
}

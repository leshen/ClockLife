package slmodule.shenle.com

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trello.rxlifecycle2.components.support.RxFragment
import android.view.animation.AnimationUtils
import android.view.animation.Animation
import com.iflytek.sunflower.FlowerCollector
import slmodule.shenle.com.utils.UIUtils


/**
 * Created by shenle on 2017/7/31.
 */
abstract class BaseFragment : RxFragment() {
    fun getTitle(): CharSequence = "标题"
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    abstract fun refresh()
    var isHid = false
    /**
     * Called when a fragment will be displayed
     */
    fun willBeDisplayed() {
        if (view != null) {
            isHid = false
            val fadeIn = AnimationUtils.loadAnimation(activity, R.anim.fade_in)
            view?.startAnimation(fadeIn)
        }
    }

    /**
     * Called when a fragment will be hidden
     */
    fun willBeHidden() {
        if (view != null) {
            isHid = true
            val fadeOut = AnimationUtils.loadAnimation(activity, R.anim.fade_out)
            view?.startAnimation(fadeOut)
        }
    }
    /**
     * Called when a fragment will be hidden
     */
    fun clearAni() {
        if (isHid) {
            willBeDisplayed()
        }
    }

    override fun onResume() {
        super.onResume()
        FlowerCollector.onResume(activity)
        FlowerCollector.onPageStart(javaClass.simpleName)
    }

    override fun onPause() {
        super.onPause()
        FlowerCollector.onPageEnd(javaClass.simpleName) //请确保 onPageEnd 在onPause 之前调用。
        FlowerCollector.onPause(activity)
    }

    override fun onDestroyView() {
        UIUtils.unbindDrawables(view)
        super.onDestroyView()
    }
}
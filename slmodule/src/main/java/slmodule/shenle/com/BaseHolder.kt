package slmodule.shenle.com

import android.view.View
import com.chad.library.adapter.base.BaseViewHolder

/**
 * Created by shenle on 2017/7/31.
 */
abstract class BaseHolder<T>(view: View?) : BaseViewHolder(view) {
    abstract fun setData(data :T)
    fun onDestroy(){}
}
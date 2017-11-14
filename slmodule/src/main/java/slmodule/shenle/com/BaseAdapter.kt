package slmodule.shenle.com

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder


/**
 * Created by shenle on 2017/9/25.
 */
class BaseAdapter<T> (layoutResId: Int, data: List<T>?,var inter:BaseAdapterInterface<T>): BaseQuickAdapter<T, BaseViewHolder> (layoutResId, data){
    override fun convert(helper: BaseViewHolder, item: T) {
//        helper.setData(item)
        inter.setData(helper.itemView,item)
    }

    interface BaseAdapterInterface<T> {
        fun setData(v: View, item:T)
    }
}
package clocklife.shenle.com.base.data

import android.graphics.Color

/**
 * Created by shenle on 2017/9/19.
 */
class NormalData {
    companion object {
        var map_show_radius = "30"//圆栅格半径
        var map_id_search = "搜索范围"//业务id
        /**
         * 地图中绘制多边形、圆形的边界颜色
         * @since 3.3.0
         */
        val STROKE_COLOR = Color.argb(180, 63, 145, 252)
        /**
         * 地图中绘制多边形、圆形的填充颜色
         * @since 3.3.0
         */
        val FILL_COLOR = Color.argb(163, 118, 212, 243)

        /**
         * 地图中绘制多边形、圆形的边框宽度
         * @since 3.3.0
         */
        val STROKE_WIDTH = 5f
    }
}
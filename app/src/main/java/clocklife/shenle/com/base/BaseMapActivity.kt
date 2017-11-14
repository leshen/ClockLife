package clocklife.shenle.com.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.CompoundButton
import android.widget.RadioGroup
import android.widget.Toast
import clocklife.shenle.com.R
import clocklife.shenle.com.base.data.NormalData
import com.amap.api.fence.GeoFence
import com.amap.api.fence.GeoFenceClient
import com.amap.api.fence.GeoFenceListener
import com.amap.api.location.*
import com.amap.api.maps2d.AMap
import com.amap.api.maps2d.CameraUpdateFactory
import com.amap.api.maps2d.LocationSource
import com.amap.api.maps2d.MapView
import com.amap.api.maps2d.model.*
import slmodule.shenle.com.BaseActivity
import java.util.ArrayList
import java.util.HashMap

/**
 * 高德地图基础类
 * Created by shenle on 2017/9/18.
 */
abstract class BaseMapActivity : BaseActivity(), AMapLocationListener, GeoFenceListener, AMap.OnMapClickListener, LocationSource {
    /**
     * 用于显示当前的位置
     *
     *
     * 示例中是为了显示当前的位置，在实际使用中，单独的地理围栏可以不使用定位接口
     *
     */
    private var mlocationClient: AMapLocationClient? = null
    private var mListener: LocationSource.OnLocationChangedListener? = null
    private var mLocationOption: AMapLocationClientOption? = null

    lateinit var mMapView: MapView
    lateinit var mAMap: AMap

    // 中心点坐标
    private var centerLatLng: LatLng? = null
    // 中心点marker
    private var centerMarker: Marker? = null
    private val ICON_YELLOW = BitmapDescriptorFactory
            .defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)
    private val ICON_RED = BitmapDescriptorFactory
            .defaultMarker(BitmapDescriptorFactory.HUE_RED)
    private var markerOption: MarkerOptions? = null
    private val markerList = ArrayList<Marker>()
    // 当前的坐标点集合，主要用于进行地图的可视区域的缩放
    private val boundsBuilder = LatLngBounds.Builder()

    // 地理围栏客户端
    private var fenceClient: GeoFenceClient? = null
    // 要创建的围栏半径
    private var fenceRadius = 0.0f
    // 触发地理围栏的行为，默认为进入提醒
    private var activatesAction = GeoFenceClient.GEOFENCE_IN
    // 地理围栏的广播action
    private val GEOFENCE_BROADCAST_ACTION = "com.example.geofence.round"

    // 记录已经添加成功的围栏
    private val fenceMap = HashMap<String, GeoFence>()

    override fun initOnCreate(savedInstanceState: Bundle?) {
        mMapView = getMapView()
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState)
        init()
        initView(savedInstanceState)
    }

    abstract fun initView(savedInstanceState: Bundle?)

    abstract fun getMapView(): MapView
    fun init() {
        mAMap = mMapView.map
        mAMap.moveCamera(CameraUpdateFactory.zoomBy(6f))
        setUpMap()
        val filter = IntentFilter(
                ConnectivityManager.CONNECTIVITY_ACTION)
        filter.addAction(GEOFENCE_BROADCAST_ACTION)
        registerReceiver(mGeoFenceReceiver, filter)

        /**
         * 创建pendingIntent
         */
        fenceClient?.createPendingIntent(GEOFENCE_BROADCAST_ACTION)
        fenceClient?.setGeoFenceListener(this)
        /**
         * 设置地理围栏的触发行为,默认为进入
         */
        fenceClient?.setActivateAction(GeoFenceClient.GEOFENCE_IN)
    }

    /**
     * 设置一些amap的属性
     */
    private fun setUpMap() {
        mAMap.setOnMapClickListener(this)
        mAMap.setLocationSource(this)// 设置定位监听
        mAMap.uiSettings.isMyLocationButtonEnabled = true// 设置默认定位按钮是否显示
        // 自定义系统定位蓝点
        val myLocationStyle = MyLocationStyle()
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(
                BitmapDescriptorFactory.fromResource(R.mipmap.gps_point))
        // 自定义精度范围的圆形边框颜色
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0))
        // 自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(0f)
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0))
        // 将自定义的 myLocationStyle 对象添加到地图上
        mAMap.setMyLocationStyle(myLocationStyle)
        mAMap.isMyLocationEnabled = true// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
    }

    /**
     * 方法必须重写
     */
    override fun onResume() {
        super.onResume()
        mMapView.onResume()
    }

    /**
     * 方法必须重写
     */
    override fun onPause() {
        super.onPause()
        mMapView.onPause()
        deactivate()
    }

    /**
     * 方法必须重写
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mMapView.onSaveInstanceState(outState)
    }

    /**
     * 方法必须重写
     */
    override fun onDestroy() {
        super.onDestroy()
        mMapView.onDestroy()
        try {
            unregisterReceiver(mGeoFenceReceiver)
        } catch (e: Throwable) {
        }
        fenceClient?.removeGeoFence()
        mlocationClient?.onDestroy()
    }

    private fun drawFence(fence: GeoFence) {
        when (fence.type) {
            GeoFence.TYPE_ROUND, GeoFence.TYPE_AMAPPOI -> drawCircle(fence)
            GeoFence.TYPE_POLYGON, GeoFence.TYPE_DISTRICT -> drawPolygon(fence)
            else -> {
            }
        }
        // // 设置所有maker显示在当前可视区域地图中
        // LatLngBounds bounds = boundsBuilder.build();
        // mAMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));

        removeMarkers()
    }

    private fun drawCircle(fence: GeoFence) {
        val center = LatLng(fence.center.latitude,
                fence.center.longitude)
        // 绘制一个圆形
        mAMap.addCircle(CircleOptions().center(center)
                .radius(fence.radius.toDouble()).strokeColor(NormalData.STROKE_COLOR)
                .fillColor(NormalData.FILL_COLOR).strokeWidth(NormalData.STROKE_WIDTH))
        boundsBuilder.include(center)
    }

    private fun drawPolygon(fence: GeoFence) {
        val pointList = fence.pointList
        if (null == pointList || pointList.isEmpty()) {
            return
        }
        for (subList in pointList) {
            val lst = ArrayList<LatLng>()

            val polygonOption = PolygonOptions()
            for (point in subList) {
                lst.add(LatLng(point.latitude, point.longitude))
                boundsBuilder.include(
                        LatLng(point.latitude, point.longitude))
            }
            polygonOption.addAll(lst)

            polygonOption.strokeColor(NormalData.STROKE_COLOR)
                    .fillColor(NormalData.FILL_COLOR).strokeWidth(NormalData.STROKE_WIDTH)
            mAMap.addPolygon(polygonOption)
        }
    }

    internal var lock = Any()
    internal fun drawFence2Map() {
        object : Thread() {
            override fun run() {
                try {
                    synchronized(lock) {
                        if (null == fenceList || fenceList!!.isEmpty()) {
                            return
                        }
                        for (fence in fenceList!!) {
                            if (fenceMap.containsKey(fence.fenceId)) {
                                continue
                            }
                            drawFence(fence)
                            fenceMap.put(fence.fenceId, fence)
                        }
                    }
                } catch (e: Throwable) {

                }

            }
        }.start()
    }

    internal var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                0 -> {
                    val sb = StringBuffer()
                    sb.append("添加围栏成功")
                    val customId = msg.obj as String
                    if (!TextUtils.isEmpty(customId)) {
                        sb.append("customId: ").append(customId)
                    }
                    Toast.makeText(applicationContext, sb.toString(),
                            Toast.LENGTH_SHORT).show()
                    drawFence2Map()
                }
                1 -> {
                    val errorCode = msg.arg1
                    Toast.makeText(applicationContext,
                            "添加围栏失败 " + errorCode, Toast.LENGTH_SHORT).show()
                }
                else -> {
                }
            }
        }
    }

    internal var fenceList: List<GeoFence>? = ArrayList()
    override fun onGeoFenceCreateFinished(geoFenceList: List<GeoFence>,
                                          errorCode: Int, customId: String) {
        val msg = Message.obtain()
        if (errorCode == GeoFence.ADDGEOFENCE_SUCCESS) {
            fenceList = geoFenceList
            msg.obj = customId
            msg.what = 0
        } else {
            msg.arg1 = errorCode
            msg.what = 1
        }
        handler.sendMessage(msg)
    }

    /**
     * 接收触发围栏后的广播,当添加围栏成功之后，会立即对所有围栏状态进行一次侦测，如果当前状态与用户设置的触发行为相符将会立即触发一次围栏广播；
     * 只有当触发围栏之后才会收到广播,对于同一触发行为只会发送一次广播不会重复发送，除非位置和围栏的关系再次发生了改变。
     */
    private val mGeoFenceReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // 接收广播
            if (intent.action == GEOFENCE_BROADCAST_ACTION) {
                val bundle = intent.extras
                val customId = bundle
                        .getString(GeoFence.BUNDLE_KEY_CUSTOMID)
                val fenceId = bundle.getString(GeoFence.BUNDLE_KEY_FENCEID)
                //status标识的是当前的围栏状态，不是围栏行为
                val status = bundle.getInt(GeoFence.BUNDLE_KEY_FENCESTATUS)
                val sb = StringBuffer()
                when (status) {
                    GeoFence.STATUS_LOCFAIL -> sb.append("定位失败")
                    GeoFence.STATUS_IN -> sb.append("进入围栏 ")
                    GeoFence.STATUS_OUT -> sb.append("离开围栏 ")
                    GeoFence.STATUS_STAYED -> sb.append("停留在围栏内 ")
                    else -> {
                    }
                }
                if (status != GeoFence.STATUS_LOCFAIL) {
                    if (!TextUtils.isEmpty(customId)) {
                        sb.append(" customId: " + customId!!)
                    }
                    sb.append(" fenceId: " + fenceId!!)
                }
                val str = sb.toString()
                val msg = Message.obtain()
                msg.obj = str
                msg.what = 2
                handler.sendMessage(msg)
            }
        }
    }

    override fun onMapClick(latLng: LatLng) {
        markerOption?.icon(ICON_YELLOW)
        centerLatLng = latLng
        addCenterMarker(centerLatLng)
        Log.e("选中的坐标：", "" + centerLatLng?.longitude + ","
                + centerLatLng?.latitude)
    }

    /**
     * 定位成功后回调函数
     */
    override fun onLocationChanged(amapLocation: AMapLocation?) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null && amapLocation.errorCode == 0) {
                mListener?.onLocationChanged(amapLocation)// 显示系统小蓝点
                val alt = LatLng(amapLocation.latitude,amapLocation.longitude)
                onMapClick(alt)
                addFence()
            } else {
                val errText = "定位失败,${amapLocation.errorCode}: ${amapLocation.errorInfo}"
                Log.e("AmapErr", errText)
            }
        }
    }

    /**
     * 激活定位
     */
    override fun activate(listener: LocationSource.OnLocationChangedListener) {
        mListener = listener
        if (mlocationClient == null) {
            mlocationClient = AMapLocationClient(this)
            mLocationOption = AMapLocationClientOption()
            // 设置定位监听
            mlocationClient?.setLocationListener(this)
            // 设置为高精度定位模式
            mLocationOption?.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy)
            // 只是为了获取当前位置，所以设置为单次定位
            mLocationOption?.setOnceLocation(true)
            // 设置定位参数
            mlocationClient?.setLocationOption(mLocationOption)
            mlocationClient?.startLocation()
        }
    }

    /**
     * 停止定位
     */
    override fun deactivate() {
        mListener = null
        mlocationClient?.stopLocation()
        mlocationClient?.onDestroy()
        mlocationClient = null
    }

    private fun addCenterMarker(latlng: LatLng?) {
        if (null == centerMarker) {
            centerMarker = mAMap.addMarker(markerOption)
        }
        centerMarker?.setPosition(latlng)
        centerMarker?.let { markerList.add(centerMarker!!) }
    }

    private fun removeMarkers() {
        centerMarker?.remove()
        centerMarker = null
        if (null != markerList && markerList.size > 0) {
            for (marker in markerList) {
                marker.remove()
            }
            markerList.clear()
        }
    }

    /**
     * 添加围栏
     *
     * @since 3.2.0
     * @author hongming.wang
     */
    open fun addFence() {
        addRoundFence()
    }

    /**
     * 添加圆形围栏
     *
     * @since 3.2.0
     * @author hongming.wang
     */
    private fun addRoundFence() {
        val customId = NormalData.map_id_search//业务id
        val radiusStr = NormalData.map_show_radius//半径
        if (null == centerLatLng || TextUtils.isEmpty(radiusStr)) {
            Toast.makeText(applicationContext, "参数不全", Toast.LENGTH_SHORT)
                    .show()
            return
        }
        val centerPoint = DPoint(centerLatLng!!.latitude,
                centerLatLng!!.longitude)
        fenceRadius = java.lang.Float.parseFloat(radiusStr)
        fenceClient?.addGeoFence(centerPoint, fenceRadius, customId)
    }

}
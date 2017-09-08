//package slmodule.shenle.com.utils;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.DatePickerDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.Bitmap.CompressFormat;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.Paint;
//import android.graphics.drawable.Drawable;
//import android.net.Uri;
//import android.os.Environment;
//import android.provider.MediaStore;
//import android.support.annotation.NonNull;
//import android.text.Html;
//import android.text.Spannable;
//import android.text.SpannableStringBuilder;
//import android.text.Spanned;
//import android.text.TextUtils;
//import android.text.method.LinkMovementMethod;
//import android.text.style.ClickableSpan;
//import android.text.style.ImageSpan;
//import android.text.style.URLSpan;
//import android.util.DisplayMetrics;
//import android.view.Gravity;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.ViewGroup.LayoutParams;
//import android.widget.AbsListView;
//import android.widget.DatePicker;
//import android.widget.EditText;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.loopj.android.jpush.http.AsyncHttpClient;
//import com.loopj.android.jpush.http.BinaryHttpResponseHandler;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.Calendar;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import slmodule.shenle.com.R;
//import slmodule.shenle.com.view.BadgeView;
//
////import cz.msebera.android.httpclient.Header;
//
///**
// * 开发中经常用到的工具
// *
// * @author shenle
// */
//public class MyUtils {
//    public static final int REQUEST_CAMERA = 800; // 打开相机
//    public static final int REQUEST_PHOTO = 801;// 选择照片
//    public static final int REQUEST_CROP = 802;// 裁切图片
//    public static final int TAKE_MORE_PICTURE = 804;// 相册返回
//    public static final int TAKE_PICTURE = 803;// 相机返回
//    /**
//     * 拍照路径
//     */
//    public static Uri currentImgUri;
//    private static MyUtils apiInstance;
//    private String currentImgPath;
//
//    public static MyUtils getInstance() {
//        if (apiInstance == null) {
//            apiInstance = new MyUtils();
//        }
//        return apiInstance;
//    }
//
//    /**
//     * textView设置DrawableLeft
//     *
//     * @param i
//     * @param tv
//     */
//    public static void setLeftDrawable(int i, TextView tv) {
//        Drawable drawable = UIUtils.getContext().getResources().getDrawable(i);
//        // / 这一步必须要做,否则不会显示.
//        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
//                drawable.getMinimumHeight());
//        tv.setCompoundDrawables(drawable, null, null, null);
//    }
//    /**
//     * textView设置DrawableLeft
//     *
//     * @param i
//     * @param tv
//     */
//    public static void setLeftDrawable(int i, TextView tv,int right,int bottom) {
//        Drawable drawable = UIUtils.getContext().getResources().getDrawable(i);
//        // / 这一步必须要做,否则不会显示.
//        drawable.setBounds(0, 0, right,
//                bottom);
//        tv.setCompoundDrawables(drawable, null, null, null);
//    }
//
//    /**
//     * 拍照返回
//     *
//     * @param requestCode
//     * @param resultCode
//     * @param data
//     * @param activity
//     * @param callBack
//     */
//    public static void onActivityResult(int requestCode, int resultCode,
//                                        Intent data, Activity activity, ImageZoomCallBack callBack) {
//        if (resultCode == Activity.RESULT_OK) {
//            switch (requestCode) {
//                case MyUtils.REQUEST_CAMERA:
//                    // 相机返回
//                    if (currentImgUri != null) {
//                        PhotoUtil.crop(activity, currentImgUri,
//                                MyUtils.REQUEST_CROP, Constants.CACHE_DIR_IMG
//                                        + Constants.CACHE_IMG_USER_HEAD);
//                    }
//                    break;
//                case MyUtils.REQUEST_PHOTO:
//                    // 相册返回
//                    Uri photoPath = data.getData();
//                    System.out.println(photoPath.toString());
//                    PhotoUtil
//                            .crop(activity, photoPath, MyUtils.REQUEST_CROP,
//                                    Constants.CACHE_DIR_IMG
//                                            + Constants.CACHE_IMG_USER_HEAD);
//
//                    break;
//                case MyUtils.REQUEST_CROP:
//                    System.out.println(Constants.CACHE_DIR_IMG
//                            + Constants.CACHE_IMG_USER_HEAD);
//                    Bitmap bm = data.getParcelableExtra("data");
//                    Uri path_crop = Uri.fromFile(new File(Constants.CACHE_DIR_IMG
//                            + Constants.CACHE_IMG_USER_HEAD));
//                    // ivPhoto.setImageBitmap(bm);
//                    PhotoUtil.zoomImage(activity.getApplicationContext(),
//                            path_crop, Constants.CACHE_DIR_IMG
//                                    + Constants.CACHE_IMG_USER_HEAD,
//                            Constants.IMAGE_MAX_WIDTH, Constants.IMAGE_MAX_HEIGHT,
//                            Constants.IMAGE_QUALITY, callBack);
//                    break;
//
//                default:
//                    break;
//            }
//
//        }
//    }
//
//    /**
//     * listView空值显示(在加载完数据后setEmptyView)
//     *
//     * @param mContext
//     * @param listView
//     * @param text
//     */
//    public static TextView getEmptyText(Context mContext, AbsListView listView,
//                                        String text) {
//        TextView textView = new TextView(mContext);
//        textView.setText(text);
//        textView.setGravity(Gravity.CENTER);
//        textView.setTextSize(18);
//        textView.setTextColor(mContext.getResources().getColor(
//                R.color.NewLightBackText));
//        textView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
//                LayoutParams.MATCH_PARENT));
//        ViewGroup parent = (ViewGroup) listView.getParent();
//        parent.addView(textView,parent.indexOfChild(listView));
//        textView.setVisibility(View.GONE);
////        listView.setEmptyView(textView);
//        return textView;
//    }
//
//    /**
//     * 验证手机号是否合法
//     *
//     * @param context
//     * @param etPhone
//     * @return
//     */
//    public static boolean isPhoneValid(Context context, EditText etPhone) {
//        boolean isValid = true;
//        String phone = etPhone.getText().toString().trim();
//        int length = phone.length();
//        if (TextUtils.isEmpty(phone)) {
//            isValid = false;
//        } else {
//            Pattern p = Pattern
//                    .compile("^((13[0-9])|(14[0-9])|(15[0-9])|(17[0-9])|(18[0-9]))\\d{8}$");
//            Matcher m = p.matcher(phone);
//            if (length > 11 || !m.matches()) {
//                isValid = false;
//            }
//        }
//        if (!isValid) {
//            Toast.makeText(context, "请输入11位有效手机号", Toast.LENGTH_SHORT).show();
//        }
//        return isValid;
//    }
//
//    /**
//     * 下载图片,保存到本地
//     *
//     * @param url     图片的url
//     * @param context
//     */
//    public static void loadImageToLocal(String url, final Context context, final BaseCallBack callBack) {
//        AsyncHttpClient client = new AsyncHttpClient();
//        // 指定文件类型
//        String[] allowedContentTypes = new String[]{"image/png", "image/jpeg"};
//        // 获取二进制数据如图片和其他文件
//        client.get(url, new BinaryHttpResponseHandler(allowedContentTypes) {
//            @Override
//            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] binaryData) {
//                // 下载成功后需要做的工作
//                Bitmap bmp = BitmapFactory.decodeByteArray(binaryData, 0,
//                        binaryData.length);
//                // 压缩格式
//                CompressFormat format = CompressFormat.JPEG;
//                // 压缩比例
//                int quality = 100;
//                String picName = "IMG" + System.currentTimeMillis() + ".png";
//                String path = null;
//                FileOutputStream fos = null;
//                try {
//
//                    if (Environment.getExternalStorageState().equals(
//                            Environment.MEDIA_MOUNTED)) {
//                        path = Environment.getExternalStorageDirectory()
//                                .getAbsolutePath()
//                                + File.separator
//                                + "zhuyitong"
//                                + File.separator
//                                + "img"
//                                + File.separator + picName;// 获取SDCard目录
//                        File file = new File(path);
//                        file.getParentFile().mkdirs();
//                        file.createNewFile();
//                        fos = new FileOutputStream(file);
//                    } else {
//                        fos = context.openFileOutput(picName,
//                                Context.MODE_PRIVATE);
//                        path = context.getFilesDir().toString();
//                    }
//                    // 压缩输出
//                    bmp.compress(format, quality, fos);
//                    // 关闭
//                    fos.close();
//                    UIUtils.showToastSafe("下载成功\n" + path);
//                    // 把文件插入到系统图库
//                    try {
//                        MediaStore.Images.Media.insertImage(context.getContentResolver(),
//                                new File(path).getAbsolutePath(), picName, "zhuyitong");
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                    if (callBack!=null){
//                        callBack.onCallBack();
//                    }
//                    // 通知图库更新
//                    AppInstance.getForegroundActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(path))));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] binaryData, Throwable error) {
//                if (callBack!=null){
//                    callBack.onFailure();
//                }
//            }
//        });
//    }
//
//    /**
//     * 弹出日期选择器
//     *
//     * @param context
//     * @param textView
//     * @param callback
//     */
//    public static void seleteData(Context context, final TextView textView,
//                                  final AlertDialogInterface callback) {
//        Calendar calendar = Calendar.getInstance();
//        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
//        new DatePickerDialog(context,
//                // 绑定监听器
//                new DatePickerDialog.OnDateSetListener() {
//                    @Override
//                    public void onDateSet(DatePicker dp, int year, int month,
//                                          int dayOfMonth) {
//                        textView.setText(year + "-" + (month + 1) + "-"
//                                + dayOfMonth);
//                        callback.onClickYes(textView.getText().toString());
//                    }
//                }
//                // 设置初始日期
//                , calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
//                calendar.get(Calendar.DAY_OF_MONTH)).show();
//    }
//
//    /**
//     * 初始化红点
//     *
//     * @param v
//     * @return
//     */
//    public static BadgeView initBadge(Context mContext, View v) {
//        BadgeView badge = new BadgeView(mContext, v);
//        badge.setTextSize(9);
//        badge.setText("0");
//        badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
//        badge.setBadgeMargin(10);
//        return badge;
//    }
//
//    /**
//     * 自适应屏幕
//     *
//     * @param context
//     * @param mWidth
//     * @param mHeight
//     * @return
//     */
//    public static RelativeLayout.LayoutParams computeContainerSize(Context context, int mWidth, int
//            mHeight) {
//        int width = DeviceUtil.getScreenWidth(context);
//        int height = width * mHeight / mWidth;
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.MATCH_PARENT,
//                RelativeLayout.LayoutParams.MATCH_PARENT);
//        params.width = width;
//        params.height = height;
//        params.addRule(RelativeLayout.CENTER_IN_PARENT);
//        return params;
//    }
//
//    /**
//     * 拍照路径
//     */
//    public Uri getCurrentImgUri() {
//        return currentImgUri;
//    }
//
//    public String getCurrentImgPath() {
//        return currentImgPath;
//    }
//
//    /**
//     * 选择相机或相册的弹出对话框
//     */
//    public AlertDialog getPhotoDialog() {
//        final Activity activity = AppInstance.getForegroundActivity();
//        AlertDialog alertDialog = new AlertDialog.Builder(activity)
//                .setItems(new String[]{"拍照", "从相册选择"},
//                        new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog,
//                                                int which) {
//                                switch (which) {
//                                    case 0:
//                                        // 拍照，自定义路径
//                                        currentImgUri = PhotoUtil
//                                                .takePhotoCustomerPath(
//                                                        activity,
//                                                        Constants.CACHE_DIR_IMG,
////                                                        Constants.CACHE_IMG_KDF_VERIFY,
//                                                        "",
//                                                        REQUEST_CAMERA);
//                                        break;
//                                    case 1:
//                                        // 本地相册
//                                        PhotoUtil
//                                                .pickPhoto(activity, REQUEST_PHOTO);
//                                        break;
//
//                                    default:
//                                        break;
//                                }
//                                dialog.dismiss();
//                            }
//                        }).setNegativeButton("取消", null).create();
//        return alertDialog;
//    }
//
//    public void photo() {
//        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        AppInstance.getForegroundActivity().startActivityForResult(
//                openCameraIntent, TAKE_PICTURE);
//    }
//
//    /**
//     * 选择相机或多选相册的弹出对话框
//     */
//    public AlertDialog getMorePhotoDialog() {
//        final Activity activity = AppInstance.getForegroundActivity();
//        AlertDialog alertDialog = new AlertDialog.Builder(activity)
//                .setItems(new String[]{"拍照", "从相册选择"},
//                        new DialogInterface.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialog,
//                                                int which) {
//                                switch (which) {
//                                    case 0:
//                                        // 拍照，自定义路径
//                                        currentImgUri = PhotoUtil
//                                                .takePhotoCustomerPath(
//                                                        activity,
//                                                        Constants.CACHE_DIR_IMG,
////                                                        Constants.CACHE_PAIZHAO, TAKE_PICTURE);
//                                                        "", TAKE_PICTURE);
//                                        break;
//                                    case 1:
//                                        // 本地相册
//                                        Intent intent = new Intent(activity,
//                                                AlbumActivity.class);
//                                        activity.startActivityForResult(intent,
//                                                TAKE_MORE_PICTURE);
//                                        break;
//
//                                    default:
//                                        break;
//                                }
//                                dialog.dismiss();
//                            }
//                        }).setNegativeButton("取消", null).create();
//        return alertDialog;
//    }
//
//    /**
//     * 获取屏幕宽
//     *
//     * @return
//     */
//    public int getWindowWidth(Activity context) {
//        DisplayMetrics metric = new DisplayMetrics();
//        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
//        int width = metric.widthPixels; // 屏幕宽度（像素）
//        return width;
//    }
//
//    /**
//     * 获取屏幕高
//     *
//     * @return
//     */
//    public int getWindowHeight(Activity context) {
//        DisplayMetrics metric = new DisplayMetrics();
//        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
//        int height = metric.heightPixels; // 屏幕宽度（像素）
//        return height;
//    }
//    public static int getMainThreadId() {
//        return AppInstance.getMainThreadId();
//    }
//
//    /**
//     * 居中图片ImageSpan
//     */
//    public static class CenteredImageSpan extends ImageSpan {
//
//        public CenteredImageSpan(Context context, final int drawableRes) {
//            super(context, drawableRes);
//        }
//
//        public CenteredImageSpan(Drawable drawableRes) {
//            super(drawableRes);
//        }
//
//        @Override
//        public void draw(@NonNull Canvas canvas, CharSequence text,
//                         int start, int end, float x,
//                         int top, int y, int bottom, @NonNull Paint paint) {
//            // image to draw
//            Drawable b = getDrawable();
//            // font metrics of text to be replaced
//            Paint.FontMetricsInt fm = paint.getFontMetricsInt();
//            int transY = (y + fm.descent + y + fm.ascent) / 2
//                    - b.getBounds().bottom / 2;
//
//            canvas.save();
//            canvas.translate(x, transY);
//            b.draw(canvas);
//            canvas.restore();
//        }
//    }
//
//    /**
//     *  text为空,控件父类不显示
//     * @param name
//     * @param view
//     */
//    public static void setInfo(String name, TextView view) {
//        ViewGroup parent = (ViewGroup) view.getParent();
//        if (UIUtils.isEmpty(name)) {
//            if (parent!=null)
//            parent.setVisibility(View.GONE);
//        } else {
//            view.setText(name);
//            if (parent!=null)
//            parent.setVisibility(View.VISIBLE);
//        }
//    }
//
//    /**
//     *  text为空,控件不显示
//     * @param name
//     * @param view
//     * @param content
//     */
//    public static  void setInfoSelf(String name, TextView view,String content) {
//        if (UIUtils.isEmpty(name)) {
//            view.setVisibility(View.GONE);
//        } else {
//            view.setText(content);
//            view.setVisibility(View.VISIBLE);
//        }
//    }
//
//    /**
//     *  text为空,控件不显示
//     * @param name
//     * @param view
//     */
//    public static  void setInfoSelf(String name, TextView view) {
//        setInfoSelf(name,view,name);
//    }
//
//    /**
//     * 从字符串里获取手机号
//     * @param sParam
//     * @return
//     */
//    public static String getTelnum(String sParam){
//        return getZZ(sParam,"(1|861)(3|4|5|7|8)\\d{9}$*");
//    }
//    /**
//     * 从字符串里获取
//     * @param sParam
//     * @return
//     */
//    public static String getZZ(String sParam,String zz){
//        if(sParam.length()<=0)
//            return "";
//        Pattern pattern = Pattern.compile(zz);
//        Matcher matcher = pattern.matcher(sParam);
//        StringBuffer bf = new StringBuffer();
//        while (matcher.find()) {
//            bf.append(matcher.group()).append(",");
//        }
//        int len = bf.length();
//        if (len > 0) {
//            bf.deleteCharAt(len - 1);
//        }
//        return bf.toString();
//    }
//
//    /**
//     * textView拦截超链接(注意:在textView上加上android:autoLink="web")
//     * @param tv
//     * @param content
//     * @param color 点击字体颜色(默认text8蓝色)
//     * @param underline 下划线
//     */
//    public static SpannableStringBuilder setTextAutoLine(TextView tv, String content, int color, boolean underline) {
//        Spanned text = Html.fromHtml(content);
//        tv.setText(text);
//        tv.setMovementMethod(LinkMovementMethod.getInstance());
//        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
//        if (text instanceof Spannable) {
//            int end = text.length();
//            URLSpan[] urlSpans = text.getSpans(0, end, URLSpan.class);
//            if (urlSpans.length == 0) {
//                spannableStringBuilder = (SpannableStringBuilder)getClickableHtml(spannableStringBuilder, spannableStringBuilder, color, underline);
//                tv.setText(spannableStringBuilder);
//                return spannableStringBuilder;
//            }
//            // 循环遍历并拦截 所有http://开头的链接
//            for (URLSpan uri : urlSpans) {
//                String url = uri.getURL();
//                if (url.indexOf("http://") == 0) {
//                    ClickableSpan customUrlSpan = new MyClickSpan(url,color,underline);
//                    spannableStringBuilder.setSpan(customUrlSpan, text.getSpanStart(uri),
//                            text.getSpanEnd(uri), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//                }
//            }
//            spannableStringBuilder = (SpannableStringBuilder)getClickableHtml(spannableStringBuilder, spannableStringBuilder, color, underline);
//            tv.setText(spannableStringBuilder);
//        }
//        return spannableStringBuilder;
//    }
//    /**
//     * textView可点击链接( 加上setMovementMethod(LinkMovementMethod.getInstance());//响应点击事件)
//     * @param html
//     * @param sps
//     * @param color
//     * @param underline
//     * @return
//     */
//    public static CharSequence getClickableHtml(CharSequence html, SpannableStringBuilder sps,int color,boolean underline) {
//        Pattern pattern = Pattern.compile(
//                "[http|https]+[://]+[0-9A-Za-z:/[-]_#[?][=][.]]*",
//                Pattern.CASE_INSENSITIVE);
//        Matcher m = pattern.matcher(html);
//        int startPoint = 0;
//        while (m.find(startPoint)) {
//            int endPoint = m.end();
//            final String hit = m.group();
//            ClickableSpan clickSpan = new MyClickSpan(hit,color,underline);
//            sps.setSpan(clickSpan, endPoint - hit.length(), endPoint,
//                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);// 用Span替换对应长度的url
//            startPoint = endPoint;
//        }
//        return sps;
//        // Spanned spannedHtml = Html.fromHtml(html);
//        // SpannableStringBuilder clickableHtmlBuilder = new
//        // SpannableStringBuilder(spannedHtml);
//        // URLSpan[] urls = clickableHtmlBuilder.getSpans(0,
//        // spannedHtml.length(), URLSpan.class);
//        // for(final URLSpan span : urls) {
//        // setLinkClickable(clickableHtmlBuilder, span);
//        // }
//        // return clickableHtmlBuilder;
//    }
//
//}

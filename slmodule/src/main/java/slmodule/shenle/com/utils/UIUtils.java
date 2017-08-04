//package slmodule.shenle.com.utils;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.content.res.ColorStateList;
//import android.content.res.Resources;
//import android.graphics.Rect;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.os.Handler;
//import android.text.SpannableString;
//import android.text.Spanned;
//import android.text.SpannedString;
//import android.text.style.AbsoluteSizeSpan;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.AdapterView;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.lty.zhuyitong.AppInstance;
//import com.lty.zhuyitong.base.holder.BaseHolder;
//import com.lty.zhuyitong.view.BadgeView;
//import com.lty.zhuyitong.zysc.GoodsDetailsActivity;
//import com.lty.zhuyitong.zysc.PaySuccessActivity;
//
//import io.rong.eventbus.EventBus;
//
//public class UIUtils {
//
//    private static Toast toast;
//
//    public static Context getContext() {
//        return AppInstance.getApplication();
//    }
//
//    public static Thread getMainThread() {
//        return AppInstance.getMainThread();
//    }
//
//    public static int getMainThreadId() {
//        return AppInstance.getMainThreadId();
//    }
//
//    /**
//     * dip转换px
//     */
//    public static int dip2px(int dip) {
//        final float scale = getContext().getResources().getDisplayMetrics().density;
//        return (int) (dip * scale + 0.5f);
//    }
//
//    /**
//     * pxz转换dip
//     */
//    public static int px2dip(int px) {
//        final float scale = getContext().getResources().getDisplayMetrics().density;
//        return (int) (px / scale + 0.5f);
//    }
//
//    /**
//     * 获取屏幕的宽 单位px
//     *
//     * @return int
//     */
//    public static int getScreenWidth() {
//        return getContext().getResources().getDisplayMetrics().widthPixels;
//    }
//
//    /**
//     * 获取屏幕的高 单位px
//     *
//     * @return int
//     */
//    public static int getScreenHeight() {
//        return getContext().getResources().getDisplayMetrics().heightPixels;
//    }
//
//    /**
//     * 获取主线程的handler
//     */
//    public static Handler getHandler() {
//        return AppInstance.getMainThreadHandler();
//    }
//
//    /**
//     * 延时在主线程执行runnable
//     */
//    public static boolean postDelayed(Runnable runnable, long delayMillis) {
//        return getHandler().postDelayed(runnable, delayMillis);
//    }
//
//    /**
//     * 在主线程执行runnable
//     */
//    public static boolean post(Runnable runnable) {
//        return getHandler().post(runnable);
//    }
//
//    /**
//     * 从主线程looper里面移除runnable
//     */
//    public static void removeCallbacks(Runnable runnable) {
//        getHandler().removeCallbacks(runnable);
//    }
//
//    public static View inflate(int resId) {
//        return LayoutInflater.from(getContext()).inflate(resId, null);
//    }
//
//    /**
//     * 获取资源
//     */
//    public static Resources getResources() {
//        return getContext().getResources();
//    }
//
//    /**
//     * 获取文字
//     */
//    public static String getString(int resId) {
//        return getResources().getString(resId);
//    }
//
//    /**
//     * 获取文字数组
//     */
//    public static String[] getStringArray(int resId) {
//        return getResources().getStringArray(resId);
//    }
//
//    /**
//     * 获取dimen
//     */
//    public static int getDimens(int resId) {
//        return getResources().getDimensionPixelSize(resId);
//    }
//
//    /**
//     * 获取drawable
//     */
//    public static Drawable getDrawable(int resId) {
//        return getResources().getDrawable(resId);
//    }
//
//    /**
//     * 获取drawable
//     */
//    public static int getDrawableId(String resName) {
//        int indentify = getResources().getIdentifier(getContext().getPackageName() + ":drawable/" +
//                resName, null, null);
//        return indentify;
//    }
//
//    /**
//     * 获取颜色
//     */
//    public static int getColor(int resId) {
//        return getResources().getColor(resId);
//    }
//
//    /**
//     * 获取颜色选择器
//     */
//    public static ColorStateList getColorStateList(int resId) {
//        return getResources().getColorStateList(resId);
//    }
//
//    // 判断当前的线程是不是在主线程
//    public static boolean isRunInMainThread() {
//        return android.os.Process.myTid() == getMainThreadId();
//    }
//
//    public static void runInMainThread(Runnable runnable) {
//        if (isRunInMainThread()) {
//            runnable.run();
//        } else {
//            post(runnable);
//        }
//    }
//
//    public static void startActivity(Class cl, Bundle bundle, String bundlename) {
//        Activity activity = AppInstance.getForegroundActivity();
//        Intent intent = new Intent(activity, cl);
//        if (bundle != null) {
//            intent.putExtra(bundlename, bundle);
//        }
//        if (activity != null) {
////			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            activity.startActivity(intent);
//        } else {
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            getContext().startActivity(intent);
//        }
//    }
//
//    public static void startActivity(Intent intent) {
//        Activity activity = AppInstance.getForegroundActivity();
//        if (activity != null) {
//            activity.startActivity(intent);
//        } else {
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            getContext().startActivity(intent);
//        }
//    }
//
//    public static void startActivity(Class cl) {
//        Activity activity = AppInstance.getForegroundActivity();
//        Intent intent = new Intent(activity, cl);
//        if (activity != null) {
////            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            activity.startActivity(intent);
//        } else {
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            getContext().startActivity(intent);
//        }
//    }
//
//    public static void startActivityForResult(Class cl, int requestcode) {
//        Activity activity = AppInstance.getForegroundActivity();
//        Intent intent = new Intent(activity, cl);
//        if (activity != null) {
////            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            activity.startActivityForResult(intent, requestcode);
//        } else {
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            getContext().startActivity(intent);
//        }
//    }
//
//    public static void startActivityForResult(Class cl, Bundle bundle, int requestcode) {
//        Activity activity = AppInstance.getForegroundActivity();
//        Intent intent = new Intent(activity, cl);
//        if (bundle != null) {
//            intent.putExtras(bundle);
//        }
//        if (activity != null) {
////            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            activity.startActivityForResult(intent, requestcode);
//        } else {
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            getContext().startActivity(intent);
//        }
//    }
//
//    public static void startActivity(Class cl, Bundle bundle) {
//        Activity activity = AppInstance.getForegroundActivity();
//        Intent intent = new Intent(activity, cl);
//        if (bundle != null) {
//            intent.putExtras(bundle);
//        }
//        if (activity != null) {
////            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            activity.startActivity(intent);
//        } else {
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            getContext().startActivity(intent);
//        }
//    }
//
//
//    /**
//     * 对toast的简易封装。线程安全，可以在非UI线程调用。
//     */
//    public static void showToastSafe(final int resId) {
//        showToastSafe(getString(resId));
//    }
//
//    /**
//     * 对toast的简易封装。线程安全，可以在非UI线程调用。
//     */
//    public static void showToastSafe(final String str) {
//        if (str.isEmpty()) {
//            return;
//        }
//        if (isRunInMainThread()) {
//            showToast(str);
//        } else {
//            post(new Runnable() {
//                @Override
//                public void run() {
//                    showToast(str);
//                }
//            });
//        }
//    }
//
//    private static void showToast(String str) {
//        Activity frontActivity = AppInstance.getForegroundActivity();
//        if (frontActivity != null) {
//            if (toast == null) {
//                toast = Toast.makeText(frontActivity, str, Toast.LENGTH_LONG);
//            } else {
//                toast.setText(str);
//            }
//            toast.show();
//        }
//    }
//
//    /**
//     * 跳转到商品详情页
//     *
//     * @param context
//     * @param goods_id 商品id
//     */
//    public static void toGoodsDetailsActivity(Context context, String goods_id) {
//        Intent intent = new Intent();
//        intent.putExtra("goods_id", goods_id);
//        intent.setClass(context, GoodsDetailsActivity.class);
//        context.startActivity(intent);
//    }
//
//    /**
//     * 跳转到 支付成功页面
//     *
//     * @param context 订单id
//     */
//    public static void toPayResult(Context context) {
//        Intent intent = new Intent();
//        intent.setClass(context, PaySuccessActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//    }
//
//    ;
//
//    /**
//     * 格式化价格
//     *
//     * @param price
//     * @return 格式化后的价格 格式为 ￥ 0.00
//     */
//    public static String formatPrice(String price) {
//        try {
//            return "￥" + String.format("%.2f", Double.parseDouble(price));
//        } catch (Exception e) {
//            return price;
//        }
//    }
//
//    /**
//     * 格式化价格,小数点后的变小
//     *
//     * @param total_fee
//     * @param textSize  小数点后的字体大小 sp
//     * @return
//     */
//    public static SpannedString formatPrice(String total_fee, int textSize) {
//        SpannableString ss = new SpannableString(total_fee);
//        if (ss.toString().contains(".")) {
//            int start = total_fee.indexOf(".");
//            if (start >= 0) {
//                AbsoluteSizeSpan ass = new AbsoluteSizeSpan(textSize, true);
//                ss.setSpan(ass, start, ss.length(),
//                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            }
//        }
//        return new SpannedString(ss);
//    }
//
//    /**
//     * 格式电话号码
//     *
//     * @param phone 格式化后 xxx-xxxx-xxxx 如果不是11位电话,则不做任何操作
//     * @return
//     */
//    public static String formatPhone(String phone) {
//        phone = phone.trim();
//        if (phone.length() == 11) {
//            StringBuilder sBuilder = new StringBuilder(phone);
//            String line = "-";
//            String newString = sBuilder.substring(0, 3) + line
//                    + sBuilder.substring(3, 7) + line
//                    + sBuilder.substring(7, sBuilder.length());
//            phone = newString;
//        }
//        return phone;
//    }
//
//    /**
//     * @param activity
//     * @return 状态栏高度  > 0 success; <= 0 fail
//     */
//    public static int getStatusHeight(Activity activity) {
//        int statusHeight = 0;
//        Rect localRect = new Rect();
//        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
//        statusHeight = localRect.top;
//        if (0 == statusHeight) {
//            Class<?> localClass;
//            try {
//                localClass = Class.forName("com.android.internal.R$dimen");
//                Object localObject = localClass.newInstance();
//                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject)
//                        .toString());
//                statusHeight = activity.getResources().getDimensionPixelSize(i5);
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (InstantiationException e) {
//                e.printStackTrace();
//            } catch (NoSuchFieldException e) {
//                e.printStackTrace();
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            }
//        }
//        return statusHeight;
//    }
//
//    public static void closeWindowKeyBoard() {
//        InputMethodManager im = (InputMethodManager) UIUtils.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (UIUtils.getActivity().getCurrentFocus() != null) {
//            im.hideSoftInputFromWindow(UIUtils.getActivity().getCurrentFocus()
//                            .getApplicationWindowToken(),
//                    InputMethodManager.HIDE_NOT_ALWAYS);
//        }
//    }
//
//    public static void closeWindowKeyBoard(EditText et) {
//        InputMethodManager imm = (InputMethodManager) UIUtils.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
//    }
//
//    public static void openWindowKeyBoard() {
//        InputMethodManager imm = (InputMethodManager) UIUtils.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//    }
//
//    public static void openWindowKeyBoard(EditText et) {
//        InputMethodManager imm = (InputMethodManager) UIUtils.getActivity()
//                .getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(et, 0);
//    }
//
//    public static void showErr() {
//        UIUtils.showToastSafe("网络访问失败");
//    }
//
//    public static Activity getActivity() {
//        return AppInstance.getForegroundActivity();
//    }
//
//
//    public static boolean isEmpty(String s) {
//        return s == null || s.trim().isEmpty() || s.equals("null");
//    }
//
//    /**
//     * EditText粘帖功能(待完善)
//     *
//     * @param et_content
//     */
//    public static void setClipboard(EditText et_content) {
////        et_content.setTextIsSelectable(true);
////        et_content.setOnLongClickListener(new MyClipboardOnLongClick(et_content));
//    }
//
//    /**
//     * 初始化红点
//     *
//     * @param view
//     * @return
//     */
//    public static BadgeView initBadge(View view, String text) {
//        BadgeView badge = new BadgeView(getActivity(), view);
//        badge.setTextSize(9);
//        if (text == null)
//            text = "";
//        badge.setText(text);
//        badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
//        badge.setBadgeMargin(UIUtils.dip2px(5));
//        return badge;
//    }
//
//    public static void register(Object o) {
//        EventBus.getDefault().register(o);
//    }
//
//    public static void unregister(Object o) {
//        EventBus.getDefault().unregister(o);
//    }
//
//    /**
//     * 获取资源文件ID
//     *
//     * @param resName
//     * @param defType
//     * @return
//     */
//    public static int getResId(String resName, String defType) {
//        return getContext().getResources().getIdentifier(resName, defType, getContext().getPackageName());
//    }
//
//    /**
//     * 回收view
//     *
//     * @param view
//     */
//    public static void unbindDrawables(View view) {
//        if (view.getTag() != null && view.getTag() instanceof BaseHolder) {
//            ((BaseHolder) view.getTag()).onDestroy();
//        }
//        if (view.getBackground() != null) {
//            view.getBackground().setCallback(null);
//        }
//        if (view instanceof ViewGroup && !(view instanceof AdapterView)) {
//            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
//                unbindDrawables(((ViewGroup) view).getChildAt(i));
//            }
//            ((ViewGroup) view).removeAllViews();
//        }
//    }
//}

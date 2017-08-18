package zyt.xunfeilib.view;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.ui.RecognizerDialog;

/**
 * Created by shenle on 2017/6/1.
 */

public class Voice2TextDialog extends RecognizerDialog {

    private final LinearLayout.LayoutParams params;
    private final TextView tv;

    public Voice2TextDialog(Context context, InitListener initListener) {
        super(context, initListener);
        tv = new TextView(context);
        tv.setText("核心技术由科大讯飞提供");
        tv.setGravity(Gravity.CENTER);
        tv.setTextColor(Color.GRAY);
        final float scale = getContext().getResources().getDisplayMetrics().density;
        int i = (int) (6 * scale + 0.5f);
        tv.setTextSize(i);
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,0,0,20);
        tv.setLayoutParams(params);
    }

    @Override
    public void show() {
        super.show();
        if (tv.getParent() == null) {
            FrameLayout view = (FrameLayout) getWindow().getDecorView();
            View childAt = ((FrameLayout)((FrameLayout)view.getChildAt(0)).getChildAt(0)).getChildAt(0);
            if (childAt != null && childAt instanceof LinearLayout) {
                ((LinearLayout) childAt).setOrientation(LinearLayout.VERTICAL);
                ((LinearLayout) childAt).addView(tv);
            }
        }
    }
}

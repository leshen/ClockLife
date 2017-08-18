package zyt.xunfeilib;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

/**
 * Created by shenle on 2017/4/18.
 */

public class XunFeiHelper {
    private static XunFeiHelper instance;
    private static final String XUNFEI_APPID = "598414f9";

    public static XunFeiHelper getInstance() {
        if (instance == null) {
            instance = new XunFeiHelper();
        }
        return instance;
    }

    /**
     * application调用一次
     * @param context
     */
    public static void init(Context context) {
        //此接口在非主进程调用会返回null对象，如需在非主进程使用语音功能，请使用参数：SpeechConstant.APPID +"=12345678," + SpeechConstant.FORCE_LOGIN +"=true"。
        SpeechUtility.createUtility(context, SpeechConstant.APPID + "=" + XUNFEI_APPID);
    }


}

package zyt.xunfeilib.newinterface;

import com.iflytek.cloud.SpeechError;

/**
 * Created by shenle on 2017/4/19.
 */

public interface ResultBackListener {
    void onResult(String results,boolean isLast);
}

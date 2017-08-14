package clocklife.shenle.com

import android.os.Bundle
import io.reactivex.Observable
import slmodule.shenle.com.BaseActivity
import java.util.concurrent.TimeUnit

class InitActivity : BaseActivity() {
    override fun getRootView(): Int {
        return R.layout.activity_init
    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        Observable.timer(2, TimeUnit.SECONDS).subscribe { LoginActivity.goHere()
            finish()}
    }
}

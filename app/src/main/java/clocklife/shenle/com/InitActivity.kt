package clocklife.shenle.com

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import slmodule.shenle.com.BaseActivity
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class InitActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_init)
        Observable.timer(2, TimeUnit.SECONDS).subscribe { MainActivity.goHere(this) }
    }
}

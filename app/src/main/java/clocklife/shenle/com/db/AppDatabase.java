package clocklife.shenle.com.db;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * 自己创的数据库
 * Created by shenle on 2016/11/1.
 */
@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase{
    //数据库名称
    public static final String NAME = "slApp";
    //数据库版本号
    public static final int VERSION = 1;
}

package clocklife.shenle.com.db.bean;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import clocklife.shenle.com.db.AppDatabase;


/**
 * app基本信息
 */
@Table(database = AppDatabase.class)
public class AppInfo extends BaseModel {
    //自增ID
    @PrimaryKey(autoincrement = true)
    public Long id;
    @Column
    public String channelId;
    @Column
    public String UUID;
    @Column
    public String province;
    @Column
    public String city;
    @Column
    public String district;
    @Column
    public String latitude;
    @Column
    public String longitude;
    @Column
    public long clearTime;//定期清理缓存
    @Column
    public long ggTime;//广告缓存时间
    @Column
    public boolean init;//是否是第一次登录
}

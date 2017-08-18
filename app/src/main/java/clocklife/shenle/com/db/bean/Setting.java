package clocklife.shenle.com.db.bean;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import clocklife.shenle.com.db.AppDatabase;


/**
 * 搜索历史记录
 * Created by shenle on 2016/11/1.
 */
@Table(database = AppDatabase.class)
public class Setting extends BaseModel {
    //自增ID
    @PrimaryKey(autoincrement = true)
    public Long id;
    @Column
    public boolean kg_push = true;//消息通知开关
    @Column
    public boolean kg_location = true;//位置信息开关
    @Column
    public boolean kg_read_txl = true;//读取通讯录开关
    @Column
    public boolean kg_auto_play_zj = false;//自动播猪价开关
    @Column
    public boolean kg_zx_show_play = false;//资讯显示播猪价开关
    @Column
    public boolean day_night_kg = false;//夜间模式开关
}

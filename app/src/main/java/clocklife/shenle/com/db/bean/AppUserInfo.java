package clocklife.shenle.com.db.bean;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import clocklife.shenle.com.db.AppDatabase;


/**
 * 个人信息
 * Created by shenle on 2016/11/1.
 */
@Table(database = AppDatabase.class)
public class AppUserInfo extends BaseModel {
    //自增ID
    @PrimaryKey(autoincrement = true)
    public Long id;
    @Column
    public String name="";
    @Column
    public String uid= "";
    @Column
    public String photo= "";
    @Column
    public String phone= "";
    @Column
    public String brithday= "";
    @Column
    public String trueName= "";
    @Column
    public String token= "";//融云令牌
    @Column
    public String password="";
    @Column
    public String tag = "";//备注
    @Column
    public int gender = 0;
    @Column
    public String sign = "";
    @Column
    public Boolean hasLogin = false;//
    public AppUserInfo(){
        super();
    }
    public AppUserInfo(String name, String password, String photo, String uid) {
        this.name = name;
        this.password = password;
        this.photo = photo;
        this.uid = uid;
    }
}

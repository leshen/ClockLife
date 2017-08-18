package clocklife.shenle.com.db.bean;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import clocklife.shenle.com.db.AppDatabase;


/**
 * app模块红点点击信息
 */
@Table(database = AppDatabase.class)
public class PointClick extends BaseModel {
    //自增ID
    @PrimaryKey(autoincrement = true)
    public Long id;
    @Column
    public String viewTag;//点击标识
    @Column
    public int num;//点击次数
    @Column
    public String about;//点击信息(备注)

    public PointClick() {
    }

    public PointClick(String viewTag, int num) {
        this.viewTag = viewTag;
        this.num = num;
    }
}

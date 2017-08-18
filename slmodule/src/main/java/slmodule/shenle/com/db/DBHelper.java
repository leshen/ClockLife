package slmodule.shenle.com.db;

import com.iflytek.cloud.thirdparty.T;
import com.raizlabs.android.dbflow.sql.language.SQLOperator;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.List;


/**
 * Created by shenle on 2016/11/1.
 */

public class DBHelper {
    /**
     * 单个查询
     * @param c
     * @return
     */
    public static <T extends BaseModel> T querySingle(Class<T> c){
        return (T)SQLite.select().from(c).querySingle();
    }
    /**
     * 单个查询,没有就创建空的
     * @param c
     * @return
     */
//    public static <T extends BaseModel> T querySingleOrMake(Class<T> c){
//        T t = SQLite.select().from(c).querySingle();
//        if (t==null){
//            try {
//                t = c.newInstance();
//                t.save();
//            } catch (InstantiationException e) {
//                e.printStackTrace();
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }
//        return t;
//    }

    /**
     *
     * @param c
     * @param where(example:model_Table.gender.eq(1)  查询单个gender是1)
     * @return
     */
    public static <T extends BaseModel>T  querySingle(Class<T> c, SQLOperator where){
        T model = SQLite.select().from(c).where(where).querySingle();
        return model;
    }
    /**
     *
     * @param c
     * @param where(example:model_Table.gender.eq(1)  查询单个gender是1)
     * @return
     */
    public static <T extends BaseModel>T querySingleOrMake(Class<T> c, SQLOperator where){
        T t = SQLite.select().from(c).where(where).querySingle();
        if (t==null){
            try {
                t = c.newInstance();
                t.save();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return t;
    }
    /**
     * 返回所有查询结果
     * @param c
     * @return
     */
    public static List<? extends BaseModel> queryList(Class<? extends BaseModel> c){
        List<? extends BaseModel> models = SQLite.select().from(c).queryList();
        return models;
    }

    /**
     *
     * @param c
     * @param where(example:model_Table.gender.eq(1)  查询所有gender是1)
     * @return
     */
    public static List<? extends BaseModel> queryList(Class<? extends BaseModel> c, SQLOperator where){
        List<? extends BaseModel> models = SQLite.select().from(c).where(where).queryList();
        return models;
    }
}

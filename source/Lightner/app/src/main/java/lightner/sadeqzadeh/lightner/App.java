package lightner.sadeqzadeh.lightner;

import android.app.Application;

import org.greenrobot.greendao.database.Database;

import lightner.sadeqzadeh.lightner.entity.DaoMaster;
import lightner.sadeqzadeh.lightner.entity.DaoSession;

public class App extends Application {

    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "lightner");
        Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}

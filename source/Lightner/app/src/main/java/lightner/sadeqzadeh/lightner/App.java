package lightner.sadeqzadeh.lightner;

import android.app.Application;

import org.greenrobot.greendao.database.Database;

import lightner.sadeqzadeh.lightner.entity.DaoMaster;
import lightner.sadeqzadeh.lightner.entity.DaoSession;
import lightner.sadeqzadeh.lightner.sqlite.UpgradeHelper;

public class App extends Application {

    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        UpgradeHelper upgradeHelper = new UpgradeHelper(this, "Wordika",null);
        Database db = upgradeHelper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}

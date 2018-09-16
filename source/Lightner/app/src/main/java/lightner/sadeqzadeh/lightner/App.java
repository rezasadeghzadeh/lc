package lightner.sadeqzadeh.lightner;

import android.app.Application;
import android.content.Context;

import org.greenrobot.greendao.database.Database;

import lightner.sadeqzadeh.lightner.entity.DaoMaster;
import lightner.sadeqzadeh.lightner.entity.DaoSession;
import lightner.sadeqzadeh.lightner.sqlite.UpgradeHelper;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class App extends Application {

    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();

        //sqlite
        UpgradeHelper upgradeHelper = new UpgradeHelper(this, "Wordika",null);
        Database db = upgradeHelper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();

        //change font for all
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/vazir.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

    }

    public DaoSession getDaoSession() {
        return daoSession;
    }



}

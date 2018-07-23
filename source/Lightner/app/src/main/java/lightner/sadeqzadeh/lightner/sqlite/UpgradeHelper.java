package lightner.sadeqzadeh.lightner.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import lightner.sadeqzadeh.lightner.entity.DaoMaster;

public class UpgradeHelper extends DaoMaster.DevOpenHelper {
    public UpgradeHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    /**
     * Apply the appropriate migrations to update the database.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        applyMigration(db, oldVersion);

        switch (newVersion) {
            case 2:
                break;
            case 3:
                break;
            default:
                return;
        }

    }

    private void applyMigration(SQLiteDatabase db, int oldVersion) {
        db.execSQL("ALTER TABLE 'CATEGORY' ADD 'LAST_VISIT' DATE");

    }
}

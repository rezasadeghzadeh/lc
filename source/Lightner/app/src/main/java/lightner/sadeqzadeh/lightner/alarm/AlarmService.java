package lightner.sadeqzadeh.lightner.alarm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.Calendar;
import java.util.Date;
import lightner.sadeqzadeh.lightner.App;
import lightner.sadeqzadeh.lightner.Const;
import lightner.sadeqzadeh.lightner.MainActivity;
import lightner.sadeqzadeh.lightner.R;
import lightner.sadeqzadeh.lightner.Util;
import lightner.sadeqzadeh.lightner.entity.DaoSession;
import lightner.sadeqzadeh.lightner.entity.Flashcard;
import lightner.sadeqzadeh.lightner.entity.FlashcardDao;

public class AlarmService extends IntentService {
    private NotificationManager alarmNotificationManager;

    public AlarmService() {
        super("AlarmService");
    }

    @Override
    public void onHandleIntent(Intent intent) {
        Calendar calendar  = Calendar.getInstance();
        int hour = Integer.parseInt(Util.fetchFromPreferences(Const.ALARM_HOUR));
        int minute = Integer.parseInt(Util.fetchFromPreferences(Const.ALARM_MINUTE));
        if(calendar.get(Calendar.HOUR_OF_DAY)!= hour ||  calendar.get(Calendar.MINUTE) != minute){
            return;
        }
        DaoSession daoSession = ((App) getApplication()).getDaoSession();
        FlashcardDao flashcardDao  = daoSession.getFlashcardDao();
        QueryBuilder<Flashcard> queryBuilder = flashcardDao.queryBuilder();
        Date currentDate  =  new Date();
        final long  total= queryBuilder.where(
                FlashcardDao.Properties.NextVisit.le(currentDate)
        ).list().size();
        if(total  > 0){
            sendNotification(getString(R.string.you_have_flashcard_to_review));
        }
    }

    private void sendNotification(String msg) {

        alarmNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        NotificationCompat.Builder alamNotificationBuilder = new NotificationCompat.Builder(
                this).setContentTitle("وردیکا").setSmallIcon(R.drawable.ic_launcher)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg);


        alamNotificationBuilder.setContentIntent(contentIntent);
        alarmNotificationManager.notify(1, alamNotificationBuilder.build());
    }
}

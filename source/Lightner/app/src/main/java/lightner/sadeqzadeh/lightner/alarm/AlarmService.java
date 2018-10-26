package lightner.sadeqzadeh.lightner.alarm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.Date;
import java.util.List;

import lightner.sadeqzadeh.lightner.App;
import lightner.sadeqzadeh.lightner.MainActivity;
import lightner.sadeqzadeh.lightner.R;
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

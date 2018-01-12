package chickie8x.firebaseauth;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

/**
 * Created by Admin on 12/11/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {
    private long [] viber={500,1000};
    private Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationGreeting(context);
    }
    public void NotificationGreeting(Context context){
        Intent notificationIntent = new Intent(context,ViewContent.class);
        TaskStackBuilder taskStackBuilder =TaskStackBuilder.create(context);
        taskStackBuilder.addParentStack(ViewContent.class);
        taskStackBuilder.addNextIntent(notificationIntent);
        PendingIntent pendingIntent =taskStackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle("Thông báo ")
                .setContentText("Hôm nay bạn có làm thêm giờ không ?")
                .setVibrate(viber)
                .setSound(uri)
                .setAutoCancel(true);
        mBuilder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,mBuilder.build());
    }
}

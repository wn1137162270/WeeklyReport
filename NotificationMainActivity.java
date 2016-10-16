package notification.imooc.lenovo.notification;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends Activity implements View.OnClickListener {
    private Button sendNotification;
    private Button cancelNotification;
    private NotificationManager notificationManager;
    private int notificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        sendNotification = (Button) findViewById(R.id.send_btn);
        cancelNotification = (Button) findViewById(R.id.cancel_btn);

        sendNotification.setOnClickListener(MainActivity.this);
        cancelNotification.setOnClickListener(MainActivity.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_btn:
                sendNotification();
                break;
            case R.id.cancel_btn:
                cancelNotification();
                break;
        }
    }

    private void sendNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.drawable.ic_launcher);
        builder.setTicker("这是一个来自Notification的通知");
        builder.setContentTitle("通知");
        builder.setContentText("这是这个通知的内容");
        builder.setWhen(System.currentTimeMillis());
        builder.setContentIntent(pendingIntent);
        builder.setDefaults(Notification.DEFAULT_SOUND);
        builder.setDefaults(Notification.DEFAULT_LIGHTS);
        builder.setDefaults(Notification.DEFAULT_VIBRATE);
        Notification notification = builder.build();
        notificationManager.notify(notificationId, notification);
    }

    private void cancelNotification() {
        notificationManager.cancel(notificationId);
    }
}


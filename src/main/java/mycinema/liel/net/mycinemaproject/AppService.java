package mycinema.liel.net.mycinemaproject;
//
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Intent;
//import android.os.IBinder;
//import android.support.annotation.Nullable;
//import android.support.v4.app.NotificationCompat;
//import android.util.Log;
//
//import java.util.concurrent.ScheduledThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//
//import mycinema.liel.net.mycinemaproject.HttpManagers.Movies.GetMovies;
//
//public class AppService extends Service {
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        super.onStartCommand(intent, flags, startId);
//
//        ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(1);
//        scheduler.scheduleWithFixedDelay(new Runnable() {
//            @Override
//            public void run() {
//                Log.d("AppService", "Scheduler run");
//                startNotification();
//            }
//        }, 0L, 60, TimeUnit.SECONDS);
//
//        return START_STICKY;
//    }
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//    }
//
//    public void startNotification() {
//        NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        NotificationCompat.Builder ncomp = new NotificationCompat.Builder(this);
//        ncomp.setContentTitle("MyCinema");
//        ncomp.setContentText("Some sweet movies are waiting for you!");
//        ncomp.setSmallIcon(R.mipmap.ic_launcher);
//        ncomp.setAutoCancel(true);
//
//        Intent intent = new Intent(this, Analyze.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
//        ncomp.setContentIntent(pendingIntent);
//
//        nManager.notify((int)System.currentTimeMillis(),ncomp.build());
//    }
//}

//import android.app.IntentService;
//
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Build;
//import android.provider.Settings;
//import android.support.annotation.Nullable;
//import android.support.v4.app.NotificationCompat;
//import android.support.v4.content.ContextCompat;
//import android.util.Log;
//
//import java.util.concurrent.ScheduledThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//
//public class AppService extends IntentService {
//
//    public static final int NOTIFICATION_ID = 1;
//    public static final int CHANNEL_ID = 2;
//    private static final String NOTIFICATION_CHANNEL_ID = "my_notification_channel";
//
//    public AppService() {
//        super("AppService");
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        createNotificationChannel();
//    }
//
//    @Override
//    protected void onHandleIntent(@Nullable Intent intent) {
//        Log.d("AppService", "Started");
//        final ScheduledThreadPoolExecutor executor_ = new ScheduledThreadPoolExecutor(1);
//        executor_.scheduleWithFixedDelay(new Runnable() {
//            @Override
//            public void run() {
//                if(MainActivity.instance == null) {
//                    Log.d("AppService", "Sending notification");
//                    sendNotification("We might have some brand new movies to offer you!");
//                    Log.d("AppService", "Notification sent");
//                } else {
//                    Log.d("AppService", "MainActivity is not null");
//                }
//            }
//        }, 1, 1, TimeUnit.MINUTES);
////        try {
////            Thread.sleep(43200000);
////        } catch (InterruptedException e) {
////            // Restore interrupt status.
////            Thread.currentThread().interrupt();
////        }
////        Log.d("AppService", "Sending notification");
////        sendNotification("We might have some brand new movies to offer you!");
////        Log.d("AppService", "Notification sent");
//    }
//
//    private void sendNotification(String msg) {
//
//        Bitmap notificationLargeIconBitmap = BitmapFactory.decodeResource(
//                getApplicationContext().getResources(),
//                R.mipmap.ic_launcher);
//
//        Intent intent = new Intent(this, Analyze.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
//
//        NotificationCompat.Builder not = new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL_ID)
//                .setSmallIcon(R.drawable.icon_500x500_no_edge)
//                .setLargeIcon(notificationLargeIconBitmap)
//                .setContentTitle("MyCinema")
//                .setContentText(msg)
//                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
//                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
//                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.official_app_color))
//                .setContentIntent(pendingIntent)
//                .setAutoCancel(true);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID);
//            notificationManager.notify(NOTIFICATION_ID, not.build());
//        } else {
//            Notification not2 = new Notification.Builder(this)
//                    .setContentTitle("MyCinema")
//                    .setContentText(msg)
//                    .setSmallIcon(R.drawable.icon_500x500_no_edge)
//                    .setLargeIcon(notificationLargeIconBitmap)
//                    .setContentIntent(pendingIntent)
//                    .setAutoCancel(true)
//                    .build();
//
//            NotificationManager nm = (NotificationManager) this
//                    .getSystemService(Context.NOTIFICATION_SERVICE);
//            nm.notify(CHANNEL_ID, not2);
//        }
//    }
//
//    private void createNotificationChannel() {
//        // Create the NotificationChannel, but only on API 26+ because
//        // the NotificationChannel class is new and not in the support library
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "my channel", importance);
//            channel.setDescription("my channel description");
//            // Register the channel with the system; you can't change the importance
//            // or other notification behaviors after this
//            if(NotificationManager.class == null) System.out.println("test");
//            NotificationManager notificationManager = this.getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class AppService extends Service {

    public static final int NOTIFICATION_ID = 1;
    public static final int CHANNEL_ID = 2;
    private static final String NOTIFICATION_CHANNEL_ID = "my_notification_channel";
    private boolean not = true;

    public int counter=0;
    public AppService(Context applicationContext) {
        super();
        Log.i("AppService", "here I am!");
    }

    public AppService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startTimer();
        if(Analyze.instance != null) {
            Log.i("AppService", "Anaylize.instance != null");
            if (!Analyze.instance.sp.getBoolean("vibrations", true)) {
                Log.i("AppService", "!boolean");
                not = false;
            }
        }

        Log.i("AppService", "onStartCommand();");
        return START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent){
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartServicePendingIntent);

        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("AppService", "onDestroy();");
        stoptimertask();
    }

    private Timer timer;
    private TimerTask timerTask;
    long oldTime=0;
    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, to wake up every 1 second
        // 43200000
        timer.schedule(timerTask, 30000, 30000); //
        Log.i("AppService", "Timer started");
    }

    /**
     * it sets the timer to print the counter every x seconds
     */
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                //if(MainActivity.instance == null) {
                if(not) {
                    sendNotification("We may have some new movies for you!");
                    Log.i("AppService", "in timer ++++  " + (counter++));
                }
                //} else {
                //    Log.i("AppService", "MainAcitivty is null");
                //}
            }
        };
    }

    /**
     * not needed
     */
    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void sendNotification(String msg) {

        Bitmap notificationLargeIconBitmap = BitmapFactory.decodeResource(
                getApplicationContext().getResources(),
                R.mipmap.ic_launcher);

        System.out.println("1");

        Intent intent = new Intent(this, Analyze.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        System.out.println("2");

        NotificationCompat.Builder not = new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_500x500_no_edge)
                .setLargeIcon(notificationLargeIconBitmap)
                .setContentTitle("MyCinema")
                .setContentText(msg)
                .setChannelId(NOTIFICATION_CHANNEL_ID)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.official_app_color))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        System.out.println("3");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int notifyID = 1;
            CharSequence name = "notifications";// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance);

            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.createNotificationChannel(mChannel);

            mNotificationManager.notify(1 , not.build());

//            NotificationManagerCompat nm = NotificationManagerCompat.from(getApplicationContext());
//            nm.notify(1, not.build());
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID);
//            notificationManager.notify(NOTIFICATION_ID, not.build());
            System.out.println("4");
        } else {
            Notification not2 = new Notification.Builder(this)
                    .setContentTitle("MyCinema")
                    .setContentText(msg)
                    .setSmallIcon(R.drawable.icon_500x500_no_edge)
                    .setLargeIcon(notificationLargeIconBitmap)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();

            NotificationManager nm = (NotificationManager) this
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            nm.notify(CHANNEL_ID, not2);
            System.out.println("5");
        }
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "my channel", importance);
            channel.setDescription("my channel description");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            if(NotificationManager.class == null) System.out.println("test");
            NotificationManager notificationManager = this.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
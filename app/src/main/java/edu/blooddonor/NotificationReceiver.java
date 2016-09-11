package edu.blooddonor;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Class creating a notification for the user.
 *
 * Class is a subclass of the BroadcastReceiver class.
 * It creates a notification for the user that he can make another donation.
 *
 * @author madasionka
 *
 */

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, AddDonationActivity.REQUEST_CODE, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(android.R.drawable.arrow_up_float)
                .setContentIntent(pendingIntent)
                .setContentTitle("Blood donation - remainder")
                .setContentText("You can now make another donation!")
                .setAutoCancel(true);

        notificationManager.notify(AddDonationActivity.REQUEST_CODE, builder.build());

    }
}


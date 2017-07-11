package ec.edu.uce.spok.Servicios;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import ec.edu.uce.spok.Mensajeria.MensajeriaActivity;
import ec.edu.uce.spok.R;

/**
 * Created by usuario on 07/07/2017.
 */

public class FirebaseServiceMensajes extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        //obtener datos de firebase
        String mensaje = remoteMessage.getData().get("mensaje");
        String hora = remoteMessage.getData().get("hora");
        mensaje(mensaje, hora);
        showNotification();
    }

    private void mensaje(String mensaje, String hora) {
        Intent i = new Intent(MensajeriaActivity.MENSAJE);
        i.putExtra("key_mensaje", mensaje);
        i.putExtra("key_hora", hora);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(i);
    }

    private void showNotification() {
        Intent i = new Intent(this, MensajeriaActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_ONE_SHOT);

        Uri sonidonotificacion = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Builder builder = new Builder(this);
        builder.setAutoCancel(true);
        builder.setContentTitle("Alerta SpokMessenger");
        builder.setContentText("Alguien te envió un mensaje");
        builder.setSound(sonidonotificacion);
        //builder.setSmallIcon(R.drawable.notificacionIcono);
        builder.setSmallIcon(R.drawable.logo);
        builder.setTicker("Esto es un ticker");
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());


    }
}

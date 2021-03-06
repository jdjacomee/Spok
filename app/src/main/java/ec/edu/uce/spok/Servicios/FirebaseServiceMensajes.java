package ec.edu.uce.spok.Servicios;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import ec.edu.uce.spok.Mensajeria.MensajeriaActivity;
import ec.edu.uce.spok.Preferences;
import ec.edu.uce.spok.R;

/**
 * Created by usuario on 07/07/2017.
 */

public class FirebaseServiceMensajes extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        //obtener datos de firebase para las notificaciones
        String mensaje = remoteMessage.getData().get("mensaje");
        String hora = remoteMessage.getData().get("hora");
        String cabecera = remoteMessage.getData().get("cabecera");
        String cuerpo = remoteMessage.getData().get("cuerpo");
        String receptor = remoteMessage.getData().get("receptor");
        String emisorPHP = remoteMessage.getData().get("emisor");
        String emisor = Preferences.obtenerPreferenceString(this, Preferences.USUARIO_PREFERENCE);
        if (emisor.equals(receptor)) {
            mensaje(mensaje, hora, emisorPHP);
            showNotification(cabecera, cuerpo, emisorPHP);
        }
    }

    //crea el mensaje que se mostrará en la notificacion
    private void mensaje(String mensaje, String hora, String emisor) {
        Intent i = new Intent(MensajeriaActivity.MENSAJE);
        i.putExtra("key_mensaje", mensaje);
        i.putExtra("key_hora", hora);
        i.putExtra("key_emisor_php", emisor);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(i);
    }

    //metodo para mostrar las notificaciones
    private void showNotification(String cabecera, String cuerpo, String emisor) {
        Intent i = new Intent(this, MensajeriaActivity.class);
        i.putExtra("key_receptor", emisor);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_ONE_SHOT);

        Uri sonidonotificacion = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Builder builder = new Builder(this);
        builder.setAutoCancel(true);
        builder.setContentTitle(cabecera);
        builder.setContentText(cuerpo);
        builder.setSound(sonidonotificacion);
        //builder.setSmallIcon(R.drawable.notificacionicono2);
        builder.setSmallIcon(R.drawable.logo);
        builder.setTicker("Mensajes recibidos en Spok Messenger");
        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }
}

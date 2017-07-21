package ec.edu.uce.spok;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by David on 17/07/2017.
 */

public class Preferences {

    public static final String STRING_PREFERENCES = "ec.edu.uce.spok.Mensajeria.MensajeriaActivity";
    public static final String PREFERENCE_ESTADO_BUTTON_SESION = "estadoSesion";
    public static final String USUARIO_PREFERENCE = "usuarioLogueado";

    //Guarda el valor del radiobutton para verificar si mantiene la sesion activa o no
    public static void savePreferenceBoolean(Context c, boolean b, String key) {
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES, c.MODE_PRIVATE);
        preferences.edit().putBoolean(key, b).apply();
    }

    //Guarda el valor del usuario ingresado
    public static void savePreferendceString(Context c, String b, String key) {
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES, c.MODE_PRIVATE);
        preferences.edit().putString(key, b).apply();
    }

    public static boolean obtenerPreferenceBoolean(Context c, String key) {
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES, c.MODE_PRIVATE);
        return preferences.getBoolean(key, false);//Si es que nunca se ha guardado nada en esta key pues retornara false
    }

    public static String obtenerPreferenceString(Context c, String key) {
        SharedPreferences preferences = c.getSharedPreferences(STRING_PREFERENCES, c.MODE_PRIVATE);
        return preferences.getString(key, "");//Si es que nunca se ha guardado nada en esta key pues retornara una cadena vacia
    }

}

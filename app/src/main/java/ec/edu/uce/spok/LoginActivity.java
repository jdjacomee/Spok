package ec.edu.uce.spok;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import ec.edu.uce.spok.Amigos.AmigosActivity;
import ec.edu.uce.spok.Mensajeria.MensajeriaActivity;

public class LoginActivity extends AppCompatActivity {

    //Creamos las variables que vamos a usar del layout
    private EditText txtUser;
    private EditText txtPwd;
    private Button btnIngresar1;
    private Button btnRegistrarse1;
    private RadioButton noCerrarSesion;

    //URL del servicio web para el login
    private static final String URL_LOGIN = "https://spok.000webhostapp.com/php/obtenerPorUsuario.php?usuario=";

    //URL del servicio web para la creacion de tokens
    private static final String URL_TOKEN = "https://spok.000webhostapp.com/php/insertarActualizarToken.php";

    //Otras variables que se usarán
    private VolleyRP volleyRP;
    private RequestQueue rq;
    private String USER = "";
    private String PASS = "";
    private boolean isActivateRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Para verificar si el usuario activo la casilla de "No cerrar sesión"
        //Si estuvo activada, ingresara a la aplicacion con el mismo usuario logueado
        if (Preferences.obtenerPreferenceBoolean(this, Preferences.PREFERENCE_ESTADO_BUTTON_SESION)) {
            Intent i = new Intent(LoginActivity.this, AmigosActivity.class);
            startActivity(i);
            finish();
        }

        //inicialización de las variables
        txtUser = (EditText) findViewById(R.id.txtUsuario);
        txtPwd = (EditText) findViewById(R.id.txtPass);
        btnIngresar1 = (Button) findViewById(R.id.btnIngresar);
        btnRegistrarse1 = (Button) findViewById(R.id.btnRegistrarse);
        noCerrarSesion = (RadioButton) findViewById(R.id.rbNoCerrarSesion);
        volleyRP = VolleyRP.getInstance(this);
        rq = volleyRP.getRequestQueue();

        //Accion del boton Iniciar Sesion
        btnIngresar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String u = txtUser.getText().toString();
                String p = txtPwd.getText().toString();
                //Invocacion del método login
                login(u, p);
            }
        });

        //DESACTIVADO
        isActivateRadioButton = noCerrarSesion.isChecked();
        noCerrarSesion.setOnClickListener(new View.OnClickListener() {
            //ACTIVADO
            @Override
            public void onClick(View v) {
                if (isActivateRadioButton) {
                    noCerrarSesion.setChecked(false);
                }
                isActivateRadioButton = noCerrarSesion.isChecked();
            }
        });

        //Accion del boton Registrarse
        btnRegistrarse1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(LoginActivity.this, RegistroActivity.class);
                startActivity(i);
            }
        });

    }

    //Metodo login
    public void login(String user, String password) {
        USER = user;
        PASS = password;
        //solictud de la cuenta del usuario
        solicitarJSON(URL_LOGIN + user);
    }

    //Verificacion de la respuesta del servicio web
    public void verificarDatos(JSONObject datos) {
        try {
            //Se obtiene el estado del usuario desde la pagina del servidor
            String seObtuvo = datos.getString("Obtenido");
            if (seObtuvo.equals("SI")) {
                String datosUser = datos.getString("Usuario");
                JSONObject jDatos = new JSONObject(datosUser);
                String user = jDatos.getString("usuario");
                String pass = jDatos.getString("password");
                if (user.equals(USER) && pass.equals(PASS)) {

                    String token = FirebaseInstanceId.getInstance().getToken();

                    if (token != null) {

                        // manejo de las versiones de los android para los token
                        if (("" + token.charAt(0)).equals("{")) {
                            JSONObject js = new JSONObject(token);
                            String tokenEditado = js.getString("token");
                            cargarToken(token);
                        } else {
                            cargarToken(token);
                        }
                    } else {
                        Toast.makeText(this, "Token nulo", Toast.LENGTH_SHORT).show();
                    }
                } else
                    Toast.makeText(this, "La contraseña es incorrecta", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(LoginActivity.this, "El usuario ingresado no existe", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //Creacion de token
    private void cargarToken(String token) {
        HashMap<String, String> hashMapToken = new HashMap<>();
        hashMapToken.put("usuario", USER);
        hashMapToken.put("token", token);

        //declaracion de la solicitud para recuperar una parte de un objeto JSON
        JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST, URL_TOKEN, new JSONObject(hashMapToken), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject datos) {

                Preferences.savePreferenceBoolean(LoginActivity.this, noCerrarSesion.isChecked(), Preferences.PREFERENCE_ESTADO_BUTTON_SESION);
                Preferences.savePreferendceString(LoginActivity.this, USER, Preferences.USUARIO_PREFERENCE);

                String obtenido = "";
                try {
                    obtenido = datos.getString("Resultado");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(LoginActivity.this, obtenido, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(LoginActivity.this, AmigosActivity.class);
                startActivity(i);
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        //añadir la peticion
        VolleyRP.addToQueue(solicitud, rq, this, volleyRP);
    }


    public void solicitarJSON(String URL) {
        //declaracion de la solicitud para recuperar una parte de un objeto JSON
        JsonObjectRequest solicitud = new JsonObjectRequest(URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject datos) {
                verificarDatos(datos);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "Error...", Toast.LENGTH_SHORT).show();
            }
        });
        //añadir la peticion
        VolleyRP.addToQueue(solicitud, rq, this, volleyRP);
    }
}

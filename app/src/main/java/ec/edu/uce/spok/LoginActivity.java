package ec.edu.uce.spok;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    private EditText txtUser;
    private EditText txtPwd;
    private Button btnIngresar1;
    private static final String URL_LOGIN = "https://spok.000webhostapp.com/php/obtenerPorUsuario.php?usuario=";

    private static final String URL_TOKEN = "https://spok.000webhostapp.com/php/insertarActualizarToken.php";

    private VolleyRP volleyRP;
    private RequestQueue rq;
    private String USER = "";
    private String PASS = "";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtUser = (EditText) findViewById(R.id.txtUsuario);
        txtPwd = (EditText) findViewById(R.id.txtPass);
        btnIngresar1 = (Button) findViewById(R.id.btnIngresar);

        volleyRP = VolleyRP.getInstance(this);
        rq = volleyRP.getRequestQueue();

        btnIngresar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String u = txtUser.getText().toString();
                String p = txtPwd.getText().toString();
                login(u, p);
            }
        });

    }

    public void login(String user, String password) {
        USER = user;
        PASS = password;
        solicitarJSON(URL_LOGIN + user);
    }

    public void verificarDatos(JSONObject datos) {
        try {
            String seObtuvo = datos.getString("Obtenido");
            if (seObtuvo.equals("SI")) {
                String datosUser = datos.getString("Usuario");
                JSONObject jDatos = new JSONObject(datosUser);
                String user = jDatos.getString("usuario");
                String pass = jDatos.getString("password");
                if (user.equals(USER) && pass.equals(PASS)) {

                    String token = FirebaseInstanceId.getInstance().getToken();

                    if (token != null) {
                        //versiones de los android para los token
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

    private void cargarToken(String token) {
        HashMap<String, String> hashMapToken = new HashMap<>();
        hashMapToken.put("usuario", USER);
        hashMapToken.put("token", token);

        JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST, URL_TOKEN, new JSONObject(hashMapToken), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject datos) {
                String obtenido = "";
                try {
                    obtenido = datos.getString("Resultado");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(LoginActivity.this, obtenido, Toast.LENGTH_SHORT).show();
                /////
                Intent i = new Intent(LoginActivity.this, MensajeriaActivity.class);
                ////
                i.putExtra("key_emisor", USER);
                startActivity(i);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "Token no se subio a la DB", Toast.LENGTH_SHORT).show();
            }
        });
        VolleyRP.addToQueue(solicitud, rq, this, volleyRP);
    }


    public void solicitarJSON(String URL) {
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
        VolleyRP.addToQueue(solicitud, rq, this, volleyRP);
    }
}

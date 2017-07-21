package ec.edu.uce.spok;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RegistroActivity extends AppCompatActivity {

    private final String URL_REGISTRAR = "https://spok.000webhostapp.com/php/insertarUsuarios.php";

    private EditText etUsuario;
    private EditText etPass;
    private EditText etNombres;
    private EditText etApellidos;
    private EditText etEmail;
    private EditText etCelular;
    private Button btRegistrase;

    private VolleyRP volleyRP;
    private RequestQueue rq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        etUsuario = (EditText) findViewById(R.id.txtUsuarioR);
        etPass = (EditText) findViewById(R.id.txtPasswordR);
        etNombres = (EditText) findViewById(R.id.txtNombres);
        etApellidos = (EditText) findViewById(R.id.txtApellidos);
        etEmail = (EditText) findViewById(R.id.txtEmail);
        etCelular = (EditText) findViewById(R.id.txtCelular);
        btRegistrase = (Button) findViewById(R.id.btnEnviarMensaje);
        //librería volley para el envío de peticiones http al servidor
        volleyRP = VolleyRP.getInstance(this);
        rq = volleyRP.getRequestQueue();

        btRegistrase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String usu = etUsuario.getText().toString();
                String pass = etPass.getText().toString();
                String nombres = etNombres.getText().toString();
                String apellidos = etApellidos.getText().toString();
                String email = etEmail.getText().toString();
                String numCel = etCelular.getText().toString();

                if (usu.equals("") || pass.equals("") || nombres.equals("") || apellidos.equals("") || email.equals("") || numCel.equals("")) {
                    Toast.makeText(RegistroActivity.this, "Todos los campos son requeridos", Toast.LENGTH_SHORT).show();
                } else {
                    registrarUsuario(usu, pass, nombres, apellidos, email, numCel);
                }

            }
        });

    }

    private void registrarUsuario(String usu, String pass, String nombres, String apellidos, String email, String numCel) {
        HashMap<String, String> hmToken = new HashMap<>();
        hmToken.put("usuario", usu);
        hmToken.put("password", pass);
        hmToken.put("nombres", nombres);
        hmToken.put("apellidos", apellidos);
        hmToken.put("email", email);
        hmToken.put("celular", numCel);

        //declaracion de la solicitud para recuperar una parte de un objeto JSON
        JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST, URL_REGISTRAR, new JSONObject(hmToken), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject datos) {
                try {
                    String estado = datos.getString("Insertado");
                    if (estado.equals("SI")) {
                        Toast.makeText(RegistroActivity.this, "Registro completado con exito", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(RegistroActivity.this, "El usuario ya existe", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(RegistroActivity.this, "No se pudo registrar", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegistroActivity.this, "Error...", Toast.LENGTH_SHORT).show();
            }
        });
        //añadir la peticion
        VolleyRP.addToQueue(solicitud, rq, this, volleyRP);
    }

}

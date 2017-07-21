package ec.edu.uce.spok.Amigos;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ec.edu.uce.spok.LoginActivity;
import ec.edu.uce.spok.Preferences;
import ec.edu.uce.spok.R;
import ec.edu.uce.spok.VolleyRP;

/**
 * Created by usuario on 14/07/2017.
 */

public class AmigosActivity extends AppCompatActivity {

    private final String URL_AMIGOS = "https://spok.000webhostapp.com/php/listarUsuariosNombresApellidos.php";
    private final String URL_ELIMINAR_TOKEN = "https://spok.000webhostapp.com/php/eliminarTokenUsuario.php";
    private RecyclerView rv;
    private List<Amigos> amigosList;
    private AmigosAdapter adapter;
    private VolleyRP volleyRP;
    private RequestQueue rq;
    private Toolbar myToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amigos);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        volleyRP = VolleyRP.getInstance(this);
        rq = volleyRP.getRequestQueue();

        amigosList = new ArrayList<>();

        rv = (RecyclerView) findViewById(R.id.rvAmigos);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        adapter = new AmigosAdapter(amigosList, this);
        rv.setAdapter(adapter);

        solicitarJSON();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cerrar_sesion:
                Preferences.savePreferenceBoolean(AmigosActivity.this, false, Preferences.PREFERENCE_ESTADO_BUTTON_SESION);
                String usuarioLogin = Preferences.obtenerPreferenceString(AmigosActivity.this, Preferences.USUARIO_PREFERENCE);
                eliminarToken(usuarioLogin);
                Intent i = new Intent(AmigosActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void agregarAmigo(int foto, String usuario, String nombre) {
        Amigos amigo = new Amigos();
        amigo.setFotoPerfil(foto);
        amigo.setUsuario(usuario);
        amigo.setNombresCompletos(nombre);
        amigosList.add(amigo);
        //actualizar lista
        adapter.notifyDataSetChanged();
    }

    public void solicitarJSON() {
        //declaracion de la solicitud para recuperar una parte de un objeto JSON
        JsonObjectRequest solicitud = new JsonObjectRequest(URL_AMIGOS, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject datos) {
                try {
                    String usuarios = datos.getString("Resultado");
                    JSONArray jsonArrayUsuarios = new JSONArray(usuarios);
                    for (int i = 0; i < jsonArrayUsuarios.length(); i++) {
                        JSONObject object = jsonArrayUsuarios.getJSONObject(i);
                        String usuario = object.getString("usuario");
                        String usuarioLogin = Preferences.obtenerPreferenceString(AmigosActivity.this, Preferences.USUARIO_PREFERENCE);
                        if (!usuarioLogin.equals(usuario)) {
                            String nombres = object.getString("nombres");
                            String apellidos = object.getString("apellidos");
                            agregarAmigo(R.drawable.usuario2, usuario, nombres + " " + apellidos);
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(AmigosActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AmigosActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        //añadir la peticion
        VolleyRP.addToQueue(solicitud, rq, this, volleyRP);
    }

    private void eliminarToken(String usu) {
        HashMap<String, String> hmToken = new HashMap<>();
        hmToken.put("usuario", usu);

        JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST, URL_ELIMINAR_TOKEN, new JSONObject(hmToken), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject datos) {
                try {
                    String estado = datos.getString("Eliminado");
                    if (estado.equals("SI")) {
                        Toast.makeText(AmigosActivity.this, "Eliminado con exito", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AmigosActivity.this, "Algo", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(AmigosActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AmigosActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        //añadir la peticion
        VolleyRP.addToQueue(solicitud, rq, this, volleyRP);
    }
}

package ec.edu.uce.spok.Mensajeria;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import ec.edu.uce.spok.LoginActivity;
import ec.edu.uce.spok.Preferences;
import ec.edu.uce.spok.R;
import ec.edu.uce.spok.VolleyRP;

public class MensajeriaActivity extends AppCompatActivity {

    public static final String MENSAJE = "MENSAJE";
    private BroadcastReceiver receiver;

    //URL del servicio web al que se le hara una peticion para cerrar sesion
    private final String URL_ELIMINAR_TOKEN = "https://spok.000webhostapp.com/php/eliminarTokenUsuario.php";

    private final String URL_LISTA_MENSAJES = "https://spok.000webhostapp.com/php/listarMensajes.php";

    private RecyclerView rv;
    private Button btnenviar;
    private EditText etMensaje;
    private List<Mensaje> listamensajes;
    private MensajeriaAdapter adapter;
    private Toolbar myToolbar;

    private VolleyRP volleyRP;
    private RequestQueue rq;


    private String MENSAJE_ENVIAR = "";
    private String EMISOR = "";
    private String RECEPTOR = "";

    //URL del servicio web al que se le hara una peticion para enviar los mensajes
    private static final String URL_MENSAJERIA = "https://spok.000webhostapp.com/php/procesoMensajeria.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensajeria);

        //Obtiene el usuario a quien se le enviará el mensaje
        Intent i = getIntent();
        Bundle bundle = i.getExtras();
        if (bundle != null) {
            RECEPTOR = bundle.getString("key_receptor");
        }

        //Obtiene el usuario con el que se está logueado
        EMISOR = Preferences.obtenerPreferenceString(this, Preferences.USUARIO_PREFERENCE);

        //Activar el boton de retroceder en el Toolbar del activity
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

        rv = (RecyclerView) findViewById(R.id.rvMensajes);
        //ordenar mensajes verticalmente
        LinearLayoutManager llm = new LinearLayoutManager(this);
        //teclado se sobrepone a los mensajes
        llm.setStackFromEnd(true);
        rv.setLayoutManager(llm);

        listamensajes = new ArrayList<>();

        btnenviar = (Button) findViewById(R.id.btnEnviarMensaje);
        etMensaje = (EditText) findViewById(R.id.etMensaje);

        adapter = new MensajeriaAdapter(listamensajes, this);
        rv.setAdapter(adapter);

        recuperarMensajes();


        btnenviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Obtiene la hora en la que se envia el mensaje
                Calendar calendario = new GregorianCalendar();
                int hora, minutos;
                hora = calendario.get(Calendar.HOUR_OF_DAY);
                minutos = calendario.get(Calendar.MINUTE);
                String horat = hora + ":" + minutos;

                //Convierte el texto ingresado en una cadena sin espacios al inicio y al final
                String mensaje = etMensaje.getText().toString().trim();

                //Verifica que el mensaje no este vacio y que el receptor que no esté vacio
                //si los dos estan vacios el mensaje no se enviará
                if (!mensaje.isEmpty() && !RECEPTOR.isEmpty()) {
                    MENSAJE_ENVIAR = mensaje;
                    enviarMensaje();
                    crearMensaje(mensaje, horat, 1);
                    etMensaje.setText("");
                }

                setScrollBarMensajes();

            }
        });

        //componente que está destinado a recibir y responder ante eventos globales generados por el sistema
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String mensaje = intent.getStringExtra("key_mensaje");
                String hora = intent.getStringExtra("key_hora");
                String emisor = intent.getStringExtra("key_emisor_php");
                if (emisor.equals(RECEPTOR)) {
                    crearMensaje(mensaje, hora, 2);
                }
            }
        };

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
                Preferences.savePreferenceBoolean(MensajeriaActivity.this, false, Preferences.PREFERENCE_ESTADO_BUTTON_SESION);
                Preferences.savePreferendceString(MensajeriaActivity.this, null, Preferences.USUARIO_PREFERENCE);
                Intent i = new Intent(MensajeriaActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected void onPause() {
        super.onPause();
        //pausar broadcast receiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(MENSAJE));

    }

    public void crearMensaje(String mensaje, String hora, int tipo_mensaje) {

        Mensaje mensajeAux = new Mensaje();
        mensajeAux.setId("0");
        mensajeAux.setMensaje(mensaje);
        mensajeAux.setHora(hora);
        mensajeAux.setTipo(tipo_mensaje);
        listamensajes.add(mensajeAux);
        adapter.notifyDataSetChanged();

    }


    private void enviarMensaje() {

        HashMap<String, String> hashMapToken = new HashMap<>();
        hashMapToken.put("emisor", EMISOR);
        hashMapToken.put("receptor", RECEPTOR);
        hashMapToken.put("mensaje", MENSAJE_ENVIAR);

        //declaracion de la solicitud para recuperar una parte de un objeto JSON
        JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST, URL_MENSAJERIA, new JSONObject(hashMapToken), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject datos) {
                Toast.makeText(MensajeriaActivity.this, "Token se subio a la BD", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MensajeriaActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        //añadir la peticion
        VolleyRP.addToQueue(solicitud, rq, this, volleyRP);

    }


    public void setScrollBarMensajes() {

        rv.scrollToPosition(adapter.getItemCount() - 1);
    }

    public void recuperarMensajes() {
        //declaracion de la solicitud para recuperar una parte de un objeto JSON

        HashMap<String, String> hmUsuario = new HashMap<>();
        hmUsuario.put("usuario", EMISOR);
        hmUsuario.put("amigo", RECEPTOR);

        JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST, URL_LISTA_MENSAJES, new JSONObject(hmUsuario), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject datos) {
                try {
                    String usuarios = datos.getString("Resultado");
                    JSONArray jsonArrayUsuarios = new JSONArray(usuarios);
                    for (int i = 0; i < jsonArrayUsuarios.length(); i++) {
                        JSONObject object = jsonArrayUsuarios.getJSONObject(i);
                        String mensaje = object.getString("mensaje");
                        String tipoMensaje = object.getString("tipo_mensaje");
                        String horaMensajeConsultada = object.getString("hora_del_mensaje");
                        String horaMensajeMostrada = horaMensajeConsultada.substring(0, 5);
                        if (tipoMensaje.equals("Enviado")) {
                            crearMensaje(mensaje, horaMensajeMostrada, 1);
                        } else if (tipoMensaje.equals("Recibido")) {
                            crearMensaje(mensaje, horaMensajeMostrada, 2);
                        }
                    }
                } catch (JSONException e) {
                    Toast.makeText(MensajeriaActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MensajeriaActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        //añadir la peticion
        VolleyRP.addToQueue(solicitud, rq, this, volleyRP);
    }

}







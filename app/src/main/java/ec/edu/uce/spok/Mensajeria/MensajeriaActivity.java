package ec.edu.uce.spok.Mensajeria;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import ec.edu.uce.spok.R;
import ec.edu.uce.spok.VolleyRP;

public class MensajeriaActivity extends AppCompatActivity {

    public static final String MENSAJE = "MENSAJE";
    private BroadcastReceiver receiver;
    private final String URL_ELIMINAR = "https://spok.000webhostapp.com/php/insertarUsuarios.php";

    private RecyclerView rv;
    private Button btnenviar;
    private EditText etMensaje;
    private List<Mensaje> listamensajes;
    private MensajeriaAdapter adapter;
    private EditText etreceptor;

    private VolleyRP volleyRP;
    private RequestQueue rq;


    private String MENSAJE_ENVIAR = "";
    private String EMISOR = "";
    private String RECEPTOR = "";
    private String USER;

    private static final String URL_MENSAJERIA = "https://spok.000webhostapp.com/php/procesoMensajeria.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensajeria);

        Bundle bundle = getIntent().getExtras();
        USER = bundle.getString("Usuario");

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MensajeriaActivity.this, "Regresar", Toast.LENGTH_SHORT).show();
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


        Intent extras = getIntent();
        Bundle bundle2 = extras.getExtras();
        if (bundle2 != null) {
            EMISOR = bundle2.getString("key_emisor");
        }

        btnenviar = (Button) findViewById(R.id.btnEnviarMensaje);
        etMensaje = (EditText) findViewById(R.id.etMensaje);


        etreceptor = (EditText) findViewById(R.id.etreceptor);

        adapter = new MensajeriaAdapter(listamensajes, this);
        rv.setAdapter(adapter);


        btnenviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendario = new GregorianCalendar();
                int hora, minutos;
                hora = calendario.get(Calendar.HOUR_OF_DAY);
                minutos = calendario.get(Calendar.MINUTE);
                String horat = hora + ":" + minutos;

                String mensaje = validarCadena(etMensaje.getText().toString());

                RECEPTOR = etreceptor.getText().toString();

                if (!mensaje.isEmpty() && !RECEPTOR.isEmpty()) {
                    MENSAJE_ENVIAR = mensaje;
                    enviarMensaje();
                    crearMensaje(mensaje, horat, 1);
                    etMensaje.setText("");
                }

                //   if (token != null) {

                //     Toast.makeText(MensajeriaActivity.this, "Mensaje enviado", Toast.LENGTH_SHORT).show();
                //}
                setScrollBarMensajes();


            }
        });


        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String mensaje = intent.getStringExtra("key_mensaje");
                String hora = intent.getStringExtra("key_hora");

                crearMensaje(mensaje, hora, 2);
            }
        };

    }

    //problemas como: "  " , "      habla"
    private String validarCadena(String cadena) {
        for (int i = 0; i < cadena.length(); i++) {
            if (!("" + cadena.charAt(i)).equals(" ")) {
                return cadena.substring(i, cadena.length());
            }
        }
        return " ";
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
            case R.id.action_actualizar:
                Toast.makeText(this, "Actualizar cuenta", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_eliminar:
                Toast.makeText(this, "Eliminar cuenta", Toast.LENGTH_SHORT).show();
                AlertDialog a = createSimpleDialog();
                return true;
            case R.id.action_cerrar_sesion:
                Toast.makeText(this, "Cerrar Sesión", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public AlertDialog createSimpleDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MensajeriaActivity.this);

        builder.setTitle("Confirmación")
                .setMessage("¿Desea eliminar su cuenta de usuario?")
                .setPositiveButton("Si",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //eliminarUsuario("");
                                Toast.makeText(MensajeriaActivity.this, "Cuenta eliminada", Toast.LENGTH_SHORT).show();
                            }
                        })
                .setNegativeButton("CANCELAR",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

        return builder.create();
    }

    private void eliminarUsuario(String usu) {
        HashMap<String, String> hmToken = new HashMap<>();
        hmToken.put("usuario", usu);

        JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST, URL_ELIMINAR, new JSONObject(hmToken), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject datos) {
                try {
                    String estado = datos.getString("Insertado");
                    if (estado.equals("SI")) {
                        Toast.makeText(MensajeriaActivity.this, "Usuario eliminado", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(MensajeriaActivity.this, "No se pudo eliminar", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(MensajeriaActivity.this, "No se pudo eliminar", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MensajeriaActivity.this, "Error...", Toast.LENGTH_SHORT).show();
            }
        });
        VolleyRP.addToQueue(solicitud, rq, this, volleyRP);
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

        JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.POST, URL_MENSAJERIA, new JSONObject(hashMapToken), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject datos) {
                Toast.makeText(MensajeriaActivity.this, "Token se subio a la BD", Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MensajeriaActivity.this, "Ocurrio un error fatal", Toast.LENGTH_SHORT).show();
            }
        });
        VolleyRP.addToQueue(solicitud, rq, this, volleyRP);

    }


    public void setScrollBarMensajes() {

        rv.scrollToPosition(adapter.getItemCount() - 1);
    }
}







package ec.edu.uce.spok.Mensajeria;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.ArrayList;
import java.util.List;

import ec.edu.uce.spok.Mensajeria.Mensaje;
import ec.edu.uce.spok.R;

public class MensajeriaActivity extends AppCompatActivity {

    public static final String MENSAJE="MENSAJE";
    private BroadcastReceiver receiver;

    private RecyclerView rv;
    private Button btnenviar;
    private EditText etMensaje;
    private List<Mensaje>listamensajes;
    private MensajeriaAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensajeria);

        rv=(RecyclerView)findViewById(R.id.rvMensajes);

        //ordenar mensajes verticalmente
        LinearLayoutManager llm=new LinearLayoutManager(this);
        //teclado se sobrepone a los mensajes
        llm.setStackFromEnd(true);
        rv.setLayoutManager(llm);

        listamensajes=new ArrayList<>();

        btnenviar=(Button)findViewById(R.id.btnEnviarMensaje);
        etMensaje=(EditText)findViewById(R.id.etMensaje);



        adapter=new MensajeriaAdapter(listamensajes,this);
        rv.setAdapter(adapter);


        btnenviar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String mensaje=etMensaje.getText().toString();
                            String token= FirebaseInstanceId.getInstance().getToken();

                            if (!mensaje.isEmpty()){
                                crearMensaje(mensaje,"06:12",1);
                                etMensaje.setText("");
                            }

                            if(token!=null){

                                Toast.makeText(MensajeriaActivity.this,token,Toast.LENGTH_SHORT).show();
                            }
                            setScrollBarMensajes();



                            }
                    });

        receiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String mensaje=intent.getStringExtra("key_mensaje");
                String hora=intent.getStringExtra("key_hora");

                crearMensaje(mensaje,hora,2);
            }
        };

    }

    protected void onPause(){
        super.onPause();

        //pausar broadcast receiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(MENSAJE));

    }

    public void crearMensaje(String mensaje, String hora,int tipo_mensaje){

        Mensaje mensajeAux=new Mensaje();
        mensajeAux.setId("0");
        mensajeAux.setMensaje(mensaje);
        mensajeAux.setHora(hora);
        mensajeAux.setTipo(tipo_mensaje);
        listamensajes.add(mensajeAux);
        adapter.notifyDataSetChanged();

    }

    public void setScrollBarMensajes(){

        rv.scrollToPosition(adapter.getItemCount()-1);
    }
}







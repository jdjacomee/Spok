package ec.edu.uce.spok.Mensajeria;



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

import java.util.ArrayList;
import java.util.List;

import ec.edu.uce.spok.Mensajeria.Mensaje;
import ec.edu.uce.spok.R;

public class MensajeriaActivity extends AppCompatActivity {

    private RecyclerView rv;
    private Button btnenviar;
    private EditText etMensaje;
    private List<Mensaje>listamensajes;
    private MensajeriaAdapter adapter;
    private int numLineas=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensajeria);

        rv=(RecyclerView)findViewById(R.id.rvMensajes);
        LinearLayoutManager llm=new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        listamensajes=new ArrayList<>();

        btnenviar=(Button)findViewById(R.id.btnEnviarMensaje);
        etMensaje=(EditText)findViewById(R.id.etMensaje);

        for(int i=0;i<10;i++){
            Mensaje mensajeAux=new Mensaje();
            mensajeAux.setId(""+i);
                       mensajeAux.setMensaje("Probando el adapter" +i);
                       mensajeAux.setMensaje("emisor: jasdjasdjasjdjasvdjasjdas" +
                                    "asdbasdkbasdbjasd" +
                                       "adsbjasbdbaksdbas " +i);
            mensajeAux.setHora("12:2"+i);
            mensajeAux.setTipo(1);
            listamensajes.add(mensajeAux);
        }


               for(int i=0;i<10;i++){
                       Mensaje mensajeAux=new Mensaje();
                       mensajeAux.setId(""+i);
                      mensajeAux.setMensaje("receptor: 54456414terter" +
                                       "erterterterdfasdfgrg   456464" +
                                       "454y4rtytytiu7yu" +i);
                       mensajeAux.setHora("12:2"+i);
                       mensajeAux.setTipo(2);
                       listamensajes.add(mensajeAux);
               }


        adapter=new MensajeriaAdapter(listamensajes,this);
        rv.setAdapter(adapter);




        //evitar la superposicon de los mensajes en el etMensaje
               etMensaje.addTextChangedListener(new TextWatcher() {
                       @Override
                       public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                   }

                              @Override
                              public void onTextChanged(CharSequence s, int start, int before, int count) {
                               //cada que se ingresa texto
                                       if(etMensaje.getLayout().getLineCount()!=numLineas){
                                       setScrollBarMensajes();
                                      numLineas=etMensaje.getLayout().getLineCount();
                                            }
                            }

                                @Override
                       public void afterTextChanged(Editable s) {

                                    }
                   });



        btnenviar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                                crearMensaje(etMensaje.getText().toString());
                                setScrollBarMensajes();
                            }
                    });


    }



    public void crearMensaje(String mensaje){

        Mensaje mensajeAux=new Mensaje();
        mensajeAux.setId("0");
        mensajeAux.setMensaje(mensaje);
        mensajeAux.setHora("18:21");
        mensajeAux.setTipo(1);
        listamensajes.add(mensajeAux);
        adapter.notifyDataSetChanged();
        etMensaje.setText("");
    }

    public void setScrollBarMensajes(){
        rv.scrollToPosition(adapter.getItemCount()-1);
    }
}







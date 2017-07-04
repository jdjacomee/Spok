package ec.edu.uce.spok.Mensajeria;



import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ec.edu.uce.spok.Mensajeria.Mensaje;
import ec.edu.uce.spok.R;

public class MensajeriaActivity extends AppCompatActivity {

    private RecyclerView rv;
    private List<Mensaje>listamensajes;
    private MensajeriaAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensajeria);

        rv=(RecyclerView)findViewById(R.id.rvMensajes);
        LinearLayoutManager llm=new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        listamensajes=new ArrayList<>();

        for(int i=0;i<10;i++){
            Mensaje mensajeAux=new Mensaje();
            mensajeAux.setId(""+i);
            mensajeAux.setMensaje("Probando el adapter" +i);
            mensajeAux.setHora("12:2"+i);
            mensajeAux.setTipo(1);
            listamensajes.add(mensajeAux);
        }

        adapter=new MensajeriaAdapter(listamensajes);
        rv.setAdapter(adapter);

    }
}

package ec.edu.uce.spok.Amigos;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import ec.edu.uce.spok.R;

/**
 * Created by usuario on 14/07/2017.
 */

public class AmigosActivity extends AppCompatActivity{

    private RecyclerView rv;
    private List<Amigos> amigosList;
    private AmigosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amigos);

        amigosList=new ArrayList<>();

        rv=(RecyclerView)findViewById(R.id.rvAmigos);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        adapter=new AmigosAdapter(amigosList,this);
        rv.setAdapter(adapter);

        for(int i=0;i<10;i++){
            agregarAmiogo(R.drawable.usuario2,"user"+i,"mensaje"+i,"22:37");
        }

    }

    public void agregarAmiogo(int foto,String nombre, String mensaje,String hora){
        Amigos amigo=new Amigos();
        amigo.setFotoPerfil(foto);
        amigo.setNombre(nombre);
        amigo.setUltimoMensaje(mensaje);
        amigo.setHoraMensaje(hora);

        amigosList.add(amigo);
        //actualizar lista
        adapter.notifyDataSetChanged();
    }
}

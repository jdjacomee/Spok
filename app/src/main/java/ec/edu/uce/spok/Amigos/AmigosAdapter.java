package ec.edu.uce.spok.Amigos;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import ec.edu.uce.spok.Mensajeria.MensajeriaAdapter;
import ec.edu.uce.spok.R;

/**
 * Created by Gigabyte on 17/07/2017.
 */

public class AmigosAdapter extends RecyclerView.Adapter<AmigosAdapter.AmigosViewHolder> {

    private List<Amigos> amigoslist;
    Context context;

    public AmigosAdapter(List<Amigos> amigosList, Context context){
        this.amigoslist=amigosList;
        this.context=context;
    }


    @Override
    public AmigosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_amigos, parent, false);
        return new AmigosAdapter.AmigosViewHolder(v);
    }


    @Override
    public void onBindViewHolder(AmigosViewHolder holder, int position) {

        holder.foto.setImageResource(amigoslist.get(position).getFotoPerfil());
        holder.nombre.setText(amigoslist.get(position).getNombre());
        holder.ultmensaje.setText(amigoslist.get(position).getUltimoMensaje());
        holder.horamensaje.setText(amigoslist.get(position).getHoraMensaje());

    }

    @Override
    public int getItemCount() {

        //tamaño de la lista
        return amigoslist.size();
    }


    //necesario para crear el AmigosAdapter
    static class AmigosViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView foto;
        TextView nombre;
        TextView ultmensaje;
        TextView horamensaje;

        public AmigosViewHolder(View itemView) {
            super(itemView);

            foto=(ImageView)itemView.findViewById(R.id.foto_amigos);
            nombre=(TextView)itemView.findViewById(R.id.tvamigos_nombre);
            ultmensaje=(TextView)itemView.findViewById(R.id.tv_mensaje_amigos);
            horamensaje=(TextView)itemView.findViewById(R.id.tv_hora_amigos);
        }
    }
}

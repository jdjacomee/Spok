package ec.edu.uce.spok.Amigos;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ec.edu.uce.spok.Mensajeria.MensajeriaActivity;
import ec.edu.uce.spok.R;

/**
 * Created by Gigabyte on 17/07/2017.
 */

public class AmigosAdapter extends RecyclerView.Adapter<AmigosAdapter.AmigosViewHolder> {

    private List<Amigos> amigoslist;
    Context context;

    public AmigosAdapter(List<Amigos> amigosList, Context context) {
        this.amigoslist = amigosList;
        this.context = context;
    }


    @Override
    public AmigosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_amigos, parent, false);
        return new AmigosAdapter.AmigosViewHolder(v);
    }


    @Override
    public void onBindViewHolder(AmigosViewHolder holder, final int position) {

        holder.foto.setImageResource(amigoslist.get(position).getFotoPerfil());
        holder.usuario.setText(amigoslist.get(position).getUsuario());
        holder.nombresCompletos.setText(amigoslist.get(position).getNombresCompletos());
        holder.ultmensaje.setText(amigoslist.get(position).getUltimoMensaje());
        holder.horamensaje.setText(amigoslist.get(position).getHoraMensaje());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, MensajeriaActivity.class);
                i.putExtra("key_receptor", amigoslist.get(position).getUsuario());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {

        //tamaño de la lista
        return amigoslist.size();
    }


    //necesario para crear el AmigosAdapter
    static class AmigosViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView foto;
        TextView usuario;
        TextView nombresCompletos;
        TextView ultmensaje;
        TextView horamensaje;

        public AmigosViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardViewAmigos);
            foto = (ImageView) itemView.findViewById(R.id.foto_amigos);
            usuario = (TextView) itemView.findViewById(R.id.tvamigos_usuarios);
            nombresCompletos = (TextView) itemView.findViewById(R.id.tvamigos_nombres_apellidos);
            ultmensaje = (TextView) itemView.findViewById(R.id.tv_mensaje_amigos);
            horamensaje = (TextView) itemView.findViewById(R.id.tv_hora_amigos);
        }
    }
}

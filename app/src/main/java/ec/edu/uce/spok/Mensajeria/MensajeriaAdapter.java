package ec.edu.uce.spok.Mensajeria;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import ec.edu.uce.spok.R;

/**
 * Created by usuario on 02/07/2017.
 */

public class MensajeriaAdapter extends RecyclerView.Adapter<MensajeriaAdapter.MensajeriaViewHolder> {

    private List<Mensaje>listamMensajes;

    public MensajeriaAdapter(List<Mensaje> listamMensajes) {
        this.listamMensajes = listamMensajes;
    }

    @Override
    public MensajeriaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.mensajes_card_view,parent,false);

        return new MensajeriaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MensajeriaViewHolder holder, int position) {
        //donde se modifica los valores de cada cardView
        holder.tvmensaje.setText(listamMensajes.get(position).getMensaje());
        holder.tvhora.setText(listamMensajes.get(position).getHora());
    }

    @Override
    public int getItemCount() {
        //elementos del recyclerView
        return listamMensajes.size();
    }



    //necesario para hacer uso del RecyclerViewAdapter
    //manejo de los elementos de mensajes_card_view.xml
    static class MensajeriaViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        TextView tvmensaje;
        TextView tvhora;
        LinearLayout mensajeBackground;

        MensajeriaViewHolder(View itemView){
            super (itemView);

            cardView=(CardView)itemView.findViewById(R.id.cardView);
            tvmensaje=(TextView)itemView.findViewById(R.id.mensaje);
            tvhora=(TextView)itemView.findViewById(R.id.hora);
            mensajeBackground=(LinearLayout)itemView.findViewById(R.id.mensajeBackground);

        }

    }
}

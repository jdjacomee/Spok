package ec.edu.uce.spok.Mensajeria;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import ec.edu.uce.spok.R;

/**
 * Created by usuario on 02/07/2017.
 */

public class MensajeriaAdapter extends RecyclerView.Adapter<MensajeriaAdapter.MensajeriaViewHolder> {

    private List<Mensaje>listamMensajes;
    private Context context;

    public MensajeriaAdapter(List<Mensaje> listamMensajes, Context context)
    {
        this.listamMensajes = listamMensajes;
        this.context=context;
    }

    @Override
    public MensajeriaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.mensajes_card_view,parent,false);

        return new MensajeriaViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MensajeriaViewHolder holder, int position) {
        //donde se modifica los valores de cada cardView

                RelativeLayout.LayoutParams rl=(RelativeLayout.LayoutParams)holder.cardView.getLayoutParams();
              FrameLayout.LayoutParams fl=(FrameLayout.LayoutParams)holder.mensajeBackground.getLayoutParams() ;

                        LinearLayout.LayoutParams llm=(LinearLayout.LayoutParams)holder.tvmensaje.getLayoutParams();
                LinearLayout.LayoutParams llh=(LinearLayout.LayoutParams)holder.tvhora.getLayoutParams();


                if(listamMensajes.get(position).getTipo()==1){
                        holder.mensajeBackground.setBackgroundResource(R.drawable.b29);

                                rl.addRule(RelativeLayout.ALIGN_PARENT_LEFT,0);
                       rl.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

                                llm.gravity= Gravity.RIGHT;
                       llh.gravity=Gravity.RIGHT;
                       fl.gravity= Gravity.RIGHT;
                        holder.tvmensaje.setGravity(Gravity.RIGHT);

                            }else
                    if(listamMensajes.get(position).getTipo()==2){
                        holder.mensajeBackground.setBackgroundResource(R.drawable.b19);
                        rl.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,0);
                        rl.addRule(RelativeLayout.ALIGN_PARENT_LEFT);

                        llm.gravity=Gravity.LEFT;
                        llh.gravity=Gravity.LEFT;
                        fl.gravity= Gravity.LEFT;
                        holder.tvmensaje.setGravity(Gravity.LEFT);
                   }

                holder.cardView.setLayoutParams(rl);
                holder.mensajeBackground.setLayoutParams(fl);
                holder.tvmensaje.setLayoutParams(llm);
                holder.tvhora.setLayoutParams(llh);



        holder.tvmensaje.setText(listamMensajes.get(position).getMensaje());
        holder.tvhora.setText(listamMensajes.get(position).getHora());

        if(Build.VERSION.SDK_INT<(Build.VERSION_CODES.LOLLIPOP)){
                      holder.cardView.getBackground().setAlpha(0);
                    }else{
                       holder.cardView.setBackgroundColor(ContextCompat.getColor(context,android.R.color.transparent));
                   }



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

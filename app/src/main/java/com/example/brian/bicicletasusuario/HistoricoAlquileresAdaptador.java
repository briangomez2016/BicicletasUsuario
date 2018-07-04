package com.example.brian.bicicletasusuario;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.brian.bicicletasusuario.Clases.Alquiler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoricoAlquileresAdaptador extends RecyclerView.Adapter<HistoricoAlquileresAdaptador.AlquileresViewHolder> {
    private Context mContext;
    private List<Alquiler> mAlquileres = new ArrayList<>();

    public HistoricoAlquileresAdaptador(Context context, List<Alquiler> alquileres) {
        mContext = context;
        mAlquileres = alquileres;
    }

    @Override
    public HistoricoAlquileresAdaptador.AlquileresViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_historico_alquileres, parent, false);
        HistoricoAlquileresAdaptador.AlquileresViewHolder viewHolder = new HistoricoAlquileresAdaptador.AlquileresViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(AlquileresViewHolder holder, int position) {
        holder.bindAlquiler(mAlquileres.get(position));
    }

    @Override
    public int getItemCount() {
        return mAlquileres.size();
    }

    public class AlquileresViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.idBici)
        TextView idBici;
        @BindView(R.id.horaInicio)
        TextView horaInicio;
        @BindView(R.id.horaFin)
        TextView horaFin;
        @BindView(R.id.parada)
        TextView parada;
        CardView cardView;
        private Context mContext;

        public AlquileresViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mContext = itemView.getContext();
            cardView = itemView.findViewById(R.id.item_historico_alquileres);

        }

        public void bindAlquiler(Alquiler alquiler) {
            parada.setText(alquiler.getParada());
            horaInicio.setText(alquiler.getHoraInicio());
            horaFin.setText(alquiler.getHoraFin());
            idBici.setText(String.valueOf(alquiler.getBicicleta()));
        }


    }
}

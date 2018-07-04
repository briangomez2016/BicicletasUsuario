package com.example.brian.bicicletasusuario;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brian.bicicletasusuario.ApiCliente.ApiCliente;
import com.example.brian.bicicletasusuario.ApiInterface.ApiInterface;
import com.example.brian.bicicletasusuario.ApiInterface.RespuestaAlquileres;
import com.example.brian.bicicletasusuario.Clases.Alquiler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoricoAlquileres extends Fragment {

    @BindView(R.id.lista_alquileres)
    RecyclerView mRecyclerView;
    HistoricoAlquileresAdaptador adaptador;
    List<Alquiler> alquileres = null;
    @BindView(R.id.alquileres_vacio)
    LinearLayout mensaje;
    @BindView(R.id.carga)
    LinearLayout cargando;

    public HistoricoAlquileres() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_historico_alquileres, container, false);
        ButterKnife.bind(this, view);
        bdCargarAlquileres();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private void bdCargarAlquileres() {
        ApiInterface bd = ApiCliente.getClient().create(ApiInterface.class);
        SharedPreferences sp = this.getActivity().getSharedPreferences("usuario", Context.MODE_PRIVATE);
        String email = sp.getString("email", null);
        Call<RespuestaAlquileres> call = bd.getAlquileres(email);
        call.enqueue(new Callback<RespuestaAlquileres>() {
            @Override
            public void onResponse(Call<RespuestaAlquileres> call, Response<RespuestaAlquileres> response) {
                if (response.isSuccessful()) {
                    if(response.body().getCodigo().equals("1")){

                        alquileres = (response.body().getAlquileres() != null) ? response.body().getAlquileres() : new ArrayList<Alquiler>();
                        Log.d("ASD", "onResponse: "+ alquileres.size());
                        cargarAlquileres(alquileres);
                    }else {
                        mensaje.setVisibility(View.VISIBLE);
                        cargando.setVisibility(View.GONE);
                    }
                }else{
                    mensaje.setVisibility(View.VISIBLE);
                    cargando.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<RespuestaAlquileres> call, Throwable t) {
                mensaje.setVisibility(View.VISIBLE);
                //Toast.makeText(getActivity(),"FAILURE",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void cargarAlquileres(List<Alquiler> alquileres) {
       if (alquileres.size() == 0) {
            mensaje.setVisibility(View.VISIBLE);
        } else {
            mensaje.setVisibility(View.GONE);
        }
        adaptador = new HistoricoAlquileresAdaptador(getActivity(), alquileres);
        mRecyclerView.setAdapter(adaptador);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        cargando.setVisibility(View.GONE);
        //swipeRefresh.setRefreshing(false);
    }

}

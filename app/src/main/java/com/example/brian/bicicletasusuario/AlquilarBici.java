package com.example.brian.bicicletasusuario;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brian.bicicletasusuario.ApiCliente.ApiCliente;
import com.example.brian.bicicletasusuario.ApiInterface.ApiInterface;
import com.example.brian.bicicletasusuario.ApiInterface.RespuestaAlquilerActual;
import com.example.brian.bicicletasusuario.ApiInterface.RespuestaIncidencia;
import com.example.brian.bicicletasusuario.Clases.Usuario;
import com.google.android.gms.common.api.Api;

import butterknife.ButterKnife;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.android.gms.flags.impl.SharedPreferencesFactory.getSharedPreferences;

public class AlquilarBici extends Fragment {

    @BindView(R.id.listaHr)
    Spinner lista;
    @BindView(R.id.pActual)
    LinearLayout pantallaActual;
    @BindView(R.id.pAlquilar)
    LinearLayout pantallaAlquilar;
    @BindView(R.id.btnScanner)
    Button btnScan;
    @BindView(R.id.btnAlquilar)
    Button btnAlquilar;
    @BindView(R.id.edId)
    EditText editText;
    @BindView(R.id.cargando)
    LinearLayout cargando;
    @BindView(R.id.tiempoActual)
    TextView tiempoActual;
    @BindView(R.id.tiempoAlquilado)
    TextView tiempoAlquilado;
    @BindView(R.id.costoActual)
    TextView costoActual;
    @BindView(R.id.costoAlquilado)
    TextView costoAlquilado;
    @BindView(R.id.titulo)
    TextView titulo;
    @BindView(R.id.identificador)
    TextView identificador;




    String id = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        yalquilo();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != 20) {
            super.onActivityResult(requestCode, resultCode, data);

        } else {
            if (data != null && data.getData() != null && data.getData().toString() != null) {

                id = data.getData().toString();
                editText.setText(id);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_alquilar_bici, container, false);
        ButterKnife.bind(this, view);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), QR.class);
                startActivityForResult(intent, 20);
            }
        });
        btnAlquilar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String tiempo = lista.getSelectedItem().toString();
                String tiempoFin = null;
                if(tiempo.equals("0 a 15 mins"))
                    tiempoFin = "15";
                if(tiempo.equals("15 a 30 mins"))
                    tiempoFin = "30";
                if(tiempo.equals("1 hora"))
                    tiempoFin = "60";
                if(tiempo.equals("2 horas"))
                    tiempoFin = "120";
                if(tiempo.equals("3 horas"))
                    tiempoFin = "180";
                if(tiempo.equals("4 horas"))
                    tiempoFin = "240";
                if(tiempo.equals("4 a 10 hrs"))
                    tiempoFin = "600";

                Log.d("tiempoFin", tiempoFin);
                if (id.isEmpty()) {
                    if (editText.getText().toString().isEmpty()) {
                        editText.setError("Se debe introducir un identificador");
                    } else {
                        id = editText.getText().toString();
                    }
                }
                SharedPreferences sp = getActivity().getSharedPreferences("usuario", Context.MODE_PRIVATE);
                SharedPreferences.Editor et = sp.edit();
                String correo = sp.getString("email", null);
                cargando.setVisibility(View.VISIBLE);
                alquilar(id, tiempoFin, correo);
            }
        });


        return view;
    }

    public void yalquilo() {
        final ApiInterface api = ApiCliente.getClient().create(ApiInterface.class);
        SharedPreferences sp = getActivity().getSharedPreferences("usuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor et = sp.edit();
        final String correo = sp.getString("email", null);
        Call<RespuestaAlquilerActual> call = api.alquilerActual(correo);
        call.enqueue(new Callback<RespuestaAlquilerActual>() {
            @Override
            public void onResponse(Call<RespuestaAlquilerActual> call, Response<RespuestaAlquilerActual> response) {
                if (response.body().getCodigo().equals("1")) {
                    if (response.body().getAlquiler() != null) {
                        if(response.body().getAlquiler().getFin() == null) {
                            tiempoAlquilado.setText(response.body().getAlquiler().getTiempoAlquilado());
                            if(response.body().getAlquiler().getCostoAlquilado().equals("0")){
                                costoActual.setText("Gratis");
                                costoAlquilado.setText("Gratis");
                            }else{
                                costoActual.setText(response.body().getAlquiler().getCostoActual());
                                costoAlquilado.setText(response.body().getAlquiler().getCostoAlquilado());
                            }
                            tiempoActual.setText(response.body().getAlquiler().getTiempoActual());
                            identificador.setText(response.body().getAlquiler().getBicicleta());
                            titulo.setText("Alquiler Actual");
                            pantallaActual.setVisibility(View.VISIBLE);

                        }else{
                            pantallaActual.setVisibility(View.GONE);
                            pantallaAlquilar.setVisibility(View.VISIBLE);
                        }
                    }else{
                        pantallaActual.setVisibility(View.GONE);
                        pantallaAlquilar.setVisibility(View.VISIBLE);
                    }
                } else {

                    Log.d("else", "onFailure: " + response.body().getMensaje());
                }
            }

            @Override
            public void onFailure(Call<RespuestaAlquilerActual> call, Throwable t) {
                Log.d("error", "onFailure: " + t.getMessage());
            }
        });

    }
    public void cargarAtributos(String correo){

    }

    public void alquilar(String identificador, String tiempo, String correo) {
        final ApiInterface api = ApiCliente.getClient().create(ApiInterface.class);
        Call<RespuestaIncidencia> call = api.alquilarBici(identificador, correo, tiempo);
        call.enqueue(new Callback<RespuestaIncidencia>() {
            @Override
            public void onResponse(Call<RespuestaIncidencia> call, Response<RespuestaIncidencia> response) {
                if (response.body().getCodigo().equals("0")) {
                    cargando.setVisibility(View.GONE);
                    pantallaAlquilar.setVisibility(View.GONE);
                    yalquilo();
                }
                if (response.body().getCodigo().equals("1")) {
                    Toast.makeText(getActivity(), response.body().getMensaje(), Toast.LENGTH_LONG).show();
                    cargando.setVisibility(View.GONE);
                }
                if (response.body().getCodigo().equals("2")) {
                    Toast.makeText(getActivity(), response.body().getMensaje(), Toast.LENGTH_LONG).show();
                    cargando.setVisibility(View.GONE);
                }
                if (response.body().getCodigo().equals("3")) {
                    Toast.makeText(getActivity(), response.body().getMensaje(), Toast.LENGTH_LONG).show();
                    cargando.setVisibility(View.GONE);
                }
                if (response.body().getCodigo().equals("4")) {
                    Toast.makeText(getActivity(), response.body().getMensaje(), Toast.LENGTH_LONG).show();
                    cargando.setVisibility(View.GONE);
                }
                if (response.body().getCodigo().equals("-1")) {
                    cargando.setVisibility(View.GONE);
                    Log.d("Error", response.body().getMensaje());
                }
            }

            @Override
            public void onFailure(Call<RespuestaIncidencia> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}



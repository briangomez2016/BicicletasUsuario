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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.brian.bicicletasusuario.ApiCliente.ApiCliente;
import com.example.brian.bicicletasusuario.ApiInterface.ApiInterface;
import com.example.brian.bicicletasusuario.ApiInterface.RespuestaIncidencia;

import butterknife.ButterKnife;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.google.android.gms.flags.impl.SharedPreferencesFactory.getSharedPreferences;

public class AlquilarBici extends Fragment{

    @BindView(R.id.listaHr)
    Spinner lista;

    @BindView(R.id.btnScanner)
    Button btnScan;
    @BindView(R.id.btnAlquilar)
    Button btnAlquilar;
    @BindView(R.id.edId)
    EditText editText;
    String id ="";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode != 20) {
            super.onActivityResult(requestCode, resultCode, data);

        }else{
            id = data.getData().toString();
            editText.setText(id);
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
                    startActivityForResult(intent,20);
                }
            });
        btnAlquilar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = getActivity().getSharedPreferences("usuario",Context.MODE_PRIVATE);
                SharedPreferences.Editor et = sp.edit ();
                String correo = sp.getString("email",null);
                String tiempo = lista.getSelectedItem().toString();
                if(id.isEmpty()){
                    if(editText.getText().toString().isEmpty()){
                        editText.setError("Se debe introducir un identificador");
                    }else{
                       id = editText.getText().toString();
                    }
                }
                alquilar(id,tiempo,correo);
            }
        });


            return view;
        }

        public  void alquilar(String identificador ,String tiempo,String correo){
            final ApiInterface api = ApiCliente.getClient().create(ApiInterface.class);
            Call<RespuestaIncidencia> call = api.alquilarBici(identificador,correo,tiempo);
            call.enqueue(new Callback<RespuestaIncidencia>() {
                @Override
                public void onResponse(Call<RespuestaIncidencia> call, Response<RespuestaIncidencia> response) {
                    if(response.body().getCodigo().equals("0")){
                        Log.d("Correcto", response.body().getMensaje());
                    }
                    if(response.body().getCodigo().equals("1")){
                        Toast.makeText(getActivity(), response.body().getMensaje(), Toast.LENGTH_LONG).show();

                    }
                    if(response.body().getCodigo().equals("2")){
                        Toast.makeText(getActivity(), response.body().getMensaje(), Toast.LENGTH_LONG).show();
                    }
                    if(response.body().getCodigo().equals("3")){
                        Toast.makeText(getActivity(), response.body().getMensaje(), Toast.LENGTH_LONG).show();
                    }
                    if(response.body().getCodigo().equals("4")){
                        Toast.makeText(getActivity(), response.body().getMensaje(), Toast.LENGTH_LONG).show();
                    }
                    if(response.body().getCodigo().equals("-1")){
                        Log.d("Correcto", response.body().getMensaje());
                    }
                }

                @Override
                public void onFailure(Call<RespuestaIncidencia> call, Throwable t) {
                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }

    }



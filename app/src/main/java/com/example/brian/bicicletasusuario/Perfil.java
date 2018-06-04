package com.example.brian.bicicletasusuario;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.brian.bicicletasusuario.ApiCliente.ApiCliente;
import com.example.brian.bicicletasusuario.ApiInterface.ApiInterface;
import com.example.brian.bicicletasusuario.ApiInterface.RespuestaUsuario;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Brian on 04/06/2018.
 */


public class Perfil extends AppCompatActivity {
    @BindView(R.id.text)
    TextView text;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil);
        ButterKnife.bind(this);


    }
    public void perfil (View v){
        final ApiInterface api = ApiCliente.getClient().create(ApiInterface.class);
        Call<RespuestaUsuario> call = api.getPerfil("brian");
        call.enqueue(new Callback<RespuestaUsuario>() {
            @Override
            public void onResponse(Call<RespuestaUsuario> call, Response<RespuestaUsuario> response) {

            }
            @Override
            public void onFailure(Call<RespuestaUsuario> call, Throwable t) {
            }
        });}
}
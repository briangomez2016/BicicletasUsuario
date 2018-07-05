package com.example.brian.bicicletasusuario;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.brian.bicicletasusuario.ApiCliente.ApiCliente;
import com.example.brian.bicicletasusuario.ApiInterface.ApiInterface;
import com.example.brian.bicicletasusuario.ApiInterface.RespuestaCambiarContra;
import com.example.brian.bicicletasusuario.ApiInterface.RespuestaCorreo;


import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Brian on 04/06/2018.
 */


public class ModificarPass extends AppCompatActivity {
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.codigo)
    EditText codigo;
    @BindView(R.id.nuevoPass)
    EditText nuevopass;
    @BindView(R.id.confirNuevo)
    EditText confirpass;
    String correo;
    String contraseña;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.modificarpass);
        ButterKnife.bind(this);
    }

    public void enviarCorreo(View v){
        ApiInterface api = ApiCliente.getClient().create(ApiInterface.class);
        String correo = email.getText().toString();
        Call<RespuestaCorreo> call = api.GenerarCodigo(correo);
        call.enqueue(new Callback<RespuestaCorreo>() {
            @Override
            public void onResponse(Call<RespuestaCorreo> call, Response<RespuestaCorreo> response) {
                if(response.body().getCodigo().equals("1")){
                    SharedPreferences sp = getSharedPreferences ("RecuperarContraseña", MODE_PRIVATE);
                    SharedPreferences.Editor et = sp.edit ();
                    et.putString ("codigo", response.body().getCodigoGenerado());
                }
            }

            @Override
            public void onFailure(Call<RespuestaCorreo> call, Throwable t) {

            }
        });


    }

    public void verificarCodigo(View v){
        String codi = codigo.getText().toString();
        SharedPreferences sp = getSharedPreferences("RecuperarContraseña" , MODE_PRIVATE);
        String CodigoGenerado = sp.getString("codigo",null);
        //Cambiar por CodigoGenerado
        if(codi.equals("1a2b3c")){
            Toast.makeText(this, "Correcto Perro", Toast.LENGTH_SHORT).show();
            nuevopass.setEnabled(true);
            confirpass.setEnabled(true);

        }else{
            codigo.setError("Codigo Incorrecto");
        }

    }
    public void CambiarPass(View v ){
        String nuevo =  nuevopass.getText().toString();
        String confirm  = confirpass.getText().toString();
        if(nuevo.equals(confirm)){
            ApiInterface api = ApiCliente.getClient().create(ApiInterface.class);
            Call<RespuestaCambiarContra> call = api.cambiarPass(nuevo);
            call.enqueue(new Callback<RespuestaCambiarContra>() {
                @Override
                public void onResponse(Call<RespuestaCambiarContra> call, Response<RespuestaCambiarContra> response) {
                    if(response.body().getCodigo().equals("1")){
                        Toast.makeText(ModificarPass.this, "Su contraseña ha sido cambiado con exito baby", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(ModificarPass.this, "FUCK", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<RespuestaCambiarContra> call, Throwable t) {
                    Toast.makeText(ModificarPass.this, "BUEN PROBLEM BABY", Toast.LENGTH_SHORT).show();
                }
            });

        }else{
            confirpass.setError("Las contraseñas no coiniciden");
        }


    }
}

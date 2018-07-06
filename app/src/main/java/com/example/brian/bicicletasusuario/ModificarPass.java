package com.example.brian.bicicletasusuario;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.brian.bicicletasusuario.ApiCliente.ApiCliente;
import com.example.brian.bicicletasusuario.ApiInterface.ApiInterface;
import com.example.brian.bicicletasusuario.ApiInterface.RespuestaCambiarContra;
import com.example.brian.bicicletasusuario.ApiInterface.RespuestaCorreo;
import com.example.brian.bicicletasusuario.ApiInterface.RespuestaUsuario;
import com.example.brian.bicicletasusuario.Clases.Usuario;


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
    @BindView(R.id.confirmPass)
    EditText confirpass;
    @BindView(R.id.cambiar)
    LinearLayout pCambiar;
    @BindView(R.id.recuperar)
    LinearLayout pRecuperar;
    String emailTemporal="";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.modificarpass);
        ButterKnife.bind(this);
        emailTemporal="";

    }

    public void enviarCorreo(View v){
        ApiInterface api = ApiCliente.getClient().create(ApiInterface.class);
        if(email.getText().toString().isEmpty()){
            email.setError("Debe ingresar un email");
            return;
        }
        final String correo = email.getText().toString();
        Call<RespuestaCorreo> call = api.GenerarCodigo(correo);
        call.enqueue(new Callback<RespuestaCorreo>() {
            @Override
            public void onResponse(Call<RespuestaCorreo> call, Response<RespuestaCorreo> response) {
                if(response.body().getCodigo().equals("1")){
                    emailTemporal=correo;
                    Log.d("Codigo", "onResponse: "+response.body().getCodigoGenerado());
                }else{
                    Log.d("Problema", "onResponse: "+response.body().getMensaje());

                }
            }

            @Override
            public void onFailure(Call<RespuestaCorreo> call, Throwable t) {
                Log.d("Error recuperar pass", "onFailure: "+t.getMessage());
            }
        });


    }

    public void verificarCodigo(View v){
        ApiInterface api = ApiCliente.getClient().create(ApiInterface.class);
        if(codigo.getText().toString().isEmpty()){
            codigo.setError("Debe ingresar un codigo");
        }
        if(email.getText().toString().isEmpty()){
            email.setError("Debe ingresar un email");
        }
        if(!email.getText().toString().isEmpty() && !codigo.getText().toString().isEmpty()) {
            final String codi = codigo.getText().toString();
            Call<RespuestaUsuario> call = api.getPerfil(emailTemporal);
            call.enqueue(new Callback<RespuestaUsuario>() {
                @Override
                public void onResponse(Call<RespuestaUsuario> call, Response<RespuestaUsuario> response) {
                    Usuario u = response.body().getUsuario();
                    if (u == null) {
                        Toast.makeText(ModificarPass.this, "No se pudo cargar el perfil", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (u.getPass().equals(codi)) {
                        pRecuperar.setVisibility(View.GONE);
                        pCambiar.setVisibility(View.VISIBLE);

                    }
                }

                @Override
                public void onFailure(Call<RespuestaUsuario> call, Throwable t) {

                }
            });
        }

    }
    public void CambiarPass(View v ){
        String nuevo =  nuevopass.getText().toString();
        String confirm  = confirpass.getText().toString();
        if(nuevo.isEmpty()){
            nuevopass.setError("Debe ingresar una contraseña");
        }
        if(confirm.isEmpty()){
            confirpass.setError("Confirmar la contraseña");
        }
        if(nuevo.equals(confirm)){
            ApiInterface api = ApiCliente.getClient().create(ApiInterface.class);
            Call<RespuestaUsuario> call = api.editarPerfil(null,null,emailTemporal,null,null,nuevo);
            call.enqueue(new Callback<RespuestaUsuario>() {
                @Override
                public void onResponse(Call<RespuestaUsuario> call, Response<RespuestaUsuario> response) {
                    if(response.body().getCodigo().equals("1")){
                        Toast.makeText(ModificarPass.this, "Su contraseña ha sido cambiado con exito", Toast.LENGTH_SHORT).show();
                        emailTemporal="";
                        Intent intent =  new Intent(ModificarPass.this,IniciarSesion.class);
                        startActivity(intent);
                    }else{
                        Toast.makeText(ModificarPass.this, "FUCK", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<RespuestaUsuario> call, Throwable t) {
                    Toast.makeText(ModificarPass.this, " Problema servidor cambiar pass", Toast.LENGTH_SHORT).show();
                }
            });

        }else{
            confirpass.setError("Las contraseñas no coinciden");
        }


    }
}

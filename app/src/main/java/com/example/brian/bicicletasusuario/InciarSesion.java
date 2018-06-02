package com.example.brian.bicicletasusuario;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.brian.bicicletasusuario.ApiCliente.ApiCliente;
import com.example.brian.bicicletasusuario.ApiInterface.ApiInterface;
import com.example.brian.bicicletasusuario.Respuesta.RespuestaUsuario;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class InciarSesion extends AppCompatActivity {
    @BindView(R.id.Usuario)
    EditText email;
    @BindView(R.id.Password)
    EditText pass;
    @BindView(R.id.IniciarSesion)
    Button iniciar;
    @BindView(R.id.Recordar)
    CheckBox recordar;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ButterKnife.bind(this);
        iniciar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String e = email.getText().toString();
                String p =pass.getText().toString();
                if(e==""){
                    email.setError("Debe Ingresar Un Correo Valido");}
                else if(p=="")
                {pass.setError("Debe Ingresar Su Contraseña");
                }else{
                    if(recordar.isChecked()){
                        recordarUsuario(e,p);
                    }
                    iniciar(e,p);
                }
            }
        });
    }


    private void recordarUsuario(String email,String pass){
        SharedPreferences sp = getSharedPreferences ("usuario", MODE_PRIVATE);
        SharedPreferences.Editor et = sp.edit ();
        et.putString ("email", email);
        et.putString ("password", pass);
        et.commit ();
    }
    private void iniciar(String email,String pass){
        final ApiInterface api = ApiCliente.getClient().create(ApiInterface.class);
        Call<RespuestaUsuario> call = api.iniciar(email,pass);
        call.enqueue(new Callback<RespuestaUsuario>() {
            @Override
            public void onResponse(Call<RespuestaUsuario> call, Response<RespuestaUsuario> response) {
                Toast.makeText(InciarSesion.this, "Bien", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onFailure(Call<RespuestaUsuario> call, Throwable t) {
                Toast.makeText(InciarSesion.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void Registrarse(View v){
        Intent intent = new Intent(InciarSesion.this, RegistroUsuario.class);
        startActivity(intent);
    }

}

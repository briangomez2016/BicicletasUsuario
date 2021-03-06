package com.example.brian.bicicletasusuario;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.brian.bicicletasusuario.ApiCliente.ApiCliente;
import com.example.brian.bicicletasusuario.ApiInterface.ApiInterface;
import com.example.brian.bicicletasusuario.ApiInterface.RespuestaUsuario;
import com.google.firebase.iid.FirebaseInstanceId;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class IniciarSesion extends AppCompatActivity {
    @BindView(R.id.Usuario)
    EditText email;
    @BindView(R.id.Password)
    EditText pass;
    @BindView(R.id.IniciarSesion)
    Button iniciar;
    @BindView(R.id.Recordar)
    CheckBox recordar;
    @BindView(R.id.UsuarioError)
    TextInputLayout usuarioError;
    @BindView(R.id.PasswordError)
    TextInputLayout passError;
    @BindView(R.id.pbISesion)
    ProgressBar pbISesion;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        SharedPreferences sp = getSharedPreferences("usuario" , MODE_PRIVATE);
        String UserEmail = sp.getString("email",null);
        String UserPass = sp.getString("password",null);
        if(UserEmail!=null && UserPass!=null){
            iniciar(UserEmail,UserPass,true);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ButterKnife.bind(this);


        iniciar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String e = email.getText().toString();
                String p = pass.getText().toString();
                if (e.isEmpty()) {
                    usuarioError.setError("Debe ingresar un Correo válido");
                    return;
                }
                if (p.isEmpty()) {
                    passError.setError("Debe ingresar su contraseña");
                    return;
                }
                if (!p.isEmpty() && !e.isEmpty()) {
                    pbISesion.setVisibility(View.VISIBLE);
                    iniciar.setVisibility(View.GONE);
                    if (recordar.isChecked()) {
                        iniciar(e, p, true);
                    } else {
                        iniciar(e, p, false);
                    }
                }
            }
        });
    }


    private void recordarUsuario(String email, String pass) {
        SharedPreferences sp = getSharedPreferences("usuario", MODE_PRIVATE);
        SharedPreferences.Editor et = sp.edit();
        et.putString("email", email);
        et.putString("password", pass);
        et.commit();
    }

    private void iniciar(final String email, final String pass, final Boolean recordar) {
        final ApiInterface api = ApiCliente.getClient().create(ApiInterface.class);
        final String idMovil = FirebaseInstanceId.getInstance().getToken();
        Log.d("IDIDID", idMovil);
        Call<RespuestaUsuario> call = api.iniciar(email, pass, idMovil);
        call.enqueue(new Callback<RespuestaUsuario>() {
            @Override
            public void onResponse(Call<RespuestaUsuario> call, Response<RespuestaUsuario> response) {
                if (response.body().getCodigo().equals("2")) {
                    if (recordar == true) {
                        recordarUsuario(email, pass);
                    } else {
                        SharedPreferences sp = getSharedPreferences("usuario", MODE_PRIVATE);
                        SharedPreferences.Editor et = sp.edit();
                        et.putString("email", email);
                        et.commit();
                    }
                    Intent intent = new Intent(IniciarSesion.this, Navigation.class);
                    startActivity(intent);

                } else {
                    pbISesion.setVisibility(View.GONE);
                    iniciar.setVisibility(View.VISIBLE);
                    Toast.makeText(IniciarSesion.this, "Usuario o contraseña inválido", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<RespuestaUsuario> call, Throwable t) {
                Toast.makeText(IniciarSesion.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                pbISesion.setVisibility(View.GONE);
                iniciar.setVisibility(View.VISIBLE);
            }
        });
    }

    public void Registrarse(View v){
        Intent intent = new Intent(IniciarSesion.this, RegistroUsuario.class);
        startActivity(intent);
    }
    public void OlvidoPass(View v){
        Intent intent = new Intent(IniciarSesion.this, ModificarPass.class);
        startActivity(intent);
    }

}

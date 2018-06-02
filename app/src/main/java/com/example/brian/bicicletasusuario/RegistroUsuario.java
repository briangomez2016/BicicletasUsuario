package com.example.brian.bicicletasusuario;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.brian.bicicletasusuario.ApiCliente.ApiCliente;
import com.example.brian.bicicletasusuario.Respuesta.RespuestaUsuario;
import com.example.brian.bicicletasusuario.ApiInterface.ApiInterface;
import com.google.firebase.iid.FirebaseInstanceId;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RegistroUsuario extends AppCompatActivity {

    @BindView(R.id.nombre)
    EditText nombre;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.telefono)
    EditText tel;
    @BindView(R.id.pass)
    EditText pass;
    @BindView(R.id.ci)
    RadioButton ci;
    @BindView(R.id.pasaporte)
    RadioButton pasaporte;
    @BindView(R.id.documento)
    EditText documento;
    @BindView(R.id.direccion)
    EditText direccion;
    @BindView(R.id.group)
    RadioGroup radio;

    public void Registrarse(View v) {
        final ApiInterface api = ApiCliente.getClient().create(ApiInterface.class);
        String nom = nombre.getText().toString();
        String em = email.getText().toString();
        String telefono = tel.getText().toString();
        String password = pass.getText().toString();
        String idMovil = FirebaseInstanceId.getInstance().getToken();
        String dir  = direccion.getText().toString();
        if (ci.isChecked()) {
            String cedula = documento.getText().toString();
            Call<RespuestaUsuario> call = api.registrar(cedula,"",nom,em,password,telefono,dir);
            call.enqueue(new Callback<RespuestaUsuario>() {
                @Override
                public void onResponse(Call<RespuestaUsuario> call, Response<RespuestaUsuario> response) {
                    Toast.makeText(RegistroUsuario.this, "Bien", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onFailure(Call<RespuestaUsuario> call, Throwable t) {
                    Toast.makeText(RegistroUsuario.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            });
            nombre.setText("");
            email.setText("");
            tel.setText("");
            documento.setText("");
        } else {
            String pasaporte = documento.getText().toString();
            Call<RespuestaUsuario> call = api.registrar("", pasaporte, nom, em, password, telefono, "lavalleja 1782");
            call.enqueue(new Callback<RespuestaUsuario>() {
                @Override
                public void onResponse(Call<RespuestaUsuario> call, Response<RespuestaUsuario> response) {
                    Toast.makeText(RegistroUsuario.this, "Bien", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onFailure(Call<RespuestaUsuario> call, Throwable t) {
                    Toast.makeText(RegistroUsuario.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            });
            nombre.setText("");
            email.setText("");
            tel.setText("");
            documento.setText("");

        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);
        ButterKnife.bind(this);

    }
}

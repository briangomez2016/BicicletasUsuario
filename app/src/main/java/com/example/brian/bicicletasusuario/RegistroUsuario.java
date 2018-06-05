package com.example.brian.bicicletasusuario;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;

import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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


public class RegistroUsuario extends AppCompatActivity {

    @BindView(R.id.nombre)
    EditText nombre;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.telefono)
    EditText tel;
    @BindView(R.id.pass)
    EditText pass;
    @BindView(R.id.passConfir)
    EditText passConfir;
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
        final String nom = nombre.getText().toString();
        final String em = email.getText().toString();
        final String telefono = tel.getText().toString();
        final String password = pass.getText().toString();
        final String idMovil = FirebaseInstanceId.getInstance().getToken();
        final String dir = direccion.getText().toString();
        final String cedula = documento.getText().toString();
        final String pasaporte = documento.getText().toString();
        final String passconf = passConfir.getText().toString();
        if(nom.isEmpty()){
            nombre.setError("Se debe ingresar un nombre");
        }
        if(em.isEmpty()){
            email.setError("Se debe ingresar un email");
        }
        if(telefono.isEmpty()){
            tel.setError("Se debe ingresar un nombre");
        }
        if(password.isEmpty()){
            pass.setError("Se debe ingresar un contraseña");
        }
        if(passconf.isEmpty()){
            passConfir.setError("Se debe confirmar la contraseña");
        }
        if(dir.isEmpty()){
            direccion.setError("Se debe confirmar la contraseña");
        }
        if(cedula.isEmpty() || pasaporte.isEmpty()){
            documento.setError("Se debe ingresar algo");
        }

        if(password.equals(passconf) && !nom.isEmpty() && !em.isEmpty() && !dir.isEmpty() && !telefono.isEmpty() && !password.isEmpty() && !cedula.isEmpty() ){
        Call<RespuestaUsuario> call = api.getPerfil(em);
        call.enqueue(new Callback<RespuestaUsuario>() {
            @Override
            public void onResponse(Call<RespuestaUsuario> call, Response<RespuestaUsuario> response) {
                if (response.body().getUsuario().getEmail()!=null) {
                    email.setError("Email ya en uso");
                } else {
                    if (ci.isChecked()) {
                        Call<RespuestaUsuario> call2 = api.registrar(cedula, null, nom, em, password, telefono, dir);
                        call2.enqueue(new Callback<RespuestaUsuario>() {
                            @Override
                            public void onResponse(Call<RespuestaUsuario> call2, Response<RespuestaUsuario> response) {
                                Intent intent = new Intent(RegistroUsuario.this, Navigation.class);
                                startActivity(intent);

                            }

                            @Override
                            public void onFailure(Call<RespuestaUsuario> call2, Throwable t) {

                            }
                        });
                    } else {
                        Call<RespuestaUsuario> call2 = api.registrar(null, pasaporte, nom, em, password, telefono, dir);
                        call2.enqueue(new Callback<RespuestaUsuario>() {
                            @Override
                            public void onResponse(Call<RespuestaUsuario> call2, Response<RespuestaUsuario> response) {
                                Intent intent = new Intent(RegistroUsuario.this, Navigation.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onFailure(Call<RespuestaUsuario> call2, Throwable t) {

                            }
                        });
                    }

                }
            }

            @Override
            public void onFailure(Call<RespuestaUsuario> call, Throwable t) {

            }
        });
        }


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);
        ButterKnife.bind(this);
        radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.ci) {
                    documento.setText("");
                    int maxLength = 8;
                    documento.setInputType(InputType.TYPE_CLASS_NUMBER);
                    documento.setHint("Cedula");
                    documento.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
                } else if (checkedId == R.id.pasaporte) {
                    documento.setText("");
                    documento.setInputType(InputType.TYPE_CLASS_TEXT);
                    documento.setHint("Pasaporte");
                }
            }

        });
        passConfir.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0) {
                    if (pass.getText().toString().equals(passConfir.getText().toString())) {
                        passConfir.setError(null);
                    } else {
                        passConfir.setError("Contraseña Incorrecta");
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}


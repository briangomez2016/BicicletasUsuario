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
import android.widget.TextView;
import android.widget.Toast;

import com.example.brian.bicicletasusuario.ApiInterface.Perfil;

import java.security.Principal;

import butterknife.BindView;
import butterknife.ButterKnife;


public class InciarSesion extends AppCompatActivity {
    @BindView(R.id.Usuario)
    EditText email;
    @BindView(R.id.Password)
    EditText pass;
    @BindView(R.id.IniciarSesion)
    Button iniciar;
    @BindView(R.id.Recordar)
    CheckBox recordar;
    Usuario UsuarioTest =  new Usuario( "brian",  "brian@gmail.com",  "5092", "",  "52941943", "user.png");
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ButterKnife.bind(this);
        iniciar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String e = email.getText().toString();
                String p =pass.getText().toString();
                if(e==""){ email.setError("Debe Ingresar Un Correo Valido");}
                else if(p==""){pass.setError("Debe Ingresar Su Contraseña");}

              else if (verificarUsuario(e,p)){
                    if(recordar.isChecked()){
                        recordarUsuario(e,p);
                        Intent intent = new Intent(InciarSesion.this, Perfil.class);
                        startActivity(intent);
                    }
                }else {
                    Toast.makeText(InciarSesion.this, "Email o Contraseña Incorrecto", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private boolean verificarUsuario(String email , String pass){
            if(email.equals(UsuarioTest.getEmail()) && pass.equals("1234")){
                return true;
            }else{
                return false;
            }

    }
    private void recordarUsuario(String email,String pass){
        SharedPreferences sp = getSharedPreferences ("usuario", MODE_PRIVATE);
        SharedPreferences.Editor et = sp.edit ();
        et.putString ("email", email);
        et.putString ("password", pass);
        et.commit ();
    }

    public void Registrarse(View v){
        Intent intent = new Intent(InciarSesion.this, RegistroUsuario.class);
        startActivity(intent);
    }

}

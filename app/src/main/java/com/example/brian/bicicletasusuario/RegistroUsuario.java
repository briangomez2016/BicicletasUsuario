package com.example.brian.bicicletasusuario;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;

import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brian.bicicletasusuario.ApiCliente.ApiCliente;
import com.example.brian.bicicletasusuario.ApiInterface.ApiInterface;
import com.example.brian.bicicletasusuario.ApiInterface.RespuestaIncidencia;
import com.example.brian.bicicletasusuario.ApiInterface.RespuestaUsuario;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

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
    @BindView(R.id.btnRegistrarse)
    Button btnRegistrarse;
    @BindView(R.id.pbRegistrarse)
    ProgressBar pbRegistrarse;
    @BindView(R.id.btnFrente)
    Button btnFrente;
    @BindView(R.id.btnDorso)
    Button btnDorso;
    @BindView(R.id.frenteSeleccionado)
    ImageView frenteSeleccionado;
    @BindView(R.id.dorsoSeleccionado)
    ImageView dorsoSeleccionado;
    @BindView(R.id.imgUno)
    ImageView imgUno;
    @BindView(R.id.imgDos)
    ImageView imgDos;

    Bitmap imgFrente = null;
    Bitmap imgDorso = null;

    boolean frente = false;
    boolean dorso = false;
    String frenteB64 = null;
    String dorsoB64 = null;

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
        if(frenteB64 == null || dorsoB64 == null){
            Toast.makeText(RegistroUsuario.this, "Falta imagen", Toast.LENGTH_LONG).show();
        }
        if (frenteB64 != null && dorsoB64 != null && password.equals(passconf) && !nom.isEmpty() && !em.isEmpty() && !dir.isEmpty() && !telefono.isEmpty() && !password.isEmpty() && !cedula.isEmpty()) {
            btnRegistrarse.setVisibility(View.GONE);
            pbRegistrarse.setVisibility(View.VISIBLE);
            Call<RespuestaUsuario> call = api.getPerfil(em);
            call.enqueue(new Callback<RespuestaUsuario>() {
                @Override
                public void onResponse(Call<RespuestaUsuario> call, Response<RespuestaUsuario> response) {
                    if (response.body().getUsuario().getEmail() != null) {
                        email.setError("Email ya en uso");
                        btnRegistrarse.setVisibility(View.VISIBLE);
                        pbRegistrarse.setVisibility(View.GONE);
                    } else {
                        if (ci.isChecked()) {
                            Call<RespuestaUsuario> call2 = api.registrar(cedula, null, nom, em, password, telefono, dir);
                            call2.enqueue(new Callback<RespuestaUsuario>() {
                                @Override
                                public void onResponse(Call<RespuestaUsuario> call2, Response<RespuestaUsuario> response) {
                                    guardarImg(em,frenteB64);
                                    guardarImg(em,dorsoB64);
                                    Intent intent = new Intent(RegistroUsuario.this, Navigation.class);
                                    startActivity(intent);
                                }
                                @Override
                                public void onFailure(Call<RespuestaUsuario> call2, Throwable t) {
                                    btnRegistrarse.setVisibility(View.VISIBLE);
                                    pbRegistrarse.setVisibility(View.GONE);
                                    Toast.makeText(RegistroUsuario.this, "Error de conexión con el servidor", Toast.LENGTH_SHORT);
                                }
                            });
                        } else {
                            Call<RespuestaUsuario> call2 = api.registrar(null, pasaporte, nom, em, password, telefono, dir);
                            call2.enqueue(new Callback<RespuestaUsuario>() {
                                @Override
                                public void onResponse(Call<RespuestaUsuario> call2, Response<RespuestaUsuario> response) {
                                    guardarImg(em,frenteB64);
                                    guardarImg(em,dorsoB64);
                                    Intent intent = new Intent(RegistroUsuario.this, Navigation.class);
                                    startActivity(intent);
                                }

                                @Override
                                public void onFailure(Call<RespuestaUsuario> call2, Throwable t) {
                                    btnRegistrarse.setVisibility(View.VISIBLE);
                                    pbRegistrarse.setVisibility(View.GONE);
                                    Toast.makeText(RegistroUsuario.this, "Error de conexión con el servidor", Toast.LENGTH_SHORT);
                                }
                            });
                        }

                }
            }

                @Override
                public void onFailure(Call<RespuestaUsuario> call, Throwable t) {
                    btnRegistrarse.setVisibility(View.VISIBLE);
                    pbRegistrarse.setVisibility(View.GONE);
                    Toast.makeText(RegistroUsuario.this, "Error de conexión con el servidor", Toast.LENGTH_SHORT);
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

        btnFrente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                frente = true;
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
            }
        });

        btnDorso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dorso = true;
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
            }
        });


    }

    public static final int GET_FROM_GALLERY = 0;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Detects request codes
        if (requestCode == GET_FROM_GALLERY && resultCode == this.RESULT_OK) {
            final Uri imageUri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                if (frente && bitmap != null) {
                    imgFrente = bitmap;
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage  = BitmapFactory.decodeStream(imageStream);
                    frenteB64 = encodeToBase64(selectedImage, Bitmap.CompressFormat.JPEG, 100);
                    frenteSeleccionado.setVisibility(View.VISIBLE);


                }
                if (dorso && bitmap != null) {
                    imgDorso = bitmap;
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage  = BitmapFactory.decodeStream(imageStream);
                    dorsoB64 = encodeToBase64(selectedImage, Bitmap.CompressFormat.JPEG, 100);
                    dorsoSeleccionado.setVisibility(View.VISIBLE);
                }
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        dorso = false;
        frente = false;
    }
    private static String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality)
    {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    private static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
    private void guardarImg(String email , String img){
        final ApiInterface api = ApiCliente.getClient().create(ApiInterface.class);
        Call<RespuestaIncidencia> call = api.guardarImagen(email,img);

        call.enqueue(new Callback<RespuestaIncidencia>() {
            @Override
            public void onResponse(Call<RespuestaIncidencia> call, Response<RespuestaIncidencia> response) {
                if(response.body().getCodigo().equals("1")){
                    Log.d("bien", "onResponse: bieeeeen");
                }else{Log.d("no", "onResponse: noooooo");}
            }

            @Override
            public void onFailure(Call<RespuestaIncidencia> call, Throwable t) {
                Log.d("Error Servidor", "onResponse:"+t.getMessage());
            }
        });

    }
}


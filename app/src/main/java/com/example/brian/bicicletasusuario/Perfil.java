package com.example.brian.bicicletasusuario;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brian.bicicletasusuario.ApiCliente.ApiCliente;
import com.example.brian.bicicletasusuario.ApiInterface.ApiInterface;
import com.example.brian.bicicletasusuario.ApiInterface.RespuestaUsuario;
import com.example.brian.bicicletasusuario.Clases.Usuario;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Perfil extends Fragment {
    private OnFragmentInteractionListener mListener;

    @BindView (R.id.ivImagen)
    ImageView ivImagen;
    @BindView (R.id.tvSaldo)
    TextView tvSaldo;
    @BindView (R.id.tvEtiquetaCed)
    TextView tvEtiquetaCed;
    @BindView (R.id.tvCedula)
    TextView tvCedula;
    @BindView (R.id.tvNombre)
    TextView tvNombre;
    @BindView (R.id.tvCorreo)
    TextView tvCorreo;
    @BindView (R.id.tvTelefono)
    TextView tvTelefono;
    @BindView (R.id.tvDireccion)
    TextView tvDireccion;
    @BindView (R.id.tvActivo)
    TextView tvActivo;
    @BindView (R.id.spPrecios)
    Spinner spPrecios;
    @BindView (R.id.btnComprarCreditos)
    Button btnComprar;
    @BindView (R.id.btnEditarPerfil)
    Button btnEditarPerfil;

    public Perfil() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        ButterKnife.bind(this, view);
        List<String> list = new ArrayList<String>();
        list.add("20");
        list.add("50");
        list.add("100");
        list.add("500");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getContext(), R.layout.spinner_precio,list);
        spPrecios.setAdapter(adapter);
        ApiInterface api = ApiCliente.getClient().create(ApiInterface.class);

        SharedPreferences pref = this.getActivity().getApplicationContext().getSharedPreferences("usuario",Context.MODE_PRIVATE);
        String email=pref.getString("email", null);
        Toast.makeText(Perfil.this.getContext(),email ,Toast.LENGTH_LONG).show();


        Call<RespuestaUsuario> call = api.getPerfil(email);
        call.enqueue(new Callback<RespuestaUsuario>() {
            @Override
            public void onResponse(Call<RespuestaUsuario> call, Response<RespuestaUsuario> response) {
                if (response.body().getCodigo().equals("1")) {
                    Usuario u = response.body().getUsuario();
                    if (u == null) {
                        Toast.makeText(Perfil.this.getContext(), "No se pudo cargar el perfil", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    tvSaldo.setText("Saldo: $ " + u.getSaldo());
                    if (u.getCedula() == null || u.getCedula().isEmpty()) {
                        tvEtiquetaCed.setText("Pasaporte:");
                        tvCedula.setText(u.getPasaporte());
                    } else
                        tvCedula.setText(u.getCedula());
                    tvNombre.setText(u.getNombre());
                    tvCorreo.setText(u.getEmail());
                    tvTelefono.setText(u.getTelefono());
                    tvDireccion.setText(u.getDireccion());
                    tvActivo.setText(u.getActivado() == 0 ? "NO" : "SI");
                    tvActivo.setTextColor(u.getActivado() == 0 ? Color.RED : Color.GREEN);
                } else
                    Toast.makeText(Perfil.this.getContext(), "No se pudo cargar el perfil", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<RespuestaUsuario> call, Throwable t) {
                Log.d ("ASD", t.getMessage());
                Toast.makeText(Perfil.this.getContext(), "No se pudo cargar el perfil", Toast.LENGTH_SHORT).show();
            }
        });

        btnComprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Masterpiece");
                String cantidad = spPrecios.getSelectedItem().toString();
                if (cantidad != null && !cantidad.isEmpty()) {
                    // TODO: Editar perfil con nuevo saldo...
                } else
                    Toast.makeText(Perfil.this.getContext(), "No se pudo realizar la compra", Toast.LENGTH_SHORT).show();
            }
        });

        btnEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cantidad = spPrecios.getSelectedItem().toString();
                if (cantidad != null && !cantidad.isEmpty()) {

                    Fragment fragment = new EditarPerfil ();
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.contenido_navigation, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                } else
                    Toast.makeText(Perfil.this.getContext(), "No se pudo editar el perfil", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}

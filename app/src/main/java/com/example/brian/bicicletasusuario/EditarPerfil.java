package com.example.brian.bicicletasusuario;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brian.bicicletasusuario.ApiCliente.ApiCliente;
import com.example.brian.bicicletasusuario.ApiInterface.ApiInterface;
import com.example.brian.bicicletasusuario.ApiInterface.RespuestaUsuario;
import com.example.brian.bicicletasusuario.Clases.Usuario;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by stephiRM on 29/06/2018.
 */

public class EditarPerfil extends Fragment {
	private List<Usuario> usuarios;
	private Marker marcador;
	@BindView (R.id.ivImagen)
	ImageView ivImagen;
	//@BindView (R.id.tvEtiquetaCed)
	//EditText tvEtiquetaCed;
	@BindView (R.id.tvCedula)
	EditText tvCedula;
	@BindView (R.id.tvNombre)
	EditText tvNombre;
	@BindView (R.id.tvTelefono)
	EditText tvTelefono;
	@BindView (R.id.tvDireccion)
	EditText tvDireccion;
	@BindView (R.id.tvpass)
	EditText tvpass;
	@BindView (R.id.btnEditar)
	Button btnEditar;
	@BindView (R.id.btnCancelar)
	Button btnCancelar;

	public EditarPerfil() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		cargarPerfil();

		View v = inflater.inflate(R.layout.fragment_editarperfil, container, false);
		ButterKnife.bind(this, v);
		Button btn = (Button) v.findViewById (R.id.btnEditar);

		btnCancelar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Fragment fragment = new Perfil ();
				FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
				fragmentTransaction.replace(R.id.contenido_navigation, fragment);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
			}
		});

		btn.setOnClickListener (new View.OnClickListener () {
			@Override
			public void onClick(View v) {
				if(tvCedula.getText().equals("") || tvNombre.getText().equals("") || tvTelefono.getText().equals("") || tvDireccion.getText().equals("") || tvpass.getText().equals("")) {
					Toast.makeText(EditarPerfil.this.getContext(), "No se puede dejar espacios en blanco", Toast.LENGTH_SHORT).show();
				}else {
					final ApiInterface api = ApiCliente.getClient ().create (ApiInterface.class);

					SharedPreferences pref = EditarPerfil.this.getActivity().getApplicationContext().getSharedPreferences("usuario", Context.MODE_PRIVATE);
					String email=pref.getString("email", null);
					Call<RespuestaUsuario> call2 = api.editarPerfil (tvCedula.getText ().toString (), tvNombre.getText ().toString (), email, tvTelefono.getText ().toString (), tvDireccion.getText ().toString (), tvpass.getText ().toString ());
					call2.enqueue (new Callback<RespuestaUsuario> () {
						@Override
						public void onResponse(Call<RespuestaUsuario> call2, Response<RespuestaUsuario> response) {
							Fragment fragment = new Perfil ();
							FragmentTransaction fragmentTransaction = getActivity ().getSupportFragmentManager ().beginTransaction ();
							fragmentTransaction.replace (R.id.contenido_navigation, fragment);
							fragmentTransaction.addToBackStack (null);
							fragmentTransaction.commit ();

						}

						@Override
						public void onFailure(Call<RespuestaUsuario> call2, Throwable t) {

						}
					});
				}
			}
		});

		return v;
	}

	private void cargarPerfil(){
		SharedPreferences pref = this.getActivity().getApplicationContext().getSharedPreferences("usuario", Context.MODE_PRIVATE);
		String email=pref.getString("email", null);
		ApiInterface ai  = ApiCliente.getClient().create(ApiInterface.class);
		Call<RespuestaUsuario> call = ai.getPerfil(email);
		call.enqueue (new Callback<RespuestaUsuario> () {
			@Override
			public void onResponse(Call<RespuestaUsuario> call, Response<RespuestaUsuario> response) {
				if (response.body().getCodigo().equals("1")) {
					Usuario u = response.body().getUsuario();
					if (u == null) {
						Toast.makeText(EditarPerfil.this.getContext(), "No se pudo cargar el perfil", Toast.LENGTH_SHORT).show();
						return;
					}
					if (u.getCedula() == null || u.getCedula().isEmpty()) {
						tvCedula.setText("Pasaporte:");
						tvCedula.setText(u.getPasaporte());
					} else
						tvCedula.setText(u.getCedula());
					tvNombre.setText(u.getNombre());
					tvTelefono.setText(u.getTelefono());
					tvDireccion.setText(u.getDireccion());
				} else
					Toast.makeText(EditarPerfil.this.getContext(), "No se pudo cargar el perfil", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onFailure(Call<RespuestaUsuario> call, Throwable t) {
				Toast.makeText(getActivity(), "Error de conexión con el servidor: " + t.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}

	public void modificarPerfil(String ci, String nombre, String correo, String tel, String dir, String pass) {
		Usuario.editarPerfil(this, ci, nombre, correo, tel, dir, pass);
	}

	public boolean editarPerfil(boolean ok){
		if(ok){
			cargarPerfil();
			Toast.makeText(getActivity(), "Perfil editado", Toast.LENGTH_LONG).show();
			tvCedula.setText("asd");
			tvNombre.setText("asd");
			tvTelefono.setText("re");
			tvDireccion.setText("asd");
		}else{
			Toast.makeText(getActivity(), "ERROR AL EDITAR", Toast.LENGTH_LONG).show();
		}

		return ok;
	}
}
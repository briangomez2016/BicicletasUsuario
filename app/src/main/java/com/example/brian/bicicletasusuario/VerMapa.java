package com.example.brian.bicicletasusuario;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.brian.bicicletasusuario.ApiCliente.ApiCliente;
import com.example.brian.bicicletasusuario.ApiInterface.ApiInterface;
import com.example.brian.bicicletasusuario.ApiInterface.RespuestaParadas;
import com.example.brian.bicicletasusuario.Clases.Parada;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerMapa extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

	private GoogleMap map = null;

	private SupportMapFragment mapFragment;

	@BindView(R.id.cbMostrarVacias)
	CheckBox cbMostrarVacias;
	@BindView (R.id.llOpciones)
	LinearLayout llOpciones;
	@BindView (R.id.btnCerrarOpciones)
	Button btnCerrarOpciones;
	@BindView (R.id.btnCerrarDevolver)
	Button btnCerrarDevolver;
	@BindView (R.id.llPanelDevolver)
	LinearLayout llPanelDevolver;
	@BindView (R.id.btnPanelAlquilar)
	Button btnPanelAluilar;
	@BindView (R.id.btnPanelDevolver)
	Button btnPanelDevolver;
	@BindView (R.id.btnQRDevolver)
	Button btnQRDevolver;
	@BindView (R.id.etQRDevolver)
	EditText etQRDevolver;

	private List<Marker> paradas;

	public VerMapa() {
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_ver_mapa, container, false);
		ButterKnife.bind(this, v);

		mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

		if (mapFragment == null) {
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			mapFragment = SupportMapFragment.newInstance();
			ft.replace(R.id.map, mapFragment).commit();
		}

		mapFragment.getMapAsync(this);

		cbMostrarVacias.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				cargarParadas();
			}
		});

		btnCerrarOpciones.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				llOpciones.setVisibility(View.GONE);
			}
		});

		btnPanelAluilar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				llOpciones.setVisibility(View.GONE);
				Toast.makeText(VerMapa.this.getActivity(), "ABRIR PANEL ALQUILAR AQUI", Toast.LENGTH_SHORT).show();
			}
		});

		btnPanelDevolver.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				llOpciones.setVisibility(View.GONE);
				llPanelDevolver.setVisibility(View.VISIBLE);
			}
		});

		btnCerrarDevolver.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				llPanelDevolver.setVisibility(View.GONE);
			}
		});

		btnQRDevolver.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				escanearQR (0);
			}
		});
		
		return v;
	}

	private void escanearQR (int unoAlquilarCeroDevolver) {
		try {
			Intent intent = new Intent("com.google.zxing.client.android.SCAN");
			intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes
			startActivityForResult(intent, unoAlquilarCeroDevolver);
		} catch (Exception e) {
			Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
			Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
			startActivity(marketIntent);
		}
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0) {

			if (resultCode == AppCompatActivity.RESULT_OK) {
				String contents = data.getStringExtra ("SCAN_RESULT");
				etQRDevolver.setText (contents);
			}
			if(resultCode == AppCompatActivity.RESULT_CANCELED){
				Toast.makeText(this.getActivity (), "No se pudo escanear el codigo", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	public void onMapReady(final GoogleMap googleMap) {
		map = googleMap;
		map.setOnMarkerClickListener (this);

		LatLng paysandu = new LatLng(-32.316465, -58.088980);
		googleMap.moveCamera(CameraUpdateFactory.newLatLng(paysandu));
		googleMap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));

		cargarParadas();

		if (ContextCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
			map.setMyLocationEnabled(true);
		} else {
			requestPermissions(
					new String[]{
							Manifest.permission.ACCESS_FINE_LOCATION,
							Manifest.permission.ACCESS_COARSE_LOCATION
					},
					7368
			);
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		if (requestCode == 7368) {
			if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				return;
			}
			map.setMyLocationEnabled(true);
		}
	}

	private void cargarParadas () {
		ApiInterface api = ApiCliente.getClient().create(ApiInterface.class);
		Call<RespuestaParadas> call = api.getParadas();
		call.enqueue(new Callback<RespuestaParadas>() {
			@Override
			public void onResponse(Call<RespuestaParadas> call, Response<RespuestaParadas> response) {
				if (!response.body().getCodigo().equals("1"))
					Toast.makeText(VerMapa.this.getContext(), "No se pudieron cargar las paradas", Toast.LENGTH_SHORT).show();
				else {
					if (paradas == null)
						paradas = new ArrayList<> ();

					for (Marker m : paradas)
						m.remove ();
					paradas.clear ();
					for (Parada p : response.body().getParadas()) {
						if (!cbMostrarVacias.isChecked () && p.getCantidadLibre () == 0)
							continue;

						MarkerOptions marker = new MarkerOptions ()
								.position (new LatLng (p.getLatitud (), p.getLongitud ()))
								.title (p.getId () + " | " + p.getNombre ())
								.draggable(false)
								.snippet (p.getDireccion ())
								.icon (BitmapDescriptorFactory.defaultMarker (
										p.getCantidadLibre () == 0 ?
												BitmapDescriptorFactory.HUE_RED
												: BitmapDescriptorFactory.HUE_GREEN
								));
						Marker m = map.addMarker (marker);

						paradas.add (m);
					}
				}
			}

			@Override
			public void onFailure(Call<RespuestaParadas> call, Throwable t) {
				Log.d ("ASD", t.getMessage());
				Toast.makeText(VerMapa.this.getContext(), "No se pudieron cargar las paradas", Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		if (llOpciones.getVisibility () == View.GONE)
			llOpciones.setVisibility (View.VISIBLE);
		return false;
	}
}
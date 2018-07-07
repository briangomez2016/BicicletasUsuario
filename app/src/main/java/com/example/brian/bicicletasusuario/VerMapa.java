package com.example.brian.bicicletasusuario;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brian.bicicletasusuario.ApiCliente.ApiCliente;
import com.example.brian.bicicletasusuario.ApiInterface.ApiInterface;
import com.example.brian.bicicletasusuario.ApiInterface.Respuesta;
import com.example.brian.bicicletasusuario.ApiInterface.RespuestaAlquilerActual;
import com.example.brian.bicicletasusuario.ApiInterface.RespuestaLugares;
import com.example.brian.bicicletasusuario.ApiInterface.RespuestaParadas;
import com.example.brian.bicicletasusuario.Clases.Lugar;
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
	@BindView (R.id.btnConfirmarDevolver)
	Button btnConfirmarDevolver;
	@BindView (R.id.etQRDevolver)
	EditText etQRDevolver;
	@BindView (R.id.progressBarDevolver)
	ProgressBar progressBarDevolver;
	@BindView (R.id.spinnerLugares)
	Spinner spinnerTipo;

	private List<Marker> paradas;

	public VerMapa() {
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	String lugarSeleccionado;

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
				if (paradaSeleccionada == null || paradaSeleccionada.getCantidadOcupada() == 0) {
					Toast.makeText(VerMapa.this.getContext(), "Parada llena, no se puede devolver una bici", Toast.LENGTH_SHORT).show();
					return;
				} else {
					final ApiInterface api = ApiCliente.getClient().create(ApiInterface.class);
					Call<RespuestaLugares> call = api.obtenerLugares(paradaSeleccionada.getId());
					call.enqueue(new Callback<RespuestaLugares>() {
						@Override
						public void onResponse(Call<RespuestaLugares> call, Response<RespuestaLugares> response) {
							if (response.body().getCodigo().equals("1")) {
								List<String> items = new ArrayList<>();
								items.add("Lugares");
								for (Lugar l : response.body().getLugares()) {
									items.add(l.getLugar() + "");
								}

								ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, items) {
									@Override
									public View getDropDownView(int position, View convertView, ViewGroup parent) {
										View view = super.getDropDownView(position, convertView, parent);
										TextView tv = (TextView) view;
										if (position == 0) {
											tv.setTextColor(Color.GRAY);
										} else {
											tv.setTextColor(Color.BLACK);
										}
										return view;
									}
								};

								spinnerTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
									public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
										//Poner el primer valor en color gris
										if (position == 0) {
											TextView textView = (TextView) parent.getChildAt(position);
											textView.setTextColor(getResources().getColor(R.color.hintReportarIncidencia));
											lugarSeleccionado = null;
										} else {
											lugarSeleccionado = parent.getItemAtPosition(position).toString();
										}

									}

									public void onNothingSelected(AdapterView<?> parent) {
									}
								});

								spinnerTipo.setAdapter(adapter);

								llOpciones.setVisibility(View.GONE);
								llPanelDevolver.setVisibility(View.VISIBLE);
							} else
								Toast.makeText(VerMapa.this.getActivity(), "No se pudo conectar con el servidor", Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onFailure(Call<RespuestaLugares> call, Throwable t) {
							Toast.makeText(VerMapa.this.getActivity(), "Error al conectarse con el servidor", Toast.LENGTH_SHORT).show();
						}
					});
				}
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
			public void onClick(View view) {
				Intent intent = new Intent(getActivity(), QR.class);
				startActivityForResult(intent, 20);
			}

		});

		btnConfirmarDevolver.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (paradaSeleccionada == null || paradaSeleccionada.getCantidadOcupada() == 0) {
					Toast.makeText(VerMapa.this.getContext(), "Parada llena, no se puede devolver una bici", Toast.LENGTH_SHORT).show();
					llPanelDevolver.setVisibility(View.GONE);
					return;
				} else {
					if (etQRDevolver.getText().toString() == null || etQRDevolver.getText().toString().isEmpty()) {
						Toast.makeText(VerMapa.this.getContext(), "Ingrese el codigo de la bicicleta", Toast.LENGTH_SHORT).show();
						return;
					}
					if (lugarSeleccionado == null || lugarSeleccionado.isEmpty()) {
						Toast.makeText(VerMapa.this.getContext(), "Seleccione un lugar en la parada", Toast.LENGTH_SHORT).show();
						return;
					}

					btnConfirmarDevolver.setVisibility(View.GONE);
					progressBarDevolver.setVisibility(View.VISIBLE);

					ApiInterface api = ApiCliente.getClient().create(ApiInterface.class);
					Call<Respuesta> call = api.devolverAlquiler(etQRDevolver.getText().toString(), paradaSeleccionada.getId(), Integer.valueOf(lugarSeleccionado));
					call.enqueue(new Callback<Respuesta>() {
						@Override
						public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
							if (!response.body().getCodigo().equals("0")) {
								Toast.makeText(VerMapa.this.getContext(), "No se pudo devolver la bicicleta", Toast.LENGTH_SHORT).show();
							} else {
								etQRDevolver.setText("");
								llPanelDevolver.setVisibility(View.GONE);
								Toast.makeText(VerMapa.this.getContext(), "Bicicleta devuelta", Toast.LENGTH_SHORT).show();
							}
							btnConfirmarDevolver.setVisibility(View.VISIBLE);
							progressBarDevolver.setVisibility(View.GONE);
						}
						@Override
						public void onFailure(Call<Respuesta> call, Throwable t) {
							Toast.makeText(VerMapa.this.getContext(), "No se conecto con el servidor y no se pudo devolver la bicicleta", Toast.LENGTH_SHORT).show();
							btnConfirmarDevolver.setVisibility(View.VISIBLE);
							progressBarDevolver.setVisibility(View.GONE);
						}
					});
				}
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
		if (requestCode != 20) {
			super.onActivityResult(requestCode, resultCode, data);

		} else {

			etQRDevolver.setText (data.getData().toString());
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

	Parada paradaSeleccionada = null;

	@Override
	public boolean onMarkerClick(Marker marker) {
		final String id = marker.getTitle().split("\\|")[0].trim();
		ApiInterface api = ApiCliente.getClient().create(ApiInterface.class);
		Call<RespuestaParadas> call = api.getParadas();
		call.enqueue(new Callback<RespuestaParadas>() {
			@Override
			public void onResponse(Call<RespuestaParadas> call, Response<RespuestaParadas> response) {
				if (response.body().getCodigo().equals("1")) {
					boolean a = false;
					for (Parada p : response.body().getParadas()) {
						if (id.equals(p.getId()+"")) {
							paradaSeleccionada = p;
							a = true;
							break;
						}
					}
					if (!a)
						paradaSeleccionada = null;

					if (paradaSeleccionada != null) {

						SharedPreferences pref = VerMapa.this.getContext().getApplicationContext().getSharedPreferences("usuario", Context.MODE_PRIVATE);
						String email=pref.getString("email", null);

						ApiInterface api = ApiCliente.getClient().create(ApiInterface.class);
						Call<RespuestaAlquilerActual> call2 = api.alquilerActual(email);
						call2.enqueue(new Callback<RespuestaAlquilerActual>() {
							@Override
							public void onResponse(Call<RespuestaAlquilerActual> call, Response<RespuestaAlquilerActual> response) {
								if (response.body().getCodigo().equals("1")) {
									if (response.body().getAlquiler() == null) {
										btnPanelAluilar.setVisibility(View.VISIBLE);
										btnPanelDevolver.setVisibility(View.GONE);
									} else {
										btnPanelAluilar.setVisibility(View.GONE);
										btnPanelDevolver.setVisibility(View.VISIBLE);
									}
									llOpciones.setVisibility(View.VISIBLE);
								} else
									Toast.makeText(VerMapa.this.getContext(), "No se pudo conectar con el servidor", Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onFailure(Call<RespuestaAlquilerActual> call, Throwable t) {
								Toast.makeText(VerMapa.this.getContext(), "No se pudo conectar con el servidor", Toast.LENGTH_SHORT).show();
							}
						});
					}
				} else
					Toast.makeText(VerMapa.this.getContext(), "No se pudo conectar con el servidor", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onFailure(Call<RespuestaParadas> call, Throwable t) {
				Toast.makeText(VerMapa.this.getContext(), "No se pudo conectar con el servidor", Toast.LENGTH_SHORT).show();
			}
		});
		
		return false;
	}
}
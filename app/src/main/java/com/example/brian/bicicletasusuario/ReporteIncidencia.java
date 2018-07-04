package com.example.brian.bicicletasusuario;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Space;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.brian.bicicletasusuario.ApiCliente.ApiCliente;
import com.example.brian.bicicletasusuario.ApiInterface.ApiInterface;
import com.example.brian.bicicletasusuario.ApiInterface.RespuestaIncidencia;
import com.example.brian.bicicletasusuario.ApiInterface.RespuestaParadas;
import com.example.brian.bicicletasusuario.ApiInterface.RespuestaUsuario;
import com.example.brian.bicicletasusuario.Clases.Parada;
import com.google.gson.internal.bind.ArrayTypeAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReporteIncidencia extends Fragment {
    List<Parada> paradas;
    @BindView(R.id.tipo)
    Spinner spinnerTipo;
    @BindView(R.id.descripcion)
    EditText descripcion;
    @BindView(R.id.parada)
    Spinner spinnerParada;
    @BindView(R.id.btnReportar)
    Button btnReportar;
    String paradaSeleccionada;
    String tipoSeleccionado;

    public ReporteIncidencia() {
        paradaSeleccionada = null;
        tipoSeleccionado = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_reporte_incidencia, container, false);
        ButterKnife.bind(this, v);
        cargarTipos();
        cargarParadas();

        btnReportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportar();
            }
        });

        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void cargarTipos() {
        List<String> items = new ArrayList<>();
        items.add("Tipo");
        items.add("Sugerencia");
        items.add("Problema con bicicleta");
        items.add("Problema con parada");
        items.add("Problema con saldo");
        items.add("Otro");

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
                    tipoSeleccionado = null;
                } else {
                    tipoSeleccionado = parent.getItemAtPosition(position).toString();
                }

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerTipo.setAdapter(adapter);
    }

    private void cargarParadas() {
        final ApiInterface api = ApiCliente.getClient().create(ApiInterface.class);
        Call<RespuestaParadas> call = api.getParadas();
        call.enqueue(new Callback<RespuestaParadas>() {
            @Override
            public void onResponse(Call<RespuestaParadas> call, Response<RespuestaParadas> response) {
                List<String> items = new ArrayList<>();
                items.add("Parada(opcional)");
                paradas = response.body().getParadas();
                if (paradas != null) {
                    for (Parada p : paradas) {
                        items.add(p.getId() + " - " + p.getNombre());
                    }
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

                spinnerParada.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        //Poner el primer valor en color gris
                        if (position == 0) {
                            TextView textView = (TextView) parent.getChildAt(position);
                            textView.setTextColor(getResources().getColor(R.color.hintReportarIncidencia));
                            paradaSeleccionada = null;
                        } else {
                            paradaSeleccionada = parent.getItemAtPosition(position).toString();
                        }

                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                spinnerParada.setAdapter(adapter);


            }

            @Override
            public void onFailure(Call<RespuestaParadas> call, Throwable t) {
                Toast.makeText(getActivity(), "No se pudieron cargar las paradas", Toast.LENGTH_SHORT);
            }
        });
    }


    private void reportar() {
        String textDescripcion = descripcion.getText().toString().trim();

        if (tipoSeleccionado == null) {
            Toast.makeText(getActivity(), "Especifique un tipo de inicidencia", Toast.LENGTH_SHORT).show();
            return;
        } else if (textDescripcion.equals("")) {
            Toast.makeText(getActivity(), "Especifique una descipción de la inicidencia", Toast.LENGTH_SHORT).show();
            return;
        } else {
            enviarReporte(textDescripcion, tipoSeleccionado);
        }
    }

    private void enviarReporte(String textDescripcion, final String textTipo) {
        SharedPreferences sp = this.getActivity().getSharedPreferences("usuario", Context.MODE_PRIVATE);
        Call<RespuestaIncidencia> call = ApiCliente.getClient().create(ApiInterface.class).reportarIncidencia(sp.getString("email", null), paradaSeleccionada, "0", null, textDescripcion,tipoSeleccionado);
        call.enqueue(new Callback<RespuestaIncidencia>() {

            @Override
            public void onResponse(Call<RespuestaIncidencia> call, Response<RespuestaIncidencia> response) {
                if (response.body().getCodigo().equals("1")) {
                    Toast.makeText(getActivity(), "Inicidencia enviada, gracias por reportarla.", Toast.LENGTH_SHORT).show();
                    descripcion.setText("");
                    spinnerTipo.setSelection(0);
                    spinnerTipo.setSelected(true);
                    spinnerParada.setSelection(0);
                    spinnerParada.setSelected(true);
                } else {
                    Toast.makeText(getActivity(), "Error al enviar la incidencia.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RespuestaIncidencia> call, Throwable t) {
                Toast.makeText(getActivity(), "Error de conexion con el servidor", Toast.LENGTH_SHORT);
            }
        });

    }
}

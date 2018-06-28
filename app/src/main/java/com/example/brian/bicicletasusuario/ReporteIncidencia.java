package com.example.brian.bicicletasusuario;

import android.content.Context;
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
import com.example.brian.bicicletasusuario.ApiInterface.RespuestaParadas;
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
    EditText tipo;
    @BindView(R.id.descripcion)
    EditText descripcion;
    @BindView(R.id.parada)
    Spinner spinnerParada;
    @BindView(R.id.btnReportar)
    Button btnReportar;
    String paradaSeleccionada;

    public ReporteIncidencia() {
        paradaSeleccionada = "";
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
        cargarParadas();

        btnReportar.setOnClickListener(new View.OnClickListener(){
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

    private void cargarParadas() {
        final ApiInterface api = ApiCliente.getClient().create(ApiInterface.class);
        Call<RespuestaParadas> call = api.getParadas();
        call.enqueue(new Callback<RespuestaParadas>() {
            @Override
            public void onResponse(Call<RespuestaParadas> call, Response<RespuestaParadas> response) {
                List<String> items = new ArrayList<>();
                items.add("Parada");
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
                            textView.setTextColor(Color.GRAY);
                            paradaSeleccionada  = "";
                        }else{
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

    private void reportar(){
        String textTipo = tipo.getText().toString().trim();
        String textDescripcion = descripcion.getText().toString().trim();

        if(textTipo.equals("")){
            Toast.makeText(getActivity(),"Especifique un tipo de inicidencia",Toast.LENGTH_SHORT).show();
            return;
        }else if(textDescripcion.equals("")){
            Toast.makeText(getActivity(),"Especifique una descipción de la inicidencia",Toast.LENGTH_SHORT).show();
            return;
        }else{
            Toast.makeText(getActivity(),"Tipo: "+textTipo,Toast.LENGTH_SHORT).show();
            Toast.makeText(getActivity(),"Descipción: "+textDescripcion,Toast.LENGTH_SHORT).show();
            Toast.makeText(getActivity(),"Parada: "+paradaSeleccionada,Toast.LENGTH_SHORT).show();
        }
    }
}

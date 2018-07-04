package com.example.brian.bicicletasusuario.utils;

import android.os.Handler;
import android.util.Log;

import com.example.brian.bicicletasusuario.ApiCliente.ApiCliente;
import com.example.brian.bicicletasusuario.ApiInterface.ApiInterface;
import com.example.brian.bicicletasusuario.ApiInterface.Respuesta;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Util {

	public static void setearRecordatorioFaltaPoco (int minutosDelAlquiler, int avisarAntesDe) {
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				ApiInterface api = ApiCliente.getClient().create(ApiInterface.class);
				Call<Respuesta> call = api.avisarFaltaPoco();
				call.enqueue(new Callback<Respuesta>() {
					@Override
					public void onResponse(Call<Respuesta> call, Response<Respuesta> response) {
						Log.d("NOTIFFFFFF", response.body().getCodigo());
						Log.d("NOTIFFFFFF", response.body().getMensaje());
					}
					@Override
					public void onFailure(Call<Respuesta> call, Throwable t) {
						Log.d("NOTIFFFFFF", "Error: " + t.getMessage());
					}
				});
			}
		}, (minutosDelAlquiler * 1000) - (avisarAntesDe * 1000));
	}
}

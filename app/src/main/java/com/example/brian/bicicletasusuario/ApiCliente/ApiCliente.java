package com.example.brian.bicicletasusuario.ApiCliente;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Brian on 25/05/2018.
 */

public class ApiCliente {

        public static final String BASE_URL = "http://192.168.1.49/BackEndMoviles/public/";
        private static Retrofit retrofit = null;


        public static Retrofit getClient() {
                if (retrofit==null) {
                        retrofit = new Retrofit.Builder()
                                .baseUrl(BASE_URL)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();
                }
                return retrofit;
        }
}
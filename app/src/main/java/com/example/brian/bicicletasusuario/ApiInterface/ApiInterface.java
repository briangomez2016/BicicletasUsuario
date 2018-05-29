package com.example.brian.bicicletasusuario.ApiInterface;

import com.example.brian.bicicletasusuario.RespuestaRegistrar;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Brian on 25/05/2018.
 */

public interface ApiInterface {


    @FormUrlEncoded
    @POST("registrarse")
    Call<RespuestaRegistrar> savePost(@Field("ci") String ci,
                                      @Field("pasaporte") String pasaporte,
                                      @Field("nombre") String nombre,
                                      @Field("email") String email,
                                      @Field("pass") String pass,
                                      @Field("telefono") String tel,
                                      @Field("direccion") String dir
    );

}

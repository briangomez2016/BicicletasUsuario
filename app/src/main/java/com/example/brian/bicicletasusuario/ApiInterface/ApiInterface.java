package com.example.brian.bicicletasusuario.ApiInterface;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Brian on 25/05/2018.
 */

public interface ApiInterface {


    @FormUrlEncoded
    @POST("registrarse")
    Call<RespuestaUsuario> registrar(@Field("ci") String ci,
                                     @Field("pasaporte") String pasaporte,
                                     @Field("nombre") String nombre,
                                     @Field("email") String email,
                                     @Field("pass") String pass,
                                     @Field("telefono") String tel,
                                     @Field("direccion") String dir
    );

    @FormUrlEncoded
    @POST("login")
    Call<RespuestaUsuario> iniciar(@Field("email") String email,
                                   @Field("password") String pass,
                                   @Field("uid") String idMovil
    );


    @FormUrlEncoded
    @POST("reportarIncidencia")
    Call<RespuestaIncidencia> reportarIncidencia(@Field("usr") String usr,
                                                 @Field("parada") String parada,
                                                 @Field("estado") String estado,
                                                 @Field("admin") String admin,
                                                 @Field("comentario") String comentario,
                                                 @Field("tipo") String tipo);

    @GET("paradas")
    Call<RespuestaParadas> getParadas();

    @GET("perfil")
    Call<RespuestaUsuario> getPerfil(@Query("email") String email);


    @GET("alquileresUsuario")
    Call<RespuestaAlquileres> getAlquileres(@Query("usuario") String email);

    @FormUrlEncoded
    @POST("editarPerfil")
    Call<RespuestaUsuario> editarPerfil(@Field("ci") String ci,
                                        @Field("nombre") String nombre,
                                        @Field("email") String email,
                                        @Field("telefono") String tel,
                                        @Field("direccion") String dir,
                                        @Field("contrasena") String pass
    );

    @GET ("consultaAlquileresActuales")
    Call<Respuesta> avisarFaltaPoco ();

    @FormUrlEncoded
    @POST("devolverBici")
    Call<Respuesta> devolverAlquiler (
            @Field("bicicleta") String bicicleta,
            @Field("parada") int parada,
            @Field("lugar") int lugar
    );

    @GET ("alquilerActual")
    Call<RespuestaAlquilerActual> alquilerActual (
            @Query("email") String email
    );
}

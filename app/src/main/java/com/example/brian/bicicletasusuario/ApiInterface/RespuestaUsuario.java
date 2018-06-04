package com.example.brian.bicicletasusuario.ApiInterface;

/**
 * Created by Brian on 04/06/2018.
 */

import com.example.brian.bicicletasusuario.Clases.Usuario;
import com.google.gson.annotations.SerializedName;

import android.app.usage.NetworkStatsManager;

import com.example.brian.bicicletasusuario.Clases.Parada;
import com.example.brian.bicicletasusuario.Clases.Usuario;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Brian on 04/06/2018.
 */

public class RespuestaUsuario {


    @SerializedName("codigo")
    private String codigo;
    @SerializedName ("mensaje")
    private String mensaje;
    @SerializedName("datosusuario")
    private Usuario user;

    public String getCodigo() { return codigo; }

    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getMensaje() { return mensaje; }

    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public Usuario getUsuario() { return user; }

    public void setUsuario(Usuario usuario) { this.user = usuario; }

}

package com.example.brian.bicicletasusuario.ApiInterface;

import com.example.brian.bicicletasusuario.Clases.Alquiler;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RespuestaAlquileres {
    @SerializedName("codigo")
    private String codigo;
    @SerializedName("mensaje")
    private String mensaje;
    @SerializedName("alquileres")
    private List<Alquiler> alquileres;

    public String getCodigo() { return codigo; }

    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getMensaje() { return mensaje; }

    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public List<Alquiler> getAlquileres() { return alquileres; }

    public void setAlquileres(List<Alquiler> alquileres) { this.alquileres = alquileres; }
}

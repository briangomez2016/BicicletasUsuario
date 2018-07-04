package com.example.brian.bicicletasusuario.ApiInterface;

import com.google.gson.annotations.SerializedName;

public class RespuestaCorreo {

    @SerializedName("codigo")
    private String codigo;
    @SerializedName ("mensaje")
    private String mensaje;

    @SerializedName ("codigoGenerao")
    private String codigoGenerado;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getCodigoGenerado() {
        return codigoGenerado;
    }

    public void setCodigoGenerado(String codigoGenerado) {
        this.codigoGenerado = codigoGenerado;
    }

    public RespuestaCorreo(String codigo, String mensaje, String codigoGenerado) {
        this.codigo = codigo;
        this.mensaje = mensaje;
        this.codigoGenerado = codigoGenerado;

    }
}

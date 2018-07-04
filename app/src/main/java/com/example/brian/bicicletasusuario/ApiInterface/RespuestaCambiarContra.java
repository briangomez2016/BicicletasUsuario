
package com.example.brian.bicicletasusuario.ApiInterface;

import com.google.gson.annotations.SerializedName;
public class RespuestaCambiarContra {

    @SerializedName("codigo")
    private String codigo;
    @SerializedName ("mensaje")
    private String mensaje;

    public RespuestaCambiarContra(String codigo, String mensaje) {
        this.codigo = codigo;
        this.mensaje = mensaje;
    }

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
}

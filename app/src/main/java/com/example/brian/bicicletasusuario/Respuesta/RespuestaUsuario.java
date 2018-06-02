package com.example.brian.bicicletasusuario.Respuesta;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Brian on 28/05/2018.
 */

public class RespuestaUsuario {
    @SerializedName("codigo")
    private String codigo;

    public String getCodigo() {
        return codigo;
    }
    public void setCodigo(String codigo) {

        this.codigo = codigo;
    }

    @SerializedName("mensaje")
    private String mensaje;

    public String getMensaje()
    {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }


}

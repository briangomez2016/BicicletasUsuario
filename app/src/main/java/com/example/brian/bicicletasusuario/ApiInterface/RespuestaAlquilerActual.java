package com.example.brian.bicicletasusuario.ApiInterface;

import com.example.brian.bicicletasusuario.Clases.Alquiler;
import com.google.gson.annotations.SerializedName;

public class RespuestaAlquilerActual {

	@SerializedName("codigo")
	private String codigo;
	@SerializedName ("mensaje")
	private String mensaje;
	@SerializedName("alquiler")
	private Alquiler alquiler;

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

	public Alquiler getAlquiler() {
		return alquiler;
	}

	public void setAlquiler(Alquiler alquiler) {
		this.alquiler = alquiler;
	}
}

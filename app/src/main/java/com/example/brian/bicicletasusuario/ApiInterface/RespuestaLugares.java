package com.example.brian.bicicletasusuario.ApiInterface;

import com.example.brian.bicicletasusuario.Clases.Lugar;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RespuestaLugares {

	@SerializedName("codigo")
	private String codigo;
	@SerializedName ("mensaje")
	private String mensaje;
	@SerializedName ("lugares")
	private List<Lugar> lugares;

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

	public List<Lugar> getLugares() {
		return lugares;
	}

	public void setLugares(List<Lugar> lugares) {
		this.lugares = lugares;
	}
}

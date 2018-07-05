package com.example.brian.bicicletasusuario.Clases;

import com.google.gson.annotations.SerializedName;

public class Lugar {

	@SerializedName ("Lugar")
	private int lugar;

	public int getLugar() {
		return lugar;
	}

	public void setLugar(int lugar) {
		this.lugar = lugar;
	}
}

package com.example.brian.bicicletasusuario.Clases;

public class Parada {
	private int id;
	private String nombre;
	private double latitud;
	private double longitud;
	private String direccion;
	private int cantBicis;
	private int cantidadLibre;
	private int cantidadOcupada;

	public Parada(int id, String nombre, double latitud, double longitud, String direccion, int cantBicis, int cantidadLibre, int cantidadOcupada) {
		this.id = id;
		this.nombre = nombre;
		this.latitud = latitud;
		this.longitud = longitud;
		this.direccion = direccion;
		this.cantBicis = cantBicis;
		this.cantidadLibre = cantidadLibre;
		this.cantidadOcupada = cantidadOcupada;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public double getLatitud() {
		return latitud;
	}

	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}

	public double getLongitud() {
		return longitud;
	}

	public void setLongitud(double longitud) { this.longitud = longitud; }

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public int getCantBicis() {
		return cantBicis;
	}

	public void setCantBicis(int cantBicis) {
		this.cantBicis = cantBicis;
	}

	public int getCantidadLibre() {
		return cantidadLibre;
	}

	public void setCantidadLibre(int cantidadLibre) {
		this.cantidadLibre = cantidadLibre;
	}

	public int getCantidadOcupada() {
		return cantidadOcupada;
	}

	public void setCantidadOcupada(int cantidadOcupada) {
		this.cantidadOcupada = cantidadOcupada;
	}
}

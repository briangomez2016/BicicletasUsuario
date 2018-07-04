package com.example.brian.bicicletasusuario.Clases;

import java.util.Date;

public class Alquiler {
    private String horaInicio;
    private String horaFin;
    private String parada;
    private int bicicleta;

    public Alquiler(String horaInicio, String horaFin, String parada, int bicicleta) {
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.parada = parada;
        this.bicicleta = bicicleta;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(String HoraFin) {
        this.horaFin = HoraFin;
    }

    public String getParada() {
        return parada;
    }

    public void setParada(String parada) {
        this.parada = parada;
    }

    public int getBicicleta() {
        return bicicleta;
    }

    public void setBicicleta(int bicicleta) {
        this.bicicleta = bicicleta;
    }
}

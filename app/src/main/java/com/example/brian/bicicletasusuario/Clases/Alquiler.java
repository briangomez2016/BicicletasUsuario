package com.example.brian.bicicletasusuario.Clases;

import java.util.Date;

public class Alquiler {
    private String horaInicio;
    private String horaFin;
    private String parada;
    private int bici;

    public Alquiler(String horaInicio, String horaFin, String parada, int bici) {
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.parada = parada;
        this.bici = bici;
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

    public int getBici() {
        return bici;
    }

    public void setBici(int bici) {
        this.bici = bici;
    }
}

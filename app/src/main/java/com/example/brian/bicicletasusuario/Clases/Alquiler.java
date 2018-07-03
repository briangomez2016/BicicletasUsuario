package com.example.brian.bicicletasusuario.Clases;

import java.util.Date;

public class Alquiler {
    private Date horaInicio;
    private Date horaFin;
    private String parada;
    private int bici;

    public Alquiler(Date horaInicio, Date horaFin, String parada, int bici) {
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.parada = parada;
        this.bici = bici;
    }

    public Date getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(Date horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Date getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(Date HoraFin) {
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

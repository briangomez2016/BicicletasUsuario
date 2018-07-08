package com.example.brian.bicicletasusuario.Clases;

import java.util.Date;

public class Alquiler {
    private String costoActual;
    private String tiempoActual;
    private String costoAlquilado;
    private String tiempoAlquilado;
    private String bicicleta;
    private String parada;
    private String horaInicio;
    private String horaFin;
    private String fin;
    public String getFin() {
        return fin;
    }

    public void setFin(String fin) {
        fin = fin;
    }



    public Alquiler(String costoActual, String tiempoActual, String costoAlquilado, String tiempoAlquilado, String bicicleta, String parada, String horaInicio, String horaFin,String Fin) {
        this.costoActual = costoActual;
        this.tiempoActual = tiempoActual;
        this.costoAlquilado = costoAlquilado;
        this.tiempoAlquilado = tiempoAlquilado;
        this.bicicleta = bicicleta;
        this.parada = parada;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.fin = Fin;
    }

    public String getParada() {

        return parada;
    }

    public void setParada(String parada) {
        this.parada = parada;
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

    public void setHoraFin(String horaFin) {
        this.horaFin = horaFin;
    }

    public String getCostoActual() {
        return costoActual;
    }

    public void setCostoActual(String costoActual) {
        this.costoActual = costoActual;
    }

    public String getTiempoActual() {
        return tiempoActual;
    }

    public void setTiempoActual(String tiempoActual) {
        this.tiempoActual = tiempoActual;
    }

    public String getCostoAlquilado() {
        return costoAlquilado;
    }

    public void setCostoAlquilado(String costoAlquilado) {
        this.costoAlquilado = costoAlquilado;
    }

    public String getTiempoAlquilado() {
        return tiempoAlquilado;
    }

    public void setTiempoAlquilado(String tiempoAlquilado) {
        this.tiempoAlquilado = tiempoAlquilado;
    }

    public String getBicicleta() {
        return bicicleta;
    }

    public void setBicicleta(String bicicleta) {
        this.bicicleta = bicicleta;
    }


}

package com.example.brian.bicicletasusuario.Clases;

import android.text.Editable;

public class Usuario {

    private String Email;
    private String Cedula;
    private String Pasaporte;
    private String Pass;
    private String Nombre;
    private String Telefono;
    private String Direccion;
    private int Activado;
    private long Saldo;
    private String IdCelular;
    private int cantidadAlquileres;

    public Usuario(String email, String cedula, String pasaporte, String pass, String nombre, String telefono, String direccion, int activado, long saldo, String idCelular, int cantidadAlquileres) {
        Email = email;
        Cedula = cedula;
        Pasaporte = pasaporte;
        Pass = pass;
        Nombre = nombre;
        Telefono = telefono;
        Direccion = direccion;
        Activado = activado;
        Saldo = saldo;
        IdCelular = idCelular;
        this.cantidadAlquileres = cantidadAlquileres;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getCedula() {
        return Cedula;
    }

    public void setCedula(String cedula) {
        Cedula = cedula;
    }

    public String getPasaporte() {
        return Pasaporte;
    }

    public void setPasaporte(String pasaporte) {
        Pasaporte = pasaporte;
    }

    public String getPass() {
        return Pass;
    }

    public void setPass(String pass) {
        Pass = pass;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public int getActivado() {
        return Activado;
    }

    public void setActivado(int activado) {
        Activado = activado;
    }

    public long getSaldo() {
        return Saldo;
    }

    public void setSaldo(long saldo) {
        Saldo = saldo;
    }

    public String getIdCelular() {
        return IdCelular;
    }

    public void setIdCelular(String idCelular) {
        IdCelular = idCelular;
    }

    public int getCantidadAlquileres() {
        return cantidadAlquileres;
    }

    public void setCantidadAlquileres(int cantidadAlquileres) {
        this.cantidadAlquileres = cantidadAlquileres;
    }



}

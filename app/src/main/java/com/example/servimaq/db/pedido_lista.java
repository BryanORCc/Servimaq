package com.example.servimaq.db;

public class pedido_lista {


    private String codigo,nombre,apellidos,correo,fechaA,fechaP,modo;
    private int dni;

    public pedido_lista(String codigo,String nombre, String apellidos, String correo, String fechaA, String fechaP, String modo, int dni){
        this.codigo=codigo;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correo = correo;
        this.fechaA = fechaA;
        this.fechaP = fechaP;
        this.modo = modo;
        this.dni = dni;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getFechaA() {
        return fechaA;
    }

    public void setFechaA(String fechaA) {
        this.fechaA = fechaA;
    }

    public String getFechaP() {
        return fechaP;
    }

    public void setFechaP(String fechaP) {
        this.fechaP = fechaP;
    }

    public String getModo() {
        return modo;
    }

    public void setModo(String modo) {
        this.modo = modo;
    }

    public int getDni() {
        return dni;
    }

    public void setDni(int dni) {
        this.dni = dni;
    }
}

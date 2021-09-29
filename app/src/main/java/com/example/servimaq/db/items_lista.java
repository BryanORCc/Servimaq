package com.example.servimaq.db;

public class items_lista {

    private String MarcaNeumatico;
    private int Ancho, Diametro, Perfil, MmCocada, Stock;
    private double Precio;

    public items_lista(String MarcaNeumatico, int Ancho, int Diametro, int Perfil, int MmCocada, double Precio, int Stock){
        this.MarcaNeumatico = MarcaNeumatico;
        this.Ancho = Ancho;
        this.Diametro = Diametro;
        this.Perfil = Perfil;
        this.MmCocada = MmCocada;
        this.Precio = Precio;
        this.Stock = Stock;
    }

    public String getMarcaNeumatico() {
        return MarcaNeumatico;
    }

    public void setMarcaNeumatico(String marcaNeumatico) {
        MarcaNeumatico = marcaNeumatico;
    }

    public int getAncho() {
        return Ancho;
    }

    public void setAncho(int ancho) {
        Ancho = ancho;
    }

    public int getDiametro() {
        return Diametro;
    }

    public void setDiametro(int diametro) {
        Diametro = diametro;
    }

    public int getPerfil() {
        return Perfil;
    }

    public void setPerfil(int perfil) {
        Perfil = perfil;
    }

    public int getMmCocada() {
        return MmCocada;
    }

    public void setMmCocada(int mmCocada) {
        MmCocada = mmCocada;
    }

    public int getStock() {
        return Stock;
    }

    public void setStock(int stock) {
        Stock = stock;
    }

    public double getPrecio() {
        return Precio;
    }

    public void setPrecio(double precio) {
        Precio = precio;
    }
}

package com.example.servimaq.db;

import android.content.Context;

import java.util.ArrayList;

public class items_lista_salida_P {

    private String nombre,modopago;
    private int   Stock;
    private double Precio;
   private String fentrega;

    public items_lista_salida_P(String nombre,String fentrega,String modopago ,double Precio,int Stock){
        this.nombre = nombre;
        this.fentrega = fentrega;
        this.modopago = modopago;
        this.Precio = Precio;
        this.Stock = Stock;
    }


    public String getnombre() {
        return nombre;
    }

    public void setnombre(String nombre) {
        nombre = nombre;
    }

    public String getFentrega() { return fentrega; }

    public void setFentrega(String fentrega) {
        this.fentrega = fentrega;
    }

    public String getmodopago() {
        return modopago;
    }

    public void setmodopago(String modopago) {
        modopago = modopago;
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

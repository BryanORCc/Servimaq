package com.example.servimaq.db;

public class Items_Salida_set_get {
    private String ItemId;
    private int Cantidad;
    private String Precio, Total;
    private  String NombreMarca, TipoVehiculo;
    String codPedido, LlantaId;

    public Items_Salida_set_get(String itemId, int cantidad, String precio, String total, String nombreMarca, String tipoVehiculo, String codPedido, String llantaId) {
        ItemId = itemId;
        Cantidad = cantidad;
        Precio = precio;
        Total = total;
        NombreMarca = nombreMarca;
        TipoVehiculo = tipoVehiculo;
        this.codPedido = codPedido;
        LlantaId = llantaId;
    }

    public String getCodPedido() {
        return codPedido;
    }

    public void setCodPedido(String codPedido) {
        this.codPedido = codPedido;
    }

    public String getLlantaId() {
        return LlantaId;
    }

    public void setLlantaId(String llantaId) {
        LlantaId = llantaId;
    }

    public String getItemId() {
        return ItemId;
    }

    public void setItemId(String itemId) {
        ItemId = itemId;
    }

    public double getCantidad() {
        return Cantidad;
    }

    public void setCantidad(int cantidad) {
        Cantidad = cantidad;
    }

    public String getPrecio() {
        return Precio;
    }

    public void setPrecio(String precio) {
        Precio = precio;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }

    public String getNombreMarca() {
        return NombreMarca;
    }

    public void setNombreMarca(String nombreMarca) {
        NombreMarca = nombreMarca;
    }

    public String getTipoVehiculo() {
        return TipoVehiculo;
    }

    public void setTipoVehiculo(String tipoVehiculo) {
        TipoVehiculo = tipoVehiculo;
    }
}

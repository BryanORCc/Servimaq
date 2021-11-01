package com.example.servimaq.db;

public class items_lista {

    private String LlantaId, NombreMarca, IndiceVelocidad, Construccion, Clasificacion, FechaFabricacion, FotoLlanta,
            FotoVehiculo, MarcaVehiculo, ModeloVehiculo, PresionMaxima, Precio;
    private int Ancho, Diametro, Perfil, Stock, IndiceCarga;
    private double MmCocada;
    private String TipoVehiculo, DetalleLlantaId, VehiculoId, MedidaLlantaId;

    public items_lista(String llantaId, String nombreMarca, int indiceCarga, String indiceVelocidad, String construccion, String clasificacion, String fechaFabricacion,
                       String fotoLlanta, String fotoVehiculo, String marcaVehiculo, String modeloVehiculo, int ancho, int diametro, int perfil, double mmCocada, int stock,
                       String presionMaxima, String precio, String tipoVehiculo, String detalleLlantaId, String vehiculoId, String medidaLlantaId) {
        LlantaId = llantaId;
        NombreMarca = nombreMarca;
        IndiceCarga = indiceCarga;
        IndiceVelocidad = indiceVelocidad;
        Construccion = construccion;
        Clasificacion = clasificacion;
        FechaFabricacion = fechaFabricacion;
        FotoLlanta = fotoLlanta;
        FotoVehiculo = fotoVehiculo;
        MarcaVehiculo = marcaVehiculo;
        ModeloVehiculo = modeloVehiculo;
        Ancho = ancho;
        Diametro = diametro;
        Perfil = perfil;
        MmCocada = mmCocada;
        Stock = stock;
        PresionMaxima = presionMaxima;
        Precio = precio;
        TipoVehiculo = tipoVehiculo;
        DetalleLlantaId = detalleLlantaId;
        VehiculoId = vehiculoId;
        MedidaLlantaId = medidaLlantaId;
    }

    public String getLlantaId() {
        return LlantaId;
    }

    public void setLlantaId(String llantaId) {
        LlantaId = llantaId;
    }

    public String getNombreMarca() {
        return NombreMarca;
    }

    public void setNombreMarca(String nombreMarca) {
        NombreMarca = nombreMarca;
    }

    public String getIndiceVelocidad() {
        return IndiceVelocidad;
    }

    public void setIndiceVelocidad(String indiceVelocidad) {
        IndiceVelocidad = indiceVelocidad;
    }

    public String getConstruccion() {
        return Construccion;
    }

    public void setConstruccion(String construccion) {
        Construccion = construccion;
    }

    public String getClasificacion() {
        return Clasificacion;
    }

    public void setClasificacion(String clasificacion) {
        Clasificacion = clasificacion;
    }

    public String getFechaFabricacion() {
        return FechaFabricacion;
    }

    public void setFechaFabricacion(String fechaFabricacion) {
        FechaFabricacion = fechaFabricacion;
    }

    public String getFotoLlanta() {
        return FotoLlanta;
    }

    public void setFotoLlanta(String fotoLlanta) {
        FotoLlanta = fotoLlanta;
    }

    public String getFotoVehiculo() {
        return FotoVehiculo;
    }

    public void setFotoVehiculo(String fotoVehiculo) {
        FotoVehiculo = fotoVehiculo;
    }

    public String getMarcaVehiculo() {
        return MarcaVehiculo;
    }

    public void setMarcaVehiculo(String marcaVehiculo) {
        MarcaVehiculo = marcaVehiculo;
    }

    public String getModeloVehiculo() {
        return ModeloVehiculo;
    }

    public void setModeloVehiculo(String modeloVehiculo) {
        ModeloVehiculo = modeloVehiculo;
    }

    public String getPresionMaxima() {
        return PresionMaxima;
    }

    public void setPresionMaxima(String presionMaxima) {
        PresionMaxima = presionMaxima;
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

    public int getStock() {
        return Stock;
    }

    public void setStock(int stock) {
        Stock = stock;
    }

    public int getIndiceCarga() {
        return IndiceCarga;
    }

    public void setIndiceCarga(int indiceCarga) {
        IndiceCarga = indiceCarga;
    }

    public String getPrecio() {
        return Precio;
    }

    public void setPrecio(String precio) {
        Precio = precio;
    }

    public double getMmCocada() {
        return MmCocada;
    }

    public void setMmCocada(double mmCocada) {
        MmCocada = mmCocada;
    }

    public String getTipoVehiculo() {
        return TipoVehiculo;
    }

    public void setTipoVehiculo(String tipoVehiculo) {
        TipoVehiculo = tipoVehiculo;
    }

    public String getDetalleLlantaId() {
        return DetalleLlantaId;
    }

    public void setDetalleLlantaId(String detalleLlantaId) {
        DetalleLlantaId = detalleLlantaId;
    }

    public String getVehiculoId() {
        return VehiculoId;
    }

    public void setVehiculoId(String vehiculoId) {
        VehiculoId = vehiculoId;
    }

    public String getMedidaLlantaId() {
        return MedidaLlantaId;
    }

    public void setMedidaLlantaId(String medidaLlantaId) {
        MedidaLlantaId = medidaLlantaId;
    }
}

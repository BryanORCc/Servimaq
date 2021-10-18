package com.example.servimaq.db;

import android.content.Context;

import java.util.ArrayList;

public class items_lista_salida_P {

   private String  tvCodigo, tvnombreyapellido,tvcorreo,tvfechaentrega, tvmodopago,tvDNI;

   public items_lista_salida_P(String tvCodigo, String tvnombreyapellido, String tvcorreo, String tvfechaentrega, String tvmodopago, String tvDNI) {
      this.tvCodigo = tvCodigo;
      this.tvnombreyapellido = tvnombreyapellido;
      this.tvcorreo = tvcorreo;
      this.tvfechaentrega = tvfechaentrega;
      this.tvmodopago = tvmodopago;
      this.tvDNI = tvDNI;
   }

   public String getTvnombreyapellido() {
      return tvnombreyapellido;
   }

   public void setTvnombreyapellido(String tvnombreyapellido) {
      this.tvnombreyapellido = tvnombreyapellido;
   }

   public String getTvcorreo() {
      return tvcorreo;
   }

   public void setTvcorreo(String tvcorreo) {
      this.tvcorreo = tvcorreo;
   }

   public String getTvmodopago() {
      return tvmodopago;
   }

   public void setTvmodopago(String tvmodopago) {
      this.tvmodopago = tvmodopago;
   }

   public String getTvDNI() {
      return tvDNI;
   }

   public void setTvDNI(String tvDNI) {
      this.tvDNI = tvDNI;
   }

   public String getTvfechaentrega() {
      return tvfechaentrega;
   }

   public void setTvfechaentrega(String tvfechaentrega) {
      this.tvfechaentrega = tvfechaentrega;
   }

   public String getTvCodigo() {
      return tvCodigo;
   }

   public void setTvdCodigo(String tvCodigo) {
      this.tvCodigo = tvCodigo;
   }
}

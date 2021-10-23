package com.example.servimaq.db;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.servimaq.R;

import net.sourceforge.jtds.util.BlobBuffer;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SQLConexion {

    public Connection ConexionDB(Context c){

        Connection conexion=null;
        try{
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            conexion= DriverManager.getConnection("jdbc:jtds:sqlserver://192.168.1.13;databaseName=Servimaq;user=sa;password=123;");
            //conexion= DriverManager.getConnection("jdbc:jtds:sqlserver://192.168.0.18;databaseName=Servimaq;user=sa;password=123;");
        }catch (Exception e){
            Toast.makeText(c,"Falla de conexion",Toast.LENGTH_SHORT).show();
        }
        return conexion;
    }

    //--VALIDAR LOGIN--------------------------------------------------------
    public boolean Validar(Context c, String usuario, String contra){
        boolean estado = false;
        try {
            Statement st = ConexionDB(c).createStatement();
            ResultSet rs = st.executeQuery("select * from Cuenta where Usuario='"+usuario+"' and Contraseña='"+contra+"';");
            if(rs.next()){
                estado = true;
                Toast toast = Toast.makeText(c,"Bienvenido", Toast.LENGTH_SHORT);
                View vista = toast.getView();
                vista.setBackgroundResource(R.drawable.estilo_color_x);
                toast.setGravity(Gravity.CENTER,0,0);
                TextView text = (TextView) vista.findViewById(android.R.id.message);
                text.setTextColor(Color.parseColor("#FFFFFF"));
                text.setTextSize(20);
                toast.show();
            }else{
                Toast toast = Toast.makeText(c,"Usuario o Contraseña incorrecto", Toast.LENGTH_SHORT);
                View vista = toast.getView();
                vista.setBackgroundResource(R.drawable.estilo_color_x);
                toast.setGravity(Gravity.CENTER,0,0);
                TextView text = (TextView) vista.findViewById(android.R.id.message);
                text.setTextColor(Color.parseColor("#FFFFFF"));
                text.setTextSize(20);
                toast.show();
                estado = false;
            }
            rs.close();

        } catch (Exception e) {
            Toast.makeText(c,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return estado;
    }

    //--REGISTRO VEHICULO--------------------------------------------------------
    public void RegistroVehiculo(Context c, String TipoVehiculo, String FotoVehiculo, String MarcaVehiculo, String ModeloVehiculo){

        PreparedStatement registro;
        int contar= 1;
        String VehiculoId="";

        try {
            //CONTAR REGISTROS------------------------------------------------------------------------
            Statement st = ConexionDB(c).createStatement();
            ResultSet rs = st.executeQuery("select * from T_Vehiculo");

            if (!rs.next()) {
                VehiculoId = "VH01";
            }
            else {
                do {
                    contar++;
                } while (rs.next());
            }

            if(contar<=9){
                VehiculoId = "VH0"+contar;
            }else if(contar>=10 && contar<=99){
                VehiculoId = "VH"+contar;
            }


            //REGISTRAR EN TABLA----------------------------------------------------------------------
            registro = ConexionDB(c).prepareStatement("insert into T_Vehiculo values(?,?,?,?,?)");
            registro.setString(1,VehiculoId);
            registro.setString(2,TipoVehiculo);
            registro.setString(3, FotoVehiculo);
            registro.setString(4,MarcaVehiculo);
            registro.setString(5,ModeloVehiculo);
            registro.executeUpdate();
            EstiloToast(c,"Registro exitoso");
            registro.close();

        } catch (Exception e) {
            Toast.makeText(c,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    //--REGISTRO TIPO LLANTA--------------------------------------------------------
    public void RegistroDetalleLlanta(Context c, String NombreMarca, int IndiceCarga, String IndiceVelocidad, String FotoLlanta, String Construccion, int PresionMaxima, String Clasificacion, String FechaFabricacion, String MedidaLlantaId){

        PreparedStatement registro;
        int contar= 1;
        String DetalleLlantaId="";
        try {
            //CONTAR REGISTROS------------------------------------------------------------------------
            Statement st = ConexionDB(c).createStatement();
            ResultSet rs = st.executeQuery("select * from T_DetalleLlanta");

            if (!rs.next()) {
                DetalleLlantaId = "DN01";
            }
            else {
                do {
                    contar++;
                } while (rs.next());
            }

            if(contar<=9){
                DetalleLlantaId = "DN0"+contar;
            }else if(contar>=10 && contar<=99){
                DetalleLlantaId = "DN"+contar;
            }

            //REGISTRAR EN TABLA----------------------------------------------------------------------
            registro = ConexionDB(c).prepareStatement("insert into T_DetalleLlanta values(?,?,?,?,?,?,?,?,?,?)");
            registro.setString(1,DetalleLlantaId);
            registro.setString(2,NombreMarca);
            registro.setInt(3,IndiceCarga);
            registro.setString(4,IndiceVelocidad);
            registro.setString(5,FotoLlanta);
            registro.setString(6,Construccion);
            registro.setInt(7,PresionMaxima);
            registro.setString(8,Clasificacion);
            registro.setString(9,FechaFabricacion);
            registro.setString(10,MedidaLlantaId);
            registro.executeUpdate();
            EstiloToast(c,"Registro exitoso");
            registro.close();
        } catch (Exception e) {
            Toast.makeText(c,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    //--REGISTRO MEDIDAS--------------------------------------------------------
    public void RegistroMedida(Context c, int Ancho, int Diametro, int Perfil ,int MmCocada){

        PreparedStatement registro;
        int contar= 1;
        String MedidaLlantaId="";

        try {
            //CONTAR REGISTROS------------------------------------------------------------------------
            Statement st = ConexionDB(c).createStatement();
            ResultSet rs = st.executeQuery("select * from T_MedidaLlanta");

            if (!rs.next()) {
                MedidaLlantaId= "MD01";
            }
            else {
                do {
                    contar++;
                } while (rs.next());
            }

            if(contar<=9){
                MedidaLlantaId = "MD0"+contar;
            }else if(contar>=10 && contar<=99){
                MedidaLlantaId= "MD"+contar;
            }


            //REGISTRAR EN TABLA----------------------------------------------------------------------
            registro = ConexionDB(c).prepareStatement("insert into T_MedidaLlanta values(?,?,?,?,?)");
            registro.setString(1,MedidaLlantaId);
            registro.setInt(2,Ancho);
            registro.setInt(3,Diametro);
            registro.setInt(4,Perfil);
            registro.setInt(5,MmCocada);
            registro.executeUpdate();
            EstiloToast(c,"Registro exitoso");
            registro.close();

        } catch (Exception e) {
            Toast.makeText(c,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    //--REGISTRAR LLANTAS MAS NA --------------------------------------------------------
    public void RegistroLlanta(Context c, double Precio , int Stock ,String DetalleLlantaId,String VehiculoId){

        PreparedStatement registro;
        int contar= 1;
        String LlantaId="";

        try {
            //CONTAR REGISTROS------------------------------------------------------------------------
            Statement st = ConexionDB(c).createStatement();
            ResultSet rs = st.executeQuery("select * from T_Llanta");

            if (!rs.next()) {
                LlantaId= "Ll01";
            }
            else {
                do {
                    contar++;
                } while (rs.next());
            }

            if(contar<=9){
                LlantaId = "Ll0"+contar;
            }else if(contar>=10 && contar<=99){
                LlantaId= "Ll"+contar;
            }


            //REGISTRAR EN TABLA----------------------------------------------------------------------
            registro = ConexionDB(c).prepareStatement("insert into T_Llanta values(?,?,?,?,?)");
            registro.setString(1,LlantaId);
            registro.setDouble(2,Precio);
            registro.setInt(3,Stock);
            registro.setString(4,DetalleLlantaId);
            registro.setString(5,VehiculoId);
            registro.executeUpdate();
            EstiloToast(c,"Registro exitoso");

            registro.close();

        } catch (Exception e) {
            Toast.makeText(c,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    //--REGISTRO Pedido de cliente--------------------------------------------------------
    public void RegistroPedidoCliente(Context c, String NombreCliente, String ApellidoCliente, String Correo, String FechaActual, String FechaEntrega, String ModoDePago , int Dni){

        PreparedStatement registro;
        int contar= 1;
        String CodPedidoId="";
        try {
            //CONTAR REGISTROS------------------------------------------------------------------------
            Statement st = ConexionDB(c).createStatement();
            ResultSet rs = st.executeQuery("select * from T_Pedido");

            if (!rs.next()) {
                CodPedidoId = "COD-001";
            }
            else {
                do {
                    contar++;
                } while (rs.next());
            }

            if(contar<=9){
                CodPedidoId = "COD-00"+contar;
            }else if(contar>=10 && contar<=99){
                CodPedidoId = "COD-0"+contar;
            }
            else if(contar>=100 && contar<=999) {
                CodPedidoId = "COD-" + contar;
            }
            //REGISTRAR EN TABLA----------------------------------------------------------------------
            registro = ConexionDB(c).prepareStatement("insert into T_Pedido values(?,?,?,?,?,?,?,?)");
            registro.setString(1,CodPedidoId);
            registro.setString(2,NombreCliente);
            registro.setString(3,ApellidoCliente);
            registro.setString(4,Correo);
            registro.setString(5,FechaActual);
            registro.setString(6,FechaEntrega);
            registro.setString(7,ModoDePago);
            registro.setInt(8,Dni);
            registro.executeUpdate();
            EstiloToast(c,"Registro exitoso");
            registro.close();
        } catch (Exception e) {
            Toast.makeText(c,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    //--REGISTRO LISTADO--------------------------------------------------------
    public void RegistroListado(Context c, int Cantidad, double Precio ,double Total, String codPedido, String LlantaId){
        int contar= 1;
        String ItemId="";
        PreparedStatement registro;
        try {
            //CONTAR REGISTROS------------------------------------------------------------------------
            Statement st = ConexionDB(c).createStatement();
            ResultSet rs = st.executeQuery("select * from T_Listado");

            if (!rs.next()) {
                ItemId = "SVQSAC-001";
            }
            else {
                do {
                    contar++;
                } while (rs.next());
            }

            if(contar<=9){
                ItemId = "SVQSAC-00"+contar;
            }else if(contar>=10 && contar<=99){
                ItemId = "SVQSAC-0"+contar;
            }
            else if(contar>=100 && contar<=999) {
                ItemId = "SVQSAC-" + contar;
            }

            //VALIDAR DATOS REGISTRADOS------------------------------------------------------------------------
            Statement st2 = ConexionDB(c).createStatement();
            ResultSet rs2 = st2.executeQuery("select * from T_Listado where codPedido = '"+codPedido+"' AND LlantaId = '"+LlantaId+"';");

            if (!rs2.next()) {
                //REGISTRAR EN TABLA----------------------------------------------------------------------
                registro = ConexionDB(c).prepareStatement("insert into T_Listado values(?,?,?,?,?,?)");
                registro.setString(1,ItemId);
                registro.setInt(2,Cantidad);
                registro.setDouble(3,Precio);
                registro.setDouble(4,Total);
                registro.setString(5,codPedido);
                registro.setString(6,LlantaId);
                registro.executeUpdate();
                EstiloToast(c,"Agregacion exitosa");
                registro.close();
            }
            else {
                Toast.makeText(c,"Neumatico ya registrado en la Lista de salida de productos\nPor favor, verificar en su listado",Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(c,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    public void EstiloToast(Context c, String mensaje){
        Toast toast = Toast.makeText(c,mensaje, Toast.LENGTH_SHORT);
        View vista = toast.getView();
        vista.setBackgroundResource(R.drawable.estilo_color_x);
        toast.setGravity(Gravity.CENTER,0,0);
        TextView text = (TextView) vista.findViewById(android.R.id.message);
        text.setTextColor(Color.parseColor("#FFFFFF"));
        text.setTextSize(20);
        toast.show();
    }

}

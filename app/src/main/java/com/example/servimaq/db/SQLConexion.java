package com.example.servimaq.db;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLConexion {

    public Connection ConexionDB(Context c){

        Connection conexion=null;
        try{
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            conexion= DriverManager.getConnection("jdbc:jtds:sqlserver://192.168.1.11;databaseName=Servimaq;user=sa;password=12345;");
        }catch (Exception e){
            Toast.makeText(c,"Falla de conexion",Toast.LENGTH_SHORT).show();
        }
        return conexion;
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
            registro.setString(3,FotoVehiculo);
            registro.setString(4,MarcaVehiculo);
            registro.setString(5,ModeloVehiculo);
            registro.executeUpdate();
            Toast.makeText(c,"Registro exitoso",Toast.LENGTH_SHORT).show();
            registro.close();

        } catch (Exception e) {
            Toast.makeText(c,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    //--VALIDAR--------------------------------------------------------
    public boolean Validar(Context c, String usuario, String contra){
        boolean estado = false;
        try {
            Statement st = ConexionDB(c).createStatement();
            ResultSet rs = st.executeQuery("select * from Cuenta where Usuario='"+usuario+"' and Contraseña='"+contra+"';");
            if(rs.next()){
                estado = true;
                Toast.makeText(c,"Bienvenido",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(c,"Usuario o Contraseña incorrecto",Toast.LENGTH_SHORT).show();
                estado = false;
            }
            rs.close();

        } catch (Exception e) {
            Toast.makeText(c,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return estado;
    }
}

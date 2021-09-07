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

    //--REGISTRO--------------------------------------------------------
    public void Registro(Context c,String codigo, String usuario, String contra){

        PreparedStatement registro;
        try {
            registro = ConexionDB(c).prepareStatement("insert into Cuenta values(?,?,?)");
            registro.setString(1,codigo);
            registro.setString(2,usuario);
            registro.setString(3,contra);
            registro.executeUpdate();
            registro.close();

            /*if(rs.next()){
                etUsuario.setText(rs.getString(2));
            }*/

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

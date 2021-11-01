package com.example.servimaq.db;


import android.content.Context;
import android.graphics.Color;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.servimaq.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;


public class SQLConexion {

    boolean estado = false;

    public Connection ConexionDB(Context c){

        Connection conexion=null;
        /*try{
            StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            conexion= DriverManager.getConnection("jdbc:jtds:sqlserver://192.168.1.13;databaseName=Servimaq;user=sa;password=123;");
            //conexion= DriverManager.getConnection("jdbc:jtds:sqlserver://192.168.0.18;databaseName=Servimaq;user=sa;password=123;");
        }catch (Exception e){
            Toast.makeText(c,"Falla de conexion",Toast.LENGTH_SHORT).show();
        }*/
        return conexion;
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
        int contar= 1, extraer = 0;
        String ceros = "";
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
                    extraer = Integer.parseInt(rs.getString(1).substring(rs.getString(1).length()-1,rs.getString(1).length()));
                    if(extraer!=contar && contar<=9 && contar>=1){
                        ItemId = "SVQSAC-00"+contar;
                        break;
                    }else if(extraer==0){
                        extraer = Integer.parseInt(rs.getString(1).substring(rs.getString(1).length()-2,rs.getString(1).length()));
                        ceros = rs.getString(1).substring(rs.getString(1).length()-2,rs.getString(1).length());
                        if(extraer!=contar && contar<=99 && contar>=10){
                            ItemId = "SVQSAC-0"+contar;
                            break;
                        }else if(ceros.equals("00")){
                            extraer = Integer.parseInt(rs.getString(1).substring(rs.getString(1).length()-3,rs.getString(1).length()));
                            if(extraer!=contar && contar<=999 && contar>=100){
                                ItemId = "SVQSAC-" + contar;
                                break;
                            }
                        }
                    }
                    contar++;
                }while (rs.next());

                if(contar<=9 && extraer!=contar){
                    ItemId = "SVQSAC-00"+contar;
                }else if(contar>=10 && contar<=99 && extraer!=contar){
                    ItemId = "SVQSAC-0"+contar;
                }
                else if(contar>=100 && contar<=999 && extraer!=contar) {
                    ItemId = "SVQSAC-" + contar;
                }
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

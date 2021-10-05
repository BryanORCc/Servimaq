package com.example.servimaq.fragments_registros;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.servimaq.R;
import com.example.servimaq.db.SQLConexion;
import com.example.servimaq.db.items_lista_salida_P;
import com.example.servimaq.producto_catalogo;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Salida_Prod extends AppCompatActivity {

    EditText etstock;
    Button btnaceptar, btneliminar;
    TextView tventrega, tvmodopago, tvPrecio,tvnombre, tvcod;
    ListView lvSalidaProducto;
    String codPedido = "";
    String salida;
    //ArrayList<String> op = new ArrayList<>();
  //  ArrayList<String> info = new ArrayList<>();
    items_lista_salida_P  lp;
ArrayList<items_lista_salida_P>lista= new ArrayList<>();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salida_prod);

        tvcod = findViewById(R.id.tvcod);
        tvnombre = findViewById(R.id.tvnombre);
        tventrega = findViewById(R.id.tventrega);
        tvmodopago = findViewById(R.id.tvmodopago);
        tvPrecio = findViewById(R.id.tvPrecio);
        etstock = findViewById(R.id.etstock);
        btnaceptar = findViewById(R.id.btnaceptar);
        btneliminar = findViewById(R.id.btneliminar);


        SQLConexion db = new SQLConexion();

        try {
            Statement st = db.ConexionDB(getApplicationContext()).createStatement();

            ResultSet rs = st.executeQuery("select I.ItemId,I.Cantidad ,I.Precio, I.Total , P.codPedido,P.NombresCliente,P.ApellidosCliente,P.Correo,P.FechaActual,P.FechaEntrega,P.ModoPago,P.DNI " +
                    "FROM T_Listado I INNER  JOIN T_Pedido P ON P.codPedido = I.codPedido  ;");


            if (!rs.next()) {
                codPedido = "";
                Toast.makeText(getApplicationContext(), "No se encontraron registros", Toast.LENGTH_SHORT).show();
            } else {
                do {

                   /* op.add(rs.getString(1));

                    salida = "-NombresCliente: " + rs.getString(2) +
                            "\n-FechaEntrega: " + rs.getString(6) +
                            "\n-ModoPago: " + rs.getString(7)+
                            "\n-Precio: " + rs.getString(7);

                  info.add(salida);  */

                    lista.add(new items_lista_salida_P(rs.getString(2), rs.getString(7), rs.getInt(19), rs.getDouble(7)));
                } while (rs.next());


             //   ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, op);


            }

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
      // lp = new items_lista_salida_P(getApplicationContext(),lista);

      //  lvSalidaProducto.setAdapter((ListAdapter) lp);


    }


}









package com.example.servimaq;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.servimaq.db.SQLConexion;
import com.example.servimaq.db.items_lista;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Catalogo extends AppCompatActivity {

    TextView tvMarca, tvAncho, tvDiametro, tvPerfil, tvMmCocada, tvPrecio, tvStock;
    SearchView svBusqueda;
    ListView lvListaProductos;
    ArrayList<items_lista> lista = new ArrayList<>();;
    producto_catalogo prod_catalogo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo);

        svBusqueda = findViewById(R.id.svBusqueda);
        lvListaProductos = findViewById(R.id.lvListaProductos);

        tvMarca = findViewById(R.id.tvMarca);
        tvAncho = findViewById(R.id.tvAncho);
        tvDiametro = findViewById(R.id.tvDiametro);
        tvPerfil = findViewById(R.id.tvPerfil);
        tvMmCocada = findViewById(R.id.tvMmCocada);
        tvPrecio = findViewById(R.id.tvPrecio);
        tvStock = findViewById(R.id.tvStock);



        SQLConexion db = new SQLConexion();

        //--SELECT INFORMACION NEUMATICO------------------------------------------------------------------------------
        try {
            Statement st = db.ConexionDB(getApplicationContext()).createStatement();
            ResultSet rs = st.executeQuery("select L.LlantaId, L.Precio, L.Stock, D.DetalleLlantaId, D.NombreMarca, D.IndiceCarga, D.IndiceVelocidad, D.FotoLlanta, D.Construccion, D.PresionMaxima,"+
                    "D.Clasificacion, D.FechaFabricacion, V.VehiculoId, V.FotoVehiculo, V.MarcaVehiculo, V.ModeloVehiculo, M.MedidaLlantaId, M.Ancho, M.Diametro,"+
                    "M.Perfil, M.MmCocada from T_Llanta L inner join T_DetalleLlanta D on L.DetalleLlantaId = D.DetalleLlantaId "+
                    "inner join T_Vehiculo V on L.VehiculoId = V.VehiculoId inner join T_MedidaLlanta M on M.MedidaLlantaId = D.MedidaLlantaId;");

            if (!rs.next()) {
                Toast.makeText(getApplicationContext(),"No se encontraron registros",Toast.LENGTH_SHORT).show();
            }else {
                do {
                    lista.add(new items_lista(rs.getString(1),rs.getString(5), rs.getInt(18), rs.getInt(19), rs.getInt(20), rs.getInt(21), rs.getDouble(2), rs.getInt(3), rs.getString(8)));
                } while (rs.next());///va agregando cada ID

            }
            rs.close();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }

        prod_catalogo = new producto_catalogo(getApplicationContext(),lista);
        lvListaProductos.setAdapter(prod_catalogo);


    }
}
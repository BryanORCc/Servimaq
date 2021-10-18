package com.example.servimaq.op_catalogo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.servimaq.R;
import com.example.servimaq.db.SQLConexion;
import com.example.servimaq.db.items_lista;
import com.example.servimaq.menu_opciones;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Catalogo extends AppCompatActivity {

    TextView tvMarca, tvAncho, tvDiametro, tvPerfil, tvMmCocada, tvPrecio, tvStock;
    SearchView svBusqueda;
    ListView lvListaProductos;
    ArrayList<items_lista> lista = new ArrayList<>();
    producto_catalogo prod_catalogo;
    String cadena_texto_buscar = null, tipo_busqueda = "codigo", campo_busqueda = null;
    Spinner spTipoBusqueda;
    ArrayList<String> tipos = new ArrayList<>();
    boolean estado = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==event.KEYCODE_BACK){
            finish();
            Intent intent = new Intent(Catalogo.this, menu_opciones.class);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo);

        //QUITAR ANIMACION DE CARGA DE VISTA*****************************
        overridePendingTransition(0, 0);
        overridePendingTransition(0, 0);

        //HABILITAR BOTON - ATRAS - EN LA BARRA DE NAVEGACION************
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        svBusqueda = findViewById(R.id.svBusqueda);
        spTipoBusqueda = findViewById(R.id.spTipoBusqueda);
        lvListaProductos = findViewById(R.id.lvListaProductos);

        tvMarca = findViewById(R.id.tvMarca);
        tvAncho = findViewById(R.id.tvAncho);
        tvDiametro = findViewById(R.id.tvDiametro);
        tvPerfil = findViewById(R.id.tvPerfil);
        tvMmCocada = findViewById(R.id.tvMmCocada);
        tvPrecio = findViewById(R.id.tvPrecio);
        tvStock = findViewById(R.id.tvStock);


        //ELEGIR TIPO DE BUSQUEDA--------------------------------------------------------------------------------------------
        tipos.add(0,"Seleccionar busqueda por...");
        tipos.add("codigo");
        tipos.add("marca");

        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, tipos);
        adapter.setDropDownViewResource(R.layout.design_spinner);
        spTipoBusqueda.setAdapter(adapter);

        spTipoBusqueda.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tipo_busqueda = tipos.get(i);
                if(tipo_busqueda.equalsIgnoreCase("Seleccionar busqueda por...")){
                    campo_busqueda = "L.LlantaId";
                    svBusqueda.setQueryHint("ingresar codigo...");
                }else if(tipo_busqueda.equalsIgnoreCase("codigo")){
                    campo_busqueda = "L.LlantaId";
                    svBusqueda.setQueryHint("ingresar codigo...");
                }else if(tipo_busqueda.equalsIgnoreCase("marca")){
                    campo_busqueda = "D.NombreMarca";
                    svBusqueda.setQueryHint("ingresar marca...");
                }
                //Toast.makeText(getApplicationContext(),tipo_busqueda,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //INICIAR CONEXION A LA DB--------------------------
        SQLConexion db = new SQLConexion();

        //CONDICION PARA MOSTRAR DATOS-------------------------------------------------------------------------------------------------------
        if(cadena_texto_buscar==null){
            //--SELECT INFORMACION NEUMATICO------------------------------------------------------------------------------
            try {
                Statement st = db.ConexionDB(getApplicationContext()).createStatement();
                ResultSet rs = st.executeQuery("select L.LlantaId, L.Precio, L.Stock, D.DetalleLlantaId, D.NombreMarca, D.IndiceCarga, D.IndiceVelocidad, D.FotoLlanta, D.Construccion, D.PresionMaxima,"+
                        "D.Clasificacion, D.FechaFabricacion, V.VehiculoId, V.FotoVehiculo, V.MarcaVehiculo, V.ModeloVehiculo, M.MedidaLlantaId, M.Ancho, M.Diametro,"+
                        "M.Perfil, M.MmCocada, V.TipoVehiculo from T_Llanta L inner join T_DetalleLlanta D on L.DetalleLlantaId = D.DetalleLlantaId "+
                        "inner join T_Vehiculo V on L.VehiculoId = V.VehiculoId inner join T_MedidaLlanta M on M.MedidaLlantaId = D.MedidaLlantaId;");

                if (!rs.next()) {
                    Toast.makeText(getApplicationContext(),"No se encontraron registros",Toast.LENGTH_SHORT).show();
                }else {
                    do {
                        lista.add(new items_lista(rs.getString(1), rs.getString(5), rs.getString(6),rs.getString(7), rs.getString(9),
                                rs.getString(11), rs.getString(12), rs.getString(8), rs.getString(14), rs.getString(15), rs.getString(16),
                                rs.getInt(18), rs.getInt(19), rs.getInt(20), rs.getInt(21), rs.getInt(3), rs.getInt(10), rs.getDouble(2),
                                rs.getString(22), rs.getString(4), rs.getString(13), rs.getString(17)));
                    } while (rs.next());///va agregando cada ID
                }
                rs.close();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }//FIN SELECT-------------

            prod_catalogo = new producto_catalogo(Catalogo.this,lista);
            lvListaProductos.setAdapter(prod_catalogo);

        }else{ //Uso del buscador---------------------------------------------
            prod_catalogo = new producto_catalogo(Catalogo.this,lista);
            lvListaProductos.setAdapter(prod_catalogo);
        }

        //EXPANDIR CUADRO DE BUSQUEDA*********************************************
        svBusqueda.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spTipoBusqueda.setVisibility(View.GONE);
            }
        });

        //CERRAR CUADRO DE BUSQUEDA*********************************************
        svBusqueda.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                spTipoBusqueda.setVisibility(View.VISIBLE);
                return false;
            }
        });

        //BUSCAR POR INGRESO DE TEXTO
        svBusqueda.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String texto_buscar) {
                cadena_texto_buscar = texto_buscar;
                return true;
            }

            @Override
            public boolean onQueryTextChange(String texto_buscar) {
                lvListaProductos.setAdapter(null);
                lista.clear();
                cadena_texto_buscar = texto_buscar;
                Toast.makeText(getApplicationContext(),texto_buscar,Toast.LENGTH_SHORT).show();
                //--SELECT INFORMACION NEUMATICO------------------------------------------------------------------------------
                try {
                    Statement st = db.ConexionDB(getApplicationContext()).createStatement();
                    ResultSet rs = st.executeQuery("select L.LlantaId, L.Precio, L.Stock, D.DetalleLlantaId, D.NombreMarca, D.IndiceCarga, D.IndiceVelocidad, D.FotoLlanta, D.Construccion, D.PresionMaxima,"+
                            "D.Clasificacion, D.FechaFabricacion, V.VehiculoId, V.FotoVehiculo, V.MarcaVehiculo, V.ModeloVehiculo, M.MedidaLlantaId, M.Ancho, M.Diametro,"+
                            "M.Perfil, M.MmCocada, V.TipoVehiculo from T_Llanta L inner join T_DetalleLlanta D on L.DetalleLlantaId = D.DetalleLlantaId "+
                            "inner join T_Vehiculo V on L.VehiculoId = V.VehiculoId inner join T_MedidaLlanta M on M.MedidaLlantaId = D.MedidaLlantaId "+
                            "where "+ campo_busqueda +" like '%"+ cadena_texto_buscar +"%';");

                    if (!rs.next()) {
                        Toast.makeText(getApplicationContext(),"No se encontraron registros",Toast.LENGTH_SHORT).show();
                    }else {
                        do {
                            lista.add(new items_lista(rs.getString(1), rs.getString(5), rs.getString(6),rs.getString(7), rs.getString(9),
                                    rs.getString(11), rs.getString(12), rs.getString(8), rs.getString(14), rs.getString(15), rs.getString(16),
                                    rs.getInt(18), rs.getInt(19), rs.getInt(20), rs.getInt(21), rs.getInt(3), rs.getInt(10), rs.getDouble(2),
                                    rs.getString(22), rs.getString(4), rs.getString(13), rs.getString(17)));
                        } while (rs.next());///va agregando cada ID

                    }
                    rs.close();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }//FIN SELECT-------------

                prod_catalogo = new producto_catalogo(Catalogo.this,lista);
                lvListaProductos.setAdapter(prod_catalogo);


                return true;
            }

        });


    }
}
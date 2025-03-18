package com.example.miprimeraaplicacion;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import java.util.ArrayList;
import android.database.Cursor;

public class MainActivity extends AppCompatActivity {

    EditText etNombre, etPrecio, etCodigo, etDescripcion, etMarca, etPresentacion, etBuscar, etCosto, etStock;
    Button btnAgregar, btnModificar, btnEliminar, btnBuscar;
    ListView listViewProductos;
    ImageView imgProducto;

    DBHelper dbHelper;
    ArrayAdapter<String> adapter;
    ArrayList<String> lista;
    ArrayList<Integer> listaIds;
    int productoSeleccionadoId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);

        etNombre = findViewById(R.id.etNombre);
        etPrecio = findViewById(R.id.etPrecio);
        etCodigo = findViewById(R.id.etCodigo);
        etDescripcion = findViewById(R.id.etDescripcion);
        etMarca = findViewById(R.id.etMarca);
        etPresentacion = findViewById(R.id.etPresentacion);
        etBuscar = findViewById(R.id.etBuscar);
        etCosto = findViewById(R.id.etCosto);
        etStock = findViewById(R.id.etStock);

        btnAgregar = findViewById(R.id.btnAgregar);
        btnModificar = findViewById(R.id.btnModificar);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnBuscar = findViewById(R.id.btnBuscar);

        imgProducto = findViewById(R.id.imgProducto);
        listViewProductos = findViewById(R.id.listViewProductos);

        lista = new ArrayList<>();
        listaIds = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lista);
        listViewProductos.setAdapter(adapter);

        cargarProductos();

        btnAgregar.setOnClickListener(v -> {

            String nombre = etNombre.getText().toString();
            String precioStr = etPrecio.getText().toString();
            String costoStr = etCosto.getText().toString();
            String stockStr = etStock.getText().toString();
            String codigo = etCodigo.getText().toString();
            String descripcion = etDescripcion.getText().toString();
            String marca = etMarca.getText().toString();
            String presentacion = etPresentacion.getText().toString();



            if (nombre.isEmpty() || precioStr.isEmpty() || costoStr.isEmpty() || stockStr.isEmpty()) {
                Toast.makeText(this, "Ingrese nombre, precio, costo y stock", Toast.LENGTH_SHORT).show();
                return;
            }

            double precio = Double.parseDouble(precioStr);
            double costo = Double.parseDouble(costoStr);
            int stock = Integer.parseInt(stockStr);
            double ganancia = precio - costo;

            boolean insertado = dbHelper.insertarProducto(codigo, nombre, descripcion, marca, presentacion, precio, "", costo, ganancia, stock);
            if (insertado) {

                CouchDBHelper.subirProductoACouchDB(
                        codigo, nombre, descripcion, marca, presentacion,
                        precio, costo, ganancia, stock
                );

                Toast.makeText(this, "Producto agregado", Toast.LENGTH_SHORT).show();
                limpiarCampos();
                cargarProductos();

            } else {
                Toast.makeText(this, "Error al agregar", Toast.LENGTH_SHORT).show();
            }


        });


        btnModificar.setOnClickListener(v -> {
            if (productoSeleccionadoId == -1) {
                Toast.makeText(this, "Seleccione un producto", Toast.LENGTH_SHORT).show();
                return;
            }

            String nombre = etNombre.getText().toString();
            String precioStr = etPrecio.getText().toString();
            String costoStr = etCosto.getText().toString();
            String stockStr = etStock.getText().toString();
            String codigo = etCodigo.getText().toString();
            String descripcion = etDescripcion.getText().toString();
            String marca = etMarca.getText().toString();
            String presentacion = etPresentacion.getText().toString();

            if (nombre.isEmpty() || precioStr.isEmpty() || costoStr.isEmpty() || stockStr.isEmpty()) {
                Toast.makeText(this, "Ingrese nombre, precio, costo y stock", Toast.LENGTH_SHORT).show();
                return;
            }

            double precio = Double.parseDouble(precioStr);
            double costo = Double.parseDouble(costoStr);
            int stock = Integer.parseInt(stockStr);
            double ganancia = precio - costo;

            boolean modificado = dbHelper.modificarProducto(productoSeleccionadoId, codigo, nombre, descripcion, marca, presentacion, precio, "", costo, ganancia, stock);
            if (modificado) {
                Toast.makeText(this, "Producto modificado", Toast.LENGTH_SHORT).show();
                limpiarCampos();
                cargarProductos();
            } else {
                Toast.makeText(this, "Error al modificar", Toast.LENGTH_SHORT).show();
            }
        });

        btnEliminar.setOnClickListener(v -> {
            if (productoSeleccionadoId == -1) {
                Toast.makeText(this, "Seleccione un producto", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean eliminado = dbHelper.eliminarProducto(productoSeleccionadoId);
            if (eliminado) {
                Toast.makeText(this, "Producto eliminado", Toast.LENGTH_SHORT).show();
                limpiarCampos();
                cargarProductos();
            } else {
                Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show();
            }
        });

        listViewProductos.setOnItemClickListener((adapterView, view, i, l) -> {
            productoSeleccionadoId = listaIds.get(i);
            Cursor cursor = dbHelper.obtenerProductos();
            if (cursor.moveToPosition(i)) {
                etCodigo.setText(cursor.getString(cursor.getColumnIndexOrThrow("codigo")));
                etNombre.setText(cursor.getString(cursor.getColumnIndexOrThrow("nombre")));
                etDescripcion.setText(cursor.getString(cursor.getColumnIndexOrThrow("descripcion")));
                etMarca.setText(cursor.getString(cursor.getColumnIndexOrThrow("marca")));
                etPresentacion.setText(cursor.getString(cursor.getColumnIndexOrThrow("presentacion")));
                etPrecio.setText(cursor.getString(cursor.getColumnIndexOrThrow("precio")));
                etCosto.setText(cursor.getString(cursor.getColumnIndexOrThrow("costo")));
                etStock.setText(cursor.getString(cursor.getColumnIndexOrThrow("stock")));
            }
            cursor.close();
        });

        btnBuscar.setOnClickListener(v -> {
            String buscar = etBuscar.getText().toString();
            Cursor cursor = dbHelper.buscarProducto(buscar);
            cargarDesdeCursor(cursor);
        });
    }

    private void cargarProductos() {
        Cursor cursor = dbHelper.obtenerProductos();
        cargarDesdeCursor(cursor);
    }

    private void cargarDesdeCursor(Cursor cursor) {
        lista.clear();
        listaIds.clear();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                String precio = cursor.getString(cursor.getColumnIndexOrThrow("precio"));
                lista.add(nombre + " - $" + precio);
                listaIds.add(id);
            } while (cursor.moveToNext());
        }
        adapter.notifyDataSetChanged();
        cursor.close();
    }

    private void limpiarCampos() {
        etNombre.setText("");
        etPrecio.setText("");
        etCodigo.setText("");
        etDescripcion.setText("");
        etMarca.setText("");
        etPresentacion.setText("");
        etCosto.setText("");
        etStock.setText("");
        productoSeleccionadoId = -1;
    }
}

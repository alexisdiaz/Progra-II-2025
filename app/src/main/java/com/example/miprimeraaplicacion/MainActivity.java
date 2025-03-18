package com.example.miprimeraaplicacion;


import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Build;
import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 100;
    private DBHelper dbHelper;
    private EditText etNombre, etPrecio, etBuscar;
    private Button btnAgregar, btnBuscar, btnEliminar, btnModificar;
    private ListView listViewProductos;
    private ImageView imgProducto;
    private String imagenUri = "";
    private int selectedId = -1;
    private ArrayAdapter<String> adapter;
    private List<String> productosList = new ArrayList<>();
    private List<Producto> listaProductos = new ArrayList<>();


    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri uri = result.getData().getData();
                    if (uri != null) {

                        getContentResolver().takePersistableUriPermission(
                                uri,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                        );


                        imagenUri = uri.toString();
                        imgProducto.setImageURI(uri);
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermissions();

        dbHelper = new DBHelper(this);
        etNombre = findViewById(R.id.etNombre);
        etPrecio = findViewById(R.id.etPrecio);
        etBuscar = findViewById(R.id.etBuscar);
        btnAgregar = findViewById(R.id.btnAgregar);
        btnBuscar = findViewById(R.id.btnBuscar);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnModificar = findViewById(R.id.btnModificar);
        listViewProductos = findViewById(R.id.listViewProductos);
        imgProducto = findViewById(R.id.imgProducto);

        imgProducto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });
        cargarListaProductos();

        listViewProductos.setOnItemClickListener((parent, view, position, id) -> {
            Producto productoSeleccionado = listaProductos.get(position);
            selectedId = productoSeleccionado.getId();
            etNombre.setText(productoSeleccionado.getNombre());
            etPrecio.setText(String.valueOf(productoSeleccionado.getPrecio()));
            imagenUri = productoSeleccionado.getImagen();
            if (imagenUri != null && !imagenUri.isEmpty()) {
                Uri uri = Uri.parse(imagenUri);


                try {
                    getContentResolver().takePersistableUriPermission(
                            uri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    );
                    imgProducto.setImageURI(uri);
                } catch (SecurityException e) {

                    Toast.makeText(this, "No tienes permisos para acceder a esta imagen", Toast.LENGTH_SHORT).show();
                    imgProducto.setImageResource(android.R.color.transparent); // Limpiar la imagen
                }
            } else {
                imgProducto.setImageResource(android.R.color.transparent);
            }
        });



        btnAgregar.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString();
            String precioStr = etPrecio.getText().toString();

            if (nombre.isEmpty() || precioStr.isEmpty()) {
                Toast.makeText(this, "Ingrese nombre y precio", Toast.LENGTH_SHORT).show();
                return;
            }
            double precio = Double.parseDouble(precioStr);
            boolean insertado = dbHelper.insertarProducto(nombre, precio, imagenUri);
            if (insertado) {
                Toast.makeText(this, "Producto agregado", Toast.LENGTH_SHORT).show();
                limpiarCampos();
                cargarListaProductos();
            }
        });

        btnModificar.setOnClickListener(v -> {
            if (selectedId == -1) {
                Toast.makeText(this, "Seleccione un producto", Toast.LENGTH_SHORT).show();
                return;
            }
            String nombre = etNombre.getText().toString();
            String precioStr = etPrecio.getText().toString();
            if (nombre.isEmpty() || precioStr.isEmpty()) {
                Toast.makeText(this, "Ingrese nombre y precio", Toast.LENGTH_SHORT).show();
                return;
            }
            double precio = Double.parseDouble(precioStr);
            boolean actualizado = dbHelper.modificarProducto(selectedId, nombre, precio, imagenUri);
            if (actualizado) {
                Toast.makeText(this, "Producto modificado", Toast.LENGTH_SHORT).show();
                limpiarCampos();
                cargarListaProductos();
            }
        });

        btnEliminar.setOnClickListener(v -> {
            if (selectedId == -1) {
                Toast.makeText(this, "Seleccione un producto", Toast.LENGTH_SHORT).show();
                return;
            }
            boolean eliminado = dbHelper.eliminarProducto(selectedId);
            if (eliminado) {
                Toast.makeText(this, "Producto eliminado", Toast.LENGTH_SHORT).show();
                limpiarCampos();
                cargarListaProductos();
            }
        });

        btnBuscar.setOnClickListener(v -> {
            String textoBuscar = etBuscar.getText().toString().trim();
            if (textoBuscar.isEmpty()) {
                cargarListaProductos();
            } else {
                buscarProducto(textoBuscar);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido
                Toast.makeText(this, "Permiso concedido", Toast.LENGTH_SHORT).show();
            } else {
                // Permiso denegado
                Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_READ_EXTERNAL_STORAGE);
            }
        }
    }
    private void cargarListaProductos() {
        listaProductos.clear();
        productosList.clear();

        Cursor cursor = dbHelper.obtenerProductos();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String nombre = cursor.getString(1);
            double precio = cursor.getDouble(2);
            String imagen = cursor.getString(3);
            listaProductos.add(new Producto(id, nombre, precio, imagen));
            productosList.add(nombre + " - $" + precio);
        }
        cursor.close();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, productosList);
        listViewProductos.setAdapter(adapter);
    }

    private void buscarProducto(String nombre) {
        listaProductos.clear();
        productosList.clear();

        Cursor cursor = dbHelper.buscarProducto(nombre);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String nombreProducto = cursor.getString(1);
            double precio = cursor.getDouble(2);
            String imagen = cursor.getString(3);
            listaProductos.add(new Producto(id, nombreProducto, precio, imagen));
            productosList.add(nombreProducto + " - $" + precio);
        }
        cursor.close();

        adapter.notifyDataSetChanged();
    }

    private void limpiarCampos() {
        etNombre.setText("");
        etPrecio.setText("");
        selectedId = -1;
        imagenUri = "";
        imgProducto.setImageResource(android.R.color.transparent);
    }
}


class Producto {
    private int id;
    private String nombre;
    private double precio;
    private String imagen;

    public Producto(int id, String nombre, double precio, String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.imagen = imagen;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public String getImagen() {
        return imagen;
    }

}

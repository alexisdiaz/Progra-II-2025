package com.example.miprimeraaplicacion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "oscar.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_PRODUCTOS = "alexis";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CODIGO = "codigo";
    public static final String COLUMN_NOMBRE = "nombre";
    public static final String COLUMN_DESCRIPCION = "descripcion";
    public static final String COLUMN_MARCA = "marca";
    public static final String COLUMN_PRESENTACION = "presentacion";
    public static final String COLUMN_PRECIO = "precio";
    public static final String COLUMN_IMAGEN = "imagen";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_PRODUCTOS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "codigo TEXT, " +
                    COLUMN_NOMBRE + " TEXT, " +
                    "descripcion TEXT, " +
                    "marca TEXT, " +
                    "presentacion TEXT, " +
                    "precio REAL, " +
                    "costo REAL, " +
                    "ganancia REAL, " +
                    "stock INTEGER, " +
                    COLUMN_IMAGEN + " TEXT);";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTOS);
        onCreate(db);
    }

    public boolean insertarProducto(String codigo, String nombre, String descripcion, String marca, String presentacion, double precio, String imagen, double costo, double ganancia, int stock) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("codigo", codigo);
        values.put("nombre", nombre);
        values.put("descripcion", descripcion);
        values.put("marca", marca);
        values.put("presentacion", presentacion);
        values.put("precio", precio);
        values.put("imagen", imagen);
        values.put("costo", costo);
        values.put("ganancia", ganancia);
        values.put("stock", stock);

        long result = db.insert("alexis", null, values);
        return result != -1;
    }



    public Cursor obtenerProductos() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PRODUCTOS, null);
    }

    public boolean eliminarProducto(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_PRODUCTOS, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return result > 0;
    }

    public boolean modificarProducto(int id, String codigo, String nombre, String descripcion, String marca, String presentacion, double precio, String imagen, double costo, double ganancia, int stock) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("codigo", codigo);
        values.put("nombre", nombre);
        values.put("descripcion", descripcion);
        values.put("marca", marca);
        values.put("presentacion", presentacion);
        values.put("precio", precio);
        values.put("imagen", imagen);
        values.put("costo", costo);
        values.put("ganancia", ganancia);
        values.put("stock", stock);

        int rowsAffected = db.update("alexis", values, "id = ?", new String[]{String.valueOf(id)});
        return rowsAffected > 0;
    }



    public Cursor buscarProducto(String nombre) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PRODUCTOS + " WHERE " + COLUMN_NOMBRE + " LIKE ?", new String[]{"%" + nombre + "%"});
    }
}

package com.example.miprimeraaplicacion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Tienda.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_PRODUCTOS = "productos";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NOMBRE = "nombre";
    public static final String COLUMN_PRECIO = "precio";
    public static final String COLUMN_IMAGEN = "imagen";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_PRODUCTOS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NOMBRE + " TEXT, " +
                    COLUMN_PRECIO + " REAL, " +
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

    public boolean insertarProducto(String nombre, double precio, String imagen) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE, nombre);
        values.put(COLUMN_PRECIO, precio);
        values.put(COLUMN_IMAGEN, imagen);
        long result = db.insert(TABLE_PRODUCTOS, null, values);
        db.close();
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

    public boolean modificarProducto(int id, String nombre, double precio, String imagen) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE, nombre);
        values.put(COLUMN_PRECIO, precio);
        values.put(COLUMN_IMAGEN, imagen);
        int result = db.update(TABLE_PRODUCTOS, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
        return result > 0;
    }
    public Cursor buscarProducto(String nombre) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PRODUCTOS + " WHERE " + COLUMN_NOMBRE + " LIKE ?", new String[]{"%" + nombre + "%"});
    }
}
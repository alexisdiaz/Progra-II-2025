package com.example.miprimeraaplicacion;



public class Producto {
    private int id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private String marca;
    private String presentacion;
    private double precio;
    private String imagen;

    public Producto(int id, String codigo, String nombre, String descripcion, String marca, String presentacion, double precio, String imagen) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.marca = marca;
        this.presentacion = presentacion;
        this.precio = precio;
        this.imagen = imagen;
    }

    // Getters y setters
    public int getId() { return id; }
    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public String getMarca() { return marca; }
    public String getPresentacion() { return presentacion; }
    public double getPrecio() { return precio; }
    public String getImagen() { return imagen; }

    public void setId(int id) { this.id = id; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setMarca(String marca) { this.marca = marca; }
    public void setPresentacion(String presentacion) { this.presentacion = presentacion; }
    public void setPrecio(double precio) { this.precio = precio; }
    public void setImagen(String imagen) { this.imagen = imagen; }
}

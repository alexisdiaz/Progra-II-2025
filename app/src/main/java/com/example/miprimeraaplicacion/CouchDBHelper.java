package com.example.miprimeraaplicacion;
import okhttp3.*;

import org.json.JSONObject;

import java.io.IOException;

public class CouchDBHelper {
    private static final String COUCHDB_URL = "http://admin:jhin@172.20.10.2:5984/oscar";
    private static final OkHttpClient client = new OkHttpClient();

    public static void subirProductoACouchDB(String codigo, String nombre, String descripcion, String marca,
                                             String presentacion, double precio, double costo, double ganancia,
                                             int stock) {
        JSONObject json = new JSONObject();
        try {
            json.put("codigo", codigo);
            json.put("nombre", nombre);
            json.put("descripcion", descripcion);
            json.put("marca", marca);
            json.put("presentacion", presentacion);
            json.put("precio", precio);
            json.put("costo", costo);
            json.put("ganancia", ganancia);
            json.put("stock", stock);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        RequestBody body = RequestBody.create(
                json.toString(),
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(COUCHDB_URL)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace(); // Muestra error en consola
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    System.out.println("Error: " + response.body().string());
                }
            }
        });
    }
}

package com.example.miprimeraaplicacion;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TabHost tbh;
    Button btn;
    TextView tempVal;
    Spinner spnDe, spnA;
    Conversores objConversores = new Conversores();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tbh = findViewById(R.id.tbhConversor);
        tbh.setup();

        tbh.addTab(tbh.newTabSpec("Monedas").setContent(R.id.tabMonedas).setIndicator("MONEDAS", null));
        tbh.addTab(tbh.newTabSpec("Longitud").setContent(R.id.tabLongitud).setIndicator("LONGITUD", null));
        tbh.addTab(tbh.newTabSpec("Tiempo").setContent(R.id.tabTiempo).setIndicator("TIEMPO", null));
        tbh.addTab(tbh.newTabSpec("Almacenamiento").setContent(R.id.tabAlmacenamiento).setIndicator("ALMACENAMIENTO", null));
        tbh.addTab(tbh.newTabSpec("Masa").setContent(R.id.tabMasa).setIndicator("MASA", null));
        tbh.addTab(tbh.newTabSpec("Volumen").setContent(R.id.tabVolumen).setIndicator("VOLUMEN", null));
        tbh.addTab(tbh.newTabSpec("Transferencia").setContent(R.id.tabTransferencia).setIndicator("TRANSFERENCIA", null));

        // Configurar Spinner según la pestaña
        actualizarSpinners(0);

        tbh.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                int tabIndex = tbh.getCurrentTab();
                actualizarSpinners(tabIndex);
            }
        });

        btn = findViewById(R.id.btnCalcular);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int opcion = tbh.getCurrentTab();
                int de = spnDe.getSelectedItemPosition();
                int a = spnA.getSelectedItemPosition();

                tempVal = findViewById(R.id.txtCantidad);
                double cantidad = Double.parseDouble(tempVal.getText().toString());

                tempVal = findViewById(R.id.lblRespuesta);
                double respuesta = objConversores.convertir(opcion, de, a, cantidad);
                tempVal.setText("Respuesta: " + respuesta);
            }
        });
    }

    private void actualizarSpinners(int tabIndex) {
        switch (tabIndex) {
            case 0: // Monedas
                spnDe = findViewById(R.id.spnDeMonedas);
                spnA = findViewById(R.id.spnAMonedas);
                break;
            case 1: // Longitud
                spnDe = findViewById(R.id.spnDeLongitud);
                spnA = findViewById(R.id.spnALongitud);
                break;
            case 2: // Tiempo
                spnDe = findViewById(R.id.spnDeTiempo);
                spnA = findViewById(R.id.spnATiempo);
                break;
            case 3: // Almacenamiento
                spnDe = findViewById(R.id.spnDeAlmacenamiento);
                spnA = findViewById(R.id.spnAAlmacenamiento);
                break;
            case 4: // Masa
                spnDe = findViewById(R.id.spnDeMasa);
                spnA = findViewById(R.id.spnAMasa);
                break;
            case 5: // Volumen
                spnDe = findViewById(R.id.spnDeVolumen);
                spnA = findViewById(R.id.spnAVolumen);
                break;
            case 6: // Transferencia
                spnDe = findViewById(R.id.spnDeTransferencia);
                spnA = findViewById(R.id.spnATransferencia);
                break;
        }
    }
}

class Conversores {
    double[][] valores = {
            {1, 0.98, 20.63, 8.76, 5.78, 4130.34, 1.43, 1.60, 0.81, 151.98}, // Monedas (USD, EUR, MXN, Colon SV, BRL, COP, CAD, AUD, GBP, JPY)
            {1, 100, 39.37, 3.28, 0.001, 0.000621, 1093.61, 3280.84, 39370.1, 100000}, // Longitud (m, cm, in, ft, km, mi, yd, ft, in, mm)
            {1, 0.0166667, 0.000277778, 0.00001157407407407407, 0.00000165343910817046, 0.000000386, 0.000000031,  1.000,  1.000000, 1.000000000}, // Tiempo (s, min, hr, día, semana, mes, año, milisegundo, microsegundo, nanosegundo)
            {1, 1024, 1048576, 1073741824, 1.0995e+12, 1.1259e+15, 1.1529e+18, 1.1806e+21, 1.2089e+24}, // Almacenamiento (B, KB, MB, GB, TB, PB, EB, ZB, YB, BB)
            {1, 1000, 2.20462, 35.274, 0.001, 0.000157, 0.000984, 0.005, 0.002204, 0.035274}, // Masa (kg, g, lb, oz, tonelada, quilate, stone, quintal, arroba, libra troy)
            {1, 1000, 33.814, 67.628, 16.907, 4.2268, 1.057, 0.2642, 0.0353, 0.001}, // Volumen (L, mL, fl oz, cup, pt, qt, gal, barril, cucharada, cm³)
            {1, 8, 1024, 1048576, 1073741824, 1.0995e+12, 1.1259e+15, 1.1529e+18, 1.1806e+21, 1.2089e+24} // Transferencia (bps, Bps, Kbps, Mbps, Gbps, Tbps, Pbps, Ebps, Zbps, Ybps)
    };

    public double convertir(int opcion, int de, int a, double cantidad) {
        return valores[opcion][a] / valores[opcion][de] * cantidad;
    }
}

package com.example.miprimeraaplicacion;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button btn;
    TextView tempVal;
    Spinner spinnerOpciones;
    String[] operaciones = {"Suma", "Resta", "Multiplicación", "División", "Factorial", "Porcentaje", "Exponenciación", "Raíz Cuadrada", "Raíz Cúbica", "Raíz N", "Módulo", "Mayor"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerOpciones = findViewById(R.id.spinnerOpciones);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, operaciones);
        spinnerOpciones.setAdapter(adapter);

        btn = findViewById(R.id.btnCalcular);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempVal = findViewById(R.id.txtNum1);
                double num1 = Double.parseDouble(tempVal.getText().toString());

                tempVal = findViewById(R.id.txtNum2);
                double num2 = 0;
                if (!tempVal.getText().toString().isEmpty()) {
                    num2 = Double.parseDouble(tempVal.getText().toString());
                }

                double respuesta = 0;
                int selectedIndex = spinnerOpciones.getSelectedItemPosition();

                switch (selectedIndex) {
                    case 0:
                        respuesta = num1 + num2;
                        break;
                    case 1:
                        respuesta = num1 - num2;
                        break;
                    case 2:
                        respuesta = num1 * num2;
                        break;
                    case 3:
                        respuesta = (num2 != 0) ? num1 / num2 : Double.NaN;
                        break;
                    case 4:
                        respuesta = factorial((int) num1);
                        break;
                    case 5:
                        respuesta = (num1 * num2) / 100;
                        break;
                    case 6:
                        respuesta = Math.pow(num1, num2);
                        break;
                    case 7:
                        respuesta = Math.sqrt(num1);
                        break;
                    case 8:
                        respuesta = Math.cbrt(num1);
                        break;
                    case 9:
                        respuesta = Math.pow(num1, 1.0 / num2);
                        break;
                    case 10:
                        respuesta = num1 % num2;
                        break;
                    case 11:
                        respuesta = Math.max(num1, num2);
                        break;
                }

                tempVal = findViewById(R.id.lblNumRespuesta);
                tempVal.setText("Respuesta: " + respuesta);
            }
        });
    }

    private int factorial(int n) {
        if (n < 0) return -1;
        int resultado = 1;
        for (int i = 1; i <= n; i++) {
            resultado *= i;
        }
        return resultado;
    }
}

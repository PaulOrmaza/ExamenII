package com.spoj.examenii;


import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private TextView txtdatos;
    private TextView textViewDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtdatos = findViewById(R.id.txtdatos);
        textViewDescription = findViewById(R.id.text_view_description);

        // Llamando al método para realizar la solicitud al servicio web
        new ConsultarClimaTask().execute("https://api.openweathermap.org/data/2.5/forecast?lat=-5.206535001080812&lon=-80.6186554254348&appid=bd5e378503939ddaee76f12ad7a97608");
    }

    private class ConsultarClimaTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String resultado = "";
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(urls[0])
                    .build();
            try {
                Response response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    resultado = response.body().string();
                } else {
                    resultado = "Error: " + response.code() + " " + response.message();
                }
            } catch (IOException e) {
                e.printStackTrace();
                resultado = "Error: " + e.getMessage();
            }
            return resultado;
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);
            try {
                // Parsear el JSON para obtener la descripción del clima correspondiente a la fecha específica
                JSONObject jsonObject = new JSONObject(resultado);
                JSONArray list = jsonObject.getJSONArray("list");
                for (int i = 0; i < list.length(); i++) {
                    JSONObject item = list.getJSONObject(i);
                    String dt_txt = item.getString("dt_txt");
                    if (dt_txt.contains("2024-02-24")) {
                        JSONArray weatherArray = item.getJSONArray("weather");
                        JSONObject weatherObject = weatherArray.getJSONObject(0);
                        String descripcionClima = weatherObject.getString("description");

                        // Mostrar la descripción del clima en el TextView
                        txtdatos.setText("Descripción del clima para el 2024-02-24: " + descripcionClima);
                        break; 
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                txtdatos.setText("Error al obtener los datos del clima.");
            }
        }
    }
}


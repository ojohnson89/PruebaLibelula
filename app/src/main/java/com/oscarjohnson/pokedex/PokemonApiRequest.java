package com.oscarjohnson.pokedex;
import android.os.AsyncTask;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PokemonApiRequest extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... params) {
        // URL de la API de Pokémon (por ejemplo, para obtener el Pokémon con el ID 1)
        String apiUrl = "https://pokeapi.co/api/v2/pokemon/" + params[0] + "/";

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Establecer el método de solicitud
            connection.setRequestMethod("GET");

            // Leer la respuesta de la API
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            // Cerrar la conexión
            connection.disconnect();

            // Devolver la respuesta como una cadena JSON
            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}

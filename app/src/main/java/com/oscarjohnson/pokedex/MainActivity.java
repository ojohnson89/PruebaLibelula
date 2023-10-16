package com.oscarjohnson.pokedex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    NestedScrollView nsvVista;
    RecyclerView rvPokemons;
    ProgressBar pgCargando;

    // Declarar una lista para almacenar los datos de Pokémon
    static List<String> pokeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new NetworkRequestTask().execute();

        nsvVista = findViewById(R.id.nsvVista);
        rvPokemons = findViewById(R.id.rvPokemons);
        rvPokemons.setLayoutManager(new GridLayoutManager(this, 1));
        pgCargando = findViewById(R.id.pbCargando);

        // Inicialmente, obtén los primeros Pokémon
        // getPokemons(0, 20, rvPokemons, this);

        nsvVista.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(@NonNull NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    // Incrementa la página para obtener más Pokémon
                    int page = (pokeList.size() / 20) + 1;

                    pgCargando.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Obtén más Pokémon a medida que el usuario se desplaza hacia abajo
                            getPokemons(page * 20, 20, rvPokemons, MainActivity.this);
                        }
                    }, 3000);
                }
            }
        });
    }

    private class NetworkRequestTask extends AsyncTask<Void, Void, List<String>> {
        @Override
        protected List<String> doInBackground(Void... voids) {
            // Realiza la solicitud de red aquí y devuelve el resultado
            return consultaApiPokemon(0, 40);
        }

        @Override
        protected void onPostExecute(List<String> result) {
            // Procesa el resultado de la solicitud aquí
            pokeList.addAll(result);
            rvPokemons.setAdapter(new AdapterPokemon(MainActivity.this, pokeList));
        }
    }

    public static void getPokemons(int offset, int limit, RecyclerView rvPokemons, Context context) {

        List<String> pokemonNames = consultaApiPokemon(offset, limit);
        pokeList.addAll(pokemonNames);
        rvPokemons.setAdapter(new AdapterPokemon(context, pokeList));
    }


    private static List<String> consultaApiPokemon(int offset, int limit) {
        List<String> nombresPokemon = new ArrayList<>();

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://pokeapi.co/api/v2/pokemon?offset=" + offset + "&limit=" + limit)
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String json = response.body().string();
                // Analizar la respuesta JSON utilizando la biblioteca Gson
                Gson gson = new Gson();
                PokemonResponse pokemonResponse = gson.fromJson(json, PokemonResponse.class);

                // Extraer los nombres de los Pokémon de la respuesta y agregarlos a la lista
                for (Pokemon pokemon : pokemonResponse.getPokemonList()) {
                    nombresPokemon.add(pokemon.getName());
                }
            } else {
                Log.e("ErrorTag", "Error en la solicitud HTTP: " + response.code());
            }
        } catch (IOException e) {
            Log.e("ErrorTag", "Excepción al hacer la solicitud HTTP: " + e.getMessage());
            e.printStackTrace();
        }

        Log.d("DebugTag", "Lista de nombres de Pokémon: " + nombresPokemon.toString());
        return nombresPokemon;
    }
    public class PokemonResponse {
        private List<Pokemon> pokemonList;

        public List<Pokemon> getPokemonList() {
            return pokemonList;
        }

        public void setPokemonList(List<Pokemon> pokemonList) {
            this.pokemonList = pokemonList;
        }
    }

    public class Pokemon {
        private String name;
        private int id;
        // Otros campos de información del Pokémon

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        // Otros métodos y campos getters/setters para otros datos del Pokémon
    }
}






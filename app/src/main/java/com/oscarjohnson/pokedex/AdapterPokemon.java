package com.oscarjohnson.pokedex;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AdapterPokemon extends RecyclerView.Adapter<AdapterPokemon.UsuariosViewHolder> {

    Context context;
    List<String> pokeList = new ArrayList<>();

    public AdapterPokemon(Context context, List<String> pokeList){
        this.context = context;
        this.pokeList = pokeList;
    }

    @NonNull
    @Override
    public UsuariosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv_pokemons, parent, false);
        return new UsuariosViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull UsuariosViewHolder holder, int position) {
        holder.tvPokemon.setText(pokeList.get(position));
    }

    @Override
    public int getItemCount() {
        return pokeList.size();
    }
    public class UsuariosViewHolder extends RecyclerView.ViewHolder{

        TextView tvPokemon;

        public UsuariosViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPokemon = itemView.findViewById(R.id.tvPokemon);
        }
    }
}

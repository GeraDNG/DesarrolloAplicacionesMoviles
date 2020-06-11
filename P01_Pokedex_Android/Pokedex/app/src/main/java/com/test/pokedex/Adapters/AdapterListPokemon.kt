package com.test.pokedex.Adapters

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.koushikdutta.ion.Ion
import com.test.pokedex.R

class AdapterListPokemon :RecyclerView.Adapter<AdapterListPokemon.ViewHolder>() {

    private lateinit var context: Context
    private lateinit var data:JsonArray

    fun AdapterListPokemon(context:Context,data:JsonArray){
        this.context = context
        this.data = data
    }

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int ): AdapterListPokemon.ViewHolder {
        var layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_display_pokemon,parent,false))
    }

    override fun getItemCount(): Int {
        return data.size()
    }

    override fun onBindViewHolder(holder: AdapterListPokemon.ViewHolder, position: Int) {
        var item:JsonObject = data.get(position).asJsonObject
        holder.bind(item, context)
    }

    class ViewHolder(view:View) :RecyclerView.ViewHolder(view) {
        private var caracteristicaPokemon: TextView = view.findViewById(R.id.caracteristicaPokemon)
        var aux: String = "";

        fun bind(item:JsonObject, context: Context) {
            caracteristicaPokemon.setText(item.get("move").asJsonObject.get("name").asString.capitalize());
            caracteristicaPokemon.typeface = Typeface.createFromAsset(caracteristicaPokemon.context.assets, "fonts/ArchivoNarrow-Regular.otf")

            /*
            Ion.with(context)
                .load(item.asString)
                .asJsonObject()
                .done { e, result ->
                    if ( e == null ) {
                        if(!result.get("sprites").isJsonNull) {
                            if(result.get("sprites").asJsonObject.get("front_default") != null) {
                                Log.i("Salida", "Holi");
                                caracteristicaNombre.setText("Nombre");
                                caracteristicaPokemon.setText(result.get("name").asString);
                            }
                            else {
                                caracteristicaNombre.setText("Caracteristica");
                                caracteristicaPokemon.setText("Pokemon");
                            }
                        }
                        else {
                            caracteristicaNombre.setText("Caracteristica");
                            caracteristicaPokemon.setText("Pokemon");
                        }
                    }
                    else {
                        caracteristicaNombre.setText("Caracteristica");
                        caracteristicaPokemon.setText("Pokemon")
                    }
                }

             */

        }

    }

}
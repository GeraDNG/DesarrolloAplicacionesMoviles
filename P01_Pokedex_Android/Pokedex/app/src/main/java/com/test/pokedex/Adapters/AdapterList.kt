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
import com.test.pokedex.Activities.ActivityDisplayPokemon
import com.test.pokedex.R
import org.json.JSONObject

class AdapterList :RecyclerView.Adapter<AdapterList.ViewHolder>() {

    private lateinit var context: Context
    private lateinit var data:JsonArray

    fun AdapterList(context:Context,data:JsonArray){
        this.context = context
        this.data = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterList.ViewHolder {
        var layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(layoutInflater.inflate(R.layout.item_list,parent,false))
    }

    override fun getItemCount(): Int {
        return data.size()
    }

    override fun onBindViewHolder(holder: AdapterList.ViewHolder, position: Int) {
        var item:JsonObject = data.get(position).asJsonObject
        holder.bind(item,context)
    }

    class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        private var imagePokemon: ImageView = view.findViewById(R.id.pokemon_image)
        private var namePokemon: TextView   = view.findViewById(R.id.pokemon_name)

        fun bind(item:JsonObject,context: Context){
            namePokemon.setText(item.get("name").asString)

            Ion.with(context)
                .load(item.get("url").asString)
                .asJsonObject()
                .done { e, result ->
                    if(e == null){
                        if(!result.get("sprites").isJsonNull){
                            if(result.get("sprites").asJsonObject.get("front_default") != null){
                                Log.i("Salida", result.get("sprites").asJsonObject.get("front_default").asString)

                                Glide
                                    .with(context)
                                    .load(result.get("sprites").asJsonObject.get("front_default").asString)
                                    .placeholder(R.drawable.pokemon_logo_min)
                                    .error(R.drawable.pokemon_logo_min)
                                    .into(imagePokemon);

                                imagePokemon.setOnClickListener { view ->
                                    val intent: Intent = Intent( this.imagePokemon.context, ActivityDisplayPokemon::class.java );
                                    intent.putExtra("URL", item.get("url").asString);
                                    this.imagePokemon.context.startActivity(intent);
                                }

                                /*
                                imagePokemon.setOnClickListener { view ->
                                    val intent: Intent = Intent(this.imagePokemon.context, ActivityDisplayPokemon::class.java);
                                    intent.putExtra("numero", result.get("id").asString);
                                    intent.putExtra("nombre", result.get("name").asString);
                                    intent.putExtra("imagen", result.get("sprites").asJsonObject.get("front_default").asString);

                                    fun guardarEnString (datoBuscado: String, subArray: String, atributo: String) : String {
                                        var stringParaGuardar: String = "";
                                        for ( i in 0 until result.getAsJsonArray(datoBuscado).size() ) {
                                            stringParaGuardar += result.getAsJsonArray(datoBuscado)[i].asJsonObject.get(subArray).asJsonObject.get(atributo).asString;
                                            stringParaGuardar += "\n";
                                        }
                                        return stringParaGuardar;
                                    }

                                    var tipos: String = "";
                                    tipos = guardarEnString("types", "type", "name");
                                    intent.putExtra("tipos", tipos);

                                    var estadisticas: String = "";
                                    estadisticas = guardarEnString("stats", "stat", "name");
                                    intent.putExtra("estadisticas", estadisticas);

                                    var movimientos: String = "";
                                    movimientos = guardarEnString("moves", "move", "name");
                                    intent.putExtra("movimientos", movimientos);

                                    this.imagePokemon.context.startActivity(intent);
                                }
                                */

                            }
                            else {
                                imagePokemon.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.pokemon_logo_min))
                            }
                        }
                        else {
                            imagePokemon.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.pokemon_logo_min))
                        }
                    }
                }
        }
    }






}
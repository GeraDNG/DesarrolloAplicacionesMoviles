package com.test.pokedex.Activities

import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.test.pokedex.R
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.gson.JsonArray
import com.koushikdutta.ion.Ion
import kotlinx.android.synthetic.main.activity_list.*
import com.test.pokedex.Adapters.AdapterListPokemon
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_display_pokemon.*

class ActivityDisplayPokemon : AppCompatActivity() {

    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: AdapterListPokemon

    private lateinit var data_selectedPokemon: JsonArray

    private lateinit var imagen: ImageView;
    private lateinit var numero: TextView;
    private lateinit var nombre: TextView;
    private lateinit var tipos: TextView;
    private lateinit var estadisticas: TextView;

    private lateinit var numeroTitulo: TextView;
    private lateinit var nombreTitulo: TextView;
    private lateinit var tiposTitulo: TextView;
    private lateinit var estadisticasTitulo: TextView;
    private lateinit var movimientosTitulo: TextView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_pokemon)
        val url: String = intent.getStringExtra("URL");
        imagen = findViewById(R.id.imagenPokemon)
        numero = findViewById(R.id.numeroPokemon)
        nombre = findViewById(R.id.nombrePokemon)
        tipos = findViewById(R.id.tiposPokemon)
        estadisticas = findViewById(R.id.estadisticasPokemon)

        numeroTitulo = findViewById(R.id.nombre)
        nombreTitulo = findViewById(R.id.numero)
        tiposTitulo = findViewById(R.id.tipos)
        estadisticasTitulo = findViewById(R.id.estadisticas)
        movimientosTitulo = findViewById(R.id.movimientos)

        val tipografiaTexto = Typeface.createFromAsset(assets, "fonts/ArchivoNarrow-Regular.otf")
        nombre.typeface = tipografiaTexto
        numero.typeface = tipografiaTexto
        tipos.typeface = tipografiaTexto
        estadisticas.typeface = tipografiaTexto
        val tipografiaTitulo = Typeface.createFromAsset(assets, "fonts/ArchivoNarrow-Bold.otf")
        numeroTitulo.typeface = tipografiaTitulo
        nombreTitulo.typeface = tipografiaTitulo
        tiposTitulo.typeface = tipografiaTitulo
        estadisticasTitulo.typeface = tipografiaTitulo
        movimientosTitulo.typeface = tipografiaTitulo

        initializeData(url);
    }

    override fun onResume() {
        super.onResume()
    }

    fun initializeData(url: String){
        Ion.with(this)
            .load(url)
            .asJsonObject()
            .done { e, result ->
                if(e == null){
                    data_selectedPokemon = result.getAsJsonArray("moves");
                    if(!result.get("sprites").isJsonNull) {
                        if(result.get("sprites").asJsonObject.get("front_default") != null) {
                            Glide
                                .with(imagen.context)
                                .load(result.get("sprites").asJsonObject.get("front_default").asString)
                                .placeholder(R.drawable.pikachu_min)
                                .error(R.drawable.pokemon_logo_min)
                                .into(imagen);
                            numero.setText(result.get("id").asString);
                            nombre.setText(result.get("name").asString.capitalize());

                            fun juntarEnString(datoBuscado: String, subArray: String, atributo: String) : String {
                                var stringParaGuardarDatos = "";
                                for ( i in 0 until result.getAsJsonArray(datoBuscado).size() ) {
                                    stringParaGuardarDatos += result.getAsJsonArray(datoBuscado)[i].asJsonObject.get(subArray).asJsonObject.get(atributo).asString.capitalize();
                                    stringParaGuardarDatos += "\n";
                                }
                                return stringParaGuardarDatos;
                            }

                            tipos.setText( juntarEnString("types", "type", "name") );
                            estadisticas.setText( juntarEnString("stats", "stat", "name") );
                        }
                    }
                    initializeList();
                }
            }
    }

    fun initializeList(){
        linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        linearLayoutManager.scrollToPosition(0)

        adapter = AdapterListPokemon()
        adapter.AdapterListPokemon(this,data_selectedPokemon)

        recycler_view_pokemon.layoutManager = linearLayoutManager
        recycler_view_pokemon.adapter = adapter
        recycler_view_pokemon.itemAnimator = DefaultItemAnimator()

    }
}


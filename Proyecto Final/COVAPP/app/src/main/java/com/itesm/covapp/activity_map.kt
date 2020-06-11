package com.itesm.covapp

import com.itesm.covapp.R

import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.itesm.covapp.models.PostModel
import com.itesm.covapp.models.UserModel
import com.itesm.covapp.utils.Msn
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.fragment_place.*
import java.util.*

class activity_map : AppCompatActivity(), OnMapReadyCallback {

    var post = ArrayList<PostModel>()

    private lateinit var MAP: GoogleMap
    private lateinit var userLastLocation: Location
    private lateinit var userCurrentLocation : FusedLocationProviderClient
    private val TAG = activity_map::class.java.simpleName

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        var bundle :Bundle ?=intent.extras
        post = bundle!!.get("postList") as ArrayList<PostModel>

    }

    override fun onMapReady(googleMap: GoogleMap) {
        MAP = googleMap
        MAP.isMyLocationEnabled = true
        MAP.uiSettings.isZoomGesturesEnabled = true
        MAP.uiSettings.isZoomControlsEnabled = true
        //MAP.mapType = GoogleMap.MAP_TYPE_TERRAIN
        setMapStyle(MAP)
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
            return
        }
        userCurrentLocation = LocationServices.getFusedLocationProviderClient(this)
        userCurrentLocation.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                userLastLocation = location
                MAP.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(location.latitude, location.longitude), 16f))

                /*
                TODO: Aquí obtendremos los datos de firebase para colocar marcadores en el mapa para cada reporte que haya.
                 Debemos meter lo siguiente en un FOR.
                 Los marcadores se colocan de la siguiente forma. Sustituiremos la latitud y longitud por las coordenadas de firebase.
                 En la etiqueta del marcador también debemos poner los datos de firebase.
                */
                for(i in 0 until post.size){
                    val title = post[i].title
                    val topic = post[i].topic
                    val latitudePost = post[i].latitude
                    val longitudePost = post[i].longitude
//                    Log.d("DebugMap", post[i].latitude)
//                    Log.d("DebugMap", post[i].longitude)
//                    Log.d("DebugMap", userLastLocation.latitude.toString())
//                    Log.d("DebugMap", userLastLocation.longitude.toString())
                    MAP.addMarker(
                        MarkerOptions()
                            .position(LatLng(latitudePost!!.toDouble(), longitudePost!!.toDouble()))
                            .title(topic) // Por ejemplo: "Sospechoso"
                            .snippet(title) // Por ejemplo: "Bla bla bla"
                    )
                }

            }
        }
    }

    private fun setMapStyle(map: GoogleMap) {
        try {
            val success = map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(this, R.raw.estilo_mapa)
            )
            if (!success) {
                Log.e(TAG, "Style error")
            }
        }
        catch (e: Resources.NotFoundException) {
            Log.e(TAG, "No se encontró el estilo. Error: ", e)
        }
    }

}

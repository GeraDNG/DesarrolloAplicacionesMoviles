package com.itesm.covapp.ui.activities

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import android.provider.Settings
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.itesm.covapp.R
import android.location.Location
import android.util.Log
import com.itesm.covapp.models.UserModel
import kotlinx.android.synthetic.main.activity_make_a_post.*
import kotlinx.android.synthetic.main.pop_up_success_save.view.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest

class MakeAPostActivity : AppCompatActivity() {

    var topic: String = ""
    var state: String = ""
    var username:String = ""
    var lastname:String = ""
    var latitude:String = ""
    var longitude:String = ""
    private lateinit var database: DatabaseReference
    lateinit var mFusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_a_post)
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        loadUserData(uid)
        database = FirebaseDatabase.getInstance().reference.child("post")

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getLastLocation()

        spinnerAdapter()
        onClick()
    }

    private fun onClick(){
        btnSavePost.setOnClickListener {
            val title = txtTitle.text.toString()
            val postContent = txtPostContent.text.toString()
            val sdf = SimpleDateFormat("dd/M/yyyy")
            val currentDate = sdf.format(Date())
            savePost(username,lastname,state,title,topic,postContent,currentDate)
        }
    }

    private fun spinnerAdapter(){
        //  Adapter States
        val adapterTopic = ArrayAdapter.createFromResource(this,R.array.topics,android.R.layout.simple_spinner_item)
        adapterTopic.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTopic.adapter = adapterTopic

        spinnerTopic.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // either one will work as well
                topic = adapterTopic.getItem(position) as String
            }
        }
    }

    private fun loadUserData(uid: String) {
        database = FirebaseDatabase.getInstance().reference.child("users").child(uid)
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(item: DataSnapshot) {
                val user = item.getValue(UserModel::class.java)
                user?.let {
                    username = it.name.toString()
                    lastname = it.lastName.toString()
                    txtState.text = it.state.toString()
                    state = it.state.toString()
                }
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    private fun savePost(
        name: String,
        lastName: String,
        state: String,
        title: String,
        topic: String,
        postContent: String,
        currentDate: String
    ) {
        val idPost = UUID.randomUUID().toString()
        database.child(state).child(idPost).child("username").setValue(name)
        database.child(state).child(idPost).child("lastName").setValue(lastName)
        database.child(state).child(idPost).child("topic").setValue(topic)
        database.child(state).child(idPost).child("state").setValue(state)
        database.child(state).child(idPost).child("title").setValue(title)
        database.child(state).child(idPost).child("date").setValue(currentDate)
        database.child(state).child(idPost).child("latitude").setValue(latitude)
        database.child(state).child(idPost).child("longitude").setValue(longitude)
        database.child(state).child(idPost).child("postContent").setValue(postContent)
            .addOnSuccessListener {
                showAlertDialog()
            }
    }
    private fun showAlertDialog(){
        val dialogBuilder = AlertDialog.Builder(this)
        val view = this.layoutInflater.inflate(R.layout.pop_up_success_save,null)
        dialogBuilder.setView(view)

        val alertDialog = dialogBuilder.create()
        alertDialog.show()

        view.btnOkSuccess.setOnClickListener {
            onBackPressed()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (isLocationEnabled()) {
            mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                var location: Location? = task.result
                if (location == null) {
                    requestNewLocationData()
                } else {
                    latitude = location.latitude.toString()
                    longitude = location.longitude.toString()
//                    findViewById<TextView>(R.id.latTextView).text = location.latitude.toString()
//                    findViewById<TextView>(R.id.lonTextView).text = location.longitude.toString()
                }
            }
        } else {
            Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
            latitude = mLastLocation.latitude.toString()
            longitude = mLastLocation.longitude.toString()
//            findViewById<TextView>(R.id.latTextView).text = mLastLocation.latitude.toString()
//            findViewById<TextView>(R.id.lonTextView).text = mLastLocation.longitude.toString()
        }
    }

}

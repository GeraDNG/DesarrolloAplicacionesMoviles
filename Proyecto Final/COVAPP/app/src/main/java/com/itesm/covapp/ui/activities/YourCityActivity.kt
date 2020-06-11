package com.itesm.covapp.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.itesm.covapp.R
import com.itesm.covapp.models.UserModel
import kotlinx.android.synthetic.main.activity_your_city.*

class YourCityActivity : AppCompatActivity() {

    var city:String = ""
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_your_city)
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        loadUserData(uid)
        database = FirebaseDatabase.getInstance().reference.child("users")
        onClick(uid)
        spinnerAdapter()
    }

    private fun onClick(uid: String) {
        btnSaveCity.setOnClickListener {
            saveCity(uid)
        }

        btnBackCity.setOnClickListener {
            onBackPressed()
        }
    }

    private fun spinnerAdapter(){
        //  Adapter States
        val adapterCity = ArrayAdapter.createFromResource(this,R.array.states,android.R.layout.simple_spinner_item)
        adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCity.adapter = adapterCity

        spinnerCity.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // either one will work as well
                city = adapterCity.getItem(position) as String
            }
        }
    }

    private fun loadUserData(uid: String){
        database = FirebaseDatabase.getInstance().reference.child("users").child(uid)
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(item: DataSnapshot) {
                val user = item.getValue(UserModel::class.java)
                user?.let {
                    if(it.state!!.isEmpty()){
                        txtMyCity.text = "Todavía no has guardado tu estado"
                    }else{
                        txtMyCity.text = it.state.toString()
                    }
                }
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    private fun saveCity(
        uid: String
    ) {
        database.child(uid).child("state").setValue(city)
            .addOnSuccessListener {
                onBackPressed()
            }
    }
}

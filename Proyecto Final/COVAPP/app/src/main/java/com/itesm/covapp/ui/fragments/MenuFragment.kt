package com.itesm.covapp.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.itesm.covapp.R
import com.itesm.covapp.models.UserModel
import com.itesm.covapp.utils.Intents
import kotlinx.android.synthetic.main.fragment_menu.*
import kotlinx.android.synthetic.main.pop_up_setlocation.*
import kotlinx.android.synthetic.main.pop_up_setlocation.view.*

class MenuFragment: Fragment() {

    //FirebaseInstance
    private lateinit var database: DatabaseReference

    companion object {
        fun newInstance(): MenuFragment{
            val frag = MenuFragment()

            return frag
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_menu,container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadData()
        loadGoogleData()
        onClick()
    }

    private fun onClick(){
        btnChooseYourCity.setOnClickListener {
            Intents.goToChooseYourCity(requireActivity())
        }

        btnSettingsProfile.setOnClickListener {
            Intents.goToProfile(requireActivity())
        }
        btnLogOut.setOnClickListener{
            signOut()
        }
    }

    private fun loadGoogleData(){
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            // Name, email address, and profile photo Url
            val photoUrl = user.photoUrl
            Glide.with(requireActivity())
                .load(photoUrl)
                .error(R.drawable.ic_user)
                .into(imgUserProfile)
        }
    }

    private fun loadData(){
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        database = FirebaseDatabase.getInstance().reference.child("users").child(uid)
        database.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(item: DataSnapshot) {
                val user = item.getValue(UserModel::class.java)
                user?.let {
                    txtWelcome.text = "¡Hola ${it.name}!"
                    if (it.state!!.isEmpty()){
                        showAlertDialog()
                        txtLocation.text = "Tu ubicación: Selecciona aquí tu estado"
                    }else{
                        txtLocation.text = "Tu ubicación: ${it.state}"
                    }
                }
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })

        val ref = FirebaseStorage.getInstance().getReference("images").child(uid)
        ref.downloadUrl.addOnSuccessListener {
            Glide.with(requireActivity())
                .load(it)
                .error(R.drawable.ic_user)
                .into(imgUserProfile)
        }
    }

    private fun signOut(){
        FirebaseAuth.getInstance().signOut()
        Intents.goToLogin(requireActivity())
        requireActivity().finish()
    }

    private fun showAlertDialog() {
        val dialogBuilder = AlertDialog.Builder(requireActivity())
        val view = this.layoutInflater.inflate(R.layout.pop_up_setlocation,null)
        dialogBuilder.setView(view)
        val alertDialog = dialogBuilder.create()

        alertDialog.show()
        view.btnSaveLocation.setOnClickListener {
            alertDialog.dismiss()
        }
    }

}
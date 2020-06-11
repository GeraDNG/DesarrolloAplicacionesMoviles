package com.itesm.covapp.ui.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.FileProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.itesm.covapp.R
import com.itesm.covapp.utils.Intents
import com.itesm.covapp.utils.Msn
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class SignUpActivity : AppCompatActivity() {

    val mAuth = FirebaseAuth.getInstance()
    private lateinit var database: DatabaseReference
    lateinit var mDatabase : DatabaseReference

    var type:String =""
    var state:String =""
    var uriProfile:Uri? =null
    var currentPath:String? = null
    val TAKE_PICTURE = 1
    val SELECT_PICTURE =2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        database = FirebaseDatabase.getInstance().reference.child("users")
        spinnerAdapter()
        onClick()
    }

    private fun onClick(){
        btnJoin.setOnClickListener {
            registerUser()
        }

        imageView2.setOnClickListener {
            alertDialog()
        }
    }

    private fun registerUser(){
        // Database reference pointing to demo node
        val userName = txtUserName.text.toString()
        val email = txtMailUser.text.toString()
        val password = txtPassword.text.toString()
        val passwordConfirm = txtPassword.text.toString()

        if(email.isEmpty() || password.isEmpty()) {
            Msn.makeToast(this,"Por favor rellene todos los campos")
            return
        }

        if(password != passwordConfirm){
            Msn.makeToast(this,"Las contraseñas no coinciden")
            return
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if(!it.isSuccessful) return@addOnCompleteListener
                //else
                val id = it.result?.user?.uid
                if (id != null) {
                    writeNewUser(id,userName,state)
                    saveImageOnFireBase(id)
                }
                Intents.goToHome(this)
                finish()
            }
            .addOnFailureListener {
                Msn.makeToast(this,"Error al crear usario, por favor verifique su conexión")
                return@addOnFailureListener
            }
    }

    private fun writeNewUser(userId: String, name: String,state:String) {
        database.child(userId).child("name").setValue(name)
        database.child(userId).child("state").setValue(state)
    }

    private fun saveImageOnFireBase(id: String) {
        if(uriProfile == null) return
        val fileName = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("images").child(id)

        ref.putFile(uriProfile!!)
            .addOnSuccessListener {
                Log.d("Register", "Success To upload Photo")
            }
    }

    private fun spinnerAdapter(){
        //  Adapter States
        val adapterStates = ArrayAdapter.createFromResource(this,R.array.states,android.R.layout.simple_spinner_item)
        adapterStates.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        txtSpinerState.adapter = adapterStates

        txtSpinerState.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // either one will work as well
                state = adapterStates.getItem(position) as String
            }
        }
    }

    private fun dispatchGalleryIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent,"Selecciona una imagen"),SELECT_PICTURE)
    }

    private fun dispatchCameraIntent() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if(intent.resolveActivity(packageManager)!=null){
            var photoFile: File? =null
            try {
                photoFile = createImage()
            }catch (e: IOException){
                e.printStackTrace()
            }
            if (photoFile != null){
                //
                var photoUri = FileProvider.getUriForFile(this,
                    "com.itesm.covapp.fileprovider",photoFile)
                intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri)
                startActivityForResult(intent,TAKE_PICTURE)
            }
        }
    }

    private fun createImage(): File{
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageName = "JPEG_"+timeStamp+"_"
        var storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        var image = File.createTempFile(imageName,".jpg",storageDir)
        currentPath = image.absolutePath
        return image
    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == TAKE_PICTURE && resultCode == Activity.RESULT_OK){
            try {
                type = "camera"
                val file = File(currentPath)
                uriProfile = Uri.fromFile(file)
                imageView2.setImageURI(uriProfile)
            }catch (e: IOException){
                e.printStackTrace()
            }
        }
        if (requestCode == SELECT_PICTURE && resultCode == Activity.RESULT_OK){
            try {
                type = "carrete"
                uriProfile = data!!.data
                imageView2.setImageURI(uriProfile)
            }catch (e: IOException){
                e.printStackTrace()
            }
        }
    }

    private fun alertDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Subir foto")
        builder.setMessage("Selecciona un método")
        builder.setPositiveButton("Foto") { dialog, which ->
            dispatchCameraIntent()
        }

        builder.setNegativeButton("Galería") { dialog, which ->
            dispatchGalleryIntent()
        }

        builder.setNeutralButton("Cancelar") { dialog, which ->

        }
        builder.show()
    }
}

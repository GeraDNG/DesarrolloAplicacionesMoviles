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
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.itesm.covapp.R
import com.itesm.covapp.models.UserModel
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.fragment_menu.*
import kotlinx.android.synthetic.main.fragment_menu.imgUserProfile
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ProfileActivity : AppCompatActivity()  {

    var type:String =""
    var uriProfile: Uri? =null
    var currentPath:String? = null
    val TAKE_PICTURE = 1
    val SELECT_PICTURE =2

    //FirebaseInstance
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        loadData()
        onClick()
    }

    private fun onClick(){
        imageView4.setOnClickListener {
            onBackPressed()
        }
        UserProfile.setOnClickListener {
            alertDialog()
        }
    }

    private fun loadData(){
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val mail = FirebaseAuth.getInstance().currentUser!!.email
        txtMailProfile.text = mail
        val storage = FirebaseStorage.getInstance()
        database = FirebaseDatabase.getInstance().reference.child("users").child(uid)
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(item: DataSnapshot) {
                val user = item.getValue(UserModel::class.java)
                user?.let {
                    txtUserNameProfile.text = it.name
                }
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })

        val ref = FirebaseStorage.getInstance().getReference("images").child(uid)
        ref.downloadUrl.addOnSuccessListener {
            Glide.with(this)
                .load(it)
                .error(R.drawable.ic_user)
                .into(imgUserProfile)
        }
    }

    private fun saveImageOnFireBase() {
        val id = FirebaseAuth.getInstance().currentUser!!.uid
        if(uriProfile == null) return
        val fileName = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("images").child(id)

        ref.putFile(uriProfile!!)
            .addOnSuccessListener {
                Log.d("Register", "Success To upload Photo")
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

    private fun createImage(): File {
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
                UserProfile.setImageURI(uriProfile)
                saveImageOnFireBase()
            }catch (e: IOException){
                e.printStackTrace()
            }
        }
        if (requestCode == SELECT_PICTURE && resultCode == Activity.RESULT_OK){
            try {
                type = "carrete"
                uriProfile = data!!.data
                UserProfile.setImageURI(uriProfile)
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

package com.itesm.covapp.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.itesm.covapp.R
import com.itesm.covapp.utils.Intents
import com.itesm.covapp.utils.Msn
import com.itesm.covapp.utils.PreferencesManager
import com.itesm.covapp.utils.Utils
import kotlinx.android.synthetic.main.activity_log_in.*
import kotlinx.android.synthetic.main.pop_loading_progress.*


class LogInActivity : AppCompatActivity() {

    private val GOOGLE_SIGN_IN = 100
    val mAuth = FirebaseAuth.getInstance();
    private lateinit var manager: PreferencesManager
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        supportActionBar?.hide()
        manager = PreferencesManager(this)
        database = FirebaseDatabase.getInstance().reference.child("users")
        val utils = Utils(this)
        permissions(utils)
        onClick()
    }

    private fun permissions(utils: Utils){
        utils.permission()
    }

    private fun onClick(){
        btnLogIn.setOnClickListener {
            progress_loader.visibility = View.VISIBLE
            val mail = txtUserMailLogin.text.toString().decapitalize()
            val password = txtuserPassowrdLogIn.text.toString()
            login(mail, password)
        }

        linearLayout2.setOnClickListener {
            Intents.goToSignUp(this)
            finish()
        }

        btnLogInGoogle.setOnClickListener {
            // Configuration
            val gso =
                GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build()
            val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
            mGoogleSignInClient.signOut()

            startActivityForResult(mGoogleSignInClient.signInIntent,GOOGLE_SIGN_IN)
        }
    }

    private fun login (email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            this.mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener ( this, OnCompleteListener<AuthResult> { task ->
                    if (task.isSuccessful) {
                            Intents.goToHome(this)
                            finish()
                    } else {
                        progress_loader.visibility = View.INVISIBLE
                        Msn.makeToast(this,"Error Logging in :(")
                    }
                })
        }else {
            progress_loader.visibility = View.INVISIBLE
            Msn.makeToast(this,"Please fill up the Credentials :|")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == GOOGLE_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if(account != null){
                    val credential = GoogleAuthProvider.getCredential(account.idToken,null)
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                        if(it.isSuccessful){
                            val uid = FirebaseAuth.getInstance().currentUser!!.uid
                            val name = FirebaseAuth.getInstance().currentUser!!.displayName
                            saveGoogleUser(uid,name)
                            Intents.goToHome(this)
                            finish()
                        }else{
                            progress_loader.visibility = View.INVISIBLE
                            Msn.makeToast(this,"Error Logging in :(")
                        }
                    }
                }
            }catch (e:ApiException){
                Msn.makeToast(this,e.toString())
            }

        }
    }

    private fun saveGoogleUser(uid: String, name: String?) {
        database.child(uid).child("name").setValue(name)
    }
}

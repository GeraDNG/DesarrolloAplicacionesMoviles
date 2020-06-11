package com.itesm.covapp.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.itesm.covapp.R
import com.itesm.covapp.utils.Intents
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        supportActionBar?.hide()

        val seconds: Long = 3
        val time: Long = seconds*1000
        val mAuth: FirebaseAuth = FirebaseAuth.getInstance()
        val currentUser = mAuth.currentUser
//        val account = GoogleSignIn.getLastSignedInAccount(this)

        Timer().schedule(object: TimerTask() {
            override fun run() {
                if(currentUser!=null){
                    Intents.goToHome(this@MainActivity)
                    finish()
                }else{
                    Intents.goToWelcome(this@MainActivity)
                    finish()
                }
            }
        }, time)

    }
}

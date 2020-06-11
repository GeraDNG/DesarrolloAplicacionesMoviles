package com.itesm.covapp.utils

import android.content.Context
import android.widget.Toast

object Msn {
    fun makeToast(context: Context,message:String){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
    }
}
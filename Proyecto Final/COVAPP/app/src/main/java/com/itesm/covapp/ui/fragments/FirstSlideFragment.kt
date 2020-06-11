package com.itesm.covapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.itesm.covapp.R

class FirstSlideFragment:Fragment() {

    companion object {
        fun newInstance(): FirstSlideFragment{
            val frag = FirstSlideFragment()

            return frag
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View? = inflater.inflate(R.layout.fragment_first_slide,container, false)

        return view
    }

}
package com.itesm.covapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.itesm.covapp.R

class ThirdSlideFragment:Fragment() {

    companion object {
        fun newInstance(): ThirdSlideFragment{
            val frag = ThirdSlideFragment()

            return frag
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View? = inflater.inflate(R.layout.fragment_third_slide,container, false)

        return view
    }
}
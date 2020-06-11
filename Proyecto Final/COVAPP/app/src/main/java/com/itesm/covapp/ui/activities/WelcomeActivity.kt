package com.itesm.covapp.ui.activities

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.github.paolorotolo.appintro.AppIntro2
import com.github.paolorotolo.appintro.AppIntro2Fragment
import com.github.paolorotolo.appintro.model.SliderPagerBuilder
import com.itesm.covapp.R
import com.itesm.covapp.ui.fragments.FirstSlideFragment
import com.itesm.covapp.ui.fragments.SecondSlideFragment
import com.itesm.covapp.ui.fragments.ThirdSlideFragment
import com.itesm.covapp.utils.Intents
import com.itesm.covapp.utils.PreferencesManager
import java.util.jar.Manifest

class WelcomeActivity : AppIntro2() {

    private lateinit var manager: PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_welcome)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        supportActionBar?.hide()

//        manager = PreferencesManager(this)
//        if (manager.isFirstRun()) {
            showSlides()
//        } else {
//            goToMain()
//        }
    }

    private fun showSlides(){
//        manager.setFirstRun()
        val firstSlide = FirstSlideFragment.newInstance()
        val secondSlide = SecondSlideFragment.newInstance()
        val thirdSlide = ThirdSlideFragment.newInstance()

        addSlide(firstSlide)
        addSlide(secondSlide)
        addSlide(thirdSlide)

        showSkipButton(false)

        showStatusBar(true)
        setDepthAnimation()
    }

    private fun goToMain() {
        Intents.goToLogin(this)
    }

    override fun onDonePressed(currentFragment: Fragment?) {
        super.onDonePressed(currentFragment)
        goToMain()
    }

}

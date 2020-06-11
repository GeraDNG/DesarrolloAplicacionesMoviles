package com.itesm.covapp.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;

import com.itesm.covapp.R;
import com.itesm.covapp.ui.fragments.HomeFragment;
import com.itesm.covapp.ui.fragments.PlaceFragment;
import com.itesm.covapp.ui.fragments.MenuFragment;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import nl.joery.animatedbottombar.AnimatedBottomBar;

public class MenuActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    AnimatedBottomBar animatedBottomBar;
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        animatedBottomBar = findViewById(R.id.animatedBottomBar);

        if(savedInstanceState == null){
            animatedBottomBar.selectTabById(R.id.tab_home,true);
            fragmentManager = getSupportFragmentManager();
            MenuFragment menuFragment = new MenuFragment();
            fragmentManager.beginTransaction().replace(R.id.fragment_container,menuFragment).commit();
        }


        animatedBottomBar.setOnTabSelectListener(new AnimatedBottomBar.OnTabSelectListener() {
            @Override
            public void onTabSelected(int lastIndex, @Nullable AnimatedBottomBar.Tab lasttab, int newIndex, @NotNull AnimatedBottomBar.Tab newTab) {
                Fragment fragment = null;
                switch (newTab.getId()){
                    case R.id.tab_menu:
                        fragment = new HomeFragment();
                        break;
                    case R.id.tab_place:
                        fragment = new PlaceFragment();
                        break;
                    case R.id.tab_home:
                        fragment = new MenuFragment();
                        break;
                }
                if(fragment!=null){
                    fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
                }else {
                    Log.e(TAG,"Error in creating Fragment");
                }
            }

            @Override
            public void onTabReselected(int i, @NotNull AnimatedBottomBar.Tab tab) {

            }
        });
    }
}

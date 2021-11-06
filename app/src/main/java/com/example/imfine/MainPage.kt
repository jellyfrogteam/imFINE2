package com.example.imfine

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainPage : AppCompatActivity(){

    private lateinit var bottomNavigationView : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        bottomNavigationView = findViewById(R.id.main_nav)

        val navController = findNavController(R.id.navController)
        bottomNavigationView.setupWithNavController(navController)

    }
}
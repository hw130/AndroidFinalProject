package com.example.auctioninginfoapp.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.auctioninginfoapp.R
import com.example.auctioninginfoapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val controller = findNavController(R.id.navigation_host)

        NavigationUI.setupActionBarWithNavController(this, controller,
            AppBarConfiguration.Builder(R.id.splashFragment, R.id.loginFragment, R.id.searchFragment, R.id.userFragment).build()
        )

        NavigationUI.setupWithNavController(binding.bottomNavigation, controller)

        controller.addOnDestinationChangedListener {_, destination, _ ->
            if(arrayListOf(R.id.searchFragment, R.id.userFragment).contains(destination.id)){
                binding.bottomNavigation.visibility = View.VISIBLE
            }else {
                binding.bottomNavigation.visibility = View.GONE
            }
        }
    }

    override fun onSupportNavigateUp() = findNavController(R.id.navigation_host).navigateUp()
}
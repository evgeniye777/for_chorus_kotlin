package com.example.for_chour_kotlin


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.for_chour_kotlin.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        //supportActionBar?.setHomeAsUpIndicator(R.drawable.image_statistic_songs)
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)
        /*val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)*/
        supportActionBar?.apply {
            /*setDisplayShowHomeEnabled(true)
            setDisplayUseLogoEnabled(true)
            setLogo(R.drawable.image_home_35)*/
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
            /*setLogo(R.drawable.image_home_35)
            setDisplayUseLogoEnabled(true);
            setDisplayShowHomeEnabled(true);
            setDisplayHomeAsUpEnabled(true);*/
            }
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                vivod("Евгений")
                return true
            }

            else -> {return super.onOptionsItemSelected(item)}
        }
    }

    fun vivod(s: String?) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }
}
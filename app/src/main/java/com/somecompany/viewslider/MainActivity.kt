package com.somecompany.viewslider

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.somecompany.viewslider.model.auth.AuthDatabase
import com.somecompany.viewslider.model.sliders.SlidersDatabase

class MainActivity : AppCompatActivity() {
  lateinit var navController: NavController

  val slidersDatabase = SlidersDatabase()
  val authDatabase = AuthDatabase()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    supportActionBar?.hide()

    slidersDatabase.startListeners()

    navController =
      (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController
  }
}



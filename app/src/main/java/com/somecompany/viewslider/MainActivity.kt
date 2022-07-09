package com.somecompany.viewslider

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.somecompany.viewslider.model.auth.AuthDatabase
import com.somecompany.viewslider.model.sliders.SlidersDatabase

class MainActivity : AppCompatActivity() {

  val slidersDatabase = SlidersDatabase()
  val authDatabase = AuthDatabase()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    supportActionBar?.hide()

    slidersDatabase.startListeners()
  }
}



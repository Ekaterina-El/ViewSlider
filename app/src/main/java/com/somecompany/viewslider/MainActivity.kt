package com.somecompany.viewslider

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.somecompany.viewslider.model.SlideView
import com.somecompany.viewslider.model.SlidersDatabase
import com.somecompany.viewslider.model.SlidersDatabase.*
import com.somecompany.viewslider.view.slider.SliderAdapter
import com.somecompany.viewslider.view.slider.SliderFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

  lateinit var slidersDatabase: SlidersDatabase

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    supportActionBar?.hide()

    slidersDatabase = SlidersDatabase()
    slidersDatabase.startListeners()

  }
}



package com.somecompany.viewslider

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.somecompany.viewslider.model.SlideView
import com.somecompany.viewslider.model.SlidersDatabase
import com.somecompany.viewslider.model.SlidersDatabase.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
  private lateinit var imageSliderAdapter: SliderAdapter
  lateinit var slidersDatabase: SlidersDatabase

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    supportActionBar?.hide()

    initViewPager()
    slidersDatabase = SlidersDatabase()
    slidersDatabase.startListeners()

    slidersDatabase.addOnValueEventListener(object: Companion.OnValueEventListener {
      override fun onDataChange(items: List<SlideView>) {
        imageSliderAdapter.clear()
        imageSliderAdapter.addItems(items)
      }
    })
  }

  private fun initViewPager() {
    imageSliderAdapter = SliderAdapter(this)
    viewPagerImages.adapter = imageSliderAdapter
  }
}



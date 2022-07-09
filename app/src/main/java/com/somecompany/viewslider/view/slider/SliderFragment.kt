package com.somecompany.viewslider.view.slider

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.somecompany.viewslider.MainActivity
import com.somecompany.viewslider.R
import com.somecompany.viewslider.model.SlideView
import com.somecompany.viewslider.model.SlidersDatabase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.slider_fragment.*

class SliderFragment : Fragment(R.layout.slider_fragment) {
  private lateinit var imageSliderAdapter: SliderAdapter
  private lateinit var sliderDatabase: SlidersDatabase
  private lateinit var DBListener: SlidersDatabase.Companion.OnValueEventListener

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initViewPager()

    DBListener = object : SlidersDatabase.Companion.OnValueEventListener {
      override fun onDataChange(items: List<SlideView>) {
        imageSliderAdapter.clear()
        imageSliderAdapter.addItems(items)
      }
    }

    sliderDatabase = (requireActivity() as MainActivity).slidersDatabase
  }

  override fun onResume() {
    super.onResume()
    sliderDatabase.addOnValueEventListener(DBListener)
  }

  override fun onDestroy() {
    super.onDestroy()
    sliderDatabase.removeOnValueEventListener(DBListener)
  }

  private fun initViewPager() {
    imageSliderAdapter = SliderAdapter(requireContext())
    viewPagerImages.adapter = imageSliderAdapter
  }
}
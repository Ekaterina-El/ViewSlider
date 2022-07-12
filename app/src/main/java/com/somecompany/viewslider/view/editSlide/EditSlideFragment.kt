package com.somecompany.viewslider.view.editSlide

import android.os.Bundle
import android.transition.Slide
import android.view.View
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.somecompany.viewslider.MainActivity
import com.somecompany.viewslider.R
import com.somecompany.viewslider.model.sliders.SlideView
import com.somecompany.viewslider.model.sliders.SlidersDatabase
import kotlinx.android.synthetic.main.edit_slide_fragment.*
import kotlinx.android.synthetic.main.slider_item_container.*
import kotlinx.android.synthetic.main.slider_item_container.view.*

class EditSlideFragment: Fragment(R.layout.edit_slide_fragment) {
  private lateinit var sliderDatabase: SlidersDatabase
  private lateinit var slide: SlideView

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    sliderDatabase = (requireActivity() as MainActivity).slidersDatabase

    slide = requireArguments().getSerializable("slide") as SlideView

    this.country_edit.setText(slide.country)
    this.place_name_edit.setText(slide.placeName)
    Glide.with(requireContext())
      .load(slide.imageUrl)
      .placeholder(R.drawable.image_placeholder)
      .into(this.image)

    this.btn_save.setOnClickListener {
      slide.country = this.country_edit.text.toString()
      slide.placeName = this.place_name_edit.text.toString()
      // change image

      sliderDatabase.editSlide(slide, {
        (requireActivity() as MainActivity).navController.navigateUp()
      }, {})
    }
  }
}
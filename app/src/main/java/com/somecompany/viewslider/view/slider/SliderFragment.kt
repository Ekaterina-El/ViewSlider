package com.somecompany.viewslider.view.slider

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.somecompany.viewslider.MainActivity
import com.somecompany.viewslider.R
import com.somecompany.viewslider.model.auth.AuthDatabase
import com.somecompany.viewslider.model.sliders.SlideView
import com.somecompany.viewslider.model.sliders.SlidersDatabase
import kotlinx.android.synthetic.main.slider_fragment.*


class SliderFragment : Fragment(R.layout.slider_fragment) {
  private lateinit var imageSliderAdapter: SliderAdapter
  private lateinit var sliderDatabase: SlidersDatabase
  private lateinit var authDatabase: AuthDatabase

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initViewPager()

    authDatabase = (requireActivity() as MainActivity).authDatabase
    sliderDatabase = (requireActivity() as MainActivity).slidersDatabase
  }

  private val DBListener = object : SlidersDatabase.Companion.OnValueEventListener {
    override fun onDataChange(items: List<SlideView>) {
      Log.d("DBListener", "items: ${items.size}")
      imageSliderAdapter.clear()
      imageSliderAdapter.addItems(items)
      imageSliderAdapter.addAddSlide()
    }
  }

  private val onSliderListener = object : SliderAdapter.Companion.OnViewClickListener {
    override fun onUnlock() {
      super.onUnlock()
      showUnlockAlert()
    }

    override fun onEdit(slide: SlideView?) {
      super.onEdit(slide)

      val bundle = Bundle()
      bundle.putSerializable("slide", slide)
      val navigator = (requireActivity() as MainActivity).navController
      navigator.navigate(R.id.action_sliderFragment_to_editSlideFragment, bundle)
    }

    override fun onDelete(slide: SlideView) {
      super.onDelete(slide)
      sliderDatabase.deleteSlide(slide.key!!)
    }
  }

  fun showUnlockAlert() {
    val passwordEdit = EditText(requireContext())

    AlertDialog.Builder(requireContext())
      .setTitle("Unlock Admin panel")
      .setMessage("Input Admin panel password")
      .setView(passwordEdit)
      .setPositiveButton(
        "Try"
      ) { _, _ ->
        val text = passwordEdit.text.toString()
        authDatabase.isCorrectPass(text) {
          if (it) {
            imageSliderAdapter.setOpenState(true)
          }
          else Toast.makeText(requireContext(), "Incorrect password", Toast.LENGTH_SHORT).show()
        }

        Log.d("UnlockAdminFunctions", "text: $text")
      }.setNegativeButton(
        "Cancel"
      ) { _, _ ->
      }.show()
  }

  override fun onResume() {
    super.onResume()
    sliderDatabase.addOnValueEventListener(DBListener)
    sliderDatabase.getSlidersOnce(DBListener)
  }


  override fun onDestroyView() {
    super.onDestroyView()
    sliderDatabase.removeOnValueEventListener(DBListener)
  }

  private fun initViewPager() {
    imageSliderAdapter = SliderAdapter(requireContext())
    imageSliderAdapter.onViewClickListener = onSliderListener
    viewPagerImages.adapter = imageSliderAdapter
  }
}
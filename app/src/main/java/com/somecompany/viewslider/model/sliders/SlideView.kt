package com.somecompany.viewslider.model.sliders

import java.io.Serializable

data class SlideView(
  var key: String? = "",
  var imageUrl: String? = "",
  var country: String? = "",
  var placeName: String? = ""
): Serializable
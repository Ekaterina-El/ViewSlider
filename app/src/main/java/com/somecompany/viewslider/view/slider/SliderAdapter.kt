package com.somecompany.viewslider.view.slider

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.somecompany.viewslider.R
import com.somecompany.viewslider.model.SlideView
import kotlinx.android.synthetic.main.slider_item_container.view.*
import java.lang.Exception

class SliderAdapter(
  val context: Context
)
  : RecyclerView.Adapter<SliderAdapter.ImageSliderViewHolder>() {
  inner class ImageSliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

  private val items = mutableListOf<SlideView>()

  fun addItem(item: SlideView) {
    items.add(item)
    notifyItemInserted(items.size - 1)
  }

  @JvmName("addItems1")
  fun addItems(items: List<*>) {
    try {
      items.forEach { addItem(it as SlideView) }
    } catch (e: Exception) {
      Log.w("addItems", "${e.localizedMessage}")
    }
  }


  fun addItems(items: List<SlideView>) {
    items.forEach { addItem(it) }
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageSliderViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.slider_item_container, parent, false)
    return ImageSliderViewHolder(view)
  }

  override fun onBindViewHolder(holder: ImageSliderViewHolder, position: Int) {
    val item = items[position]
    holder.itemView.country.text = item.country
    holder.itemView.placeName.text = item.placeName
    Glide.with(context)
      .load(item.imageUrl)
      .placeholder(R.drawable.image_placeholder)
      .into(holder.itemView.imageView)
  }

  override fun getItemCount(): Int = items.size
  fun clear() {
    items.clear()
    notifyDataSetChanged()
  }
}
package com.somecompany.viewslider.view.slider

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.somecompany.viewslider.R
import com.somecompany.viewslider.model.sliders.SlideView
import kotlinx.android.synthetic.main.slider_item_container.view.*
import java.lang.Exception

class SliderAdapter(
  val context: Context
)
  : RecyclerView.Adapter<SliderAdapter.ImageSliderViewHolder>() {
  inner class ImageSliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun changeOpenState(isOpen: Boolean) {
      itemView.lock_btn.visibility = if (isOpen) View.GONE else View.VISIBLE
      itemView.edit_btn.visibility = if (isOpen) View.VISIBLE else View.GONE
      itemView.delete_btn.visibility = if (isOpen) View.VISIBLE else View.GONE
    }
  }


  override fun onViewAttachedToWindow(holder: ImageSliderViewHolder) {
    super.onViewAttachedToWindow(holder)
    holder.changeOpenState(isOpen)
    currentView = holder

    val item = items[holder.adapterPosition]

    holder.itemView.delete_btn.setOnClickListener { onViewClickListener?.onDelete(item) }
    holder.itemView.edit_btn.setOnClickListener { onViewClickListener?.onEdit(item) }
    holder.itemView.lock_btn.setOnClickListener { onViewClickListener?.onUnlock() }
  }

  override fun onViewDetachedFromWindow(holder: ImageSliderViewHolder) {
    super.onViewDetachedFromWindow(holder)
    holder.itemView.delete_btn.setOnClickListener(null)
    holder.itemView.edit_btn.setOnClickListener(null)
    holder.itemView.lock_btn.setOnClickListener(null)
  }

  private val items = mutableListOf<SlideView>()
  private var isOpen = false;
  private var currentView: ImageSliderViewHolder? = null

  var onViewClickListener: OnViewClickListener? = null

  fun setOpenState(state: Boolean) {
    isOpen = state;
    currentView?.changeOpenState(isOpen)
  }

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

  companion object {
    interface OnViewClickListener {
      fun onUnlock() {}
      fun onEdit(slide: SlideView) {}
      fun onDelete(slide: SlideView) {}
    }
  }
}
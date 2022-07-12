package com.somecompany.viewslider.model.sliders

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class SlidersDatabase() {
  private var onValueEventListeners = mutableListOf<OnValueEventListener>()
  private val database = Firebase.database
  val slidersRef = database.getReference("sliders")

  fun startListeners() {
    slidersRef.addValueEventListener(object : ValueEventListener {
      override fun onDataChange(snapshot: DataSnapshot) {
        // Если слушателей нет, то нет необходимости обрабатывать данные
        if (onValueEventListeners.size == 0) return;

        // Обработка данных - начало
        val items = mutableListOf<SlideView>()
        snapshot.children.forEach {
          val item = it.getValue<SlideView>()         // Приведение значения к классу SlideView
          item!!.key = it.key                         // Запись ключа элемента
          //Log.w("slidersRef", item.toString())
          items.add(item)
        }
        // Обработка данных - конец

        // Уведомляем всех слушателей об изменении
        onValueEventListeners.forEach { it.onDataChange(items) }
      }

      override fun onCancelled(error: DatabaseError) {
        Log.e("SliderDatabase","Error: [${error.code}] ${error.message}")
      }
    })
  }

  fun deleteSlide(sliderId: String) {
    slidersRef.child(sliderId).removeValue()
  }

  fun addOnValueEventListener(listener: OnValueEventListener) {
    onValueEventListeners.add(listener)
  }

  fun removeOnValueEventListener(dbListener: OnValueEventListener) {
    onValueEventListeners = onValueEventListeners.filter { it != dbListener }.toMutableList()
  }

  fun editSlide(slide: SlideView, onSuccess: () -> Unit, onFailure: () -> Unit) {
    val postValue = HashMap<String, Any>()
    postValue["country"] = slide.country!!
    postValue["placeName"] = slide.placeName!!
    postValue["imageUrl"] = slide.imageUrl!!

    slidersRef.child(slide.key!!)
      .updateChildren(postValue)
      .addOnSuccessListener { onSuccess() }
      .addOnFailureListener { onFailure() }
  }

  companion object {
    interface OnValueEventListener {
      fun onDataChange(items: List<SlideView>)
    }
  }
}
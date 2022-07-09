package com.somecompany.viewslider.model

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

  fun addOnValueEventListener(listener: OnValueEventListener) {
    onValueEventListeners.add(listener)
  }

  fun removeOnValueEventListener(dbListener: OnValueEventListener) {
    onValueEventListeners = onValueEventListeners.filter { it != dbListener }.toMutableList()
  }

  companion object {
    interface OnValueEventListener {
      fun onDataChange(items: List<SlideView>)
    }
  }
}
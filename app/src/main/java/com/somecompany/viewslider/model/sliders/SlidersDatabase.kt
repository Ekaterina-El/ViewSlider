package com.somecompany.viewslider.model.sliders

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class SlidersDatabase {
  private var onValueEventListeners = mutableListOf<OnValueEventListener>()
  private val database = Firebase.database
  val slidersRef = database.getReference("sliders")

  fun startListeners() {
    slidersRef.addValueEventListener(object : ValueEventListener {
      override fun onDataChange(snapshot: DataSnapshot) {
        // Если слушателей нет, то нет необходимости обрабатывать данные
        if (onValueEventListeners.size == 0) return

        val items = snapshotToSliders(snapshot)

        // Уведомляем всех слушателей об изменении
        onValueEventListeners.forEach { it.onDataChange(items) }
      }

      override fun onCancelled(error: DatabaseError) {
        Log.e("SliderDatabase", "Error: [${error.code}] ${error.message}")
      }
    })
  }

  private fun snapshotToSliders(snapshot: DataSnapshot): List<SlideView> {
    val items = mutableListOf<SlideView>()
    snapshot.children.forEach {
      try {
        val item = it.getValue<SlideView>()         // Приведение значения к классу SlideView
        item!!.key = it.key                         // Запись ключа элемента
        //Log.w("slidersRef", item.toString())
        items.add(item)
      } catch (e: DatabaseException) {
      }

    }
    return items

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


    if (slide.key != "") {
      Log.d("NewChild 1", "key: ${slide.key == null}")

      slidersRef.child(slide.key!!)
        .updateChildren(postValue)
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener { onFailure() }
    } else {
      val child = slidersRef.push()
      Log.d("NewChild 2", "key: ${child.key}")
      slidersRef.child(child.key!!).updateChildren(postValue)
        .addOnSuccessListener { onSuccess() }
        .addOnFailureListener { onFailure() }

//      slidersRef.child(System.currentTimeMillis().toString()).setValue(postValue)
//        .addOnSuccessListener { onSuccess() }
//        .addOnFailureListener { onFailure() }
    }

  }

  fun getSlidersOnce(dbListener: OnValueEventListener) {
    slidersRef.addListenerForSingleValueEvent(object : ValueEventListener {
      override fun onDataChange(snapshot: DataSnapshot) {
        dbListener.onDataChange(snapshotToSliders(snapshot))
      }

      override fun onCancelled(error: DatabaseError) {
      }

    })
  }

  companion object {
    interface OnValueEventListener {
      fun onDataChange(items: List<SlideView>)
    }
  }
}
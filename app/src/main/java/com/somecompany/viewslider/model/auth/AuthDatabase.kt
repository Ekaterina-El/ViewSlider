package com.somecompany.viewslider.model.auth

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class AuthDatabase {
  private val passRef = Firebase.database.getReference("password")

  fun isCorrectPass(password: String, onResponse: (state: Boolean) -> Unit) {
    passRef.addListenerForSingleValueEvent(object: ValueEventListener {
      override fun onDataChange(snapshot: DataSnapshot) {
        onResponse(password == snapshot.getValue<String>().toString())
      }

      override fun onCancelled(error: DatabaseError) {}
    })
  }
}
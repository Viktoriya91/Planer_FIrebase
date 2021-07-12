package com.example.planerfire.fb

import android.content.Context
import android.widget.ArrayAdapter
import com.example.planerfire.Adapter
import com.example.planerfire.MainActivity
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class FirebaseManager(context : Context) {
    var database: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")

    fun saveOffline(){
        Firebase.database.setPersistenceEnabled(true)
        database.keepSynced(true)
    }

    fun insertFirebase(title: String, point : String, dateCreate: String){
        val idItem = database.push().key
        val item = ListItem(idItem, title, point, dateCreate)
        if (idItem != null) {
            database.push().setValue(item)
        }
    }

    fun updateFirebase(id: String, title: String, point : String, dateCreate: String){
        database.orderByChild("id")
            .equalTo(id)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach{
                        val key = it.key.toString()
                        val item = ListItem(id, title, point, dateCreate)
                        val value = item.toMap()
                        val childUpdate = hashMapOf<String, Any>(
                            "/$key" to value

                        )
                        database.updateChildren(childUpdate)
                    }
                }
            })
    }

    fun removeFirebase(id: String?){
        database.orderByChild("id")
            .equalTo(id)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach{
                        val key = it.key.toString()
                        database.child(key).removeValue()
                    }
                }
            })
    }
}
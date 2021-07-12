package com.example.planerfire

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.planerfire.fb.FirebaseManager
import com.example.planerfire.fb.ListItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val manager = FirebaseManager(this)
    val adapter = Adapter(ArrayList(),this)
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()

    }

    override fun onResume() {
        super.onResume()
        allDbAdapter()
    }

    fun init(){
        auth = Firebase.auth
        rcView.layoutManager = LinearLayoutManager(this)
        rcView.adapter = adapter
        val swapHepler = getSwapMg()
        swapHepler.attachToRecyclerView(rcView)
    }

    private fun getSwapMg(): ItemTouchHelper {
        return ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter.removeItem(viewHolder.adapterPosition, manager)
            }
        })
    }

    fun onClickAdd(view: View) {
        val i = Intent(this, EditActivity::class.java)
        startActivity(i)
    }

    fun onClickBack(view: View){
        FirebaseAuth.getInstance().signOut()
        val i = Intent(this, LoginActivity::class.java)
        startActivity(i)
    }
    fun allDbAdapter(){
        val database: DatabaseReference = FirebaseDatabase.getInstance().getReference("users")
        manager.saveOffline()
        val dList = ArrayList<ListItem?>()
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(dList.size > 0) dList.clear()
                if(snapshot!!.exists()) {
                    for (ds in snapshot.children) {
                        val item = ds.getValue(ListItem::class.java)
                        dList.add(item)
                    }
                    adapter.notifyDataSetChanged()
                    adapter.updateAdapter(dList)
                    if (dList.size > 0) {
                        txtEmpty.visibility = View.GONE
                    } else {
                        txtEmpty.visibility = View.VISIBLE
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        database.addValueEventListener(postListener)
        }
}
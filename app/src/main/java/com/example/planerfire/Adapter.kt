package com.example.planerfire

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.planerfire.fb.FirebaseManager
import com.example.planerfire.fb.IntentConstants
import com.example.planerfire.fb.ListItem

class Adapter(listMain:ArrayList<ListItem?>, contextMain: Context):
    RecyclerView.Adapter<Adapter.Holder>() {
    val listArray = listMain
    var context = contextMain
    class Holder(itemView: View, contextView: Context): RecyclerView.ViewHolder(itemView){
        val textTitle: TextView = itemView.findViewById(R.id.txtTitle)
        val textPoint: TextView = itemView.findViewById(R.id.txtPoint)
        val textDateCreate: TextView = itemView.findViewById(R.id.txtDateCreate)
        val context = contextView

        fun setData(item:ListItem){
            textTitle.text = item.title
            textPoint.text = item.point
            textDateCreate.text = item.date_create
            itemView.setOnClickListener{
                val intentCardAdapter = Intent(context, EditActivity::class.java).apply {
                    putExtra(IntentConstants.TITLE_KEY, item.title)
                    putExtra(IntentConstants.POINT_KEY, item.point)
                    putExtra(IntentConstants.DATE_CREATE_KEY, item.date_create)
                    putExtra(IntentConstants.ID_KEY, item.id)
                }
                context.startActivity(intentCardAdapter)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        return Holder(inflater.inflate(R.layout.activity_adapter,parent,false),context)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        listArray.get(position)?.let { holder.setData(it) }
    }

    fun updateAdapter (listItems:List<ListItem?>){
        listArray.clear()
        listArray.addAll(listItems)
        notifyDataSetChanged()
    }

    fun removeItem(position: Int, manager: FirebaseManager){
        val item = listArray[position]
        if (item != null) {
            manager.removeFirebase(item.id)
            listArray.removeAt(position)
            notifyItemRangeChanged(0,listArray.size)
            notifyItemRemoved(position)
        }
    }

    override fun getItemCount(): Int {
        return listArray.size
    }
}
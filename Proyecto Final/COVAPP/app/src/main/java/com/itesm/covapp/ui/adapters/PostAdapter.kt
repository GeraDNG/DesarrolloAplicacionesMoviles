package com.itesm.covapp.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.itesm.covapp.R
import com.itesm.covapp.models.PostModel
import java.util.ArrayList

class PostAdapter(val context: Context,
                  private val postList: ArrayList<PostModel>) :
    RecyclerView.Adapter<PostAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_post,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount() = postList.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val order = postList[position]
        holder.userName.text = order.username
        holder.topic.text = order.topic
        holder.title.text = order.title
        holder.post.text = order.postContent
    }

    class ViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){
        val userName = itemView.findViewById<TextView>(R.id.UserNameItem)
        val topic = itemView.findViewById<TextView>(R.id.txtTopic)
        val title = itemView.findViewById<TextView>(R.id.txtTile)
        val post = itemView.findViewById<TextView>(R.id.txtPost)
        val txtDate = itemView.findViewById<TextView>(R.id.txtDate)
    }
}
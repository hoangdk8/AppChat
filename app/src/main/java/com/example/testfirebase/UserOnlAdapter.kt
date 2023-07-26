package com.example.testfirebase

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserOnlAdapter(
    private val userList: List<UserDatabase.User>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<UserOnlAdapter.UserViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(email: String?)
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewUserEmail: TextView = itemView.findViewById(R.id.text_view_user_onl)
        val imageActive: ImageView = itemView.findViewById(R.id.image_active)
        val imageUnactive: ImageView = itemView.findViewById(R.id.image_unactive)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_user_onl, parent, false)
        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.textViewUserEmail.text = user.email
        if (user.status == true) {
            holder.imageActive.visibility = View.VISIBLE
            holder.imageUnactive.visibility = View.INVISIBLE
        } else {
            holder.imageActive.visibility = View.INVISIBLE
            holder.imageUnactive.visibility = View.VISIBLE
        }
        holder.textViewUserEmail.setOnClickListener {

            onItemClickListener.onItemClick(user.email)

        }
        Log.d("Hoangtv", "onBindViewHolder: $userList")

    }

    override fun getItemCount(): Int {
        return userList.size
    }
}
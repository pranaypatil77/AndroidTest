package com.example.android.androidtest.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android.androidtest.R
import com.example.android.androidtest.model.usersdata.Data
import kotlinx.android.synthetic.main.userslist_items.view.*
import java.util.ArrayList

class UsersAdapter(val udata: ArrayList<Data>):RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.userslist_items,parent,false)
        return UserViewHolder(v)
    }

    override fun getItemCount(): Int {
        return udata.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val name = udata[position].first_name+" "+udata[position].last_name
        //data bind to item view
        holder.itemView.apply {
            tvUsername.text = name
            tvEmail.text = udata[position].email
            val avatar = udata[position].avatar
            Glide.with(imgUser).load(avatar).into(imgUser)
        }
    }
    inner class UserViewHolder(view: View):RecyclerView.ViewHolder(view){

    }
}
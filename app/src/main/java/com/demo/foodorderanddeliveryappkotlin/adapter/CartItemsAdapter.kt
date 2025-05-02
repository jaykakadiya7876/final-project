package com.demo.foodorderanddeliveryappkotlin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.demo.foodorderanddeliveryappkotlin.R
import com.demo.foodorderanddeliveryappkotlin.models.Menus

class CartItemsAdapter(val menuList: List<Menus?>?): RecyclerView.Adapter<CartItemsAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.cart_items_row, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if(menuList == null) 0 else menuList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.menuName.text = menuList?.get(position)?.name
        holder.menuPrice.text = "Price: $" + String.format("%.2f", menuList?.get(position)?.price)
        holder.menuQty.text = "Qty: " + menuList?.get(position)?.totalInCart
        
        Glide.with(holder.thumbImage)
            .load(menuList?.get(position)?.url)
            .into(holder.thumbImage)
    }

    inner class MyViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var menuName: TextView = view.findViewById(R.id.menuName)
        var menuPrice: TextView = view.findViewById(R.id.menuPrice)
        var menuQty: TextView = view.findViewById(R.id.menuQty)
        var thumbImage: ImageView = view.findViewById(R.id.thumbImage)
    }
} 
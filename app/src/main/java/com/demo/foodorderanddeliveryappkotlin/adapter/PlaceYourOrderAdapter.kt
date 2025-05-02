package com.demo.foodorderanddeliveryappkotlin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.demo.foodorderanddeliveryappkotlin.R
import com.demo.foodorderanddeliveryappkotlin.models.Menus

class PlaceYourOrderAdapter(private val menuList: List<Menus>) : 
    RecyclerView.Adapter<PlaceYourOrderAdapter.PlaceYourOrderViewHolder>() {

    class PlaceYourOrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemName: TextView = view.findViewById(R.id.tvItemName)
        val itemPrice: TextView = view.findViewById(R.id.tvItemPrice)
        val itemQuantity: TextView = view.findViewById(R.id.tvItemQuantity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceYourOrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_place_order, parent, false)
        return PlaceYourOrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaceYourOrderViewHolder, position: Int) {
        val menu = menuList[position]
        holder.itemName.text = menu.name
        holder.itemPrice.text = "$${String.format("%.2f", menu.price)}"
        holder.itemQuantity.text = "x${menu.totalInCart}"
    }

    override fun getItemCount() = menuList.size
}
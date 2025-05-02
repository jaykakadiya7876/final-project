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

class CartAdapter(
    private var menuList: List<Menus>,
    private val onItemRemoved: (Menus) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemImage: ImageView = view.findViewById(R.id.ivItemImage)
        val itemName: TextView = view.findViewById(R.id.tvItemName)
        val itemPrice: TextView = view.findViewById(R.id.tvItemPrice)
        val itemQuantity: TextView = view.findViewById(R.id.tvItemQuantity)
        val removeButton: ImageView = view.findViewById(R.id.ivRemove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val menu = menuList[position]
        
        holder.itemName.text = menu.name
        holder.itemPrice.text = "$${String.format("%.2f", menu.price)}"
        holder.itemQuantity.text = "Quantity: ${menu.totalInCart}"
        
        Glide.with(holder.itemImage)
            .load(menu.url)
            .into(holder.itemImage)
            
        holder.removeButton.setOnClickListener {
            onItemRemoved(menu)
        }
    }

    override fun getItemCount() = menuList.size

    fun updateItems(newMenuList: List<Menus>) {
        menuList = newMenuList
        notifyDataSetChanged()
    }
} 
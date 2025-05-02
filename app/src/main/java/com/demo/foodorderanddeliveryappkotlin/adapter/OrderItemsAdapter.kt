package com.demo.foodorderanddeliveryappkotlin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.demo.foodorderanddeliveryappkotlin.R
import com.demo.foodorderanddeliveryappkotlin.models.OrderModel
import java.text.NumberFormat
import java.util.Locale

class OrderItemsAdapter : RecyclerView.Adapter<OrderItemsAdapter.OrderItemViewHolder>() {
    private var items = listOf<OrderModel.OrderItem>()
    private val currencyFormat = NumberFormat.getCurrencyInstance(Locale.US)

    class OrderItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemImage: ImageView = view.findViewById(R.id.itemImage)
        val itemName: TextView = view.findViewById(R.id.itemName)
        val itemPrice: TextView = view.findViewById(R.id.itemPrice)
        val itemQuantity: TextView = view.findViewById(R.id.itemQuantity)
        val itemTotal: TextView = view.findViewById(R.id.itemTotal)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order_detail, parent, false)
        return OrderItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderItemViewHolder, position: Int) {
        val item = items[position]
        
        holder.itemName.text = item.name
        holder.itemPrice.text = currencyFormat.format(item.price)
        holder.itemQuantity.text = "x${item.quantity}"
        holder.itemTotal.text = currencyFormat.format(item.price * item.quantity)

        Glide.with(holder.itemImage.context)
            .load(item.imageUrl)
            .placeholder(R.drawable.placeholder_image)
            .into(holder.itemImage)
    }

    override fun getItemCount() = items.size

    fun updateItems(newItems: List<OrderModel.OrderItem>) {
        items = newItems
        notifyDataSetChanged()
    }
} 
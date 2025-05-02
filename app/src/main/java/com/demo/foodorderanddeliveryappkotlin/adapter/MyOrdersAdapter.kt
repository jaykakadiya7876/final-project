package com.demo.foodorderanddeliveryappkotlin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.demo.foodorderanddeliveryappkotlin.R
import java.text.SimpleDateFormat
import java.util.Locale

class MyOrdersAdapter : RecyclerView.Adapter<MyOrdersAdapter.MyOrdersViewHolder>() {
    private var orders = mutableListOf<Map<String, Any>>()

    fun updateOrders(newOrders: List<Map<String, Any>>) {
        orders.clear()
        orders.addAll(newOrders)
        notifyDataSetChanged()
    }

    class MyOrdersViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val orderIdText: TextView = view.findViewById(R.id.orderIdText)
        val orderDetailsText: TextView = view.findViewById(R.id.orderDetailsText)
        val expandButton: ImageButton = view.findViewById(R.id.expandButton)
        val timelineLayout: View = view.findViewById(R.id.timelineLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyOrdersViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_order, parent, false)
        return MyOrdersViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyOrdersViewHolder, position: Int) {
        val order = orders[position]
        
        // Set order ID
        val orderId = order["id"]?.toString() ?: ""
        holder.orderIdText.text = "Order #$orderId"
        
        // Set order details (items count and status)
        val items = (order["items"] as? List<*>)?.size ?: 0
        val status = order["status"]?.toString() ?: "Pending"
        holder.orderDetailsText.text = "$items Items · ${status.capitalize()}"
        
        // Initially hide timeline
        holder.timelineLayout.visibility = View.GONE
        
        // Set up expand/collapse functionality
        holder.expandButton.setOnClickListener {
            if (holder.timelineLayout.visibility == View.VISIBLE) {
                holder.timelineLayout.visibility = View.GONE
                holder.expandButton.setImageResource(R.drawable.ic_expand)
            } else {
                holder.timelineLayout.visibility = View.VISIBLE
                holder.expandButton.setImageResource(R.drawable.ic_collapse)
            }
        }
    }

    override fun getItemCount() = orders.size
} 
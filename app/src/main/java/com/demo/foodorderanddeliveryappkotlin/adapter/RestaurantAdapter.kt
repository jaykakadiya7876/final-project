package com.demo.foodorderanddeliveryappkotlin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.demo.foodorderanddeliveryappkotlin.R
import com.demo.foodorderanddeliveryappkotlin.models.Restaurant

class RestaurantAdapter(
    private val restaurants: List<Restaurant>,
    private val onRestaurantClick: (Restaurant) -> Unit
) : RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder>() {

    class RestaurantViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivRestaurant: ImageView = view.findViewById(R.id.ivRestaurant)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvDescription: TextView = view.findViewById(R.id.tvDescription)
        val tvRating: TextView = view.findViewById(R.id.tvRating)
        val tvDeliveryTime: TextView = view.findViewById(R.id.tvDeliveryTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_restaurant, parent, false)
        return RestaurantViewHolder(view)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val restaurant = restaurants[position]
        
        // Load image from URL with proper error handling
        Glide.with(holder.itemView.context)
            .load(restaurant.imageUrl)
            .placeholder(android.R.drawable.ic_menu_gallery) // Default Android placeholder
            .into(holder.ivRestaurant)
            
        holder.tvName.text = restaurant.name
        holder.tvDescription.text = restaurant.description
        holder.tvRating.text = "★ ${restaurant.rating}"
        holder.tvDeliveryTime.text = restaurant.deliveryTime

        holder.itemView.setOnClickListener {
            onRestaurantClick(restaurant)
        }
    }

    override fun getItemCount() = restaurants.size
} 
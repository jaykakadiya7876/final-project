package com.demo.foodorderanddeliveryappkotlin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.demo.foodorderanddeliveryappkotlin.R
import com.demo.foodorderanddeliveryappkotlin.models.FirestoreRestaurantModel

class RestaurantListAdapter(
    private val restaurantList: List<FirestoreRestaurantModel>,
    private val clickListener: RestaurantListClickListener
) : RecyclerView.Adapter<RestaurantListAdapter.RestaurantViewHolder>() {

    interface RestaurantListClickListener {
        fun onItemClick(restaurant: FirestoreRestaurantModel)
    }

    class RestaurantViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val restaurantImage: ImageView = view.findViewById(R.id.restaurantImage)
        val restaurantName: TextView = view.findViewById(R.id.restaurantName)
        val ratingBar: RatingBar = view.findViewById(R.id.ratingBar)
        val reviews: TextView = view.findViewById(R.id.reviews)
        val priceRange: TextView = view.findViewById(R.id.priceRange)
        val restaurantType: TextView = view.findViewById(R.id.restaurantType)
        val address: TextView = view.findViewById(R.id.address)
        val openingHours: TextView = view.findViewById(R.id.openingHours)

        fun bind(restaurant: FirestoreRestaurantModel, clickListener: RestaurantListClickListener) {
            itemView.setOnClickListener {
                clickListener.onItemClick(restaurant)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.restaurant_list_row, parent, false)
        return RestaurantViewHolder(view)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val restaurant = restaurantList[position]
        holder.restaurantName.text = restaurant.title
        holder.ratingBar.rating = restaurant.rating.toFloat()
        holder.reviews.text = "(${restaurant.reviews})"
        holder.priceRange.text = restaurant.price
        holder.restaurantType.text = " • ${restaurant.type}"
        holder.address.text = restaurant.address
        holder.openingHours.text = restaurant.openState
        holder.openingHours.setTextColor(
            when {
                restaurant.openState.contains("Open") -> holder.itemView.context.getColor(android.R.color.holo_green_dark)
                else -> holder.itemView.context.getColor(android.R.color.darker_gray)
            }
        )

        Glide.with(holder.itemView.context)
            .load(restaurant.thumbnail)
            .centerCrop()
            .placeholder(R.drawable.restaurant_image_placeholder)
            .into(holder.restaurantImage)

        holder.bind(restaurant, clickListener)
    }

    override fun getItemCount(): Int = restaurantList.size
}

package com.demo.foodorderanddeliveryappkotlin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.demo.foodorderanddeliveryappkotlin.R
import com.demo.foodorderanddeliveryappkotlin.models.FavoriteMenu
import java.text.NumberFormat
import java.util.Locale

class FavoritesAdapter(
    private var favorites: List<FavoriteMenu>,
    private val onFavoriteClick: (FavoriteMenu) -> Unit,
    private val onItemClick: (FavoriteMenu) -> Unit
) : RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder>() {

    class FavoriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val menuImage: ImageView = view.findViewById(R.id.menuImage)
        val menuName: TextView = view.findViewById(R.id.menuName)
        val menuDescription: TextView = view.findViewById(R.id.menuDescription)
        val menuPrice: TextView = view.findViewById(R.id.menuPrice)
        val favoriteButton: ImageButton = view.findViewById(R.id.favoriteButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favorite_menu, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val menu = favorites[position]
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale.US)

        holder.menuName.text = menu.name
        holder.menuDescription.text = menu.description
        holder.menuPrice.text = currencyFormat.format(menu.price)

        Glide.with(holder.menuImage.context)
            .load(menu.imageUrl)
            .placeholder(R.drawable.placeholder_image)
            .into(holder.menuImage)

        holder.favoriteButton.setOnClickListener {
            onFavoriteClick(menu)
        }

        holder.itemView.setOnClickListener {
            onItemClick(menu)
        }
    }

    override fun getItemCount() = favorites.size

    fun updateFavorites(newFavorites: List<FavoriteMenu>) {
        favorites = newFavorites
        notifyDataSetChanged()
    }
} 
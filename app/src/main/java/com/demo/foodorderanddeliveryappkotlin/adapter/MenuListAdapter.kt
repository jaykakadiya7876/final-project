package com.demo.foodorderanddeliveryappkotlin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.demo.foodorderanddeliveryappkotlin.R
import com.demo.foodorderanddeliveryappkotlin.models.FavoriteMenu
import com.demo.foodorderanddeliveryappkotlin.models.Menus
import com.demo.foodorderanddeliveryappkotlin.utils.FavoritesManager

class MenuListAdapter(
    private val menuList: List<Menus?>?,
    private val restaurantId: String,
    private val restaurantName: String,
    private val clickListener: MenuListClickListener
) : RecyclerView.Adapter<MenuListAdapter.MyViewHolder>() {

    private lateinit var favoritesManager: FavoritesManager

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.menu_list_row, parent, false)
        favoritesManager = FavoritesManager(parent.context)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(menuList?.get(position)!!)
    }

    override fun getItemCount(): Int {
        return menuList?.size ?: 0
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var thumbImage: ImageView = view.findViewById(R.id.thumbImage)
        val menuName: TextView = view.findViewById(R.id.menuName)
        val menuPrice: TextView = view.findViewById(R.id.menuPrice)
        val menuDescription: TextView = view.findViewById(R.id.menuDescription)
        val addToCartButton: TextView = view.findViewById(R.id.addToCartButton)
        val addMoreLayout: LinearLayout = view.findViewById(R.id.addMoreLayout)
        val imageMinus: ImageView = view.findViewById(R.id.imageMinus)
        val imageAddOne: ImageView = view.findViewById(R.id.imageAddOne)
        val tvCount: TextView = view.findViewById(R.id.tvCount)
        val favoriteButton: ImageButton = view.findViewById(R.id.favoriteButton)

        fun bind(menu: Menus) {
            menuName.text = menu.name
            menuPrice.text = "$ ${menu.price}"
            menuDescription.text = menu.description ?: ""

            Glide.with(thumbImage)
                .load(menu.url)
                .placeholder(R.drawable.placeholder_image)
                .into(thumbImage)

            // Set up favorite button
            val isFavorite = favoritesManager.isFavorite(menu.name ?: "")
            favoriteButton.setImageResource(
                if (isFavorite) R.drawable.ic_favorite_filled
                else R.drawable.ic_favorite_border
            )

            favoriteButton.setOnClickListener {
                val favoriteMenu = FavoriteMenu(
                    id = menu.name ?: "",
                    name = menu.name ?: "",
                    description = menu.description ?: "",
                    price = menu.price,
                    imageUrl = menu.url ?: "",
                    restaurantId = restaurantId,
                    restaurantName = restaurantName
                )

                if (isFavorite) {
                    favoritesManager.removeFavorite(menu.name ?: "")
                    favoriteButton.setImageResource(R.drawable.ic_favorite_border)
                } else {
                    favoritesManager.addFavorite(favoriteMenu)
                    favoriteButton.setImageResource(R.drawable.ic_favorite_filled)
                }
            }

            if(menu.totalInCart > 0) {
                addMoreLayout.visibility = View.VISIBLE
                addToCartButton.visibility = View.GONE
                tvCount.text = menu.totalInCart.toString()
            } else {
                addMoreLayout.visibility = View.GONE
                addToCartButton.visibility = View.VISIBLE
            }

            addToCartButton.setOnClickListener {
                menu.totalInCart = 1
                clickListener.addToCartClickListener(menu)
                addMoreLayout.visibility = View.VISIBLE
                addToCartButton.visibility = View.GONE
                tvCount.text = menu.totalInCart.toString()
            }

            imageMinus.setOnClickListener {
                var total = menu.totalInCart
                total--
                if(total > 0) {
                    menu.totalInCart = total
                    clickListener.updateCartClickListener(menu)
                    tvCount.text = total.toString()
                } else {
                    menu.totalInCart = total
                    clickListener.removeFromCartClickListener(menu)
                    addMoreLayout.visibility = View.GONE
                    addToCartButton.visibility = View.VISIBLE
                }
            }

            imageAddOne.setOnClickListener {
                var total = menu.totalInCart
                total++
                if(total <= 10) {
                    menu.totalInCart = total
                    clickListener.updateCartClickListener(menu)
                    tvCount.text = total.toString()
                }
            }
        }
    }

    interface MenuListClickListener {
        fun addToCartClickListener(menu: Menus)
        fun updateCartClickListener(menu: Menus)
        fun removeFromCartClickListener(menu: Menus)
    }
}
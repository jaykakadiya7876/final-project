package com.demo.foodorderanddeliveryappkotlin.activities

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.foodorderanddeliveryappkotlin.R
import com.demo.foodorderanddeliveryappkotlin.adapter.FavoritesAdapter
import com.demo.foodorderanddeliveryappkotlin.models.FavoriteMenu
import com.demo.foodorderanddeliveryappkotlin.utils.FavoritesManager

class FavoritesActivity : BaseActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var favoritesAdapter: FavoritesAdapter
    private lateinit var favoritesManager: FavoritesManager
    private lateinit var emptyText: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        favoritesManager = FavoritesManager(this)
        emptyText = findViewById(R.id.emptyText)
        setupRecyclerView()
        setSelectedItem(R.id.nav_favorites)
    }

    override fun onResume() {
        super.onResume()
        loadFavorites()
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.favoritesRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        
        favoritesAdapter = FavoritesAdapter(
            favorites = emptyList(),
            onFavoriteClick = { menu ->
                favoritesManager.removeFavorite(menu.id)
                loadFavorites()
            },
            onItemClick = { menu ->
                // Navigate to restaurant menu with this item
                navigateTo(RestaurantMenuActivity::class.java) { intent ->
                    intent.putExtra("restaurantId", menu.restaurantId)
                }
            }
        )
        
        recyclerView.adapter = favoritesAdapter
    }

    private fun loadFavorites() {
        val favorites = favoritesManager.getFavorites()
        favoritesAdapter.updateFavorites(favorites)
        
        // Show/hide empty state
        emptyText.visibility = if (favorites.isEmpty()) View.VISIBLE else View.GONE
        recyclerView.visibility = if (favorites.isEmpty()) View.GONE else View.VISIBLE
    }
} 
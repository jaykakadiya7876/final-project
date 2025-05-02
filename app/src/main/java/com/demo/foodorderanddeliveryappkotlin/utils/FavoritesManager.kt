package com.demo.foodorderanddeliveryappkotlin.utils

import android.content.Context
import android.content.SharedPreferences
import com.demo.foodorderanddeliveryappkotlin.models.FavoriteMenu
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FavoritesManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    fun addFavorite(menu: FavoriteMenu) {
        val favorites = getFavorites().toMutableList()
        if (!favorites.any { it.id == menu.id }) {
            favorites.add(menu)
            saveFavorites(favorites)
        }
    }

    fun removeFavorite(menuId: String) {
        val favorites = getFavorites().toMutableList()
        favorites.removeAll { it.id == menuId }
        saveFavorites(favorites)
    }

    fun isFavorite(menuId: String): Boolean {
        return getFavorites().any { it.id == menuId }
    }

    fun getFavorites(): List<FavoriteMenu> {
        val json = prefs.getString(KEY_FAVORITES, null) ?: return emptyList()
        val type = object : TypeToken<List<FavoriteMenu>>() {}.type
        return try {
            gson.fromJson(json, type)
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun saveFavorites(favorites: List<FavoriteMenu>) {
        val json = gson.toJson(favorites)
        prefs.edit().putString(KEY_FAVORITES, json).apply()
    }

    companion object {
        private const val PREFS_NAME = "FoodAppFavorites"
        private const val KEY_FAVORITES = "favorites"
    }
} 
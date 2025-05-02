package com.demo.foodorderanddeliveryappkotlin.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.demo.foodorderanddeliveryappkotlin.R
import com.demo.foodorderanddeliveryappkotlin.adapter.RestaurantAdapter
import com.demo.foodorderanddeliveryappkotlin.models.Hours
import com.demo.foodorderanddeliveryappkotlin.models.Menus
import com.demo.foodorderanddeliveryappkotlin.models.Restaurant
import com.demo.foodorderanddeliveryappkotlin.models.RestaurantModel
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log
import androidx.appcompat.widget.SearchView
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class RestaurantListActivity : BaseActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var recyclerView: RecyclerView
    private lateinit var restaurantAdapter: RestaurantAdapter
    private val restaurantsList = mutableListOf<Restaurant>()
    private val filteredList = mutableListOf<Restaurant>()
    private var selectedType: String? = null
    private var selectedRating: Double? = null
    private var currentSearchQuery: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_list)

        // Set the title
        supportActionBar?.let { actionBar ->
            actionBar.title = "Restaurants Near You"
        }

        db = FirebaseFirestore.getInstance()
        setSelectedItem(R.id.nav_home)

        recyclerView = findViewById(R.id.recyclerViewRestaurant)
        recyclerView.layoutManager = LinearLayoutManager(this)
        restaurantAdapter = RestaurantAdapter(filteredList) { restaurant ->
            Log.d("RestaurantClick", "Adapter onClick lambda invoked in Activity for restaurant: ${restaurant.name}")
            loadRestaurantMenus(restaurant)
        }
        recyclerView.adapter = restaurantAdapter

        // Set up filter chips
        setupFilterChips()

        // Set up search view
        setupSearchView()

        // Load restaurants with a slight delay to ensure UI is set up
        recyclerView.post {
            loadRestaurants()
        }
    }
    
    // Flag to prevent multiple clicks
    private var isLoadingMenu = false
    
    private fun setupFilterChips() {
        val chipType = findViewById<Chip>(R.id.chipType)
        val chipRating = findViewById<Chip>(R.id.chipRating)

        chipType.setOnClickListener {
            showCuisineTypeDialog()
        }

        chipRating.setOnClickListener {
            showRatingDialog()
        }
    }

    private fun showCuisineTypeDialog() {
        // Get unique types from all restaurants
        val allTypes = restaurantsList.flatMap { restaurant -> 
            restaurant.types ?: listOf()
        }.distinct().filter { 
            // Filter out generic "Restaurant" type and empty strings
            it.isNotBlank() && it != "Restaurant" 
        }.sorted()

        val types = listOf("All") + allTypes

        MaterialAlertDialogBuilder(this)
            .setTitle("Select Restaurant Type")
            .setItems(types.toTypedArray()) { _, which ->
                selectedType = if (which == 0) null else types[which]
                applyFilters()
            }
            .show()
    }

    private fun showRatingDialog() {
        val ratings = listOf("All", "4.5+", "4.0+", "3.5+", "3.0+")
        MaterialAlertDialogBuilder(this)
            .setTitle("Select Minimum Rating")
            .setItems(ratings.toTypedArray()) { _, which ->
                selectedRating = when (which) {
                    0 -> null
                    1 -> 4.5
                    2 -> 4.0
                    3 -> 3.5
                    4 -> 3.0
                    else -> null
                }
                applyFilters()
            }
            .show()
    }

    private fun setupSearchView() {
        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                currentSearchQuery = query?.trim()?.lowercase() ?: ""
                applyFilters()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                currentSearchQuery = newText?.trim()?.lowercase() ?: ""
                applyFilters()
                return true
            }
        })
    }

    private fun applyFilters() {
        filteredList.clear()
        filteredList.addAll(restaurantsList.filter { restaurant ->
            val matchesSearch = if (currentSearchQuery.isNotBlank()) {
                restaurant.name.lowercase().contains(currentSearchQuery) ||
                restaurant.description.lowercase().contains(currentSearchQuery) ||
                restaurant.types.any { it.lowercase().contains(currentSearchQuery) }
            } else {
                true
            }

            val matchesType = selectedType?.let { type ->
                restaurant.types.contains(type)
            } ?: true

            val matchesRating = selectedRating?.let { minRating ->
                restaurant.rating >= minRating
            } ?: true

            matchesSearch && matchesRating && matchesType
        })
        restaurantAdapter.notifyDataSetChanged()

        if (filteredList.isEmpty()) {
            Toast.makeText(this, "No restaurants match the selected filters", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadRestaurantMenus(restaurant: Restaurant) {
        Log.d("LoadMenu", "loadRestaurantMenus started for restaurant ID: ${restaurant.id}")
        
        // Prevent multiple simultaneous loading attempts
        if (isLoadingMenu) {
            Log.w("LoadMenu", "Already loading a menu, ignoring click for ID: ${restaurant.id}")
            return
        }
        
        // Disable the RecyclerView to prevent multiple clicks
        Log.d("LoadMenu", "Disabling RecyclerView")
        recyclerView.isEnabled = false
        
        isLoadingMenu = true
        
        // Show loading state
        Log.d("LoadMenu", "Showing loading toast for ID: ${restaurant.id}")
        Toast.makeText(this, "Loading menu... "+restaurant.id, Toast.LENGTH_SHORT).show()
        
        // Log before Firestore call
        Log.d("LoadMenu", "Attempting to fetch document from Firestore: Restaurant/${restaurant.id}")
        // Get the restaurant document from Firestore
        db.collection("Restaurant").document(restaurant.id)
            .get()
            .addOnSuccessListener { document ->
                Log.d("LoadMenu", "Firestore get() success for ID: ${restaurant.id}. Document exists: ${document?.exists()}")
                if (document != null && document.exists()) {
                    try {
                        Log.d("LoadMenu", "Document exists. Parsing data...")
                        val menuList = mutableListOf<Menus>()
                        
                        // Log all available fields in the document for debugging
                        Log.d("LoadMenu", "Document fields: ${document.data?.keys?.joinToString()}")
                        
                        // Get menu items from the restaurant document
                        @Suppress("UNCHECKED_CAST")
                        val menuItems = document.get("menus") as? List<Map<String, Any>> ?: listOf()
                        Log.d("LoadMenu", "Found ${menuItems.size} raw menu items in Firestore.")

                        if (menuItems.isNotEmpty()) {
                            for (item in menuItems) {
                                val name = item["name"] as? String ?: ""
                                val description = item["description"] as? String ?: ""
                                val imageUrl = item["image_url"] as? String ?: ""
                                val priceStr = item["price"] as? String ?: "$0.00"
                                
                                // Log individual menu item
                                Log.d("LoadMenu", "Menu item: name=$name, desc=$description, price=$priceStr")
                                
                                // Parse the price (remove $ sign and convert to float)
                                val price = priceStr.replace("$", "").toFloatOrNull() ?: 0.0f
                                
                                menuList.add(Menus(
                                    name = name,
                                    price = price,
                                    url = imageUrl,
                                    totalInCart = 0,
                                    description = description
                                ))
                            }
                        } else {
                            // If no menu items found, add a placeholder item
                            Log.w("LoadMenu", "No menu items found in Firestore for ${restaurant.name}. Adding placeholder.")
                            menuList.add(Menus(
                                name = "Menu not available",
                                price = 0.0f,
                                url = restaurant.imageUrl,
                                totalInCart = 0,
                                description = "Please check back later for menu items"
                            ))
                        }
                        
                        // Get operating hours if available
                        @Suppress("UNCHECKED_CAST")
                        val operatingHoursMap = document.get("operating_hours") as? Map<String, String>
                        val hours = if (operatingHoursMap != null) {
                            Hours(
                                Sunday = operatingHoursMap["sunday"] ?: "",
                                Monday = operatingHoursMap["monday"] ?: "",
                                Tuesday = operatingHoursMap["tuesday"] ?: "",
                                Wednesday = operatingHoursMap["wednesday"] ?: "",
                                Thursday = operatingHoursMap["thursday"] ?: "",
                                Friday = operatingHoursMap["friday"] ?: "",
                                Saturday = operatingHoursMap["saturday"] ?: ""
                            )
                        } else {
                            null
                        }
                        
                        // Create RestaurantModel from Restaurant and Firestore data
                        Log.d("LoadMenu", "Creating RestaurantModel...")
                        val restaurantModel = RestaurantModel(
                            id = restaurant.id,
                            name = restaurant.name,
                            address = document.getString("address") ?: restaurant.description,
                            delivery_charge = "0.00", // Could get from a field in Firestore
                            image = restaurant.imageUrl,
                            hours = hours,
                            menus = menuList,
                            type = document.getString("type") ?: "",
                            rating = restaurant.rating.toFloat(),
                            reviews = document.getLong("reviews")?.toInt() ?: 0
                        )
                        
                        // Navigate to RestaurantMenuActivity with the restaurant model
                        Log.i("LoadMenu", "Preparing to navigate to RestaurantMenuActivity with restaurant: ${restaurantModel.name}")
                        
                        // Reset loading flag before starting the activity
                        isLoadingMenu = false
                        Log.d("LoadMenu", "isLoadingMenu reset to false.")
                        
                        // Re-enable recycler view
                        recyclerView.isEnabled = true
                        
                        // Use the new navigateTo method to navigate to the RestaurantMenuActivity
                        navigateTo(RestaurantMenuActivity::class.java) { intent ->
                            intent.putExtra("RestaurantModel", restaurantModel)
                        }
                        
                    } catch (e: Exception) {
                        Log.e("LoadMenu", "Error parsing menu data or creating intent for ID: ${restaurant.id}", e)
                        Toast.makeText(this, "Error loading menu data: ${e.message}", Toast.LENGTH_SHORT).show()
                        isLoadingMenu = false
                        recyclerView.isEnabled = true 
                    }
                } else {
                    Log.e("LoadMenu", "Firestore document Restaurant/${restaurant.id} does not exist.")
                    Toast.makeText(this, "Restaurant data not found", Toast.LENGTH_SHORT).show()
                    // Reset state
                    isLoadingMenu = false
                    recyclerView.isEnabled = true
                }
            }
            .addOnFailureListener { e ->
                Log.e("LoadMenu", "Firestore get() failed for ID: ${restaurant.id}", e)
                Toast.makeText(this, "Failed to load restaurant menu: ${e.message}", Toast.LENGTH_SHORT).show()
                // Reset state
                isLoadingMenu = false
                recyclerView.isEnabled = true
            }
    }

    private fun loadRestaurants() {
        // Show loading indicator
        val progressBar = findViewById<android.widget.ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        
        db.collection("Restaurant")
            .get()
            .addOnSuccessListener { documents ->
                restaurantsList.clear()
                filteredList.clear()
                if (documents.isEmpty) {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, "No restaurants found. Please add data to Firestore.", Toast.LENGTH_LONG).show()
                } else {
                    for (document in documents) {
                        try {
                            val data = document.data
                            @Suppress("UNCHECKED_CAST")
                            val restaurant = Restaurant(
                                id = document.id,
                                name = data["title"] as? String ?: "",
                                description = data["description"] as? String ?: "",
                                imageUrl = data["thumbnail"] as? String ?: "",
                                rating = (data["rating"] as? Double) ?: 0.0,
                                deliveryTime = (data["open_state"] as? String) ?: "Open",
                                deliveryFee = 0.0,  // Default or can be extracted from another field
                                types = (data["types"] as? List<String>) ?: listOf()
                            )
                            restaurantsList.add(restaurant)
                            filteredList.add(restaurant)
                            android.util.Log.d("RestaurantListActivity", "Added restaurant: ${restaurant.name}")
                        } catch (e: Exception) {
                            android.util.Log.e("RestaurantListActivity", "Error parsing restaurant data", e)
                        }
                    }
                }
                restaurantAdapter.notifyDataSetChanged()
                progressBar.visibility = View.GONE
                
                // Log the count for debugging
                android.util.Log.d("RestaurantListActivity", "Loaded ${restaurantsList.size} restaurants")
                
                // If still empty, show a message
                if (restaurantsList.isEmpty()) {
                    Toast.makeText(this, "No restaurants found. Please check your Firestore database.", Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener {
                progressBar.visibility = View.GONE
                Toast.makeText(this, "Failed to load restaurants: ${it.message}", Toast.LENGTH_LONG).show()
                android.util.Log.e("RestaurantListActivity", "Failed to load restaurants", it)
            }
    }
    
    private fun extractPrice(priceStr: String?): Double {
        return try {
            priceStr?.replace("$", "")?.replace("–50", "")?.toDoubleOrNull() ?: 0.0
        } catch (e: Exception) {
            0.0
        }
    }
}

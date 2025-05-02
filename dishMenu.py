import requests
import json
import random
images = ["https://www.foodiesfeed.com/wp-content/uploads/2023/05/pizza-margherita-with-fresh-basil.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/09/latte-art.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/ff-images/2025/03/delicious-pistachios-in-a-close-up-view.png",
"https://www.foodiesfeed.com/wp-content/uploads/2023/09/pears.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/09/red-apple-background.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/05/steak-cooked-on-fire.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/03/bacon-cheeseburger-close-up.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/06/coffee-latte-with-croissant.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/06/perfect-coffee-orange-desk.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/12/pink-macarons.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/10/caipirinha-top-view.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/09/mexican-tacos-lined-up.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/05/slice-of-pizza-salami.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/05/fried-chicken-commercial.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/12/cutting-pizza.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/10/hot-chocolate-with-cream.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/07/healthy-foods.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/06/raspberry-cream-waffle.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/04/avocado-chicken-salad.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/ff-images/2025/01/flavorful-shrimp-feast-with-lemon-and-corn.png",
"https://www.foodiesfeed.com/wp-content/uploads/2023/05/freshly-prepared-beef-steak-with-vegetables.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/10/coffee-book.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/09/limes.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/ff-images/2025/04/fresh-and-juicy-tomatoes-with-water-droplets.png",
"https://www.foodiesfeed.com/wp-content/uploads/ff-images/2025/01/delicious-mushroom-soup-with-fresh-herbs.png",
"https://www.foodiesfeed.com/wp-content/uploads/2024/01/best-burger-in-town.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/08/grilled-crispy-pork-with-rice.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/04/fresh-fruit-salad-with-mint.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/09/beef-cut-close-up.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/09/green-apple-full-frame.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/07/butter-chicken-in-a-pan.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/05/blueberries-full-frame.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/08/sushi-roll-macro.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/07/beans-and-legumes-in-compartments.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/09/tacos.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/04/delicious-steak-with-herbs-cut-on-slices.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/09/pile-of-peaches.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/08/apple-macro-shot.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/04/grilled-whole-chicken.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/12/pizza-salami-close-up.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/ff-images/2024/12/decadent-pavlova-topped-with-fresh-berries-delight.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/08/lemon-with-leaves.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/07/fresh-fruit-platter.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/06/burger-with-melted-cheese.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/09/fresh-vegetables.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/ff-images/2024/12/refreshing-lemon-cheesecake-slice-with-mint-garnish.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/ff-images/2025/01/colorful-bowl-of-deliciousness-with-fried-egg.png",
"https://www.foodiesfeed.com/wp-content/uploads/2023/04/strawberry-milk-splash.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/10/bowl-of-ice-cream-with-chocolate.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/08/crispy-spicy-chicken-wings.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/05/slice-of-lime-under-water.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/09/healthy-food.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/06/boiled-eggs-on-a-plate.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/09/peaches.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/06/ice-cream-cone-splash.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/ff-images/2025/01/fresh-salmon-salad-with-herbs-and-citrus.png",
"https://www.foodiesfeed.com/wp-content/uploads/2023/06/pouring-honey-on-pancakes.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/09/broccoli-and-bell-peppers.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/10/hot-chocolate.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/05/juicy-cheeseburger.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/05/pizza-salami.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/05/coffee-beans-falling.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/ff-images/2025/03/refreshing-cucumber-mint-cooler-in-sparkling-drink.png",
"https://www.foodiesfeed.com/wp-content/uploads/ff-images/2025/01/colorful-healthy-fruit-juices-and-fresh-ingredients.png",
"https://www.foodiesfeed.com/wp-content/uploads/2020/05/suco-de-limao-com-slash.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/06/coffee-crema.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/08/tartelettes-top-view.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/04/apple-water-splash.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/04/grilled-cheese-sandwich.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/03/full-frame-of-fresh-strawberries.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/03/coffee-beans.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2015/09/summer-barbeque-feast.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/10/glass-of-hot-chocolate.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/08/lemons-with-drops-of-water.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/08/spicy-meatballs.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/06/banana-milkshake.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/06/chinese-dumplings.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/05/exotic-spices.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/05/fresh-fruit-and-berries.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/05/colorful-vegetables-stir-fry.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/05/bowl-of-ramen-soup.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/05/classic-pizza-margherita-with-basil-leaves.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/04/man-holding-a-wooden-box-with-fresh-vegetables.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/04/orange-juice-splash.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/03/cupcake-with-colorful-icing.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2019/03/iced-coffee-with-milk-cafe-latte.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/ff-images/2025/01/fresh-red-apples-on-a-rustic-wooden-surface.png",
"https://www.foodiesfeed.com/wp-content/uploads/ff-images/2025/01/stylish-cupcakes-with-pink-frosting-and-bows.png",
"https://www.foodiesfeed.com/wp-content/uploads/ff-images/2024/12/fresh-blackberries-close-up-on-green-background.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/12/neapolitan-pizza-margherita.jpg",
"https://www.foodiesfeed.com/wp-content/uploads/2023/10/mediterranean-chickpea-salad.jpg"]
def restaurantFetcher(page):
    headers = {
        "accept": "application/json",
        "accept-language": "en-US,en;q=0.9,gu;q=0.8,ru;q=0.7,hi;q=0.6",
        "authorization": "Bearer a80d327c-2104-47c5-8ac7-3c4218effd9c",
        "cache-control": "max-age=0",
        "if-modified-since": "0",
        "origin": "https://www.grubhub.com",
        "perimeter-x": "eyJ1IjoiMmEyM2Q1YjAtMjVlNy0xMWYwLWI2OTctM2Y3ZWE3MTEzMmRkIiwidiI6IjBjMGVhM2Q3LTI1NzMtMTFmMC1hZGEzLTVhNzc4NWM5ZjJmZCIsInQiOjE3NDYwMzQwMjQ1ODEsImgiOiJiNTcxOWU4NjZmYmYwZmQ5NTVhZDEyMGMyMjc2ZWJhYjQ5ZDg0NTFlOWQ0OTY3NDhlZjE1MDg2NTRmNWYxZGM3In0=",
        "pragma": "no-cache",
        "priority": "u=1, i",
        "referer": "https://www.grubhub.com/",
        "sec-ch-ua": '"Google Chrome";v="135", "Not-A.Brand";v="8", "Chromium";v="135"',
        "sec-ch-ua-mobile": "?0",
        "sec-ch-ua-platform": '"Windows"',
        "sec-fetch-dest": "empty",
        "sec-fetch-mode": "cors",
        "sec-fetch-site": "same-site",
        "user-agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/135.0.0.0 Safari/537.36",
    }

    response = requests.get(
        "https://api-gtm.grubhub.com/topics-gateway/v1/topic/content?pageSource=HOME&topicSource=search%2Flisting&applicationId=web&topicId=8b13ddfc-29b0-4452-b943-fa79e5cceebf&locationMode=DELIVERY&operationId=47b6ab6f-da05-4617-994a-e00e8069c94f&position="
        + str(page)
        + "&location=POINT(-74.05992127%2040.74834060)&parameter=location.wkt%3APOINT(-74.05992127%2040.74834060)&parameter=locationMode%3ADELIVERY&parameter=radius%3A4&geohash=dr5rfdp0qc0p",
        headers=headers,
    )
    restaurantMenu = []
    for restaurant in response.json()["object"]["data"]["content"]:
        print(restaurant["entity"]["restaurant_id"])
        id = restaurant["entity"]["restaurant_id"]
        catData = getCats(id)
        if len(catData) > 10:
            restaurantMenu.append(catData)
    print("Total Restaurants Fetched: ", len(restaurantMenu))
    
    return restaurantMenu


def getCats(id):

	headers = {
		"accept": "application/json",
		"accept-language": "en-US,en;q=0.9,gu;q=0.8,ru;q=0.7,hi;q=0.6",
		"authorization": "Bearer a80d327c-2104-47c5-8ac7-3c4218effd9c",
		"cache-control": "max-age=0",
		"if-modified-since": "0",
		"origin": "https://www.grubhub.com",
		"perimeter-x": "eyJ1IjoiMmEyM2Q1YjAtMjVlNy0xMWYwLWI2OTctM2Y3ZWE3MTEzMmRkIiwidiI6IjBjMGVhM2Q3LTI1NzMtMTFmMC1hZGEzLTVhNzc4NWM5ZjJmZCIsInQiOjE3NDYwMzQwMjQ1ODEsImgiOiJiNTcxOWU4NjZmYmYwZmQ5NTVhZDEyMGMyMjc2ZWJhYjQ5ZDg0NTFlOWQ0OTY3NDhlZjE1MDg2NTRmNWYxZGM3In0=",
		"pragma": "no-cache",
		"priority": "u=1, i",
		"referer": "https://www.grubhub.com/",
		"sec-ch-ua": '"Google Chrome";v="135", "Not-A.Brand";v="8", "Chromium";v="135"',
		"sec-ch-ua-mobile": "?0",
		"sec-ch-ua-platform": '"Windows"',
		"sec-fetch-dest": "empty",
		"sec-fetch-mode": "cors",
		"sec-fetch-site": "same-site",
		"user-agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/135.0.0.0 Safari/537.36",
	}

	response = requests.get(
		"https://api-gtm.grubhub.com/restaurant_gateway/info/nonvolatile/"
		+ str(id)
		+ "?orderType=STANDARD&platform=WEB&enhancedFeed=true&location=POINT(-74.05992127%2040.74834060)",
		headers=headers,
	)
	menu = []
	for feed in response.json()["object"]["data"]["enhanced_feed"]:
		if "MENU_ITEM" == feed["data_type"]:
			if feed["id"] != "None":
				try:
					print(feed["id"])
					catId = feed["id"]
					menu.extend(menuFetcher(id, catId))
				except:
					pass
	print("Main Menu Length: ", len(menu))
	return menu


def menuFetcher(id, catId):
	headers = {
		"accept": "application/json",
		"accept-language": "en-US,en;q=0.9,gu;q=0.8,ru;q=0.7,hi;q=0.6",
		"authorization": "Bearer a80d327c-2104-47c5-8ac7-3c4218effd9c",
		"cache-control": "max-age=0",
		"if-modified-since": "0",
		"origin": "https://www.grubhub.com",
		"perimeter-x": "eyJ1IjoiMmEyM2Q1YjAtMjVlNy0xMWYwLWI2OTctM2Y3ZWE3MTEzMmRkIiwidiI6IjBjMGVhM2Q3LTI1NzMtMTFmMC1hZGEzLTVhNzc4NWM5ZjJmZCIsInQiOjE3NDYwMzQwMjQ1ODEsImgiOiJiNTcxOWU4NjZmYmYwZmQ5NTVhZDEyMGMyMjc2ZWJhYjQ5ZDg0NTFlOWQ0OTY3NDhlZjE1MDg2NTRmNWYxZGM3In0=",
		"pragma": "no-cache",
		"priority": "u=1, i",
		"referer": "https://www.grubhub.com/",
		"sec-ch-ua": '"Google Chrome";v="135", "Not-A.Brand";v="8", "Chromium";v="135"',
		"sec-ch-ua-mobile": "?0",
		"sec-ch-ua-platform": '"Windows"',
		"sec-fetch-dest": "empty",
		"sec-fetch-mode": "cors",
		"sec-fetch-site": "same-site",
		"user-agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/135.0.0.0 Safari/537.36",
	}

	response = requests.get(
		"https://api-gtm.grubhub.com/restaurant_gateway/feed/"
		+ str(id)
		+ "/"
		+ str(catId)
		+ "?time=1746028620333&location=POINT(-74.05992127%2040.74834060)&operationId=c0533730-25db-11f0-b731-4929a3eb56f6&isFutureOrder=false&restaurantStatus=ORDERABLE&brandUuid=615f1a80-34ad-11e9-9dcb-a7f1979d50be&isNonRestaurantMerchant=false&merchantTypes=&orderType=STANDARD&agent=false&task=CATEGORY&platform=WEB&weightedItemDataIncluded=true",
		headers=headers,
	)
	menu = []
	for content in response.json()["object"]["data"]["content"]:
		dish = {}
		try:
			dish["name"] = content["entity"]["item_name"]
		except:
			dish["name"] = ""
		try:
			dish["description"] = content["entity"]["item_description"]
		except:
			dish["description"] = ""
		try:
			dish["image_url"] = (
				content["entity"]["media_image"]["base_url"]
				+ "d_search:browse-images:default.jpg/"
				+ content["entity"]["media_image"]["public_id"]
			)
		except:
			dish["image_url"] = random.choice(images)
		try:
			dish["price"] = content["entity"]["item_price"]["delivery"]["styled_text"][
				"text"
			]
		except:
			dish["price"] = "$"
		if len(dish["name"]) > 5:
			menu.append(dish)
	print("Menu Length: ", len(menu))
	return menu

restaurentMenus = []

for i in range(1, 10):
	try:
		restaurentMenus.extend(restaurantFetcher(i))
		print("Total Menus: ", len(restaurentMenus))
	except Exception as e:
		print("Error: ", e)
		break

with open("restaurantMenu.json", "w") as outfile:
	outfile.write(json.dumps(restaurentMenus, indent=4))
 
 
# package com.demo.foodorderanddeliveryappkotlin.models

# import android.os.Parcel
# import android.os.Parcelable

# data class RestaurentModel(
#     val name: String?,
#     val address: String?,
#     val delivery_charge: String?,
#     val image: String?,
#     val hours: Hours?,
#     var menus: List<Menus?>?,
#     val type: String? = "",
#     val rating: Float = 0.0f,
#     val reviews: Int = 0
# ) : Parcelable {
#     constructor(parcel: Parcel) : this(
#         parcel.readString(),
#         parcel.readString(),
#         parcel.readString(),
#         parcel.readString(),
#         parcel.readParcelable(Hours::class.java.classLoader),
#         parcel.createTypedArrayList(Menus.CREATOR),
#         parcel.readString(),
#         parcel.readFloat(),
#         parcel.readInt()
#     )

#     override fun writeToParcel(parcel: Parcel, flags: Int) {
#         parcel.writeString(name)
#         parcel.writeString(address)
#         parcel.writeString(delivery_charge)
#         parcel.writeString(image)
#         parcel.writeParcelable(hours, flags)
#         parcel.writeTypedList(menus)
#         parcel.writeString(type)
#         parcel.writeFloat(rating)
#         parcel.writeInt(reviews)
#     }

#     override fun describeContents(): Int {
#         return 0
#     }

#     companion object CREATOR : Parcelable.Creator<RestaurentModel> {
#         override fun createFromParcel(parcel: Parcel): RestaurentModel {
#             return RestaurentModel(parcel)
#         }

#         override fun newArray(size: Int): Array<RestaurentModel?> {
#             return arrayOfNulls(size)
#         }
#     }
# }

# data class Menus(val name: String?, val price: Float, val url: String?, var totalInCart: Int, val description: String? = null) :
#     Parcelable {
#     constructor(parcel: Parcel) : this(
#         parcel.readString(),
#         parcel.readFloat(),
#         parcel.readString(),
#         parcel.readInt(),
#         parcel.readString()
#     )

#     override fun writeToParcel(parcel: Parcel, flags: Int) {
#         parcel.writeString(name)
#         parcel.writeFloat(price)
#         parcel.writeString(url)
#         parcel.writeInt(totalInCart)
#         parcel.writeString(description)
#     }

#     override fun describeContents(): Int {
#         return 0
#     }

#     companion object CREATOR : Parcelable.Creator<Menus> {
#         override fun createFromParcel(parcel: Parcel): Menus {
#             return Menus(parcel)
#         }

#         override fun newArray(size: Int): Array<Menus?> {
#             return arrayOfNulls(size)
#         }
#     }
# }
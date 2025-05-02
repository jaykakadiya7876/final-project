import json
import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore

# Path to your service account key JSON file
SERVICE_ACCOUNT_KEY_PATH = 'foodorder-firebase-adminsdk.json'

# Initialize the Firebase Admin SDK
cred = credentials.Certificate(SERVICE_ACCOUNT_KEY_PATH)
firebase_admin.initialize_app(cred)

# Get a Firestore client
db = firestore.client()

# Path to your JSON file containing the restaurant data
JSON_FILE_PATH = 'restaurentData.json'

MENU_JSON_FILE_PATH = "restaurantMenu.json"

# Collection name in Firestore
COLLECTION_NAME = 'Restaurant'


def import_data():
	"""Imports restaurant data from a JSON file into Cloud Firestore."""

	with open(JSON_FILE_PATH, 'r',encoding="utf-8") as f:
		restaurant_data = json.load(f)
	with open(MENU_JSON_FILE_PATH, 'r',encoding="utf-8") as f:
		menu_data = json.load(f)
	count = 0
	for restaurant in restaurant_data:
		try:
			del restaurant['unsupported_extensions']
		except:
			pass
		try:
			del restaurant['extensions']
		except:
			pass
		try:
			del restaurant['reserve_a_table']
		except:
			pass
		try:
			del restaurant['type_ids']
		except:
			pass
		try:
			del restaurant['type_id']
		except:
			pass
		try:
			del restaurant['position']
		except:
			pass
		try:
			del restaurant['serpapi_thumbnail']
		except:
			pass
		try:
			del restaurant['provider_id']
		except:
			pass
		try:
			del restaurant['place_id_search']
		except:
			pass
		try:
			del restaurant['reviews_link']
		except:
			pass
		try:
			del restaurant['photos_link']
		except:
			pass
		try:
			restaurant["menus"] = menu_data[count]
			count += 1
			# Add each restaurant as a new document in the "Restaurant" collection
			db.collection(COLLECTION_NAME).add(restaurant)
			print(f"Imported restaurant: {restaurant.get('title')}")
		except Exception as e:
			try:
				restaurant["menus"] = menu_data[count]
				count += 1
				# Add each restaurant as a new document in the "Restaurant" collection
				db.collection(COLLECTION_NAME).add(restaurant)
				print(f"Imported restaurant: {restaurant.get('title')}")
			except Exception as e:
				try:
					restaurant["menus"] = menu_data[count]
					count += 1
					# Add each restaurant as a new document in the "Restaurant" collection
					db.collection(COLLECTION_NAME).add(restaurant)
					print(f"Imported restaurant: {restaurant.get('title')}")
				except Exception as e:
					try:
						restaurant["menus"] = menu_data[count]
						count += 1
						# Add each restaurant as a new document in the "Restaurant" collection
						db.collection(COLLECTION_NAME).add(restaurant)
						print(f"Imported restaurant: {restaurant.get('title')}")
					except Exception as e:
						print(f"Error importing restaurant: {restaurant.get('title')}. Error: {e}")


if __name__ == "__main__":
    import_data()
    print("Data import complete!")

package com.demo.foodorderanddeliveryappkotlin.models

import android.os.Parcel
import android.os.Parcelable

data class RestaurantModel(
    val id: String? = null,
    val name: String?,
    val address: String?,
    val delivery_charge: String?,
    val image: String?,
    val hours: Hours?,
    var menus: List<Menus?>?,
    val type: String? = "",
    val rating: Float = 0.0f,
    val reviews: Int = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(Hours::class.java.classLoader),
        parcel.createTypedArrayList(Menus.CREATOR),
        parcel.readString(),
        parcel.readFloat(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(address)
        parcel.writeString(delivery_charge)
        parcel.writeString(image)
        parcel.writeParcelable(hours, flags)
        parcel.writeTypedList(menus)
        parcel.writeString(type)
        parcel.writeFloat(rating)
        parcel.writeInt(reviews)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RestaurantModel> {
        override fun createFromParcel(parcel: Parcel): RestaurantModel {
            return RestaurantModel(parcel)
        }

        override fun newArray(size: Int): Array<RestaurantModel?> {
            return arrayOfNulls(size)
        }
    }
}
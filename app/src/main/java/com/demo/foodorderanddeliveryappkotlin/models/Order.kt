package com.demo.foodorderanddeliveryappkotlin.models

import android.os.Parcel
import android.os.Parcelable

data class Order(
    val orderId: String,
    val itemCount: Int,
    val status: String,
    val dateTime: String
)

data class OrderItem(
    val foodId: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val quantity: Int = 1
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(foodId)
        parcel.writeString(name)
        parcel.writeDouble(price)
        parcel.writeInt(quantity)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderItem> {
        override fun createFromParcel(parcel: Parcel): OrderItem {
            return OrderItem(parcel)
        }

        override fun newArray(size: Int): Array<OrderItem?> {
            return arrayOfNulls(size)
        }
    }
} 
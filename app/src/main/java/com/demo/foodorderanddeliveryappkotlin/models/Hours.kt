package com.demo.foodorderanddeliveryappkotlin.models

import android.os.Parcel
import android.os.Parcelable

data class Hours(
    val Sunday: String?,
    val Monday: String?,
    val Tuesday: String?,
    val Wednesday: String?,
    val Thursday: String?,
    val Friday: String?,
    val Saturday: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(Sunday)
        parcel.writeString(Monday)
        parcel.writeString(Tuesday)
        parcel.writeString(Wednesday)
        parcel.writeString(Thursday)
        parcel.writeString(Friday)
        parcel.writeString(Saturday)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Hours> {
        override fun createFromParcel(parcel: Parcel): Hours {
            return Hours(parcel)
        }

        override fun newArray(size: Int): Array<Hours?> {
            return arrayOfNulls(size)
        }
    }
}

package com.acrcloud.data

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ExternalIds : Parcelable {

    @SerializedName("isrc")
    @Expose
    var isrc: String? = null
    @SerializedName("upc")
    @Expose
    var upc: String? = null

    protected constructor(`in`: Parcel) {
        this.isrc = `in`.readValue(String::class.java.classLoader) as String
        this.upc = `in`.readValue(String::class.java.classLoader) as String
    }

    /**
     * No args constructor for use in serialization
     */
    constructor() {}

    /**
     * @param isrc
     * @param upc
     */
    constructor(isrc: String, upc: String) : super() {
        this.isrc = isrc
        this.upc = upc
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(isrc)
        dest.writeValue(upc)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ExternalIds> {
        override fun createFromParcel(parcel: Parcel): ExternalIds {
            return ExternalIds(parcel)
        }

        override fun newArray(size: Int): Array<ExternalIds?> {
            return arrayOfNulls(size)
        }
    }


}

package com.acrcloud.data

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Artist__ : Parcelable {

    @SerializedName("name")
    @Expose
    var name: String? = null

    protected constructor(`in`: Parcel) {
        this.name = `in`.readValue(String::class.java.classLoader) as String
    }

    /**
     * No args constructor for use in serialization
     */
    constructor() {}

    /**
     * @param name
     */
    constructor(name: String) : super() {
        this.name = name
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Artist__> {
        override fun createFromParcel(parcel: Parcel): Artist__ {
            return Artist__(parcel)
        }

        override fun newArray(size: Int): Array<Artist__?> {
            return arrayOfNulls(size)
        }
    }


}

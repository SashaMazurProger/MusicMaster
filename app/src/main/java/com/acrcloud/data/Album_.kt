package com.acrcloud.data

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Album_ : Parcelable {

    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("id")
    @Expose
    var id: String? = null

    protected constructor(`in`: Parcel) {
        this.name = `in`.readValue(String::class.java.classLoader) as String
        this.id = `in`.readValue(String::class.java.classLoader) as String
    }

    /**
     * No args constructor for use in serialization
     */
    constructor() {}

    /**
     * @param id
     * @param name
     */
    constructor(name: String, id: String) : super() {
        this.name = name
        this.id = id
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(name)
        dest.writeValue(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Album_> {
        override fun createFromParcel(parcel: Parcel): Album_ {
            return Album_(parcel)
        }

        override fun newArray(size: Int): Array<Album_?> {
            return arrayOfNulls(size)
        }
    }


}

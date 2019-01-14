package com.acrcloud.data

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Youtube : Parcelable {

    @SerializedName("vid")
    @Expose
    var vid: String? = null

    protected constructor(`in`: Parcel) {
        this.vid = `in`.readValue(String::class.java.classLoader) as String
    }

    /**
     * No args constructor for use in serialization
     */
    constructor() {}

    /**
     * @param vid
     */
    constructor(vid: String) : super() {
        this.vid = vid
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(vid)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Youtube> {
        override fun createFromParcel(parcel: Parcel): Youtube {
            return Youtube(parcel)
        }

        override fun newArray(size: Int): Array<Youtube?> {
            return arrayOfNulls(size)
        }
    }


}

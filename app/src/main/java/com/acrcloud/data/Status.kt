package com.acrcloud.data

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Status : Parcelable {

    @SerializedName("msg")
    @Expose
    var msg: String? = null
    @SerializedName("code")
    @Expose
    var code: Int? = null
    @SerializedName("version")
    @Expose
    var version: String? = null

    protected constructor(`in`: Parcel) {
        this.msg = `in`.readValue(String::class.java.classLoader) as String
        this.code = `in`.readValue(Int::class.java.classLoader) as Int
        this.version = `in`.readValue(String::class.java.classLoader) as String
    }

    /**
     * No args constructor for use in serialization
     */
    constructor() {}

    /**
     * @param code
     * @param msg
     * @param version
     */
    constructor(msg: String, code: Int?, version: String) : super() {
        this.msg = msg
        this.code = code
        this.version = version
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(msg)
        dest.writeValue(code)
        dest.writeValue(version)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Status> {
        override fun createFromParcel(parcel: Parcel): Status {
            return Status(parcel)
        }

        override fun newArray(size: Int): Array<Status?> {
            return arrayOfNulls(size)
        }
    }


}

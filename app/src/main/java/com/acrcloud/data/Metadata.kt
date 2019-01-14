package com.acrcloud.data

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.util.ArrayList

class Metadata : Parcelable {

    @SerializedName("music")
    @Expose
    var music: List<Music> = ArrayList()
    @SerializedName("timestamp_utc")
    @Expose
    var timestampUtc: String? = null

    protected constructor(`in`: Parcel) {
        `in`.readList(this.music, Music::class.java.classLoader)
        this.timestampUtc = `in`.readValue(String::class.java.classLoader) as String
    }

    /**
     * No args constructor for use in serialization
     */
    constructor() {}

    /**
     * @param music
     * @param timestampUtc
     */
    constructor(music: List<Music>, timestampUtc: String) : super() {
        this.music = music
        this.timestampUtc = timestampUtc
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeList(music)
        dest.writeValue(timestampUtc)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Metadata> {
        override fun createFromParcel(parcel: Parcel): Metadata {
            return Metadata(parcel)
        }

        override fun newArray(size: Int): Array<Metadata?> {
            return arrayOfNulls(size)
        }
    }


}

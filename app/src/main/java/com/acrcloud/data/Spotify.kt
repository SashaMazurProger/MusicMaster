package com.acrcloud.data

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.util.ArrayList

class Spotify : Parcelable {

    @SerializedName("album")
    @Expose
    var album: Album_? = null
    @SerializedName("artists")
    @Expose
    var artists: List<Artist_> = ArrayList()
    @SerializedName("track")
    @Expose
    var track: Track_? = null

    protected constructor(`in`: Parcel) {
        this.album = `in`.readValue(Album_::class.java.classLoader) as Album_
        `in`.readList(this.artists, Artist_::class.java.classLoader)
        this.track = `in`.readValue(Track_::class.java.classLoader) as Track_
    }

    /**
     * No args constructor for use in serialization
     */
    constructor() {}

    /**
     * @param artists
     * @param album
     * @param track
     */
    constructor(album: Album_, artists: List<Artist_>, track: Track_) : super() {
        this.album = album
        this.artists = artists
        this.track = track
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(album)
        dest.writeList(artists)
        dest.writeValue(track)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Spotify> {
        override fun createFromParcel(parcel: Parcel): Spotify {
            return Spotify(parcel)
        }

        override fun newArray(size: Int): Array<Spotify?> {
            return arrayOfNulls(size)
        }
    }


}

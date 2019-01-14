package com.acrcloud.data

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

import java.util.ArrayList

class Deezer : Parcelable {

    @SerializedName("album")
    @Expose
    var album: Album? = null
    @SerializedName("artists")
    @Expose
    var artists: List<Artist> = ArrayList()
    @SerializedName("track")
    @Expose
    var track: Track? = null

    protected constructor(`in`: Parcel) {
        this.album = `in`.readValue(Album::class.java.classLoader) as Album
        `in`.readList(this.artists, Artist::class.java.classLoader)
        this.track = `in`.readValue(Track::class.java.classLoader) as Track
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
    constructor(album: Album, artists: List<Artist>, track: Track) : super() {
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

    companion object CREATOR : Parcelable.Creator<Deezer> {
        override fun createFromParcel(parcel: Parcel): Deezer {
            return Deezer(parcel)
        }

        override fun newArray(size: Int): Array<Deezer?> {
            return arrayOfNulls(size)
        }
    }


}

package com.acrcloud.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Spotify implements Parcelable {

    @SerializedName("album")
    @Expose
    private Album_ album;
    @SerializedName("artists")
    @Expose
    private List<Artist_> artists = new ArrayList<Artist_>();
    @SerializedName("track")
    @Expose
    private Track_ track;
    public final static Creator<Spotify> CREATOR = new Creator<Spotify>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Spotify createFromParcel(Parcel in) {
            return new Spotify(in);
        }

        public Spotify[] newArray(int size) {
            return (new Spotify[size]);
        }

    };

    protected Spotify(Parcel in) {
        this.album = ((Album_) in.readValue((Album_.class.getClassLoader())));
        in.readList(this.artists, (Artist_.class.getClassLoader()));
        this.track = ((Track_) in.readValue((Track_.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     */
    public Spotify() {
    }

    /**
     * @param artists
     * @param album
     * @param track
     */
    public Spotify(Album_ album, List<Artist_> artists, Track_ track) {
        super();
        this.album = album;
        this.artists = artists;
        this.track = track;
    }

    public Album_ getAlbum() {
        return album;
    }

    public void setAlbum(Album_ album) {
        this.album = album;
    }

    public List<Artist_> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist_> artists) {
        this.artists = artists;
    }

    public Track_ getTrack() {
        return track;
    }

    public void setTrack(Track_ track) {
        this.track = track;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(album);
        dest.writeList(artists);
        dest.writeValue(track);
    }

    public int describeContents() {
        return 0;
    }

}

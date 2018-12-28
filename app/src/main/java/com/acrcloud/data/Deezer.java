package com.acrcloud.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Deezer implements Parcelable {

    @SerializedName("album")
    @Expose
    private Album album;
    @SerializedName("artists")
    @Expose
    private List<Artist> artists = new ArrayList<Artist>();
    @SerializedName("track")
    @Expose
    private Track track;
    public final static Creator<Deezer> CREATOR = new Creator<Deezer>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Deezer createFromParcel(Parcel in) {
            return new Deezer(in);
        }

        public Deezer[] newArray(int size) {
            return (new Deezer[size]);
        }

    };

    protected Deezer(Parcel in) {
        this.album = ((Album) in.readValue((Album.class.getClassLoader())));
        in.readList(this.artists, (Artist.class.getClassLoader()));
        this.track = ((Track) in.readValue((Track.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     */
    public Deezer() {
    }

    /**
     * @param artists
     * @param album
     * @param track
     */
    public Deezer(Album album, List<Artist> artists, Track track) {
        super();
        this.album = album;
        this.artists = artists;
        this.track = track;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
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

package com.acrcloud.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Music implements Parcelable {

    @SerializedName("external_ids")
    @Expose
    private ExternalIds externalIds;
    @SerializedName("play_offset_ms")
    @Expose
    private Integer playOffsetMs;
    @SerializedName("external_metadata")
    @Expose
    private ExternalMetadata externalMetadata;
    @SerializedName("release_date")
    @Expose
    private String releaseDate;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("genres")
    @Expose
    private List<Genre> genres = new ArrayList<Genre>();
    @SerializedName("artists")
    @Expose
    private List<Artist__> artists = new ArrayList<Artist__>();
    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("duration_ms")
    @Expose
    private Integer durationMs;
    @SerializedName("album")
    @Expose
    private Album__ album;
    @SerializedName("acrid")
    @Expose
    private String acrid;
    @SerializedName("result_from")
    @Expose
    private Integer resultFrom;
    @SerializedName("score")
    @Expose
    private Integer score;
    public final static Creator<Music> CREATOR = new Creator<Music>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Music createFromParcel(Parcel in) {
            return new Music(in);
        }

        public Music[] newArray(int size) {
            return (new Music[size]);
        }

    };

    protected Music(Parcel in) {
        this.externalIds = ((ExternalIds) in.readValue((ExternalIds.class.getClassLoader())));
        this.playOffsetMs = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.externalMetadata = ((ExternalMetadata) in.readValue((ExternalMetadata.class.getClassLoader())));
        this.releaseDate = ((String) in.readValue((String.class.getClassLoader())));
        this.title = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.genres, (Genre.class.getClassLoader()));
        in.readList(this.artists, (Artist__.class.getClassLoader()));
        this.label = ((String) in.readValue((String.class.getClassLoader())));
        this.durationMs = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.album = ((Album__) in.readValue((Album__.class.getClassLoader())));
        this.acrid = ((String) in.readValue((String.class.getClassLoader())));
        this.resultFrom = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.score = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     */
    public Music() {
    }

    /**
     * @param acrid
     * @param artists
     * @param externalIds
     * @param durationMs
     * @param title
     * @param resultFrom
     * @param releaseDate
     * @param externalMetadata
     * @param playOffsetMs
     * @param genres
     * @param album
     * @param score
     * @param label
     */
    public Music(ExternalIds externalIds, Integer playOffsetMs, ExternalMetadata externalMetadata, String releaseDate, String title, List<Genre> genres, List<Artist__> artists, String label, Integer durationMs, Album__ album, String acrid, Integer resultFrom, Integer score) {
        super();
        this.externalIds = externalIds;
        this.playOffsetMs = playOffsetMs;
        this.externalMetadata = externalMetadata;
        this.releaseDate = releaseDate;
        this.title = title;
        this.genres = genres;
        this.artists = artists;
        this.label = label;
        this.durationMs = durationMs;
        this.album = album;
        this.acrid = acrid;
        this.resultFrom = resultFrom;
        this.score = score;
    }

    public ExternalIds getExternalIds() {
        return externalIds;
    }

    public void setExternalIds(ExternalIds externalIds) {
        this.externalIds = externalIds;
    }

    public Integer getPlayOffsetMs() {
        return playOffsetMs;
    }

    public void setPlayOffsetMs(Integer playOffsetMs) {
        this.playOffsetMs = playOffsetMs;
    }

    public ExternalMetadata getExternalMetadata() {
        return externalMetadata;
    }

    public void setExternalMetadata(ExternalMetadata externalMetadata) {
        this.externalMetadata = externalMetadata;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public List<Artist__> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist__> artists) {
        this.artists = artists;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(Integer durationMs) {
        this.durationMs = durationMs;
    }

    public Album__ getAlbum() {
        return album;
    }

    public void setAlbum(Album__ album) {
        this.album = album;
    }

    public String getAcrid() {
        return acrid;
    }

    public void setAcrid(String acrid) {
        this.acrid = acrid;
    }

    public Integer getResultFrom() {
        return resultFrom;
    }

    public void setResultFrom(Integer resultFrom) {
        this.resultFrom = resultFrom;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(externalIds);
        dest.writeValue(playOffsetMs);
        dest.writeValue(externalMetadata);
        dest.writeValue(releaseDate);
        dest.writeValue(title);
        dest.writeList(genres);
        dest.writeList(artists);
        dest.writeValue(label);
        dest.writeValue(durationMs);
        dest.writeValue(album);
        dest.writeValue(acrid);
        dest.writeValue(resultFrom);
        dest.writeValue(score);
    }

    public int describeContents() {
        return 0;
    }

}

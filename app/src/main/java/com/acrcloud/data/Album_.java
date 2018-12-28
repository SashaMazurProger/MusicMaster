package com.acrcloud.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Album_ implements Parcelable {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("id")
    @Expose
    private String id;
    public final static Creator<Album_> CREATOR = new Creator<Album_>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Album_ createFromParcel(Parcel in) {
            return new Album_(in);
        }

        public Album_[] newArray(int size) {
            return (new Album_[size]);
        }

    };

    protected Album_(Parcel in) {
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.id = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     */
    public Album_() {
    }

    /**
     * @param id
     * @param name
     */
    public Album_(String name, String id) {
        super();
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(name);
        dest.writeValue(id);
    }

    public int describeContents() {
        return 0;
    }

}

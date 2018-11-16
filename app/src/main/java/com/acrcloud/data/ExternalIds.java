
package com.acrcloud.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExternalIds implements Parcelable
{

    @SerializedName("isrc")
    @Expose
    private String isrc;
    @SerializedName("upc")
    @Expose
    private String upc;
    public final static Creator<ExternalIds> CREATOR = new Creator<ExternalIds>() {


        @SuppressWarnings({
            "unchecked"
        })
        public ExternalIds createFromParcel(Parcel in) {
            return new ExternalIds(in);
        }

        public ExternalIds[] newArray(int size) {
            return (new ExternalIds[size]);
        }

    }
    ;

    protected ExternalIds(Parcel in) {
        this.isrc = ((String) in.readValue((String.class.getClassLoader())));
        this.upc = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public ExternalIds() {
    }

    /**
     * 
     * @param isrc
     * @param upc
     */
    public ExternalIds(String isrc, String upc) {
        super();
        this.isrc = isrc;
        this.upc = upc;
    }

    public String getIsrc() {
        return isrc;
    }

    public void setIsrc(String isrc) {
        this.isrc = isrc;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(isrc);
        dest.writeValue(upc);
    }

    public int describeContents() {
        return  0;
    }

}


package com.acrcloud.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ACRRecognizeResponse implements Parcelable
{

    @SerializedName("status")
    @Expose
    private Status status;
    @SerializedName("metadata")
    @Expose
    private Metadata metadata;
    @SerializedName("cost_time")
    @Expose
    private Double costTime;
    @SerializedName("result_type")
    @Expose
    private Integer resultType;
    public final static Creator<ACRRecognizeResponse> CREATOR = new Creator<ACRRecognizeResponse>() {


        @SuppressWarnings({
            "unchecked"
        })
        public ACRRecognizeResponse createFromParcel(Parcel in) {
            return new ACRRecognizeResponse(in);
        }

        public ACRRecognizeResponse[] newArray(int size) {
            return (new ACRRecognizeResponse[size]);
        }

    }
    ;

    protected ACRRecognizeResponse(Parcel in) {
        this.status = ((Status) in.readValue((Status.class.getClassLoader())));
        this.metadata = ((Metadata) in.readValue((Metadata.class.getClassLoader())));
        this.costTime = ((Double) in.readValue((Double.class.getClassLoader())));
        this.resultType = ((Integer) in.readValue((Integer.class.getClassLoader())));
    }

    /**
     * No args constructor for use in serialization
     * 
     */
    public ACRRecognizeResponse() {
    }

    /**
     * 
     * @param status
     * @param resultType
     * @param costTime
     * @param metadata
     */
    public ACRRecognizeResponse(Status status, Metadata metadata, Double costTime, Integer resultType) {
        super();
        this.status = status;
        this.metadata = metadata;
        this.costTime = costTime;
        this.resultType = resultType;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public Double getCostTime() {
        return costTime;
    }

    public void setCostTime(Double costTime) {
        this.costTime = costTime;
    }

    public Integer getResultType() {
        return resultType;
    }

    public void setResultType(Integer resultType) {
        this.resultType = resultType;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(metadata);
        dest.writeValue(costTime);
        dest.writeValue(resultType);
    }

    public int describeContents() {
        return  0;
    }

}

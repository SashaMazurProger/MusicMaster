package com.acrcloud.ui

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.os.Parcel
import android.os.Parcelable

class EditSong : BaseObservable, Parcelable {

    var type = TYPE.SONG
    var title: String? = null
    var path: String? = null
    private var editing = false


    var isEditing: Boolean
        @Bindable
        get() = editing
        set(isEditing) {
            editing = isEditing
            notifyPropertyChanged(BR.editing)
        }


    enum class TYPE {
        SONG, FOLDER
    }

    constructor(path: String, title: String) {
        this.title = title
        this.path = path
    }

    constructor(title: String, path: String, editing: Boolean) {
        this.title = title
        this.path = path
        isEditing = editing
    }

    protected constructor(`in`: Parcel) {
        title = `in`.readString()
        path = `in`.readString()
        editing = `in`.readByte().toInt() != 0
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(title)
        dest.writeString(path)
        dest.writeByte((if (editing) 1 else 0).toByte())
    }

    companion object {
        val KEY = "song"

        @JvmField
        val CREATOR: Parcelable.Creator<EditSong> = object : Parcelable.Creator<EditSong> {
            override fun createFromParcel(`in`: Parcel): EditSong {
                return EditSong(`in`)
            }

            override fun newArray(size: Int): Array<EditSong?> {
                return arrayOfNulls(size)
            }
        }
    }

}

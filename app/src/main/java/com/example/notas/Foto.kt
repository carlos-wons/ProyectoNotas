package com.example.notas

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable

class Foto(var idFoto: Int, var idNota: Int, var description: String?, var Photo: Bitmap?): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readParcelable(Bitmap::class.java.classLoader)
    ) {
    }

    constructor(description: String, Photo: Bitmap):this(0,0,description,Photo)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(idFoto)
        parcel.writeInt(idNota)
        parcel.writeString(description)
        parcel.writeParcelable(Photo, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Foto> {
        override fun createFromParcel(parcel: Parcel): Foto {
            return Foto(parcel)
        }

        override fun newArray(size: Int): Array<Foto?> {
            return arrayOfNulls(size)
        }
    }
}
package com.example.notas.data

import android.graphics.Bitmap
import android.net.Uri
import java.io.Serializable

class RecursosNota(val id: Int, val uri: String, val tipo: String):Serializable{
    constructor(uri: String, tipo: String): this(0,uri,tipo)
}
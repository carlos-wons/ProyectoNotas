package com.example.notas.data

import java.io.Serializable

class RecursosTarea(val id: Int, val uri: String, val tipo: String):Serializable {

    constructor(uri: String, tipo: String): this(0,uri,tipo)
}
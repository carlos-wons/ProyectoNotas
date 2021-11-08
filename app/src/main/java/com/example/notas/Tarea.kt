package com.example.notas_001.datos

class Tarea(var idTarea: Int, var titulo: String, var descripcion: String, var fecha:String) {
    constructor(titulo: String, descripcion: String, fecha: String):this(0,titulo,descripcion,fecha)
}
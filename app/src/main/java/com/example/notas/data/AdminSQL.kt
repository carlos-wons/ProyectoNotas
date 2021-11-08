package com.example.notas.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AdminSQL(
    context: Context?
) : SQLiteOpenHelper(context, "notasTareas", null, 1) {
    override fun onCreate(baseDatos: SQLiteDatabase?) {
        val query_tarea: String? = "CREATE TABLE ${Tabla_tarea.nombre_tabla} (" +
                "${Tabla_tarea.campo_id} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${Tabla_tarea.campo_titulo} VARCHAR(30)," +
                "${Tabla_tarea.campo_descripcion} VARCHAR(100)," +
                "${Tabla_tarea.campo_fecha} VARCHAR(30));"
        baseDatos?.execSQL(query_tarea)
        val query_nota: String = "CREATE TABLE ${Tabla_nota.nombre_tabla} (" +
                "${Tabla_nota.campo_id} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${Tabla_nota.campo_nombre} VARCHAR(30)," +
                "${Tabla_nota.campo_descripcion} VARCHAR(200) );"
        baseDatos?.execSQL(query_nota)
        val queryTabla_RecursosNota: String = "CREATE TABLE ${Tabla_Recursos_nota.nombre_tabla} ( " +
                "${Tabla_Recursos_nota.campo_idNota} INTEGER," +
                "${Tabla_Recursos_nota.campo_uri} TEXT NOT NULL," +
                "${Tabla_Recursos_nota.campo_tipo} VARCHAR(10)," +
                "foreign key(${Tabla_Recursos_nota.campo_idNota}) references ${Tabla_nota.nombre_tabla}(${Tabla_nota.campo_id}) );"
        baseDatos?.execSQL(queryTabla_RecursosNota)
       val queryTabla_RecursosTarea: String =
           "CREATE TABLE ${Tabla_Recursos_tarea.nombre_tabla} (" +
                   "${Tabla_Recursos_tarea.campo_idTarea} INTEGER," +
                   "${Tabla_Recursos_tarea.campo_uri} TEXT NOT NULL," +
                   "${Tabla_Recursos_tarea.campo_tipo} VARCHAR(10)," +
                   "FOREIGN KEY(${Tabla_Recursos_tarea.campo_idTarea}) REFERENCES ${Tabla_tarea.nombre_tabla}(${Tabla_tarea.campo_id}) );"
        baseDatos?.execSQL(queryTabla_RecursosTarea)

    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${Tabla_nota.nombre_tabla}");
        db?.execSQL("DROP TABLE IF EXISTS ${Tabla_tarea.nombre_tabla}");
        onCreate(db);
    }
}
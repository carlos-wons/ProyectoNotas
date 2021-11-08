package com.example.notas.data

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.widget.Toast
import java.util.ArrayList
import kotlin.Exception

class daoRecursosTarea(
    val context: Context
) {
    private val database: AdminSQL = AdminSQL(context)
    var base: SQLiteDatabase = database.writableDatabase

    fun insert(recursosTarea: RecursosTarea): Boolean{
        val query: String = "INSERT INTO ${Tabla_Recursos_tarea.nombre_tabla} (" +
                "${Tabla_Recursos_tarea.campo_idTarea},${Tabla_Recursos_tarea.campo_uri}," +
                "${Tabla_Recursos_tarea.campo_tipo} ) VALUES(" +
                "'${daoTarea(context).getLastId()}','${recursosTarea.uri}','${recursosTarea.tipo}' );"
        return try {
            base.execSQL(query)
            Toast.makeText(context,"Tarea agregada correctamente", Toast.LENGTH_SHORT).show()
            true
        }catch (e: Exception){
            Toast.makeText(context,e.message, Toast.LENGTH_SHORT).show()
            false
        }
    }
    fun getAllByAudioType(id: Int): ArrayList<RecursosTarea>{
        base = database.readableDatabase
        val list: ArrayList<RecursosTarea> = ArrayList()
        try {
            val query =
                "SELECT * FROM ${Tabla_Recursos_tarea.nombre_tabla} WHERE ${Tabla_Recursos_tarea.campo_idTarea} = '${id}' " +
                        "AND ${Tabla_Recursos_tarea.campo_tipo} = 'audio'"
            val cursor: Cursor = base.rawQuery(query, null)
            while (cursor.moveToNext()){
                list.add(
                    RecursosTarea(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2)
                    )
                )
            }
            cursor.close()
        }catch (e: Exception){
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        }
        return list
    }
    fun getAllByVideoType(id: Int): ArrayList<RecursosTarea>{
        base = database.readableDatabase
        val list: ArrayList<RecursosTarea> = ArrayList()
        try {
            val query =
                "SELECT * FROM ${Tabla_Recursos_tarea.nombre_tabla} WHERE ${Tabla_Recursos_tarea.campo_idTarea} = '${id}' " +
                        "AND ${Tabla_Recursos_tarea.campo_tipo} = 'video'"
            val cursor: Cursor = base.rawQuery(query, null)
            while (cursor.moveToNext()){
                list.add(
                    RecursosTarea(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2)
                    )
                )
            }
            cursor.close()
        }catch (e: Exception){
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        }
        return list
    }

    fun getAllByImageType(id: Int): ArrayList<RecursosTarea>{
        base = database.readableDatabase
        val list: ArrayList<RecursosTarea> = ArrayList()
        try {
            val query =
                "SELECT * FROM ${Tabla_Recursos_tarea.nombre_tabla} WHERE ${Tabla_Recursos_tarea.campo_idTarea} = '${id}' " +
                        "AND ${Tabla_Recursos_tarea.campo_tipo} = 'image'"
            val cursor: Cursor = base.rawQuery(query,null)
            while(cursor.moveToNext()){
                list.add(
                    RecursosTarea(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2)
                    )
                )
            }
            cursor.close()
        }catch (e: Exception){
            Toast.makeText(context,e.message,Toast.LENGTH_SHORT).show()
        }
        return list
    }
    fun getAllByFileType(id: Int): ArrayList<RecursosTarea>{
        base = database.readableDatabase
        val list: ArrayList<RecursosTarea> = ArrayList()
        try {
            val query =
                "SELECT * FROM ${Tabla_Recursos_tarea.nombre_tabla} WHERE ${Tabla_Recursos_tarea.campo_idTarea} = '${id}' " +
                        "AND ${Tabla_Recursos_tarea.campo_tipo} = 'file'"
            val cursor: Cursor = base.rawQuery(query,null)
            while(cursor.moveToNext()){
                list.add(
                    RecursosTarea(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2)
                    )
                )
            }
            cursor.close()
        }catch (e: Exception){
            Toast.makeText(context,e.message,Toast.LENGTH_SHORT).show()
        }
        Toast.makeText(context, "Files ${list.size}", Toast.LENGTH_SHORT).show()
        return list
    }
    fun getAllById(id: Int): ArrayList<RecursosTarea>? {
        base = database.readableDatabase
        val list: ArrayList<RecursosTarea> = ArrayList()
        try {
            val query: String = "SELECT * FROM ${Tabla_Recursos_tarea.nombre_tabla} WHERE ${Tabla_Recursos_tarea.campo_idTarea} == '${id}'"
            val cursor: Cursor = base.rawQuery(query, null)
            while (cursor.moveToNext()) {
                list.add(
                    RecursosTarea(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2)
                    )
                )
            }
            cursor.close()
        } catch (e: Exception) {
            Toast.makeText(
                context,
                e.message, Toast.LENGTH_SHORT
            ).show()
        }
        return list
    }
}
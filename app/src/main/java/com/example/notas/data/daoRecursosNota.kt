package com.example.notas.data

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.widget.Toast
import java.util.*
import kotlin.collections.ArrayList


class daoRecursosNota(
    val contexto: Context
) {
    private val database: AdminSQL = AdminSQL(contexto)
    var base: SQLiteDatabase = database.writableDatabase

    fun insert(recursosNota: RecursosNota): Boolean {
        val query: String = "INSERT INTO ${Tabla_Recursos_nota.nombre_tabla} (" +
                "${Tabla_Recursos_nota.campo_idNota},${Tabla_Recursos_nota.campo_uri}," +
                "${Tabla_Recursos_nota.campo_tipo} ) VALUES(" +
                "'${daoNota(contexto).getLastId()}','${recursosNota.uri}','${recursosNota.tipo}' );"
        return try {
            base.execSQL(query)
            Toast.makeText(contexto, "Agregado correctamente", Toast.LENGTH_SHORT).show()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getAllByAudioType(id: Int): ArrayList<RecursosNota> {
        base = database.readableDatabase
        val list: ArrayList<RecursosNota> = ArrayList()
        try {
            val query =
                "SELECT * FROM ${Tabla_Recursos_nota.nombre_tabla} WHERE ${Tabla_Recursos_nota.campo_idNota} = '${id}' " +
                        "AND ${Tabla_Recursos_nota.campo_tipo} = 'audio'"
            val cursor: Cursor = base.rawQuery(query, null)
            while (cursor.moveToNext()) {
                list.add(
                    RecursosNota(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2)
                    )
                )
            }
            cursor.close()
        } catch (e: Exception) {
            Toast.makeText(contexto, e.message, Toast.LENGTH_LONG).show()
        }
        return list
    }

    fun getAllByVideoType(id: Int): ArrayList<RecursosNota> {
        base = database.readableDatabase
        val list: ArrayList<RecursosNota> = ArrayList()
        try {
            val query =
                "SELECT * FROM ${Tabla_Recursos_nota.nombre_tabla} WHERE ${Tabla_Recursos_nota.campo_idNota} = '${id}' " +
                        "AND ${Tabla_Recursos_nota.campo_tipo} = 'video'"
            val cursor: Cursor = base.rawQuery(query, null)
            while (cursor.moveToNext()) {
                list.add(
                    RecursosNota(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2)
                    )
                )
            }
            cursor.close()
        } catch (e: Exception) {
            Toast.makeText(contexto, e.message, Toast.LENGTH_LONG).show()
        }
        return list
    }

    fun getAllByImageType(id: Int): ArrayList<RecursosNota> {
        base = database.readableDatabase
        val list: ArrayList<RecursosNota> = ArrayList()
        try {
            val query =
                "SELECT * FROM ${Tabla_Recursos_nota.nombre_tabla} WHERE ${Tabla_Recursos_nota.campo_idNota} == '${id}' " +
                        "AND ${Tabla_Recursos_nota.campo_tipo} = 'image'"
            val cursor: Cursor = base.rawQuery(query, null)
            while (cursor.moveToNext()) {
                list.add(
                    RecursosNota(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2)
                    )
                )
            }
            cursor.close()
        } catch (e: Exception) {
            Toast.makeText(contexto, e.message, Toast.LENGTH_SHORT).show()
        }
        return list
    }

    fun getAllByFileType(id: Int): ArrayList<RecursosNota> {
        base = database.readableDatabase
        val list: ArrayList<RecursosNota> = ArrayList()
        try {
            val query =
                "SELECT * FROM ${Tabla_Recursos_nota.nombre_tabla} " +
                        "WHERE ${Tabla_Recursos_nota.campo_idNota} = '${id}' " +
                        "AND ${Tabla_Recursos_nota.campo_tipo} = 'file'"
            val cursor: Cursor = base.rawQuery(query, null)
            while (cursor.moveToNext()) {
                list.add(
                    RecursosNota(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2)
                    )
                )
            }
            cursor.close()
        } catch (e: Exception) {
            Toast.makeText(contexto, e.message, Toast.LENGTH_SHORT).show()
        }
        Toast.makeText(contexto, "Files ${list.size.toString()}", Toast.LENGTH_SHORT).show()
        return list
    }

    fun getAllById(id: Int): ArrayList<RecursosNota> {
        base = database.readableDatabase
        val list: ArrayList<RecursosNota> = ArrayList()
        try {
            val query: String =
                "SELECT * FROM ${Tabla_Recursos_nota.nombre_tabla} WHERE ${Tabla_Recursos_nota.campo_idNota} == '${id}'"
            val cursor: Cursor = base.rawQuery(query, null)
            while (cursor.moveToNext()) {
                list.add(
                    RecursosNota(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2)
                    )
                )
            }
            cursor.close()
        } catch (e: Exception) {
            Toast.makeText(
                contexto,
                e.message, Toast.LENGTH_SHORT
            ).show()
        }
        return list
    }

    fun getAll(): ArrayList<RecursosNota> {
        base = database.readableDatabase
        val list: ArrayList<RecursosNota> = ArrayList()
        try {
            val query: String = "SELECT * FROM ${Tabla_Recursos_nota.nombre_tabla}"
            val cursor: Cursor = base.rawQuery(query, null)
            while (cursor.moveToNext()) {
                list.add(
                    RecursosNota(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2)
                    )
                )
            }
            cursor.close()
        } catch (e: Exception) {
            Toast.makeText(
                contexto,
                e.message, Toast.LENGTH_SHORT
            ).show()
        }
        return list
    }
}
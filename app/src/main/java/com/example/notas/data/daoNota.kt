package com.example.notas.data

import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.widget.Toast
import com.example.notas.Nota
import java.lang.Exception

class daoNota(
   val contexto: Context, private val database: AdminSQL = AdminSQL(contexto),
    var base: SQLiteDatabase = database.writableDatabase
) {
    fun insert(nota: Nota): Boolean{
        val query: String =
            "INSERT INTO ${Tabla_nota.nombre_tabla} (" +
                    "${Tabla_nota.campo_nombre},${Tabla_nota.campo_descripcion}) " +
                    "VALUES('${nota.titulo}', '${nota.descripcion}' );"
        return try{
            base.execSQL(query)
            Toast.makeText(contexto, "Nota agregada ", Toast.LENGTH_SHORT).show()
            true
        } catch (e: Exception){
            Toast.makeText(contexto, e.message, Toast.LENGTH_SHORT).show()
            false
        }finally {
            base.close()
        }
    }

    fun getAll(): ArrayList<Nota>?{
        base = database.readableDatabase
        val listaNotas: ArrayList<Nota> = ArrayList()
        try{
            val readQuery: String = "SELECT * FROM ${Tabla_nota.nombre_tabla}"
            val cursor: Cursor =  base.rawQuery(readQuery,null)
            while(cursor.moveToNext()){
                listaNotas.add(Nota(cursor.getInt(0),cursor.getString(1),cursor.getString(2)))
            }
            cursor.close()
        }catch (e: Exception){
            Toast.makeText(contexto, e.message, Toast.LENGTH_SHORT).show()
        }

        return listaNotas;
    }

    fun getOneById(id: Int): Nota?{
        base = database.readableDatabase
        val readQuery = "SELECT * FROM ${Tabla_nota.nombre_tabla} " +
                "WHERE ${Tabla_nota.campo_id} = '${id}'"
        val cursor: Cursor = base.rawQuery(readQuery,null)
        if(cursor.moveToNext()){
            return Nota(cursor.getInt(0),cursor.getString(1),cursor.getString(2))
        }else{
            return null
        }
    }
    fun getLastId(): Int{
        base = database.readableDatabase
        try{
            val query = "SELECT * FROM ${Tabla_nota.nombre_tabla}"
            val cursor: Cursor = base.rawQuery(query,null)
            cursor.moveToLast()
            val id = cursor.getInt(0)
            cursor.close()
            return id
        }catch (e: SQLException){
            Toast.makeText(contexto,e.message,Toast.LENGTH_SHORT).show()
            return 0
        }
    }
}
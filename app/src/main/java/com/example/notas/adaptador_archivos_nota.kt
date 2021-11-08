package com.example.notas

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.notas.data.RecursosNota
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder

class adaptador_archivos_nota(
    var contexto: Context,
    var listaRecurso: ArrayList<RecursosNota>
) : RecyclerView.Adapter<adaptador_archivos_nota.ViewHolder>() {
    var inflador: LayoutInflater =
        contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var text: TextView = itemView.findViewById(R.id.txt_recycle_archivo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = inflador.inflate(R.layout.item_recycle_archivos, null)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listaRecurso.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val file = listaRecurso[position]
        holder.text.text = readFile(file.uri)
    }

    private fun readFile(name: String): String {
        var fileInputStream: FileInputStream? = null
        var stringBuilder = StringBuilder()
        try {
            fileInputStream = contexto?.openFileInput(name)
            val inputStreamReader = InputStreamReader(fileInputStream)
            val buffereader: BufferedReader = BufferedReader(inputStreamReader)
            var texto: String? = ""
            stringBuilder = StringBuilder()
            while (texto != null) {
                texto = buffereader.readLine()
                if (texto != null) {
                    stringBuilder.append(texto).append("\n")
                } else
                    break
            }
        } catch (e: IOException) {
            Toast.makeText(contexto,e.message, Toast.LENGTH_SHORT).show()
        }
        return stringBuilder.toString()
    }

}

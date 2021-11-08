package com.example.notas

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notas.data.RecursosNota

class adaptador_imagen(
    contexto: Context,
    var listaRecurso: ArrayList<RecursosNota>
): RecyclerView.Adapter<adaptador_imagen.ViewHolder>() {


    var inflador: LayoutInflater =
        contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var imagen: ImageView = itemView.findViewById(R.id.image_recycle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = inflador.inflate(R.layout.item_recycle_images, null)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listaRecurso.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val resourse: RecursosNota = listaRecurso[position]
        holder.imagen.setImageURI(Uri.parse(resourse.uri))
    }
}
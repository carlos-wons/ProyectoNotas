package com.example.notas

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class adaptador_notas(
    var contexto: Context,
    val listaNotas: ArrayList<Nota>
): RecyclerView.Adapter<adaptador_notas.ViewHolder>() {

    lateinit var onClickListener: View.OnClickListener

    fun setOnclickListener(onclickListener: View.OnClickListener) {
        this.onClickListener = onclickListener
    }

    var inflador: LayoutInflater =
        contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var titulo: TextView
        var descripcion: TextView
        init {
            this.titulo = itemView.findViewById<View>(R.id.titulo_mostrar) as TextView
            this.descripcion = itemView.findViewById<View>(R.id.descripcion_mostrar) as TextView
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): adaptador_notas.ViewHolder {
        val view: View = inflador.inflate(R.layout.item_recycle_notas, null)
        view.setOnClickListener(onClickListener)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: adaptador_notas.ViewHolder, position: Int) {
        val note: Nota = listaNotas[position]
        holder.titulo.text = "Titulo: " + note.titulo
        holder.descripcion.text = "Descripcion: "+note.descripcion
    }

    override fun getItemCount(): Int {
        return listaNotas.size
    }

}
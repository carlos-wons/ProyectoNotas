package com.example.notas

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.notas_001.datos.Tarea
import org.w3c.dom.Text

class adaptador_tarea(
    var contexto: Context,
    val listaTarea: ArrayList<Tarea>
): RecyclerView.Adapter<adaptador_tarea.ViewHolder>() {

    lateinit var onClickListener: View.OnClickListener

    var inflador: LayoutInflater =
        contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var titulo: TextView
        var descripcion: TextView
        var fecha: TextView
        init {
            this.titulo = itemView.findViewById<View>(R.id.titulo_mostrar_tarea) as TextView
            this.descripcion = itemView.findViewById<View>(R.id.descripcion_mostrar_tarea) as TextView
            this.fecha = itemView.findViewById(R.id.fecha_mostrar_tarea) as TextView
        }
    }

    fun setOnclickListener(onclickListener: View.OnClickListener) {
        this.onClickListener = onclickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = inflador.inflate(R.layout.item_recycle_tarea, null)
        view.setOnClickListener(onClickListener)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val tarea = listaTarea[position]
        holder.descripcion.text = "Descripción ${tarea.descripcion}"
        holder.titulo.text = "Título ${tarea.titulo}"
        holder.fecha.text = "Fecha límite ${tarea.fecha}"
    }

    override fun getItemCount(): Int {
       return listaTarea.size
    }
}
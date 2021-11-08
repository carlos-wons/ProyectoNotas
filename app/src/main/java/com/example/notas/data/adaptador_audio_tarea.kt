package com.example.notas.data

import android.content.Context
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.notas.R
import com.example.notas.adaptador_audio_nota
import java.io.IOException

class adaptador_audio_tarea(
    val contexto: Context,
    var listaRecurso: ArrayList<RecursosTarea>
) : RecyclerView.Adapter<adaptador_audio_tarea.ViewHolder>(){


    var inflador: LayoutInflater =
        contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var imagen: ImageView = itemView.findViewById(R.id.image_play)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view: View = inflador.inflate(R.layout.item_recycle_audio, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val resource = listaRecurso[position]
        holder.imagen.setOnClickListener {
            val m = MediaPlayer()
            try {
                m.setDataSource(resource.uri);
            } catch (e: Exception) {
                e.printStackTrace();
            }
            try {
                m.prepare();
            } catch (e: IOException) {
                e.printStackTrace();
            }

            m.start();
            Toast.makeText(contexto, "reproducción de audio", Toast.LENGTH_LONG).show();
        }
    }

    override fun getItemCount(): Int {
       return listaRecurso.size
    }
}
package com.example.notas

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.example.notas.data.RecursosNota
import com.example.notas.data.RecursosTarea

class adaptador_video_nota(
    var context: Context,
    var listaRecurso: ArrayList<RecursosNota>
):
    RecyclerView.Adapter<adaptador_video_nota.ViewHolder>(){

    var inflador: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val video: VideoView = itemView.findViewById(R.id.item_video_recycle)
    }



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view: View = inflador.inflate(R.layout.item_recycle_video, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       val resource = listaRecurso[position]
        holder.video.setVideoURI(Uri.parse(resource.uri))
        holder.video.setMediaController(MediaController(context))
        holder.video.requestFocus()
        holder.video.start()
    }

    override fun getItemCount(): Int {
       return listaRecurso.size
    }

}
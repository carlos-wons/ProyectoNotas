package com.example.notas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notas.data.RecursosNota
import com.example.notas.data.RecursosTarea
import com.example.notas.data.daoRecursosNota
import com.example.notas.data.daoRecursosTarea
import java.util.ArrayList

class fragment_ver_imagenes_tarea: Fragment() {

    var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bundle: Bundle? = arguments
        val id: Int? = bundle?.getInt("idImagen")
        val vista = inflater.inflate(R.layout.fragment_ver_imagenes, container, false)
        if (id != null) {
            Toast.makeText(context,"Id imagen ${id}", Toast.LENGTH_SHORT).show()
        }
        if (id != null) {
            initialize(vista, id)
        }
        return vista
    }

    private fun initialize(root: View, id: Int){
        recyclerView = root.findViewById(R.id.recycle_view_images)
        layoutManager = GridLayoutManager(context, 2)
        recyclerView.layoutManager = layoutManager
        val list: ArrayList<RecursosTarea>? = context?.let { daoRecursosTarea(it).getAllByImageType(id) }
        if (list != null) {
            val adapter: adaptador_imagen_tarea? = context?.let { adaptador_imagen_tarea(it,list) }
            recyclerView.adapter = adapter
        }
    }
}
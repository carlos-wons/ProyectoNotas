package com.example.notas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notas.data.RecursosTarea
import com.example.notas.data.daoRecursosTarea

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragment_ver_videos.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragment_ver_videos : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val bundle: Bundle? = arguments
        val id: Int? = bundle?.getInt("idVideo")
        val vista = inflater.inflate(R.layout.fragment_ver_videos, container, false)
        if(id != null){
            Toast.makeText(context,"Id imagen ${id}", Toast.LENGTH_SHORT).show()
            initialize(vista, id)
        }
        return vista
    }

    private fun initialize(root: View, id: Int){
        recyclerView = root.findViewById(R.id.recycle_view_videos)
        layoutManager = GridLayoutManager(context, 2)
        recyclerView.layoutManager = layoutManager
        val list: ArrayList<RecursosTarea>? =
            context?.let { daoRecursosTarea(it).getAllByVideoType(id) }
        if(list != null){
            val adapter =
                context?.let {
                    adaptador_video_tarea(it,list)
                }
            recyclerView.adapter = adapter
        }
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fragment_ver_videos.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            fragment_ver_videos().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
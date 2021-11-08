package com.example.notas

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.notas.data.RecursosNota
import com.example.notas.data.daoRecursosNota

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragment_ver_archivos.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragment_ver_archivos : Fragment() {
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
        val vista =  inflater.inflate(R.layout.fragment_ver_archivos, container, false)
        val bundle: Bundle? = arguments
        val id = bundle?.getInt("idArchivo")
        if(id != null){
            initialize(vista, id)
        }
        return vista
    }

    private fun initialize(root: View,id: Int){
        recyclerView = root.findViewById(R.id.recycle_view_files)
        layoutManager = GridLayoutManager(context, 2)
        recyclerView.layoutManager = layoutManager
        val lista: ArrayList<RecursosNota>? = context?.let {
            daoRecursosNota(it).getAllByFileType(id)
        }
        if(lista != null){
            val adapter = context?.let {
                adaptador_archivos_nota(it,lista)
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
         * @return A new instance of fragment fragment_ver_archivos.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            fragment_ver_archivos().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
package com.example.notas

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.notas.data.daoTarea
import com.example.notas_001.datos.Tarea
import kotlinx.android.synthetic.main.fragment_ver_tarea_selecionada.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragment_ver_tarea_selecionada.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragment_ver_tarea_selecionada : Fragment(),
    PopupMenu.OnMenuItemClickListener{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var title: EditText
    private lateinit var description: EditText
    private lateinit var date: TextView
    private lateinit var mainActivity: MainActivity
    private lateinit var btnAccept: Button
    private lateinit var btnCancel: Button
    private lateinit var floating: com.google.android.material.floatingactionbutton.FloatingActionButton
    private lateinit var floating2: com.google.android.material.floatingactionbutton.FloatingActionButton

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

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
        val vista = inflater.inflate(R.layout.fragment_ver_tarea_selecionada, container, false)
        try {
            val bundle: Bundle? = arguments
            val id: Int? = bundle?.getInt("idTarea")
            if(id != null){
                val obj = context?.let { daoTarea(it).getOneById(id) }
                Toast.makeText(context,obj?.titulo,Toast.LENGTH_SHORT).show()
                if (obj != null) {
                    initialize(vista,obj)
                }
            }
        }catch (e: Exception){
            Toast.makeText(context,e.message,Toast.LENGTH_SHORT).show()
        }


        return vista
    }

    private fun initialize(root: View, obj: Tarea){
        title = root.findViewById(R.id.txtTitleViewTask)
        description = root.findViewById(R.id.txtDescriptionViewTask)
        date = root.findViewById(R.id.lbDateViewTast)
        btnAccept=root.findViewById(R.id.btnaceptar)
        btnAccept.setOnClickListener(View.OnClickListener {
            getActivity()?.onBackPressed();
        })
        floating = root.findViewById(R.id.floatingViewTask)
        floating.setOnClickListener {
            val popup: PopupMenu = PopupMenu(activity,floating)
            popup.menuInflater.inflate(R.menu.popup_menu_vista,popup.menu)
            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener {
                when(it.itemId){
                   R.id.item_view_images -> {
                       val imagenTarea = fragment_ver_imagenes_tarea()
                       val bundle: Bundle = Bundle()
                       bundle.putInt("idImagen",obj.idTarea)
                       imagenTarea.arguments = bundle
                       mainActivity.changeFragmentViewImagesTask(imagenTarea)
                       return@OnMenuItemClickListener true
                   }
                    R.id.item_view_files -> {
                        val archivoTarea = fragment_ver_archivos_tarea()
                        val bundle = Bundle()
                        bundle.putInt("idArchivo", obj.idTarea)
                        archivoTarea.arguments = bundle
                        mainActivity.changeFragmentViewFilesTask(archivoTarea)
                        return@OnMenuItemClickListener true
                    }
                    R.id.item_view_video ->{
                        val videoTarea = fragment_ver_videos()
                        val bundle = Bundle()
                        bundle.putInt("idVideo", obj.idTarea)
                        videoTarea.arguments = bundle
                        mainActivity.changeFragmentViewVideoTask(videoTarea)
                        return@OnMenuItemClickListener true
                    }
                    R.id.item_view_audios -> {
                        val audioTarea = fragment_ver_audios_tareas()
                        val bundle = Bundle()
                        bundle.putInt("idAudio", obj.idTarea)
                        audioTarea.arguments = bundle
                        mainActivity.changeFragmentViewAudioTask(audioTarea)
                        return@OnMenuItemClickListener true
                    }
                 }
                true
            })
            popup.show()
        }
        title.setText(obj.titulo)
        description.setText(obj.descripcion)
        date.text = obj.fecha
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fragment_ver_tarea_selecionada.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            fragment_ver_tarea_selecionada().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        TODO("Not yet implemented")
    }
}
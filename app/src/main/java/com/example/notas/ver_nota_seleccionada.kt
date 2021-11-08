package com.example.notas

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.Toast
import com.example.notas.data.daoNota
import kotlinx.android.synthetic.main.fragment_ver_nota_seleccionada.*
import java.lang.Exception

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ver_nota_seleccionada.newInstance] factory method to
 * create an instance of this fragment.
 */
class ver_nota_seleccionada : Fragment(),
    PopupMenu.OnMenuItemClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var title: EditText
    private lateinit var description: EditText
    private lateinit var btnAccept: Button
    private lateinit var activity: MainActivity
    private lateinit var floating: com.google.android.material.floatingactionbutton.FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val bundle: Bundle? = arguments
        val id: Int? = bundle?.getInt("idNota")
        val vista = inflater.inflate(R.layout.fragment_ver_nota_seleccionada, container, false)
        if (id != null) {
            initialize(vista, id)
        }
        return vista
    }

    private fun initialize(root: View, id: Int) {
        floating = root.findViewById(R.id.floatingShowResourcesNote)
        title = root.findViewById(R.id.txtViewTitleNote)
        description = root.findViewById(R.id.txtViewDescriptionNote)
        btnAccept = root.findViewById(R.id.btnViewAcceptNote)
        btnAccept.setOnClickListener {
            getActivity()?.onBackPressed();
        }
        floating.setOnClickListener {
            val popup: PopupMenu = PopupMenu(activity, floating)
            popup.menuInflater.inflate(R.menu.popup_menu_vista, popup.menu)
            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.item_view_images -> {
                        try {
                            val bundle = Bundle()
                            bundle.putInt("idImagen", id)
                            val fragmentVerImagenes = fragment_ver_imagenes()
                            fragmentVerImagenes.arguments = bundle
                            activity.changeFragmentViewImages(fragmentVerImagenes)
                        } catch (e: Exception) {
                            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                        }
                        return@OnMenuItemClickListener true
                    }
                    R.id.item_view_files -> {
                        val bundle: Bundle = Bundle()
                        bundle.putInt("idArchivo", id)
                        val fragmentVerArchivos = fragment_ver_archivos()
                        fragmentVerArchivos.arguments = bundle
                        activity.changeFragmentViewFilesNote(fragmentVerArchivos)
                        return@OnMenuItemClickListener true
                    }
                    R.id.item_view_video -> {
                        val verVideo:fragment_ver_videos_notas = fragment_ver_videos_notas()
                        val bundle: Bundle = Bundle()
                        bundle.putInt("idVideo", id)
                        verVideo.arguments = bundle
                        activity.changeFragmentViewVideoNote(verVideo)
                        return@OnMenuItemClickListener true
                    }
                    R.id.item_view_audios -> {
                        val verAudio = fragment_ver_audios()
                        val bundle = Bundle()
                        bundle.putInt("idAudio", id)
                        verAudio.arguments = bundle
                        activity.changeFragmentViewAudioNote(verAudio)
                    }
                }
                true
            })
            popup.show()
        }
        val nota = context?.let { daoNota(it).getOneById(id) }
        title.setText(nota?.titulo)
        description.setText(nota?.descripcion)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ver_nota_seleccionada.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ver_nota_seleccionada().apply {
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
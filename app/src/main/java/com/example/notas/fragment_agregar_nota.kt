package com.example.notas

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.example.notas.data.RecursosNota
import com.example.notas.data.RecursosTarea
import com.example.notas.data.daoNota
import com.example.notas.data.daoRecursosNota
import com.google.android.material.snackbar.Snackbar
import java.io.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragment_agregar_nota.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragment_agregar_nota : Fragment(),
    PopupMenu.OnMenuItemClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var vista: View
    private lateinit var title: EditText
    private lateinit var description: EditText
    private lateinit var btnSave: Button
    private lateinit var fragmentManag: FragmentManager
    private lateinit var fragmentTransaction: FragmentTransaction
    private lateinit var floating: com.google.android.material.floatingactionbutton.FloatingActionButton
    private lateinit var activity: MainActivity
    private lateinit var listaRecursos: ArrayList<RecursosNota>
    private var miGrabacion: MediaRecorder? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_agregar_nota, container, false)

        initialize(vista)
        return vista
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("recursos",listaRecursos)

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState!=null){
            if (savedInstanceState.getSerializable("recursos")!=null){
                listaRecursos= savedInstanceState.getSerializable("recursos") as ArrayList<RecursosNota>
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun initialize(root: View) {
        listaRecursos = ArrayList<RecursosNota>()
        title = root.findViewById(R.id.txtNoteTitle)
        description = root.findViewById(R.id.txtDescriptionNote)
        btnSave = root.findViewById(R.id.btnAddNote)
        floating = root.findViewById(R.id.floatingNote)
        floating.setOnClickListener {
            val popup: PopupMenu = PopupMenu(getActivity(), floating)
            popup.menuInflater.inflate(R.menu.popup_menu, popup.menu)
            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                try {
                    when (item.itemId) {
                        R.id.item_add_from_gallery -> {
                            takeImageFromGallery()
                            return@OnMenuItemClickListener true
                        }
                        R.id.item_add_from_camera -> {
                            takePicture()
                            return@OnMenuItemClickListener true
                        }
                        R.id.item_add_file -> {
                            custom_dialog()
                            return@OnMenuItemClickListener true
                        }
                        R.id.item_take_video -> {
                            takeVideo()
                            return@OnMenuItemClickListener true
                        }
                        R.id.item_add_audio -> {
                            customDialogAudio()
                            return@OnMenuItemClickListener true
                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
                true
            })
            popup.show()
        }
        btnSave.setOnClickListener {
            addNote()
            addResourcesToDB()
            getActivity()?.onBackPressed();
        }
    }

    private fun takeVideo() {
        val intent: Intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        if (intent.resolveActivity(activity.packageManager) != null) {
            var videoFile: File? = null
            try {
                videoFile = createVideo()
            } catch (e: IOException) {
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
            if (videoFile != null) {
                val uriVideo: Uri? = activity?.let {
                    FileProvider.getUriForFile(
                        it,
                        "com.example.notas.fileprovider",
                        videoFile
                    )
                }
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uriVideo)
            }
            startActivityForResult(intent, VIDEO_REQUEST)
        }
    }

    private fun takePicture() {
        val intent: Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(getActivity()!!.packageManager) != null) {
            var imageFile: File? = null
            try {
                imageFile = createImage()
            } catch (e: IOException) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
            if (imageFile != null) {
                val uriImage: Uri? = getActivity()?.let {
                    FileProvider.getUriForFile(
                        it,
                        "com.example.notas.fileprovider",
                        imageFile
                    )
                }
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uriImage)
            }
            startActivityForResult(intent, CAMERA_REQUEST)
        }
    }


    private fun startRecord() {
        createAudio()
        if (ruteAudio != "") {
            miGrabacion = MediaRecorder()
            miGrabacion?.setAudioSource(MediaRecorder.AudioSource.MIC);
            miGrabacion?.setOutputFormat(
                MediaRecorder.OutputFormat.THREE_GPP
            );
            miGrabacion?.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
            miGrabacion?.setOutputFile(ruteAudio);
            try {
                miGrabacion?.prepare();
                miGrabacion?.start();
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun stopRecord() {
        listaRecursos.add(
            RecursosNota(
                ruteAudio,
                "audio"
            )
        )
        if (miGrabacion != null) {
            miGrabacion?.stop();
            miGrabacion?.release();
            miGrabacion = null;
        }
        Toast.makeText(context, "Se detuvo la grabacion", Toast.LENGTH_LONG).show()
        val m = MediaPlayer()
        try {
            m.setDataSource(ruteAudio);
        } catch (e: Exception) {
            e.printStackTrace();
        }
        try {
            m.prepare();
        } catch (e: IOException) {
            e.printStackTrace();
        }
        m.start();
        Toast.makeText(context, "reproducción de audio", Toast.LENGTH_LONG).show();
    }
    var rute: String = ""
    var ruteAudio = ""

    private fun createAudio() {
        val audioName = "audio_"
        val directory: File? = activity.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
        val audio = File.createTempFile(
            audioName,
            ".3gp",
            directory
        )
        ruteAudio = audio.absolutePath
    }


    @Throws(IOException::class)
    private fun createImage(): File {
        val imageName = "foto_"
        val directory: File? = getActivity()!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(imageName, ".jpg", directory)
        rute = image.absolutePath
        Toast.makeText(context, rute, Toast.LENGTH_SHORT).show()
        return image
    }

    var videoRute = ""
    private fun createVideo(): File {
        val videoName = "video_"
        val directory: File? =
            activity.getExternalFilesDir(Environment.DIRECTORY_MOVIES)
        val video =
            File.createTempFile(
                videoName, ".mp4",
                directory
            )
        videoRute = video.absolutePath
        return video
    }


    private fun addNote() {
        try {
            context?.let {
                daoNota(it).insert(
                    Nota(
                        title.text.toString(),
                        description.text.toString()
                    )
                )
            }
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun addResourcesToDB() {

        try {
            Toast.makeText(context, listaRecursos.size.toString(), Toast.LENGTH_SHORT).show()
            listaRecursos.forEach {
                context?.let { context ->
                    daoRecursosNota(context)
                        .insert(it)
                }
            }
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
        }
    }

    private fun takeImageFromGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Seleccione una imagen"),
            GALLERY_REQUEST
        )
    }

    private fun custom_dialog() {
        val dialog: Dialog? = context?.let { Dialog(it) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.dialog_custom)
        val text: EditText? = dialog?.findViewById(R.id.custom_text)
        val button: Button? = dialog?.findViewById(R.id.custom_button)
        button?.setOnClickListener {
            saveFile(text?.text.toString())
            Snackbar.make(button, "Holi", Snackbar.LENGTH_SHORT).show()
        }
        dialog?.show()
    }

    private fun customDialogAudio() {
        val dialog: Dialog? = context?.let {
            Dialog(it)
        }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.dialog_custom_audio)
        val play: Button? = dialog?.findViewById(R.id.button_record)
        val stop: Button? = dialog?.findViewById(R.id.button_stop)
        play?.setOnClickListener {
            startRecord()
        }
        stop?.setOnClickListener {
            stopRecord()
        }
        dialog?.show()
    }

    private fun saveFile(text: String) {
        val file_name = "file_${System.currentTimeMillis()}.txt"
        var fileOutputStream: FileOutputStream? = null
        try {
            fileOutputStream = context?.openFileOutput(file_name, MODE_PRIVATE)
            fileOutputStream?.write(text.toByteArray())
            Toast.makeText(context, "${context?.filesDir}/${file_name}", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (fileOutputStream != null) {
                try {
                    listaRecursos.add(RecursosNota(file_name, "file"))
                    fileOutputStream.close()
                } catch (e: IOException) {

                }
            }
        }
        Toast.makeText(context, readFile(file_name), Toast.LENGTH_SHORT).show()
    }

    private fun readFile(name: String): String {
        var fileInputStream: FileInputStream? = null
        var stringBuilder = StringBuilder()
        try {
            fileInputStream = context?.openFileInput(name)
            var inputStreamReader = InputStreamReader(fileInputStream)
            var buffereader: BufferedReader = BufferedReader(inputStreamReader)
            var texto: String? = ""
            stringBuilder = StringBuilder()
            while (texto != null) {
                texto = buffereader.readLine()
                if (texto != null) {
                    stringBuilder.append(texto).append("\n")
                } else
                    break
            }
        } catch (e: IOException) {

        }
        return stringBuilder.toString()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK) {
            val uri: Uri? = data?.data
            listaRecursos.add(RecursosNota(uri.toString(), "image"))
            Toast.makeText(context, uri.toString(), Toast.LENGTH_SHORT).show()
        }
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            val bit: Bitmap = BitmapFactory.decodeFile(rute)
            listaRecursos.add(RecursosNota(rute, "image"))

        }
        if (requestCode == VIDEO_REQUEST && resultCode == Activity.RESULT_OK) {
            val uri: Uri? = data!!.data
            listaRecursos.add(RecursosNota(videoRute, "video"))
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == CAMERA_REQUEST) {
            if (permissions.size >= 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePicture()
            }
        }
        if (requestCode == PERMISSION_WRITTE_STORAGE) {
            if (permissions.size >= 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePicture()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    companion object {
        private const val CAMERA_REQUEST = 123
        private const val GALLERY_REQUEST = 124
        private const val PERMISSION_WRITTE_STORAGE = 125
        private const val VIDEO_REQUEST = 126

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment fragment_agregar_nota.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            fragment_agregar_nota().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return true
    }
}
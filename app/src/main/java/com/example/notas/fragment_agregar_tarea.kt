package com.example.notas

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
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
import com.example.notas.data.RecursosNota
import com.example.notas.data.RecursosTarea
import com.example.notas.data.daoRecursosTarea
import com.example.notas.data.daoTarea
import com.example.notas_001.datos.Tarea
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_agregar_tarea.*
import java.io.*
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.jvm.Throws

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [fragment_agregar_tarea.newInstance] factory method to
 * create an instance of this fragment.
 */
class fragment_agregar_tarea : Fragment(),
    PopupMenu.OnMenuItemClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var title: EditText
    private lateinit var description: EditText
    private lateinit var btnAccept: Button
    private lateinit var floating: com.google.android.material.floatingactionbutton.FloatingActionButton
    private lateinit var calendar: ImageView
    private lateinit var time: ImageView
    private var fechaActual = ""
    private var fecha_seleccionada = ""
    private var hora_seleccionada: String? = null
    private lateinit var listaRecursos: ArrayList<RecursosTarea>
    private var miGrabacion: MediaRecorder? = null
    private var ruteAudio = ""

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
        actualTime()
        val vista: View = inflater.inflate(R.layout.fragment_agregar_tarea, container, false)
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
                listaRecursos= savedInstanceState.getSerializable("recursos") as ArrayList<RecursosTarea>
            }
        }
    }

    private fun initialize(root: View) {
        listaRecursos = ArrayList<RecursosTarea>()
        title = root.findViewById(R.id.txtTitleTask)
        description = root.findViewById(R.id.txtDescriptionTask)
        btnAccept = root.findViewById(R.id.btnAddTask)
        floating = root.findViewById(R.id.floatingTask)
        calendar = root.findViewById(R.id.imageCalendar)
        time = root.findViewById(R.id.imageTime)
        btnAccept.setOnClickListener {
            addTaskToDataBase()
            addResourcesToDB()
            getActivity()?.onBackPressed();
        }
        floating.setOnClickListener {
            val popup: PopupMenu = PopupMenu(
                context, floating
            )
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
        calendar.setOnClickListener {
            showDatePickerDialog()
        }
        time.setOnClickListener {
            showTimePicked()
        }
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
                Toast.makeText(context, "Comenzando a grabar audio", Toast.LENGTH_LONG).show()
                miGrabacion?.prepare();
                miGrabacion?.start();
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun stopRecord() {
        listaRecursos.add(
            RecursosTarea(
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

    private fun createAudio() {
        val audioName = "audio_"
        val directory: File? = activity!!.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
        val audio = File.createTempFile(
            audioName,
            ".mp3",
            directory
        )
        ruteAudio = audio.absolutePath
    }

    private fun custom_dialog() {
        val dialog: Dialog? = context?.let { Dialog(it) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.dialog_custom)
        val text: EditText? = dialog?.findViewById(R.id.custom_text)
        val button: Button? = dialog?.findViewById(R.id.custom_button)
        button?.setOnClickListener {
            saveFile(text?.text.toString())
            Snackbar.make(button, "Archivo guardado", Snackbar.LENGTH_SHORT).show()
        }
        dialog?.show()
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

    private fun takeVideo() {
        val intent: Intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE)
        if (intent.resolveActivity(activity!!.packageManager) != null) {
            var videoFile: File? = null
            try {
                videoFile = createVideo()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            if (videoFile != null) {
                val uriVideo: Uri? = getActivity()?.let {
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
        val directory: File? = activity!!.getExternalFilesDir(Environment.DIRECTORY_MOVIES)
        val video = File.createTempFile(videoName, ".mp4", directory)
        videoRute = video.absolutePath
        return video
    }

    var rute: String = ""
    private fun addTaskToDataBase() {
        try {
            val fecha_final = "$fecha_seleccionada $hora_seleccionada"
            val tarea = Tarea(title.text.toString(), description.text.toString(), fecha_final)
            context?.let { daoTarea(it).insert(tarea) }
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun addResourcesToDB() {
        try {
            Toast.makeText(context, listaRecursos.size.toString(), Toast.LENGTH_SHORT).show()
            listaRecursos.forEach { item ->
                context?.let {
                    daoRecursosTarea(it)
                        .insert(item)
                }
            }
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun actualTime() {
        val dfDate_day = SimpleDateFormat("dd/MM/yyyy hh:mm a")
        val c = Calendar.getInstance()
        fechaActual = dfDate_day.format(c.time)
    }

    private fun showTimePicked() {
        val getDate = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(
            context,
            OnTimeSetListener { view, hourOfDay, minute ->
                getDate[Calendar.HOUR_OF_DAY] = hourOfDay
                getDate[Calendar.MINUTE] = minute
                val timeformat = SimpleDateFormat("hh:mm a")
                hora_seleccionada = timeformat.format(getDate.time)
            }, getDate[Calendar.HOUR_OF_DAY], getDate[Calendar.MINUTE], false
        )
        timePickerDialog.show()
    }

    private fun showDatePickerDialog() {
        val newFragment =
            DatePickerFragment.newInstance { datePicker, year, month, day -> // +1 because January is zero
                fecha_seleccionada = day.toString() + "/" + (month + 1) + "/" + year
            }
        activity?.supportFragmentManager?.let { newFragment.show(it, "datePicker") }
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

    private fun saveFile(text: String) {
        val file_name = "file_${System.currentTimeMillis()}.txt"
        var fileOutputStream: FileOutputStream? = null
        try {
            fileOutputStream = context?.openFileOutput(file_name, Context.MODE_PRIVATE)
            fileOutputStream?.write(text.toByteArray())
            Toast.makeText(context, "${context?.filesDir}/${file_name}", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (fileOutputStream != null) {
                try {
                    listaRecursos.add(RecursosTarea(file_name, "file"))
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
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_REQUEST && resultCode == Activity.RESULT_OK) {
            val uri: Uri? = data?.data
            listaRecursos.add(RecursosTarea(uri.toString(), "image"))
            Toast.makeText(context, uri.toString(), Toast.LENGTH_SHORT).show()
        }
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            val bit: Bitmap = BitmapFactory.decodeFile(rute)
            listaRecursos.add(RecursosTarea(rute, "image"))

        }
        if (requestCode == VIDEO_REQUEST && resultCode == Activity.RESULT_OK) {
            val uri: Uri? = data!!.data
            listaRecursos.add(RecursosTarea(videoRute, "video"))
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
         * @return A new instance of fragment fragment_agregar_tarea.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            fragment_agregar_tarea().apply {
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
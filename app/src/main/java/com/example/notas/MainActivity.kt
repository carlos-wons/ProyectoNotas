package com.example.notas

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.notas.data.daoTarea
import com.google.android.material.navigation.NavigationView
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var toolBar: Toolbar
    private lateinit var navigationView: NavigationView

    private var handler: Handler? = null

    //VARIABLES PARA CARGAR EL FRAGMENT
    private lateinit var fragmentManager: FragmentManager
    private lateinit var fragmentTransaction: FragmentTransaction
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Mostrar los componentes
    if (ContextCompat.checkSelfPermission(
                applicationContext,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                applicationContext,
                android.Manifest.permission.RECORD_AUDIO
            )
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                applicationContext,
                android.Manifest.permission.CAMERA
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.RECORD_AUDIO, android.Manifest.permission.CAMERA
                ),
                1000
            )
        }


        toolBar = findViewById(R.id.toolBar)
        handler = Handler()
        setSupportActionBar(toolBar)
        drawerLayout = findViewById(R.id.drawer)
        navigationView = findViewById(R.id.navigationView)

        //ESTABLECER EVENTO ONCLICK A NAVIGATIONVIEW
        navigationView.setNavigationItemSelectedListener(this)
        actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolBar,
            R.string.open,
            R.string.close
        )
        notification()
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.isDrawerIndicatorEnabled = true
        actionBarDrawerToggle.syncState()
        // CARGAR FRAGMENT PRINCIPAL
        fragmentManager = supportFragmentManager
        fragmentTransaction = fragmentManager.beginTransaction().addToBackStack(null)
        fragmentTransaction.add(R.id.contenedor_pequeño, fragment_ver_notas())
        fragmentTransaction.commit()


    }


    fun changeFragmentAddTask(obj: fragment_agregar_tarea) {
        try {
            fragmentManager = supportFragmentManager
            fragmentTransaction = fragmentManager.beginTransaction().addToBackStack(null)
            fragmentTransaction.replace(R.id.contenedor_pequeño, obj)
            fragmentTransaction.commit()
        } catch (e: Exception) {
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun changeFragmentAddNote(obj: fragment_agregar_nota) {
        try {
            fragmentManager = supportFragmentManager
            fragmentTransaction = fragmentManager.beginTransaction().addToBackStack(null)
            fragmentTransaction.replace(R.id.contenedor_pequeño, obj)
            fragmentTransaction.commit()
        } catch (e: Exception) {
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun changeFragmentViewNote(obj: ver_nota_seleccionada) {
        try {
            fragmentManager = supportFragmentManager
            fragmentTransaction = fragmentManager.beginTransaction().addToBackStack(null)
            fragmentTransaction.replace(R.id.contenedor_pequeño, obj)
            fragmentTransaction.commit()
        } catch (e: Exception) {
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun changeFragmentViewFilesNote(obj: fragment_ver_archivos) {
        try {
            fragmentManager = supportFragmentManager
            fragmentTransaction = fragmentManager.beginTransaction().addToBackStack(null)
            fragmentTransaction.replace(R.id.contenedor_pequeño, obj)
            fragmentTransaction.commit()
        } catch (e: Exception) {
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun changeFragmentViewAudioNote(obj: fragment_ver_audios){
        try {
            fragmentManager = supportFragmentManager
            fragmentTransaction = fragmentManager.beginTransaction().addToBackStack(null)
            fragmentTransaction.replace(R.id.contenedor_pequeño, obj)
            fragmentTransaction.commit()
        } catch (e: Exception) {
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun changeFragmentViewAudioTask(obj: fragment_ver_audios_tareas){
        try {
            fragmentManager = supportFragmentManager
            fragmentTransaction = fragmentManager.beginTransaction().addToBackStack(null)
            fragmentTransaction.replace(R.id.contenedor_pequeño, obj)
            fragmentTransaction.commit()
        } catch (e: Exception) {
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun changeFragmentViewVideoNote(obj: fragment_ver_videos_notas) {
        try {
            fragmentManager = supportFragmentManager
            fragmentTransaction = fragmentManager.beginTransaction().addToBackStack(null)
            fragmentTransaction.replace(R.id.contenedor_pequeño, obj)
            fragmentTransaction.commit()
        } catch (e: Exception) {
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun changeFragmentViewFilesTask(obj: fragment_ver_archivos_tarea) {
        try {
            fragmentManager = supportFragmentManager
            fragmentTransaction = fragmentManager.beginTransaction().addToBackStack(null)
            fragmentTransaction.replace(R.id.contenedor_pequeño, obj)
            fragmentTransaction.commit()
        } catch (e: Exception) {
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun changeFragmentViewVideoTask(obj: fragment_ver_videos) {
        try {
            fragmentManager = supportFragmentManager
            fragmentTransaction = fragmentManager.beginTransaction().addToBackStack(null)
            fragmentTransaction.replace(R.id.contenedor_pequeño, obj)
            fragmentTransaction.commit()
        } catch (e: Exception) {
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun changeFragmentViewImages(obj: fragment_ver_imagenes) {
        try {
            fragmentManager = supportFragmentManager
            fragmentTransaction = fragmentManager.beginTransaction().addToBackStack(null)
            fragmentTransaction.replace(R.id.contenedor_pequeño, obj)
            fragmentTransaction.commit()
        } catch (e: Exception) {
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun changeFragmentViewImagesTask(obj: fragment_ver_imagenes_tarea) {
        try {
            fragmentManager = supportFragmentManager
            fragmentTransaction = fragmentManager.beginTransaction().addToBackStack(null)
            fragmentTransaction.replace(R.id.contenedor_pequeño, obj)
            fragmentTransaction.commit()
        } catch (e: Exception) {
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun changeFragmentViewTask(obj: fragment_ver_tarea_selecionada) {
        try {
            fragmentManager = supportFragmentManager
            fragmentTransaction = fragmentManager.beginTransaction().addToBackStack(null)
            fragmentTransaction.replace(R.id.contenedor_pequeño, obj)
            fragmentTransaction.commit()
        } catch (e: Exception) {
            Toast.makeText(applicationContext, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        drawerLayout.closeDrawer(GravityCompat.START)
        when (item.itemId) {
            R.id.agregar_nota -> {
                if (findViewById<View?>(R.id.contenedor_pequeño) != null
                ) {
                    fragmentManager = supportFragmentManager
                    fragmentTransaction = fragmentManager.beginTransaction().addToBackStack(null)
                    fragmentTransaction.replace(R.id.contenedor_pequeño, fragment_ver_notas())
                    fragmentTransaction.commit()
                }

            }
            R.id.agregar_tarea -> {
                fragmentManager = supportFragmentManager
                fragmentTransaction = fragmentManager.beginTransaction().addToBackStack(null)
                fragmentTransaction.replace(R.id.contenedor_pequeño, fragment_ver_tareas())
                fragmentTransaction.commit()
            }
        }
        return false
    }

    private fun message(
        titulo: String,
        texto: String,
        idTarea: Int
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel("n", "n", NotificationManager.IMPORTANCE_DEFAULT)
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(notificationChannel)
        }

        //AGREGAR ACCION AL TOCAR LA NOTIFICACION
        val intent = Intent(this, fragment_ver_tarea_selecionada::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra("idTarea", idTarea)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val builder =
            NotificationCompat.Builder(this, "n")
                .setContentText(titulo)
                .setSmallIcon(R.drawable.ic_notification)
                .setAutoCancel(true)
                .setContentTitle(texto)
                .setContentIntent(pendingIntent)
        val managerCompat = NotificationManagerCompat.from(this)
        managerCompat.notify(999, builder.build())
    }

    private fun notification() {
        handler?.postDelayed(object : Runnable {
            @RequiresApi(api = Build.VERSION_CODES.N)
            override fun run() {
                checkdateFromDataBase()
                handler?.postDelayed(this, 2000)
            }
        }, 2000)
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private fun checkdateFromDataBase() {
        val list = daoTarea(this).getAll()
            ?.forEach { item ->
                var op: String = actualTime()
                if (actualTime().startsWith('0')) {
                    op = actualTime().substring(1)
                }
                if (item.fecha == op) {
                    message(
                        "Hoy es la fecha de esta tarea ",
                        "La tarea es ${item.titulo}  \n ${item.fecha}", item.idTarea
                    )
                }
            }
    }

    private fun actualTime(): String {
        val dfDate_day =
            SimpleDateFormat("dd/MM/yyyy hh:mm a")
        val c = Calendar.getInstance()
        return dfDate_day.format(c.time)
    }


}
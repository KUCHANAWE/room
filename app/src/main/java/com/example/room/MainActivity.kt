package com.example.room

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.room.database.daftarBelanja
import com.example.room.database.daftarBelanjaDB
import com.example.room.database.historyBelanja
import com.example.room.database.historyBelanjaDB
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var DB: daftarBelanjaDB
    private lateinit var DBHistory: historyBelanjaDB
    private lateinit var adapterDaftar: adapterDaftar
    private var arDaftar: MutableList<daftarBelanja> = mutableListOf()

    private var isShowingHistory = false

    override fun onCreate(savedInstanceState: Bundle?) {
        adapterDaftar = adapterDaftar(arDaftar)

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        var _rvDaftar = findViewById<RecyclerView>(R.id.rvNotes)
        _rvDaftar.layoutManager = LinearLayoutManager(this)
        _rvDaftar.adapter = adapterDaftar
        DB = daftarBelanjaDB.getDatabase(this)
        DBHistory = historyBelanjaDB.getDatabase(this)

        val _fabAdd = findViewById<FloatingActionButton>(R.id.fabAdd)
        val _btnHistory = findViewById<Button>(R.id.btnHistory)

        _fabAdd.setOnClickListener {
            startActivity(Intent(this, TambahDaftar::class.java))
        }

        adapterDaftar.setOnItemClickCallback(
            object : adapterDaftar.OnItemClickCallback {
                override fun detData(dtBelanja: daftarBelanja) {
                    CoroutineScope(Dispatchers.IO).async {
                        DB.fundaftarBelanjaDAO().delete(dtBelanja)
                        val daftar = DB.fundaftarBelanjaDAO().selectALL()
                        withContext(Dispatchers.Main) {
                            adapterDaftar.isiData(daftar)
                        }
                    }
                }

                override fun selData(dtBelanja: daftarBelanja) {
                    CoroutineScope(Dispatchers.IO).async {
                        DB.fundaftarBelanjaDAO().delete(dtBelanja)
                        // Pindahkan ke historyBelanja
                        val historyItem = historyBelanja(
                            tanggal = dtBelanja.tanggal,
                            item = dtBelanja.item,
                            jumlah = dtBelanja.jumlah,
                            status = 1 // 1 menandakan selesai
                        )
                        DBHistory.funhistoryBelanjaDAO().insert(historyItem)

                        // Perbarui tampilan daftar
                        val daftar = DB.fundaftarBelanjaDAO().selectALL()
                        withContext(Dispatchers.Main) {
                            adapterDaftar.isiData(daftar)
                        }
                    }
                }

            })

        _btnHistory.setOnClickListener {
            if (isShowingHistory) {
                // Tampilkan daftar belanja yang belum selesai
                CoroutineScope(Dispatchers.Main).launch {
                    val daftarBelanja = DB.fundaftarBelanjaDAO().selectALL()
                    adapterDaftar.isiData(daftarBelanja)
                    _btnHistory.text = "History" // Ubah teks tombol
                    isShowingHistory = false
                }
            } else {
                // Tampilkan daftar history
                CoroutineScope(Dispatchers.Main).launch {
                    val daftarHistory = DBHistory.funhistoryBelanjaDAO().selectAll()
                    adapterDaftar.isiData(daftarHistory.map {
                        daftarBelanja(
                            id = it.id,
                            tanggal = it.tanggal,
                            item = it.item,
                            jumlah = it.jumlah
                        )
                    })
                    _btnHistory.text = "Kembali" // Ubah teks tombol
                    isShowingHistory = true
                }
            }
        }


        super.onStart()
        CoroutineScope(Dispatchers.Main).async {
            val daftarBelanaja = DB.fundaftarBelanjaDAO().selectALL()
            Log.d("data ROOM", daftarBelanaja.toString())
            adapterDaftar.isiData(daftarBelanaja)
        }
    }
}
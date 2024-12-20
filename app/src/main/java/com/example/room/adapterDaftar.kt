package com.example.room

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.room.database.daftarBelanja
import com.google.android.material.floatingactionbutton.FloatingActionButton

class adapterDaftar (private val daftarBelanja : MutableList<daftarBelanja>): RecyclerView.Adapter<adapterDaftar.ListViewHolder>(){

    private lateinit var onItemClickCallback : OnItemClickCallback

    interface OnItemClickCallback{
        fun detData(dtBelanja: daftarBelanja)
        fun selData(dtBelanja: daftarBelanja)
    }
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): adapterDaftar.ListViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.item_recycle,parent,false)
        return ListViewHolder(view)
    }

    inner class  ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var _tvItemBarang = itemView.findViewById<TextView>(R.id.tvItemBarang)
        var _tvjumlahBarang = itemView.findViewById<TextView>(R.id.tvJumlahBarang)
        var _tvTanggal = itemView.findViewById<TextView>(R.id.tvTanggal)

        var _btnEdit = itemView.findViewById<ImageButton>(R.id.btnEdit)
        var _btnDelete = itemView.findViewById<ImageButton>(R.id.btnDelete)
        var _btnSelesai = itemView.findViewById<Button>(R.id.btnSelesai)
    }
    override fun getItemCount(): Int {
        return daftarBelanja.size
    }

    fun isiData (daftar: List<daftarBelanja>){
        daftarBelanja.clear()
        daftarBelanja.addAll(daftar)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: adapterDaftar.ListViewHolder, position: Int) {
        var daftar = daftarBelanja[position]
        holder._tvTanggal.setText(daftar.tanggal)
        holder._tvItemBarang.setText(daftar.item)
        holder._tvjumlahBarang.setText(daftar.jumlah)

        holder._btnEdit.setOnClickListener {
            val intent = Intent(it.context, TambahDaftar::class.java)
            intent.putExtra("id", daftar.id)

            intent.putExtra("addEdit",1)
            it.context.startActivity(intent)
        }
        holder._btnDelete.setOnClickListener {
            onItemClickCallback.detData(daftar)
        }
        holder._btnSelesai.setOnClickListener {
            onItemClickCallback.selData(daftar)
        }
    }
}

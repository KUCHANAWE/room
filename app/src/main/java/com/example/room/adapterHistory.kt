package com.example.room

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.room.database.historyBelanja
import com.example.room.adapterDaftar.OnItemClickCallback

class adapterHistory (private val historyBelanja : MutableList<historyBelanja>): RecyclerView.Adapter<adapterHistory.ListViewHolder>() {

    private lateinit var onItemClickCallbac : OnItemClickCallback


    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var _tvTanggaal = itemView.findViewById<TextView>(R.id.Tanggal)
        var _tvItemBarang = itemView.findViewById<TextView>(R.id.ItemBarang)
        var _tvjumlahBarang = itemView.findViewById<TextView>(R.id.JumlahBarang)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): adapterHistory.ListViewHolder {
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.item_recycle_history,parent,false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: adapterHistory.ListViewHolder, position: Int) {
        val history = historyBelanja[position]
        holder._tvTanggaal.text = history.tanggal
        holder._tvItemBarang.text = history.item
        holder._tvjumlahBarang.text = history.jumlah
    }

    override fun getItemCount(): Int {
        return historyBelanja.size
    }
}

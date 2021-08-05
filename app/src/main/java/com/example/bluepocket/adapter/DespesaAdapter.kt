package com.example.bluepocket.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bluepocket.R
import com.example.bluepocket.entity.Despesa
import java.text.SimpleDateFormat
import java.util.*

class DespesaAdapter (val context: Context, val despesas: List<Despesa>): RecyclerView.Adapter<DespesaAdapter.DespesaViewHolder>(){

    private val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DespesaViewHolder {
        val view = layoutInflater.inflate(R.layout.itens_recyclerview_list, parent, false)
        val holder = DespesaViewHolder(context, view)

        return holder
    }

    override fun getItemCount(): Int {
        return despesas.size
    }

    override fun onBindViewHolder(holder: DespesaViewHolder, position: Int) {
        var formatter: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")

        var dateString = formatter.format(Date(despesas[position].data))

        holder.listName.text = despesas[position].nome
        holder.listDate.text = dateString
        holder.listvalue.text = "R$ ${despesas[position].valor}"
    }

    class DespesaViewHolder(val context: Context, view: View): RecyclerView.ViewHolder(view){
        var listName: TextView = view.findViewById(R.id.itens_textView_name)

        var listDate: TextView = view.findViewById(R.id.itens_textView_date)

        var listvalue: TextView = view.findViewById(R.id.itens_textView_value)
    }
}
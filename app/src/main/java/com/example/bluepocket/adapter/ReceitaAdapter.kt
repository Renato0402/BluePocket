package com.example.bluepocket.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bluepocket.R
import com.example.bluepocket.entity.Receita
import java.text.SimpleDateFormat
import java.util.*

class ReceitaAdapter (val context: Context, val receitas: List<Receita>): RecyclerView.Adapter<ReceitaAdapter.ReceitaViewHolder>(){

    private val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceitaViewHolder {
        val view = layoutInflater.inflate(R.layout.itens_recyclerview_list, parent, false)
        val holder = ReceitaViewHolder(context, view)

        return holder
    }

    override fun getItemCount(): Int {
        return receitas.size
    }

    override fun onBindViewHolder(holder: ReceitaViewHolder, position: Int) {

        var formatter: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")

        var dateString = formatter.format(Date(receitas[position].data))

        holder.listName.text = receitas[position].nome
        holder.listDate.text = dateString
        holder.listvalue.text = "R$ ${receitas[position].valor}"
    }

    class ReceitaViewHolder(val context: Context, view: View): RecyclerView.ViewHolder(view){

        var listName: TextView = view.findViewById(R.id.itens_textView_name)

        var listDate: TextView = view.findViewById(R.id.itens_textView_date)

        var listvalue: TextView = view.findViewById(R.id.itens_textView_value)
    }
}
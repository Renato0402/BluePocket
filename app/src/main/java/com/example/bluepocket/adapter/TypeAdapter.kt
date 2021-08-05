package com.example.bluepocket.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bluepocket.R
import com.example.bluepocket.activity.NavigationActivity
import com.example.bluepocket.entity.Type
import com.example.bluepocket.fragments.FragmentList


class TypeAdapter(val context: Context, val types: List<Type>, var controle: Int) : RecyclerView.Adapter<TypeAdapter.TypeViewHolder>() {

    private val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeViewHolder {
        val view = layoutInflater.inflate(R.layout.item_recyclerview_list, parent, false)
        val holder = TypeViewHolder(context, view, types, controle)

        return holder
    }

    override fun getItemCount(): Int {
        return types.size
    }

    override fun onBindViewHolder(holder: TypeViewHolder, position: Int) {
        holder.typesaName.text = types[position].nome

        holder.listener()
    }

    class TypeViewHolder(val context: Context, view: View, val types: List<Type>, var controle: Int) : RecyclerView.ViewHolder(view), View.OnClickListener {

        var typesaName: TextView = view.findViewById(R.id.item_textView_name)

        var view = view

        fun listener() {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val fragmentList = FragmentList(types[position].id, controle)

            if (context is NavigationActivity) {
                fragmentList.addFragmentListener(context)

                context.setmTypeId(types[position].id)

                context.supportFragmentManager.beginTransaction()
                    .replace(R.id.navigation_framelayout_container, fragmentList)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
}
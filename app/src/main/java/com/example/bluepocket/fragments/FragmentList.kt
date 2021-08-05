package com.example.bluepocket.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bluepocket.R
import com.example.bluepocket.activity.MainActivity
import com.example.bluepocket.adapter.DespesaAdapter
import com.example.bluepocket.adapter.ReceitaAdapter
import com.example.bluepocket.entity.Despesa
import com.example.bluepocket.entity.Receita
import com.example.bluepocket.util.IFragmentListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FragmentList(val typeId: String, val controle: Int) : Fragment() {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mFloatButton: FloatingActionButton

    private var mFragmentListener: IFragmentListener? = null

    lateinit var mainDatabase: FirebaseDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_list, container, false)

        mRecyclerView = view.findViewById(R.id.recyclerView_list)

        mFloatButton = view.findViewById(R.id.fragment_floatingButton_add)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mFloatButton.setOnClickListener {

            mFragmentListener?.onFragmentClick(it)
        }
    }

    override fun onStart() {
        super.onStart()

        mainDatabase = FirebaseDatabase.getInstance()

        if (controle == 1) {
            val receitas = mutableListOf<Receita>()

            val layoutManager = LinearLayoutManager(context!!)
            val adapter = ReceitaAdapter(context!!, receitas)

            mRecyclerView.layoutManager = layoutManager
            mRecyclerView.adapter = adapter

            val ReceitaRef = mainDatabase.getReference("receitas")
            val query = ReceitaRef.orderByChild("typeId").equalTo(typeId) .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(databseError: DatabaseError) {

                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        dataSnapshot.children.forEach {
                            val receita = it.getValue(Receita::class.java)

                            receitas.add(receita!!)

                            receitas.sortBy { it.data }

                            adapter.notifyDataSetChanged()
                        }
                    }
                })
        } else if (controle == 2) {
            val despesas = mutableListOf<Despesa>()

            val layoutManager = LinearLayoutManager(context!!)
            val adapter = DespesaAdapter(context!!, despesas)

            mRecyclerView.layoutManager = layoutManager
            mRecyclerView.adapter = adapter

            val despesRef = mainDatabase.getReference("despesas")
            val query = despesRef.orderByChild("typeId").equalTo(typeId).addValueEventListener(object : ValueEventListener {
                override fun onCancelled(databseError: DatabaseError) {

                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataSnapshot.children.forEach {
                        val despesa = it.getValue(Despesa::class.java)

                        despesas.add(despesa!!)

                        despesas.sortBy { it.data }

                        adapter.notifyDataSetChanged()
                    }
                }
            })
        }
    }

    fun addFragmentListener(listener: IFragmentListener) {
        this.mFragmentListener = listener
    }
}
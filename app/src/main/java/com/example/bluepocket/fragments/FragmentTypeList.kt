package com.example.bluepocket.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bluepocket.R
import com.example.bluepocket.adapter.TypeAdapter
import com.example.bluepocket.entity.Type
import com.example.bluepocket.util.IFragmentListener
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FragmentTypeList(val userId: String, val controle: Int) : Fragment() {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mFloatButton: FloatingActionButton

    private var mFragmentListener: IFragmentListener? = null

    lateinit var mainDatabase: FirebaseDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_type_list, container, false)

        mRecyclerView = view.findViewById(R.id.type_recyclerView_list)

        mFloatButton = view.findViewById(R.id.fragment_type_floatingButton_add)


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

        val types = mutableListOf<Type>()

        val layoutManager = LinearLayoutManager(context!!)
        val adapter = TypeAdapter(context!!, types, controle)

        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.adapter = adapter

        val typeRef = mainDatabase.getReference("types")
        val query = typeRef.orderByChild("userID").equalTo(userId).addValueEventListener(object: ValueEventListener {
            override fun onCancelled(databseError: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach{

                    if(controle == 1){
                        val type = it.getValue(Type::class.java)

                        if (type!!.typeOf == "receita"){
                            types.add(type)
                        }
                    }else if(controle == 2){
                        val type = it.getValue(Type::class.java)

                        if (type!!.typeOf == "despesa"){
                            types.add(type)
                        }
                    }

                    adapter.notifyDataSetChanged()
                }
            }
        })
    }

    fun addFragmentListener(listener: IFragmentListener) {
        this.mFragmentListener = listener
    }
}
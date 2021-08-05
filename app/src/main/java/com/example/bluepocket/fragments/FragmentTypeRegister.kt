package com.example.bluepocket.fragments

import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.bluepocket.R
import com.example.bluepocket.entity.Type
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class FragmentTypeRegister(val userId: String, val controle: Int) : Fragment() {

    private lateinit var tipoTexto: TextView
    private lateinit var mRegisterName: EditText
    private lateinit var mRegisterAdd: Button

    lateinit var mainDatabase: FirebaseDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_type_register, container, false)

        tipoTexto = view.findViewById(R.id.name_textView)
        mRegisterAdd = view.findViewById(R.id.fragment_TypeRegister_add)
        mRegisterName = view.findViewById(R.id.fragment_TypeRegister_name)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(controle == 1)
            tipoTexto.text = "Novo tipo de receita"

        else if(controle == 2)

            tipoTexto.text = "Novo tipo de despesa"


        mRegisterAdd.setOnClickListener {

            if (mRegisterName.text.isEmpty()) {
                mRegisterName.error = "Este campo não pode ser nulo"
            } else {
                val name = mRegisterName.text.toString()

                var tipo = ""

                if(controle == 1)

                    tipo = "receita"
                else if(controle == 2)

                    tipo = "despesa"


                val typeRef: DatabaseReference = mainDatabase.getReference("types")
                val typeId = typeRef.push().key

                val type = Type(id = typeId!!, nome = name, typeOf = tipo, userID = userId)

                Log.i("BluePocket", "tipo " + type.toString())

                typeRef.child(typeId).setValue(type).addOnCompleteListener {
                    val dialog = AlertDialog.Builder(context!!)
                        .setMessage("Tipo inserido com sucesso")
                        .setTitle("Blue Pocket")
                        .setCancelable(false)
                        .setNeutralButton("OK", DialogInterface.OnClickListener { dialog, id ->
                            dialog.dismiss()
                            val imm =
                                context!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                            imm.hideSoftInputFromWindow(view!!.windowToken, 0)
                            activity!!.supportFragmentManager.popBackStack()
                        }).create()
                    dialog.show()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        mainDatabase = FirebaseDatabase.getInstance()
    }
}


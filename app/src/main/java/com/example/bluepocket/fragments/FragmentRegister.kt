package com.example.bluepocket.fragments

import android.app.Activity
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.bluepocket.R
import com.example.bluepocket.entity.Receita
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import kotlin.system.measureTimeMillis


class FragmentRegister(val tipoId: String, val controle: Int) : Fragment() {

    private lateinit var mNovoTexto: TextView
    private lateinit var mRegisterName: EditText
    private lateinit var mValorEditText: EditText
    private lateinit var mDateButton: Button
    private lateinit var mRegisterAdd: Button

    lateinit var mainDatabase: FirebaseDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_register, container, false)

        mNovoTexto = view.findViewById(R.id.fragment_titulo_textView)
        mRegisterName = view.findViewById(R.id.fragment_Register_name)
        mValorEditText = view.findViewById(R.id.fragment_valor_editText)
        mDateButton = view.findViewById(R.id.fragment_date_button)
        mRegisterAdd = view.findViewById(R.id.fragment_Register_add)

        return view
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val calendar = Calendar.getInstance()
        val dia = calendar.get(Calendar.DAY_OF_MONTH)
        val mes = calendar.get(Calendar.MONTH)
        val ano = calendar.get(Calendar.YEAR)

        if(controle == 1)
            mNovoTexto.text = "Nova receita"

        else if(controle == 2)

            mNovoTexto.text = "Nova despesa"

        mDateButton.setOnClickListener{
            val dialog = DatePickerDialog(context, DatePickerDialog.OnDateSetListener{view, mAno, mMes, mDia -> mDateButton.text = "$mDia/${mMes+1}/$mAno"

                calendar.set(mAno, mMes, mDia)
            }, ano, mes, dia)

            dialog.show()
        }

        mRegisterAdd.setOnClickListener {

            if (mRegisterName.text.isEmpty())
                mRegisterName.error = "Este campo não pode ser vazio"

            else if(mValorEditText.text.isEmpty())
                mValorEditText.error = "Este campo não pode ser vazio"

             else if(mDateButton.text.toString() == "Data"){
                val dialog = AlertDialog.Builder(context!!)
                    .setMessage("A data de ocorrência do evento deve ser selecionada!")
                    .setTitle("Blue Pocket")
                    .setCancelable(false)
                    .setNeutralButton("OK", DialogInterface.OnClickListener { dialog, id ->
                        dialog.dismiss()
                        val imm =
                            context!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
                    }).create()
                dialog.show()
            } else {
                val name = mRegisterName.text.toString()

                var tipo = ""

                lateinit var novo: Any

                if(controle == 1){
                    tipo = "receita"

                    novo = Receita(nome = name, tipo = tipo, data = calendar.timeInMillis, valor = mValorEditText.text.toString().toDouble(), typeId = tipoId)

                    val receitaRef: DatabaseReference = mainDatabase.getReference("receitas")
                    val receitaId = receitaRef.push().key

                    receitaRef.child(receitaId!!).setValue(novo).addOnCompleteListener {
                        val dialog = AlertDialog.Builder(context!!)
                            .setMessage("Receita inserida com sucesso")
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

                else if(controle == 2){
                    tipo = "despesa"

                    novo = Receita(nome = name, tipo = tipo, data = calendar.timeInMillis, valor = mValorEditText.text.toString().toDouble(), typeId = tipoId)

                    val despesaRef: DatabaseReference = mainDatabase.getReference("despesas")
                    val despesaId = despesaRef.push().key

                    despesaRef.child(despesaId!!).setValue(novo).addOnCompleteListener {
                        val dialog = AlertDialog.Builder(context!!)
                            .setMessage("Despesa inserida com sucesso")
                            .setTitle("Blue Pocket")
                            .setCancelable(false)
                            .setNeutralButton("OK", DialogInterface.OnClickListener { dialog, id ->
                                dialog.dismiss()
                                val imm = context!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                                imm.hideSoftInputFromWindow(view!!.windowToken, 0)
                                activity!!.supportFragmentManager.popBackStack()
                            }).create()
                        dialog.show()
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        mainDatabase = FirebaseDatabase.getInstance()
    }
}


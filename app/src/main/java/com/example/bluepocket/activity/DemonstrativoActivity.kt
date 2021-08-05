package com.example.bluepocket.activity

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.bluepocket.R
import com.example.bluepocket.adapter.DemonstrativoAdapter
import com.example.bluepocket.entity.Despesa
import com.example.bluepocket.entity.Receita
import com.example.bluepocket.entity.Type
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class DemonstrativoActivity : AppCompatActivity() {
    lateinit var receitaTextView: TextView
    lateinit var despesaTextView: TextView

    lateinit var mNumberPicker: com.shawnlin.numberpicker.NumberPicker

    lateinit var mainDatabase: FirebaseDatabase

    lateinit var viewPager: ViewPager

    private lateinit var demonstrativoAdapter: DemonstrativoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demonstrativo)

        mainDatabase = FirebaseDatabase.getInstance()

        receitaTextView = findViewById(R.id.receitasDemo_textView)
        despesaTextView = findViewById(R.id.despesasDemo_textView)

        viewPager = findViewById(R.id.viewPager)

        mNumberPicker = findViewById(R.id.numberDemo_picker)

        mNumberPicker.minValue = 1

        mNumberPicker.maxValue = 12

        val calendar = Calendar.getInstance()

        var mes = calendar.get(Calendar.MONTH) + 1

        mNumberPicker.value = mes

        mNumberPicker.setOnValueChangedListener { picker, oldVal, newVal ->

            loadDemonstrativo(newVal)
        }

        loadDemonstrativo(mes)
    }

    fun loadDemonstrativo(mes: Int) {

        var ids = mutableListOf<Triple<String, String, String>>()

        var idsReceitas = mutableListOf<Pair<String, String>>()

        var idsDespesas = mutableListOf<Pair<String, String>>()

        var qtdtypesReceitas = mutableListOf<Pair<String, Int>>()

        var qtdtypesDespesas = mutableListOf<Pair<String, Int>>()

        var receitas = mutableListOf<Receita>()

        var despesas = mutableListOf<Despesa>()

        var itens = mutableListOf<Any>()

        var acumulaReceitaValor: Double = 0.0

        var acumulaDespesaValor: Double = 0.0

        val formatter: NumberFormat = DecimalFormat("###,###,##0.00")

        var formatterMes: SimpleDateFormat = SimpleDateFormat("MM")

        val receitaRef = mainDatabase.getReference("receitas")

        val despesaRef = mainDatabase.getReference("despesas")

        val typeRef = mainDatabase.getReference("types")

        val query = typeRef.orderByChild("userID")

        val query1 = receitaRef.orderByChild("typeId")

        val query2 = despesaRef.orderByChild("typeId")


        query.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databseError: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                acumulaDespesaValor = 0.0

                dataSnapshot.children.forEach {
                    var localType = it.getValue(Type::class.java)

                    ids.add(Triple(localType!!.id, localType.nome, localType.typeOf))

                }

                ids.forEach{
                    if(it.third == "receita"){
                        idsReceitas.add(Pair(it.first, it.second))

                    }else if(it.third == "despesa"){
                        idsDespesas.add(Pair(it.first, it.second))
                    }
                }

                query1.addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(databseError: DatabaseError) {

                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        acumulaReceitaValor = 0.0
                        var cont = 0


                        for (i in 0 until idsReceitas.size){

                            dataSnapshot.children.forEach {

                                if(idsReceitas[i].first == it.getValue(Receita::class.java)!!.typeId){
                                    cont++
                                }
                            }

                            qtdtypesReceitas.add(Pair(idsReceitas[i].second, cont))

                            cont = 0
                        }

                        qtdtypesReceitas.sortBy { it.second }

                        qtdtypesReceitas.reverse()

                        Log.i("BluePocket", "qtdtypesReceitas " + qtdtypesReceitas.toString())

                        dataSnapshot.children.forEach {

                            val receita = it.getValue(Receita::class.java)

                            var mesString = formatterMes.format(Date(receita!!.data))

                            if (mesString.toInt() == mes) {
                                acumulaReceitaValor += receita.valor

                                receitas.add(receita)
                            }

                        }

                        receitas.sortBy { it.valor }

                        var tamanho = receitas.size

                        for (i in 0..tamanho - 6) {
                            receitas.removeAt(i)
                        }

                        receitas.reverse()

                        itens.add(receitas)

                        val formattedNumber = formatter.format(acumulaReceitaValor)

                        receitaTextView.text = "R$ $formattedNumber"

                        query2.addValueEventListener(object : ValueEventListener {
                            override fun onCancelled(databseError: DatabaseError) {

                            }

                            override fun onDataChange(dataSnapshot: DataSnapshot) {

                                acumulaDespesaValor = 0.0

                                var cont1 = 0


                                for (i in 0 until idsDespesas.size){

                                    dataSnapshot.children.forEach {

                                        if(idsDespesas[i].first == it.getValue(Despesa::class.java)!!.typeId){
                                            cont++
                                        }
                                    }

                                    qtdtypesDespesas.add(Pair(idsDespesas[i].second, cont))

                                    cont = 0
                                }

                                qtdtypesDespesas.sortBy { it.second }

                                qtdtypesDespesas.reverse()

                                dataSnapshot.children.forEach {
                                    val despesa = it.getValue(Despesa::class.java)

                                    var mesString = formatterMes.format(Date(despesa!!.data))

                                    if (mesString.toInt() == mes) {
                                        acumulaDespesaValor += despesa.valor

                                        despesas.add(despesa)
                                    }
                                }

                                despesas.sortBy { it.valor }

                                tamanho = despesas.size

                                for (i in 0..tamanho - 6) {
                                    despesas.removeAt(i)
                                }

                                despesas.reverse()

                                itens.add(despesas)

                                itens.add(qtdtypesReceitas)
                                itens.add(qtdtypesDespesas)

                                demonstrativoAdapter = DemonstrativoAdapter(baseContext, itens)

                                viewPager.adapter = demonstrativoAdapter

                                demonstrativoAdapter.notifyDataSetChanged()

                                var formattedNumber = formatter.format(acumulaDespesaValor)

                                val despesaAnterior = despesaTextView.text

                                despesaTextView.text = "R$ $formattedNumber"

                            }
                        })
                    }
                })
            }
        })
    }
}
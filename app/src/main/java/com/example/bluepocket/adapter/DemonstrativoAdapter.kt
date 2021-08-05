package com.example.bluepocket.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.example.bluepocket.R
import com.example.bluepocket.entity.Despesa
import com.example.bluepocket.entity.Receita
import com.example.bluepocket.entity.Type

class DemonstrativoAdapter(context: Context, val itens: List<Any>) : PagerAdapter() {

    var context = context

    var receitas: List<Receita>? = null

    var despesas: List<Despesa>? = null

    var tipos: List<Pair<String, Int>>? = null

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view.equals(`object`)
    }

    override fun getCount(): Int {
        return 4
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = LayoutInflater.from(context)
        var view: View = layoutInflater.inflate(R.layout.item_cincomaiores, container, false)

        var titulo: TextView = view.findViewById(R.id.titulo_textView)

        var nome: TextView = view.findViewById(R.id.nome_textView)

        var nome1: TextView = view.findViewById(R.id.nome1_textView)

        var nome2: TextView = view.findViewById(R.id.nome2_textView)

        var nome3: TextView = view.findViewById(R.id.nome3_textView)

        var nome4: TextView = view.findViewById(R.id.nome4_textView)

        var valor: TextView = view.findViewById(R.id.valor_textView)

        var valor1: TextView = view.findViewById(R.id.valor1_textView)

        var valor2: TextView = view.findViewById(R.id.valor2_textView)

        var valor3: TextView = view.findViewById(R.id.valor3_textView)

        var valor4: TextView = view.findViewById(R.id.valor4_textView)

        val listaNomes = mutableListOf<TextView>(nome, nome1, nome2, nome3, nome4)
        val listaValores = mutableListOf<TextView>(valor, valor1, valor2, valor3, valor4)


        when(position){
            0 ->{
                receitas = itens[position] as List<Receita>

                titulo.text = "CInco maiores receitas"

                for(i in 0..receitas!!.size-1){

                    listaNomes[i].text = receitas!![i].nome
                }

                listaNomes.forEach{
                    if(it.text == "Nome"){
                        it.visibility = View.INVISIBLE
                    }
                }

                for(i in 0..receitas!!.size-1){

                    listaValores[i].text = "R$ ${receitas!![i].valor}"
                }

                listaValores.forEach{
                    if(it.text == "Valor"){
                        it.visibility = View.INVISIBLE
                    }
                }
            }

            1 ->{
                despesas = itens[position] as List<Despesa>

                titulo.text = "CInco maiores despesas"

                for(i in 0..despesas!!.size-1){

                    listaNomes[i].text = despesas!![i].nome
                }

                listaNomes.forEach{
                    if(it.text == "Nome"){
                        it.visibility = View.INVISIBLE
                    }
                }

                for(i in 0..despesas!!.size-1){

                    listaValores[i].text = "R$ ${despesas!![i].valor}"
                }

                listaValores.forEach{
                    if(it.text == "Valor"){
                        it.visibility = View.INVISIBLE
                    }
                }
            }

            2 ->{
                tipos = itens[position] as List<Pair<String, Int>>

                view = layoutInflater.inflate(R.layout.item_tres_tipos, container, false)

                var titulo: TextView = view.findViewById(R.id.tituloTres_textView)

                var nome: TextView = view.findViewById(R.id.nomeDoTipo_textView)

                var nome1: TextView = view.findViewById(R.id.nomeDoTipo1_textView)

                var nome2: TextView = view.findViewById(R.id.nomeDoTipo2_textView)

                titulo.text = "Três tipos mais comuns de receitas"

                val listaNomes = mutableListOf<TextView>(nome, nome1, nome2)

                for(i in 0..listaNomes!!.size-1){

                    listaNomes[i].text = tipos!![i].first
                }
            }

            3 ->{
                tipos = itens[position] as List<Pair<String, Int>>

                view = layoutInflater.inflate(R.layout.item_tres_tipos, container, false)

                var titulo: TextView = view.findViewById(R.id.tituloTres_textView)

                var nome: TextView = view.findViewById(R.id.nomeDoTipo_textView)

                var nome1: TextView = view.findViewById(R.id.nomeDoTipo1_textView)

                var nome2: TextView = view.findViewById(R.id.nomeDoTipo2_textView)

                titulo.text = "Três tipos mais comuns de despesas"

                val listaNomes = mutableListOf<TextView>(nome, nome1, nome2)

                for(i in 0..listaNomes!!.size-1){

                    listaNomes[i].text = tipos!![i].first
                }
            }
        }

        container.addView(view, 0)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}
package com.example.bluepocket.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.bluepocket.R
import com.example.bluepocket.entity.Type
import com.example.bluepocket.entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CadastroActivity : AppCompatActivity(), View.OnClickListener, AdapterView.OnItemSelectedListener {

    lateinit var nome:EditText

    lateinit var email:EditText

    lateinit var senha:EditText

    lateinit var confSenha:EditText

    lateinit var botao: Button

    lateinit var mainAuth: FirebaseAuth

    lateinit var mainDatabase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        nome = findViewById(R.id.nome_cadastro)

        email = findViewById(R.id.email_cadastro)

        senha = findViewById(R.id.senha_cadastro)

        confSenha = findViewById(R.id.confirma_senha_cadastro)

        botao = findViewById(R.id.button_cadastro)

        botao.setOnClickListener(this)
    }

    override fun onClick(view: View) {


        if(view.id == R.id.button_cadastro){

            if (nome.text.isEmpty()) {

                nome.error = "Um Nome deve ser digitado."

                return
            }

            if (email.text.isEmpty()) {

                email.error = "Um Email deve ser digitado."

                return
            }

            if (senha.text.isEmpty()) {

                senha.error = "Uma Senha deve ser digitado."

                return
            }

            if (confSenha.text.isEmpty()) {

                confSenha.error = "Você deve confirmar sua senha."

                return
            }

            var name = nome.text.toString()
            var email = email.text.toString()
            var senha = senha.text.toString()

            mainAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener{
                if(it.isSuccessful){
                    //TODO: Criar um objeto do tipo usuário
                    val userID = mainAuth.currentUser!!.uid
                    val user = User(name, email)
                    val userChanges: UserProfileChangeRequest = UserProfileChangeRequest.Builder().setDisplayName(name).build()

                    mainAuth.currentUser!!.updateProfile(userChanges)

                    //TODO: Amazenar objeto usuário no banco de dados
                    val userRef = mainDatabase.getReference("users")
                    userRef.child(userID).setValue(user)

                    var types = listOf<Type>(
                        Type(nome = "Salário", typeOf = "receita", userID = userID),
                        Type(nome = "Aluguel", typeOf = "receita", userID = userID),
                        Type(nome = "Dividendos", typeOf = "receita", userID = userID),
                        Type(nome = "Serviços", typeOf = "receita", userID = userID),
                        Type(nome = "Enérgia elétrica", typeOf = "despesa", userID = userID),
                        Type(nome = "Água e esgoto", typeOf = "despesa", userID = userID),
                        Type(nome = "Refeição", typeOf = "despesa", userID = userID),
                        Type(nome = "Lazer", typeOf = "despesa", userID = userID)
                    )

                    val typeRef: DatabaseReference = mainDatabase.getReference("types")

                    types.forEach({
                        val typeID = typeRef.push().key

                        it.id = typeID!!

                        typeRef.child(typeID).setValue(it)
                    })

                    Toast.makeText(this, "Conta criada com sucesso!", Toast.LENGTH_SHORT).show()

                    val intent = Intent(baseContext, LoginActivity::class.java)

                    startActivity(intent);

                    finish();
                }else{
                    val dialog = AlertDialog.Builder(this@CadastroActivity)
                        .setMessage(it.exception.toString())
                        .setTitle("Blue Pocket")
                        .setCancelable(false)
                        .setNeutralButton("Ok") { dialog, id ->
                            dialog.dismiss()
                        }.create()

                    dialog.show()
                }
            }
        }

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        Toast.makeText(this, "Uma pergunta de segurança deve ser selecionada", Toast.LENGTH_SHORT).show()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }

    override fun onStart() {
        super.onStart()

        mainAuth = FirebaseAuth.getInstance()
        mainDatabase = FirebaseDatabase.getInstance()
    }
}

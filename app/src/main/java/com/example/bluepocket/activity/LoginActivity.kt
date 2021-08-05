package com.example.bluepocket.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.bluepocket.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var email: EditText

    lateinit var senha: EditText

    lateinit var cadastro: TextView

    lateinit var login: Button

    lateinit var recuperarSenha: TextView

    lateinit var mainAuth: FirebaseAuth

    lateinit var mainDatabase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        email = findViewById(R.id.email_login);
        senha = findViewById(R.id.senha_login);
        cadastro = findViewById(R.id.cadastro_login);
        login = findViewById(R.id.button_login)
        recuperarSenha = findViewById(R.id.recuperarSenha_textView)

        cadastro.setOnClickListener(this)

        login.setOnClickListener(this)

        recuperarSenha.setOnClickListener(this)

    }

    override fun onClick(view: View) {

        if (view.id == R.id.cadastro_login) {

            val intent = Intent(this, CadastroActivity::class.java);

            startActivity(intent);

        }

        if (view.id == R.id.button_login) {


            // Verificar se email foi digitado

            if (email.text.isEmpty()) {

                email.error = "Um Email deve ser digitado."

                return
            }

            // Verificar se senha foi digitada

            if (senha.text.isEmpty()) {

                senha.error = "Uma Senha deve ser digitado."

                return
            }

            if (senha.text.isEmpty() && email.text.isEmpty()) {

                email.error = "Um Email deve ser digitado."

                senha.error = "Uma Senha deve ser digitado."

                return

            }

            val email_string = email.text.toString()

            val senha_string = senha.text.toString()

            mainAuth.signInWithEmailAndPassword(email_string, senha_string).addOnCompleteListener {
                if (it.isSuccessful) {
                    val intent = Intent(baseContext, MainActivity::class.java);
                    intent.putExtra("user_id", mainAuth.currentUser!!.uid)

                    startActivity(intent);
                }else {
                    val dialog = AlertDialog.Builder(this@LoginActivity)
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

        else if(view.id == R.id.recuperarSenha_textView){
            val intent = Intent(this, RecuperarSenhaActivity::class.java)

            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()

        mainAuth = FirebaseAuth.getInstance()
        mainDatabase = FirebaseDatabase.getInstance()
    }
}
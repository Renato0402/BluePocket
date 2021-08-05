package com.example.bluepocket.activity

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.bluepocket.R
import com.example.bluepocket.entity.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_recuperar_senha.*

class RecuperarSenhaActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var email: EditText

    lateinit var recuperarButton: Button

    lateinit var mainAuth: FirebaseAuth

    lateinit var mainDatabase: FirebaseDatabase

    lateinit var mainDatabaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recuperar_senha)

        email = findViewById(R.id.email_editText)

        recuperarButton = findViewById(R.id.recuperar_button)

        recuperarButton.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        var userRef = mainDatabase.getReference("users")
        val query = userRef.orderByChild("email").equalTo(email.text.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    dataSnapshot.children.forEach {
                        val user = it.getValue(User::class.java)

                        Log.i("etiu", user.toString())

                        if (v.id == R.id.recuperar_button) {
                            if (user != null) {
                                mainAuth.sendPasswordResetEmail(email.text.toString()).addOnCompleteListener{
                                    if(it.isComplete){
                                        Toast.makeText(baseContext, "Email enviado com sucesso!", Toast.LENGTH_SHORT).show()
                                    }else{
                                        Toast.makeText(baseContext, it.result.toString(), Toast.LENGTH_SHORT).show()
                                    }
                                }

                                val intent = Intent(baseContext, LoginActivity::class.java)

                                startActivity(intent)
                            } else {
                                email.error = "Email não cadastrado!"
                            }
                        }
                    }
                }
            })
    }

    override fun onStart() {
        super.onStart()

        mainAuth = FirebaseAuth.getInstance()
        mainDatabase = FirebaseDatabase.getInstance()
        mainDatabaseReference = FirebaseDatabase.getInstance().reference
    }
}

package com.example.bluepocket.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.example.bluepocket.R
import com.example.bluepocket.fragments.FragmentRegister
import com.example.bluepocket.fragments.FragmentTypeList
import com.example.bluepocket.fragments.FragmentTypeRegister
import com.example.bluepocket.util.IFragmentListener
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import kotlin.contracts.contract

class NavigationActivity : AppCompatActivity(), IFragmentListener {
    lateinit var userNameTextView: TextView

    lateinit var navigatioView: NavigationView

    lateinit var view: View

    lateinit var mainAuth: FirebaseAuth

    var userControle: Int = 0

    var mTypeId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)

        navigatioView = findViewById(R.id.navigation_view)
        view = navigatioView.getHeaderView(0)
        userNameTextView = view.findViewById(R.id.userName_textView)

        mainAuth = FirebaseAuth.getInstance()

        userNameTextView.text = mainAuth.currentUser!!.displayName

        userControle = intent.getIntExtra("controle", 0)

        val fragmentTypeList = FragmentTypeList(mainAuth.currentUser!!.uid, userControle)
        fragmentTypeList.addFragmentListener(this)

        supportFragmentManager.beginTransaction()
            .replace(R.id.navigation_framelayout_container, fragmentTypeList)
            .addToBackStack(null)
            .commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()

        if(supportFragmentManager.backStackEntryCount == 0){
            finish()
        }
    }

    override fun onFragmentClick(view: View) {

        when (view.id) {
            R.id.fragment_type_floatingButton_add -> {

                val fragmentTypeRegister = FragmentTypeRegister(mainAuth.currentUser!!.uid, userControle)

                supportFragmentManager.beginTransaction()
                    .replace(R.id.navigation_framelayout_container, fragmentTypeRegister)
                    .addToBackStack(null)
                    .commit()
            }

            R.id.fragment_floatingButton_add ->{
                val fragmentRegister = FragmentRegister(mTypeId, userControle)

                supportFragmentManager.beginTransaction()
                    .replace(R.id.navigation_framelayout_container, fragmentRegister)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    fun setmTypeId(id: String){
        mTypeId = id
    }
}

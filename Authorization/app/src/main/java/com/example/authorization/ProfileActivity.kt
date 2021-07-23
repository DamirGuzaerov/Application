package com.example.authorization

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.authorization.databinding.ProfileWindowBinding

class ProfileActivity: AppCompatActivity() {
    private lateinit var bindingClass: ProfileWindowBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ProfileWindowBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)

        bindingClass.nameSecondName.text =  intent?.extras?.getString("EXTRA_NAME")
        bindingClass.email.text = intent?.extras?.getString("EXTRA_EMAIL")
        bindingClass.avatar.setImageResource(intent.extras?.getInt("EXTRA_IMAGE")!!)
        initListeners()
    }

    private fun initListeners() {
       bindingClass.btnExit.setOnClickListener{onClickExit(it)}
    }

    private fun onClickExit(view: View){
        val myPrefs = getSharedPreferences("myPrefs", MODE_PRIVATE)
        val prefsEditor = myPrefs.edit()
        prefsEditor.putBoolean("REMEMBER",false)
        prefsEditor.apply()
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onStop() {
        Log.d("aaaa","aaaaaaaaaaaaaaa")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d("bbbbb","bbbbbbbbbbbbbb")
        super.onDestroy()
    }
}
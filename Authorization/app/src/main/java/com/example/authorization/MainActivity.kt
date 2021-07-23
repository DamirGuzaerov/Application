package com.example.authorization

import android.content.Intent
import android.content.SharedPreferences
import android.opengl.ETC1.isValid
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.authorization.databinding.ActivityMainBinding
import com.example.authorization.PasswordValidator


class MainActivity : AppCompatActivity() {
    private val users = hashMapOf<String,User>(
        "guzaerov.damir49@gmail.com" to User("Guzaerov Damir","Qwerty_555",R.drawable.guz),
        "bill_gates@mcrsft.ru" to User("Bill Gates", "Bill_1955",R.drawable.bill),
        "elon_musk@space.com" to User("Elon Musk","Tesla2021",R.drawable.elon),
    )
    private lateinit var bindingClass: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        bindingClass = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)
        initListeners()
        checkAuto()
    }
    private fun checkAuto(){
        val myPrefs = this.getSharedPreferences("myPrefs", MODE_PRIVATE)
        val isRemember = myPrefs.getBoolean("REMEMBER",false)
        if(!isRemember) return
        else {
            val intent = Intent(this, ProfileActivity::class.java)
            sendInfo(intent,myPrefs)
            startActivity(intent)
            finish()
        }
    }

    private fun sendInfo(intent: Intent, myPrefs: SharedPreferences) {
        intent.putExtra("EXTRA_IMAGE",users[myPrefs.getString("EMAIL",null)]?.avatar)
        intent.putExtra("EXTRA_NAME",users[myPrefs.getString("EMAIL",null)]?.name)
        intent.putExtra("EXTRA_EMAIL",myPrefs.getString("EMAIL",null))
    }

    private fun initListeners(){
        bindingClass.btnLogin.setOnClickListener{onClickGoProfile(it)}
        bindingClass.emailField.addTextChangedListener{
            bindingClass.tiEmail.isErrorEnabled = false
            bindingClass.tiEmail.error = null
        }
        bindingClass.passwordField.addTextChangedListener{
            bindingClass.tiPass.isErrorEnabled = false
            bindingClass.tiPass.error = null
        }
    }

    fun onClickGoProfile(view: View){
        val passwordValidator = PasswordValidator()
        val email = bindingClass.emailField.text.toString()
        val password = bindingClass.passwordField.text.toString()
        val passFromBase: String? = users[email]?.password

        val myPrefs = this.getSharedPreferences("myPrefs", MODE_PRIVATE)
        val prefsEditor = myPrefs.edit()
        val emailIsValid = isValidEmail(email)
        val passIsValid = passwordValidator.isValid(password)

        if (!emailIsValid||!passIsValid) {
            if (!emailIsValid)
                bindingClass.tiEmail.error = getString(R.string.emailError)
            if(!passIsValid)
                bindingClass.tiPass.error = getString(R.string.passwordError)
            return
        }

        when {
            passFromBase == null -> {
                showMessage(R.string.user_not_found)
            }
            password == passFromBase -> {
                showMessage(R.string.auth_completed)
                val intent = Intent(this, ProfileActivity::class.java)
                sendSettings(prefsEditor)
                sendInfo(intent,myPrefs)
                startActivity(intent)
                finish()
            }
            else -> {
                showMessage(R.string.pass_wrong)
            }
        }
    }

    private fun sendSettings(prefsEditor: SharedPreferences.Editor) {
        val email = bindingClass.emailField.text.toString()
        prefsEditor.putBoolean("REMEMBER",true)
        prefsEditor.putString("EMAIL",email)
        prefsEditor.apply()
    }

    private fun isValidEmail(target: CharSequence): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    private fun showMessage(message: Int){
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT)
            .show()
    }
}
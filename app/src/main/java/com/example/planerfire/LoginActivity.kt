package com.example.planerfire

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = Firebase.auth
    }


    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null){
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }
    }

    fun onClickSignUp(view : View){
        val loginUser = edLogin.text.toString()
        val passwordUser = edPassword.text.toString()
        if(loginUser != "" && passwordUser != ""){
            auth.createUserWithEmailAndPassword(loginUser,passwordUser).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this,"Регистрация прошла успешно", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this,"Ошибка регистрации", Toast.LENGTH_LONG).show()
                }
            }
        } else {
            Toast.makeText(this,"Введите логин и пароль", Toast.LENGTH_LONG).show()
        }
    }

    fun onClickSignIn(view: View) {
            if (!TextUtils.isEmpty(edLogin.text.toString()) && !TextUtils.isEmpty(edPassword.text.toString())) {
                auth.signInWithEmailAndPassword(edLogin.text.toString(), edPassword.text.toString())
                    .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                val i = Intent(this, MainActivity::class.java)
                                startActivity(i)
                            } else {
                                Toast.makeText(
                                    this,
                                    "Ошибка, проверьте правильность логина и пароля",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
            }
    }

    fun onClickSignOut(view: View){
        FirebaseAuth.getInstance().signOut()
    }

}
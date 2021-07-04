package com.example.neeksiknefazla.loginsignupactivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.example.neeksiknefazla.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_signup_client.*

class SignUpActivityClient : AppCompatActivity() {

    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val db : FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_client)

        textEmailClient.doOnTextChanged { text, start, before, count ->
            EmailClient.error = null
        }

        passwordTextClient.doOnTextChanged { text, start, before, count ->
            PasswordClient.error = null
        }

        buttonClientGo2.setOnClickListener {
            validate()
        }
    }

    private fun validate(){

//        if (validateEmail().not()) return
        if (validatePassword().not()) return

        auth.createUserWithEmailAndPassword(textEmailClient.text.toString(), passwordTextClient.text.toString()).addOnCompleteListener { task ->

            if (task.isSuccessful) {
                val kayitbilgileri = hashMapOf(
                    "email" to textEmailClient.text.toString(),
                    "password" to passwordTextClient.text.toString(),
                    "name" to nameClient.text.toString(),
                    "surname" to surnameClient.text.toString(),
                    "birthday" to birthdayClient.text.toString(),
                    "logintype" to "client"
                )

                db.collection("kisiler").document(textEmailClient.text.toString())
                    .set(kayitbilgileri)
                    .addOnSuccessListener { Log.d("başarılı", "DocumentSnapshot successfully written!") }
                    .addOnFailureListener { e -> Log.w("hatalı", "Error writing document", e) }

                Toast.makeText(applicationContext, "Client is successfully created!", Toast.LENGTH_LONG).show()
                val intent = Intent(this@SignUpActivityClient, LoginActivity::class.java)
                startActivity(intent)
            }   else {
                Toast.makeText(applicationContext, "Client is not created!", Toast.LENGTH_LONG).show()
            }

        }.addOnFailureListener { exception ->
            Toast.makeText(applicationContext, exception.localizedMessage.toString(), Toast.LENGTH_LONG).show()
            Toast.makeText(applicationContext, "Client is not created!", Toast.LENGTH_LONG).show()
        }
    }

 /*   private fun validateEmail():Boolean{

        val email = textEmailClient.text.toString()

        if (email.isEmpty()){
            EmailClient.error = getString(R.string.email_empty_error)
            return false
        }

        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            EmailClient.error = getString(R.string.email_invalid_error)
            return false
        }
        EmailClient.error = null

        return true
    }*/

    private fun validatePassword():Boolean{
        val password = passwordTextClient.text.toString()

        if (password.length < 8){
            PasswordClient.error = getString(R.string.password_length_error)
            return false
        }
        PasswordClient.error = null

        return true
    }

    private fun toast(text: String){
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show()
    }
}
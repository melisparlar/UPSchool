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
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_signup_enterprise.*

class SignUpActivityEnterprise : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_enterprise)


        emailEntreprise.doOnTextChanged { text, start, before, count ->
            EmailEntreprise.error = null
        }

        passwordTextEntreprise.doOnTextChanged { text, start, before, count ->
            PasswordEntreprise.error = null
        }

        buttonEnterpriseGo.setOnClickListener {
            validate()
        }
    }

    private fun validate(){

        //if (validateEmail().not()) return
        if (validatePassword().not()) return

        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        val db : FirebaseFirestore = FirebaseFirestore.getInstance()

        auth.createUserWithEmailAndPassword(emailEntreprise.text.toString(), passwordTextEntreprise.text.toString()).addOnCompleteListener { task ->

            if (task.isSuccessful) {
                val city = hashMapOf(
                    "email" to emailEntreprise.text.toString(),
                    "password" to passwordTextEntreprise.text.toString(),
                    "companysname" to nameEntreprise.text.toString(),
                    "companylocation" to surnameEntreprise.text.toString(),
                    "companyabout" to resumeEntreprise.text.toString(),
                    "logintype" to "enterprise"
                )

                db.collection("kisiler").document(emailEntreprise.text.toString())
                    .set(city)
                    .addOnSuccessListener { Log.d("başarılı", "DocumentSnapshot successfully written!") }
                    .addOnFailureListener { e -> Log.w("hatalı", "Error writing document", e) }

                Toast.makeText(applicationContext, "Enterprise is successfully created!", Toast.LENGTH_LONG).show()
                val intent = Intent(this@SignUpActivityEnterprise, LoginActivity::class.java)
                startActivity(intent)
            }   else {
                Toast.makeText(applicationContext, "Enterprise is not created!", Toast.LENGTH_LONG).show()
            }

        }.addOnFailureListener { exception ->
            Toast.makeText(applicationContext, exception.localizedMessage.toString(), Toast.LENGTH_LONG).show()
            Toast.makeText(applicationContext, "Enterprise is not created!", Toast.LENGTH_LONG).show()
        }
    }

/*    private fun validateEmail():Boolean{

        val email = emailEntreprise.text.toString()

        if (email.isEmpty()){
            EmailEntreprise.error = getString(R.string.email_empty_error)
            return false
        }

        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            EmailEntreprise.error = getString(R.string.email_invalid_error)
            return false
        }
        EmailEntreprise.error = null

        return true
    }*/

    private fun validatePassword():Boolean{
        val password = passwordTextEntreprise.text.toString()

        if (password.length < 8){
            PasswordEntreprise.error = getString(R.string.password_length_error)
            return false
        }

        PasswordEntreprise.error = null

        return true
    }

    private fun toast(text: String){
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show()
    }

}
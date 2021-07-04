package com.example.neeksiknefazla.loginsignupactivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.example.neeksiknefazla.AddProductActivity
import com.example.neeksiknefazla.ClientHomePageActivity
import com.example.neeksiknefazla.EntrepriseHomePageActivity
import com.example.neeksiknefazla.R
import com.example.neeksiknefazla.clientfragments.DiscoverFragment
import com.example.neeksiknefazla.clientfragments.ProfileFragment
import com.example.neeksiknefazla.enterprisefragments.EnterpriseProfileFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_signup_client.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


/*        textEmailLogin.doOnTextChanged { text, start, before, count ->
            Email.error = null
        }

        passwordText.doOnTextChanged { text, start, before, count ->
            Password.error = null
        }*/

        buttonClientGo1.setOnClickListener {

            val auth : FirebaseAuth = FirebaseAuth.getInstance()

            auth.signInWithEmailAndPassword(textEmailLogin.text.toString(), passwordText.text.toString()).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "Welcome: ${auth.currentUser?.email.toString()}", Toast.LENGTH_LONG).show()

                    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

                    val docRef = db.collection("kisiler").document(textEmailLogin.text.toString())
                    docRef.get()
                        .addOnSuccessListener { document ->
                            if (document != null) {

                                if (document.data.toString().contains("client")) {
                                    val intent = Intent(this@LoginActivity, ClientHomePageActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }   else if (document.data.toString().contains("enterprise")) {
                                    val intent = Intent(this@LoginActivity, EntrepriseHomePageActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }

                            } else {
                                Log.d("TAG", "No such document")
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.d("TAG", "get failed with ", exception)
                        }


//                    val intent = Intent(applicationContext, FeedActivity::class.java)
//                    startActivity(intent)
//                    finish()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(applicationContext, exception.localizedMessage.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

 /*   private fun validate(){

        if (validateEmail().not()) return
        if (validatePassword().not()) return

        toast(getString(R.string.login_succesful))
    }

    private fun validateEmail():Boolean{
        val email = textEmailLogin.text.toString()

        if (email.isEmpty()){
            Email.error = getString(R.string.email_empty_error)
            return false
        }

        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Email.error = getString(R.string.email_invalid_error)
            return false
        }
        Email.error = null

        return true
    }

    private fun validatePassword():Boolean{
        val password = passwordText.text.toString()

        if (password.length < 8){
            Password.error = getString(R.string.password_length_error)
            return false
        }

        Password.error = null

        return true
    }

    private fun toast(text: String){
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show()
    }*/
}
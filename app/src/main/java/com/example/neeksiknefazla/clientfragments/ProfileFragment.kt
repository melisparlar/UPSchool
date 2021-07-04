package com.example.neeksiknefazla.clientfragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.neeksiknefazla.MainActivity
import com.example.neeksiknefazla.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_enterprise_profile.*
import kotlinx.android.synthetic.main.fragment_profile.*
import java.util.ArrayList

class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db : FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        getProfileData()

        buttonLogOut.setOnClickListener {
            auth.signOut()
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        }

    }

    fun getProfileData() {
        val user = Firebase.auth.currentUser
        if(user != null) {

            val email = user.email

            val docRef = db.collection("kisiler").document(email.toString())
            docRef.get().addOnSuccessListener { document ->
                if (document != null) {
                    textViewClientName.setText(document.getString("name").toString())
                    textViewClientSurname.setText(document.getString("surname").toString())
                    textViewBirthday.setText(document.getString("email").toString())
                    textViewEmail.setText(document.getString("birthday").toString())
                }
            }
        }
        else{
            Log.d("başarılı", "Snapshot successfully written!")
        }
    }
}
package com.example.neeksiknefazla.enterprisefragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.neeksiknefazla.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_enterprise_profile.*
import kotlinx.android.synthetic.main.fragment_products.*
import kotlin.concurrent.timerTask

class EnterpriseProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db : FirebaseFirestore
    private var email:String = ""

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_enterprise_profile, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        getProfileData()

        buttonLogoutCompany.setOnClickListener {
            auth.signOut()
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
        }

        imageButtonLocation.setOnClickListener {
            val intent = Intent(activity, MapsActivity::class.java)
            startActivity(intent)
        }
    }

    fun getProfileData() {
        val user = Firebase.auth.currentUser
        if(user != null) {

            email = user.email.toString()

            val docRef = db.collection("kisiler").document(email.toString())
            docRef.get().addOnSuccessListener { document ->
                if (document != null) {
                    textView20.setText(document.getString("email").toString())
                    textView15.setText(document.getString("companysname").toString())
                    textView17.setText(document.getString("companylocation").toString())
                    textView18.setText(document.getString("companyabout").toString())
                }
            }
        }
        else{
            Log.d("başarılı", "Snapshot successfully written!")
        }
    }
}
package com.example.neeksiknefazla.enterprisefragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.neeksiknefazla.R
import com.example.neeksiknefazla.dataclass.Order
import com.example.neeksiknefazla.dataclass.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_enterprise_profile.*
import kotlinx.android.synthetic.main.fragment_notifications.*
import kotlinx.android.synthetic.main.fragment_reports.*
import java.util.ArrayList

class ReportsFragment : Fragment () {

    private lateinit var firestoreDb: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private var companyName : String = ""

    private var totalNoDon : Int = 0
    private var totalNoSold : Int = 0
    private var totalAmountDon : Int = 0
    private var totalAmountSold : Int = 0


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reports, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        firestoreDb = FirebaseFirestore.getInstance()

        getData()
        Log.e("TAG", "Snapshot getDAta successfully written!")

    }
    fun getData() {

        var price : String = ""
        var number : String = ""
        var totalAmount : Int = 0

        val user = Firebase.auth.currentUser
        if(user != null) {

            val docRef = firestoreDb.collection("kisiler").document(user.email.toString())
            docRef.get().addOnSuccessListener { document ->
                if (document != null) {
                    companyName = document.getString("companysname").toString()
                    Log.d("TAG", "Snapshot successfully written! $companyName")
                    textView28.setText((companyName))
                }
            }

            firestoreDb = FirebaseFirestore.getInstance()
            val postsRef = firestoreDb.collection("siparisler")
            postsRef.addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.e("TAG", "Exception while query!", exception)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    for (document in snapshot.documents) {
                        var pid = document.id
                        var cmpName = document.getString("company_name").toString()
                        Log.e("TAG", "Price : $cmpName!")

                        number = document.getString("number").toString()
                        price = document.getString("price").toString()
                          //  totalAmount += number*price
                        Log.e("TAG", "Price : $number!")
                        Log.e("TAG", "Price : $price!")
                        totalAmount += (number.toInt())*(price.toInt())
                        Log.e("TAG", "Document : $totalAmount!")
                      /*  if( cmpName.toString() == companyName.toString()) {
                            totalAmount += (number.toInt())*(price.toInt())
                            Log.e("TAG", "Document : $totalAmount!")
                        }*/

                    }
                    textView26.setText(totalAmount.toString())
                }
            }
        }
        else{
            Log.d("başarılı", "Snapshot successfully written!")
        }

    }

}
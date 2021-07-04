package com.example.neeksiknefazla

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.neeksiknefazla.dataclass.Order
import com.example.neeksiknefazla.dataclass.Sepet
import com.example.neeksiknefazla.loginsignupactivities.SignUpActivityEnterprise
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_payment.*
import kotlinx.android.synthetic.main.activity_signup.*
import kotlinx.android.synthetic.main.fragment_notifications.*

class PaymentActivity : AppCompatActivity() {

    private lateinit var firestoreDb: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    val user = Firebase.auth.currentUser
    val email = user?.email.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        buttonPay.setOnClickListener {
            Toast.makeText(applicationContext, "Payment successful!", Toast.LENGTH_LONG).show()
            val intent = Intent(this@PaymentActivity, ClientHomePageActivity::class.java)
            startActivity(intent)
        }
    }

    /*   fun paymentValidation(email : String) {

    //    val user = Firebase.auth.currentUser
    //    val email = user?.email.toString()
        //  firestoreDb = FirebaseFirestore.getInstance()
        val firestoreDb: FirebaseFirestore = FirebaseFirestore.getInstance()

        val docRef = firestoreDb.collection("sepet").document(data.productId)
        firestoreDb.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)
            val onDon = snapshot.get("no_on_don")!!.toString().toInt()
            val onSale = snapshot.get("no_on_sale")!!.toString().toInt()
            if (onSale > 0) {
                val Don = onDon.inc().toString()
                transaction.update(docRef,"no_on_don", Don)
                val Sale = onSale.dec().toString()
                transaction.update(docRef,"no_on_sale", Sale)
            }
        }.addOnSuccessListener { Log.d("TAG", "Transaction success!") }
                .addOnFailureListener { e -> Log.w("TAG", "Transaction failure.", e) }


        /*val sepetRef = firestoreDb.collection("sepet")
        sepetRef.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.e("TAG", "Exception while query!", exception)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                for (document in snapshot.documents) {


                    var pid = document.id
                    var clientEmail = document.get("client_email").toString()
                    var totalAmount = document.get("total_amount").toString()
                    var isPayed = document.get("isPayed")
                    var orderPost = Sepet(
                            pid,
                            clientEmail,
                            totalAmount,
                            isPayed as Boolean
                    )
                    if (email == clientEmail) {
                        ordersList.add(orderPost)
                        var totalNumber = Integer.parseInt(number)
                        var totalPrice = Integer.parseInt(price)
                        totalAmount += totalNumber*totalPrice
                        Log.e("TAG", "Document : ${document.id}: ${document.data}!" + orderPost)
                    }
                }
            }
        }

    }*/


  */
}
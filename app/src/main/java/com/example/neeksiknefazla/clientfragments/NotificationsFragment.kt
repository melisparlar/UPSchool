package com.example.neeksiknefazla.clientfragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.neeksiknefazla.PaymentActivity
import com.example.neeksiknefazla.R
import com.example.neeksiknefazla.adapter.SepetAdapter
import com.example.neeksiknefazla.dataclass.Order
import com.example.neeksiknefazla.dataclass.Sepet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_notifications.*
import java.util.ArrayList

class NotificationsFragment : Fragment() {

    private lateinit var firestoreDb: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private var ordersList: ArrayList<Order> = ArrayList()
    var totalAmount : Int = 0
    private var totalNoDon : Int = 0
    private var totalNoSold : Int = 0
    private var totalAmountDon : Int = 0
    private var totalAmountSold : Int = 0

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonPayment.setOnClickListener {
            if (totalAmount.toString() != "0") {
                val intent = Intent(activity, PaymentActivity::class.java)
                startActivity(intent)
                shoppingCart();
            } else if (totalAmount.toString() == "0") {
                Toast.makeText(activity, "Shopping cart is empty!",  Toast.LENGTH_LONG).show()
            }
        }

        ordersList = arrayListOf<Order>()
        showOrders(ordersList)

        val sepetAdapter = SepetAdapter(ordersList)
        rv_orders.layoutManager = LinearLayoutManager(
                activity, LinearLayoutManager.VERTICAL, false)
        rv_orders.adapter = sepetAdapter
        sepetAdapter.notifyDataSetChanged()
    }

    fun showOrders(ordersList: ArrayList<Order>) {

        val user = Firebase.auth.currentUser
        val email = user?.email.toString()

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
                    var clientEmail = document.get("client_email").toString()
                    var productName = document.get("product_name").toString()
                    var companyName = document.get("company_name").toString()
                    var number = document.get("number").toString()
                    var price = document.get("price").toString()
                    var type = "order".toString()
                    var orderPost = Order(
                            pid,
                            clientEmail,
                            productName,
                            companyName,
                            number,
                            price,
                            type
                    )
                    if (email == clientEmail) {
                        ordersList.add(orderPost)
                        var totalNumber = number.toInt()
                        var totalPrice = price.toInt()
                        totalAmount += totalNumber*totalPrice
                        totalNoSold++
                        totalAmountSold += totalNumber*totalPrice
                        Log.e("TAG", "Document : ${document.id}: ${document.data}!" + orderPost)
                    }
                }
            }
        }
        val postsRef2 = firestoreDb.collection("bagislar")
        postsRef2.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.e("TAG", "Exception while query!", exception)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                for (document in snapshot.documents) {
                    var pid = document.id
                    var clientEmail = document.get("client_email").toString()
                    var productName = document.get("product_name").toString()
                    var companyName = document.get("company_name").toString()
                    var number = document.get("number").toString()
                    var price = document.get("price").toString()
                    var type = "donation".toString()
                    var orderPost = Order(
                            pid,
                            clientEmail,
                            productName,
                            companyName,
                            number,
                            price,
                            type
                    )
                    if (email == clientEmail) {
                        ordersList.add(orderPost)
                        var totalNumber = number.toInt()
                        var totalPrice =  price.toInt()
                        totalAmount += totalNumber*totalPrice
                        totalNoDon++
                        totalAmountDon += totalNumber*totalPrice
                        Log.e("TAG", "Document : ${document.id}: ${document.data}!" + orderPost + totalAmount)
                    }
                    Log.e("TAG", "Document : ${document.id}: ${document.data}!" + orderPost + totalAmount)
                }
            }
            textView47.text = totalAmount.toString() + " TL"
        }
    }

    fun shoppingCart () {

        val firestoreDb: FirebaseFirestore = FirebaseFirestore.getInstance()
        val user = Firebase.auth.currentUser
        val email = user?.email.toString()

        val sepetInfo = hashMapOf(
                "total_amount" to totalAmount,
                "client_email" to email,
                "isPayed" to false,
                "totalNoDon" to totalNoDon,
                "totalNoSold" to totalNoSold,
                "totalAmountDon" to totalAmountDon,
                "totalAmountSold" to totalAmountSold
        )
        firestoreDb.collection("sepet")
                .add(sepetInfo)
                .addOnSuccessListener { Log.d("başarılı", "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.w("hatalı", "Error writing document", e) }
        Toast.makeText(activity, "Package is successfully added!", Toast.LENGTH_LONG).show()
    }
}

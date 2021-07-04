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
import coil.load
import com.example.neeksiknefazla.*
import com.example.neeksiknefazla.adapter.UrunAdapter
import com.example.neeksiknefazla.dataclass.Product
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_add_product.*
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.client_product_tasarim.*
import kotlinx.android.synthetic.main.fragment_discover.*
import java.util.ArrayList

class DiscoverFragment : Fragment(), UrunAdapter.IcerikClickListener {

    lateinit var invendusList : ArrayList<Product>
    lateinit var yemekResimList : ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       return inflater.inflate(R.layout.fragment_discover, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        invendusList = arrayListOf<Product>()
        yemekResimList = arrayListOf<String>()

        VeriCekveYazdir(invendusList, yemekResimList)

        val urunAdapter = UrunAdapter(invendusList, yemekResimList, this)

        urunRecyclerView.layoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )

        urunRecyclerView.adapter = urunAdapter
        urunAdapter.notifyDataSetChanged()

    }

    fun VeriCekveYazdir(invendusList: ArrayList<Product>, yemekResimList: ArrayList<String>) {

        val firestoreDb: FirebaseFirestore = FirebaseFirestore.getInstance()
        firestoreDb.collection("urunler").addSnapshotListener { snapshot, error ->
            if (error != null){
                Toast.makeText(activity, error.localizedMessage.toString(), Toast.LENGTH_SHORT).show()
            }
            else {
                if (snapshot != null) {
                    if (!snapshot.isEmpty){
                        val veriler = snapshot.documents
                        invendusList.clear()
                        yemekResimList.clear()
                        for (document in veriler){
                            var pid = document.id
                            var companyName = document.get("company_name").toString()
                            var noDonation = document.get("no_on_don").toString()
                            var noOnSale = document.get("no_on_sale").toString()
                            var price = document.get("price").toString()
                            var productName = document.get("product_name").toString()
                            var productImage = document.get("urunresim").toString()
                            var companyEmail =document.get("company_email").toString()
                            var productPost = Product(
                                    pid,
                                    companyName,
                                    noDonation,
                                    noOnSale,
                                    price,
                                    productName,
                                    companyEmail,
                                    productImage
                            )
                            // Eger paket sayisi 0 ise kullanici goruntulemesin
                            if (noOnSale.toInt() > 0 || noDonation.toInt() > 0) {
                                invendusList.add(productPost)
                                yemekResimList.add(productImage)
                            }
                            Log.e("TAG", "Document : ${document.id}: ${document.data}!" + productPost + invendusList )
                        }
                    }
                }
            }
        }
    }

    override fun onIcerikClickListener(data: Product) {

        urunIcerikFrameLayout.visibility = View.VISIBLE

        val db: FirebaseFirestore = FirebaseFirestore.getInstance()
        var companyEmail:String = ""

        var docRef = db.collection("urunler").document(data.productId)
        docRef.get().addOnSuccessListener { document ->
            if (document != null) {
                urunGorselImageView.load(document.get("urunresim").toString())
                    icerik1TextView.text = document.get("company_name").toString()
                    icerik2TextView.text = document.get("no_on_don").toString() + " package Askıda"
                    icerik3TextView.text = document.get("no_on_sale").toString() + " package on sale"
                    icerik4TextView.text = document.get("price").toString() + " TL"
                    companyEmail = document.get("company_email").toString()
                    Log.d("TAG", "No such document $companyEmail")
            } else {
                Log.d("TAG", "No such document")
            }
        }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
        }


        backButton.setOnClickListener {
            urunIcerikFrameLayout.visibility = View.INVISIBLE
        }
        buyButton.setOnClickListener {
            makeOrder(data)
            decrementProductOnSale(data)
        }
        donateButton.setOnClickListener {
            makeDonation(data)
            incrementDonation(data)
        }
        imageButtonSeeLocation.setOnClickListener {
            val intent = Intent(activity, MapsActivityClient::class.java)
            intent.putExtra("profileName", companyEmail)
            startActivity(intent)
        }

    }

    fun makeOrder(data : Product) {

        val firestoreDb: FirebaseFirestore = FirebaseFirestore.getInstance()
        val user = Firebase.auth.currentUser
        val email = user?.email.toString()

        val orderInfo = hashMapOf(
                "product_name" to data.product_name,
                "price" to data.price,
                "number" to "1",
                "company_name" to data.company_name,
                "client_email" to email
        )
        firestoreDb.collection("siparisler")
                .add(orderInfo)
                .addOnSuccessListener { Log.d("başarılı", "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.w("hatalı", "Error writing document", e) }
    }

    fun makeDonation(data : Product) {

        val firestoreDb: FirebaseFirestore = FirebaseFirestore.getInstance()
        val user = Firebase.auth.currentUser
        val email = user?.email.toString()

        val donInfo = hashMapOf(
                "product_name" to data.product_name,
                "price" to data.price,
                "number" to "1",
                "company_name" to data.company_name,
                "client_email" to email
        )
        firestoreDb.collection("bagislar")
                .add(donInfo)
                .addOnSuccessListener { Log.d("başarılı", "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.w("hatalı", "Error writing document", e) }
    }

    fun decrementProductOnSale (data: Product) {

        val firestoreDb: FirebaseFirestore = FirebaseFirestore.getInstance()

        val docRef = firestoreDb.collection("urunler").document(data.productId)
        firestoreDb.runTransaction { transaction ->
            val snapshot = transaction.get(docRef)
            val onSale = snapshot.get("no_on_sale")!!.toString().toInt()
            if (onSale > 0) {
                val Sale = onSale.dec().toString()
                transaction.update(docRef,"no_on_sale", Sale)
            }
        }.addOnSuccessListener { Log.d("TAG", "Transaction success!") }
                .addOnFailureListener { e -> Log.w("TAG", "Transaction failure.", e) }
    }

    fun incrementDonation (data: Product) {

        val firestoreDb: FirebaseFirestore = FirebaseFirestore.getInstance()

        val docRef = firestoreDb.collection("urunler").document(data.productId)
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
    }
}
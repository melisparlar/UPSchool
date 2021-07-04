package com.example.neeksiknefazla.enterprisefragments

import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.neeksiknefazla.AddProductActivity
import com.example.neeksiknefazla.R
import com.example.neeksiknefazla.adapter.AddProductAdapter
import com.example.neeksiknefazla.dataclass.Product

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_products.*
import java.util.ArrayList
import android.content.Context
import coil.load
import com.example.neeksiknefazla.PaymentActivity
import com.example.neeksiknefazla.adapter.UrunAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_discover.*
import kotlinx.android.synthetic.main.fragment_enterprise_profile.*
import kotlinx.android.synthetic.main.fragment_notifications.*
import java.lang.Exception

class AddProductFragment  : Fragment () {

    private lateinit var firestoreDb: FirebaseFirestore
    private lateinit var auth : FirebaseAuth
    private var posts: ArrayList<Product> = ArrayList()
    private var productImageList: List<String> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_products, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonAddProduct.setOnClickListener {
            val intent = Intent(activity, AddProductActivity::class.java)
            startActivity(intent)
        }

        val user = Firebase.auth.currentUser
        val email = user?.email.toString()

        firestoreDb = FirebaseFirestore.getInstance()
        val postsRef = firestoreDb.collection("urunler")
        postsRef.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                Log.e("TAG", "Exception while query!", exception)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                for (document in snapshot.documents) {
                    var pid = document.id
                    var companyName = document.get("company_name").toString()
                    var noDonation = document.get("no_on_don").toString()
                    var noOnSale = document.get("no_on_sale").toString()
                    var price = document.get("price").toString()
                    var productName = document.get("product_name").toString()
                    var companyEmail = document.get("company_email").toString()
                    var productImage = document.get("urunresim").toString()
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
                    if (email == companyEmail) {
                        posts.add(productPost)
                        Log.e("TAG", "Document : ${document.id}: ${document.data}!" + productPost)
                    }

                }
            }
        }

        // Init recyclerview
        val adapterent = AddProductAdapter(posts)
        rv_products.layoutManager = LinearLayoutManager(
            activity, LinearLayoutManager.VERTICAL,false)
        rv_products.adapter = adapterent

    }
}

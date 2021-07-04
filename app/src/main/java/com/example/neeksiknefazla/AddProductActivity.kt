package com.example.neeksiknefazla

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_add_product.*
import kotlinx.android.synthetic.main.activity_signup_client.*
import kotlinx.android.synthetic.main.fragment_enterprise_profile.*
import java.lang.Exception
import java.util.*

class AddProductActivity : AppCompatActivity() {

    var secilenResim : Uri? = null
    private lateinit var db : FirebaseFirestore
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        urunResimImageView.setOnClickListener {
            ResimEkle()
        }

        ekleButton.setOnClickListener {
            UrunEkle()
            finish()
        }
    }

    private fun ResimEkle() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)
        } else {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent,2)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent,2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {

            secilenResim = data.data
            try {
                if (secilenResim != null) {

                    if (Build.VERSION.SDK_INT >= 28) {

                        val source = ImageDecoder.createSource(contentResolver, secilenResim!!)
                        val bitmap = ImageDecoder.decodeBitmap(source)
                        urunResimImageView.setImageBitmap(bitmap)
                    } else {
                        val bitmap =
                                MediaStore.Images.Media.getBitmap(this.contentResolver, secilenResim)
                        urunResimImageView.setImageBitmap(bitmap)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)    }

    fun UrunEkle() {

        val uuid = UUID.randomUUID()
        val imageName = "$uuid.jpg"

        val storage = FirebaseStorage.getInstance()
        val reference = storage.reference
        val imagesReference = reference.child("images").child(imageName)

        val user = Firebase.auth.currentUser
        val email = user?.email.toString()
        var companyName : String? = null

        val docRef = db.collection("kisiler").document(email.toString())
        docRef.get().addOnSuccessListener { document ->
            if (document != null) {
                companyName = document.get("companysname").toString()
                Log.e("TAG", "Document : ${document.id}: ${document.data}!" + companyName)
            }
        }

        if (secilenResim != null) {
            imagesReference.putFile(secilenResim!!).addOnSuccessListener { taskSnapshot ->

                val uploadedPictureReference = FirebaseStorage.getInstance().reference.child("images").child(imageName)
                uploadedPictureReference.downloadUrl.addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()
                    println(downloadUrl)

                    val kayitbilgileri = hashMapOf(
                            "urunresim" to downloadUrl,
                            "product_name" to urunIcerik1Text.text.toString(),
                            "price" to urunIcerik2Text.text.toString(),
                            "no_on_sale" to urunIcerik3Text.text.toString(),
                            "no_on_don" to urunIcerik4Text.text.toString(),
                            "company_name" to companyName,
                            "company_email" to email
                    )
                    db.collection("urunler")
                            .add(kayitbilgileri)
                            .addOnSuccessListener { Log.d("başarılı", "DocumentSnapshot successfully written!") }
                            .addOnFailureListener { e -> Log.w("hatalı", "Error writing document", e) }
                    Toast.makeText(applicationContext, "Product is successfully added!", Toast.LENGTH_LONG).show()
                }
            }
        } else {
            Toast.makeText(applicationContext, "Please select an image!", Toast.LENGTH_LONG).show()
        }
    }

}
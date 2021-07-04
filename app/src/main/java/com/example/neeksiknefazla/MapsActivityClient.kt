package com.example.neeksiknefazla

import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import coil.load

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.fragment_discover.*

class MapsActivityClient : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    var enlem:Double = 41.102715
    var boylam:Double = 28.982018333333333
    private var companyName:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps_client)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        val intent = getIntent()
        companyName = intent.getStringExtra("profileName").toString()
        Log.e("TAG", "Document : $companyName")
        getLocation()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val konum = LatLng(enlem,boylam)
        mMap.addMarker(MarkerOptions().position(konum).title("Location is here!"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(konum))
    }


    fun getLocation() {

        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        var docRef = db.collection("konum").whereEqualTo("company_email", companyName)
        docRef.get().addOnSuccessListener { document ->
            for (doc in document) {
                enlem = doc.get("enlem") as Double
                boylam = doc.get("boylam") as Double
                Log.d("TAG", "Location is: $enlem, $boylam")

            }
        }
            .addOnFailureListener { exception ->
                Log.d("TAG", "get failed with ", exception)
            }
    }
}
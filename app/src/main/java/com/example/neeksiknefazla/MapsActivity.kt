package com.example.neeksiknefazla

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.neeksiknefazla.enterprisefragments.EnterpriseProfileFragment
import com.example.neeksiknefazla.loginsignupactivities.LoginActivity

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_add_location.*
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.activity_signup_client.*
import kotlinx.android.synthetic.main.fragment_enterprise_profile.*
import kotlinx.android.synthetic.main.fragment_profile.*

class MapsActivity : AppCompatActivity(), LocationListener, OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var izinKontrol = 0
    private lateinit var konumYoneticisi: LocationManager
    private val konumSaglayici = "gps"
    private var enlem:Double = 41.102715
    private var boylam:Double = 28.982018333333333


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        konumYoneticisi = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        buttonSaveLocation.setOnClickListener {
            saveLocation()
            val intent = Intent(this@MapsActivity, EntrepriseHomePageActivity::class.java)
            startActivity(intent)
        }

        buttonGetLocation.setOnClickListener {

            izinKontrol = ContextCompat.checkSelfPermission(this@MapsActivity, android.Manifest.permission.ACCESS_FINE_LOCATION)

            if(izinKontrol != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this@MapsActivity,
                        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 100)

            } else {
                var konum = konumYoneticisi.getLastKnownLocation(konumSaglayici)

                if(konum != null) {
                    onLocationChanged(konum)
                } else {
                    editTextEnlem.setText("Failed to retrieve location!")
                    editTextBoylam.setText("Failed to retrieve location!")
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {

        val moda = LatLng(43.9800231, 29.0140759)
       // onLocationChanged()
        mMap = googleMap

        mMap.addMarker(MarkerOptions().position(moda).title("Moda"))
       // mMap.moveCamera(CameraUpdateFactory.newLatLng(moda))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(moda,15f))
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        if (requestCode == 100) {

            izinKontrol = ContextCompat.checkSelfPermission(this@MapsActivity, android.Manifest.permission.ACCESS_FINE_LOCATION)

            if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "Permission is granted and location service is activated!", Toast.LENGTH_SHORT).show()

                var konum = konumYoneticisi.getLastKnownLocation(konumSaglayici)

                if(konum != null) {
                    onLocationChanged(konum)
                } else {
                    editTextEnlem.setText("Failed!")
                    editTextBoylam.setText("Failed!")
                }
            } else {
                Toast.makeText(applicationContext, "Permission is not granted and location service is not activated!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onLocationChanged(location: Location) {
        enlem = location.latitude.toDouble()
        boylam = location.longitude.toDouble()

        editTextEnlem.setText("$enlem")
        editTextBoylam.setText("$boylam")

        Log.e("TAG", "Document : $enlem, $boylam ")
    }

    private fun changeMap () {
        var loc = LatLng(enlem, boylam)

        mMap.addMarker(MarkerOptions().position(loc)
            .title("Here you are:)")
            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_baseline_location_on_24)))

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc,30f))
        Log.e("TAG", "Document : onLocationChanged")

    }

    private fun saveLocation(){
        val firestoreDb: FirebaseFirestore = FirebaseFirestore.getInstance()
        val user = Firebase.auth.currentUser
        val email = user?.email.toString()

        val orderInfo = hashMapOf(
            "company_email" to email,
            "enlem" to enlem,
            "boylam" to boylam
        )
        firestoreDb.collection("konum")
            .add(orderInfo)
            .addOnSuccessListener { Log.d("başarılı", "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w("hatalı", "Error writing document", e) }
        Toast.makeText(applicationContext, "Location is saved.", Toast.LENGTH_SHORT).show()

    }
}
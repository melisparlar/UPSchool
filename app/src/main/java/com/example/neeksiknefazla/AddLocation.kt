package com.example.neeksiknefazla

import android.content.Context
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
import kotlinx.android.synthetic.main.activity_add_location.*
import java.util.jar.Manifest

class AddLocation : AppCompatActivity(), LocationListener {

    private var izinKontrol = 0
    private lateinit var konumYoneticisi:LocationManager
    private val konumSaglayici = "gps"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_location)

        konumYoneticisi = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        buttonKonumAl.setOnClickListener {

            izinKontrol = ContextCompat.checkSelfPermission(this@AddLocation, android.Manifest.permission.ACCESS_FINE_LOCATION)

            if(izinKontrol != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this@AddLocation,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 100)

            } else {
                var konum = konumYoneticisi.getLastKnownLocation(konumSaglayici)

                if(konum != null) {
                    onLocationChanged(konum)
                    Log.e("TAG", "Document : onLocationChanged")

                } else {
                    textViewBoylam.text = "Failed to retrieve location!"
                    textViewEnlem.text = "Failed to retrieve location!"
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {

        if (requestCode == 100) {

            izinKontrol = ContextCompat.checkSelfPermission(this@AddLocation, android.Manifest.permission.ACCESS_FINE_LOCATION)

            if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "Permission is granted and location service is activated!", Toast.LENGTH_SHORT).show()

                var konum = konumYoneticisi.getLastKnownLocation(konumSaglayici)

                if(konum != null) {
                    onLocationChanged(konum)
                } else {
                    textViewBoylam.text = "Failed!"
                    textViewEnlem.text = "Failed"
                }
            } else {
                Toast.makeText(applicationContext, "Permission is not granted and location service is not activated!", Toast.LENGTH_SHORT).show()

            }
        }
    }

    override fun onLocationChanged(location: Location) {
        val enlem = location.latitude
        val boylam = location.longitude

        textViewEnlem.text = "Enlem: $enlem"
        textViewBoylam.text = "Boylam: $boylam"

        Log.e("TAG", "Document : $enlem, $boylam ")
    }
}
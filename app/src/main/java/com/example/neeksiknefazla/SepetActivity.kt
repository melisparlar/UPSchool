package com.example.neeksiknefazla

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SepetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sepet)
    }

    override fun onBackPressed() {
        // Geri tusuna basilinca direkt uygulamadan cikis yapacak
        val intent = Intent(this@SepetActivity, ClientHomePageActivity::class.java)
        startActivity(intent)
    }
}
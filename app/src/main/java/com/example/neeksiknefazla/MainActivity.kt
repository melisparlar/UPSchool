package com.example.neeksiknefazla

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.neeksiknefazla.loginsignupactivities.LoginActivity
import com.example.neeksiknefazla.loginsignupactivities.SignUpActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonLogin.setOnClickListener {
            // SAYFA GECISI SAGLAMA
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        buttonSignUp.setOnClickListener {
            // SAYFA GECISI SAGLAMA
            val intent = Intent(this@MainActivity, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        // Geri tusuna basilinca direkt uygulamadan cikis yapacak
        val cikisIntent = Intent(Intent.ACTION_MAIN)
        cikisIntent.addCategory(Intent.CATEGORY_HOME)
        cikisIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(cikisIntent)
    }
}
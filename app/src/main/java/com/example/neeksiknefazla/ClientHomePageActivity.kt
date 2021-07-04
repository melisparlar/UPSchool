package com.example.neeksiknefazla

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.neeksiknefazla.clientfragments.DiscoverFragment
import com.example.neeksiknefazla.clientfragments.NotificationsFragment
import com.example.neeksiknefazla.clientfragments.ProfileFragment
import com.example.neeksiknefazla.enterprisefragments.AddProductFragment
import com.example.neeksiknefazla.enterprisefragments.EnterpriseProfileFragment
import com.example.neeksiknefazla.enterprisefragments.ReportsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_client_home_page.*
import kotlinx.android.synthetic.main.activity_entreprise_home_page.*

class ClientHomePageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_home_page)

        val navHostFragment=supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        NavigationUI.setupWithNavController(nav_view, navHostFragment.navController)
        nav_view.selectedItemId=R.id.discoverFragment
    }


}
package com.example.neeksiknefazla

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.neeksiknefazla.clientfragments.NotificationsFragment
import com.example.neeksiknefazla.enterprisefragments.AddProductFragment
import com.example.neeksiknefazla.enterprisefragments.EnterpriseProfileFragment
import com.example.neeksiknefazla.enterprisefragments.ReportsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_entreprise_home_page.*
import kotlinx.android.synthetic.main.fragment_products.*


class EntrepriseHomePageActivity : AppCompatActivity() {

    private val mOnNavigationItemSelectedListener = BottomNavigationView
        .OnNavigationItemSelectedListener {item ->
            when(item.itemId) {
                R.id.addProductFragment2 -> {
                    replaceFragment(AddProductFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.reportsFragment2 -> {
                    replaceFragment(ReportsFragment())
                    return@OnNavigationItemSelectedListener true
                }
                R.id.enterpriseProfileFragment2 -> {
                    replaceFragment(EnterpriseProfileFragment())
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entreprise_home_page)

        enterprise_bottom_nav_menu.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        replaceFragment(AddProductFragment())


    }
    private fun replaceFragment (fragment : Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_enteprise,fragment)
        fragmentTransaction.commit()
    }
}
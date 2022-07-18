package com.example.logintask.dashboard


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.logintask.R
import com.example.logintask.dashboard.fragment.home.HomeFragment
import com.example.logintask.databinding.ActivityDashboardBinding
import com.example.logintask.lib.utils.logoutDialog
import com.google.android.material.navigation.NavigationView

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val menuBinding = binding.menu
        overridePendingTransition(R.anim.slide_in_animation, R.anim.slide_out_animation)
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val toolbarBinding = binding.toolbar
        drawerLayout.drawerElevation = 0F
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.nav_host_fragment_content_dashboard, HomeFragment()).commit()

        toolbarBinding.ivDrawer.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        toolbarBinding.ivLogout.setOnClickListener {
            logoutDialog(this)
        }

        menuBinding.llHome.setOnClickListener {
            overridePendingTransition(R.anim.slide_in_animation, R.anim.slide_out_animation)
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            }else {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
            }
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
           fragmentTransaction.replace(R.id.nav_host_fragment_content_dashboard, HomeFragment()).commit()
        }

        menuBinding.llLogout.setOnClickListener {
            overridePendingTransition(R.anim.slide_in_animation, R.anim.slide_out_animation)
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START)
            } else {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
            }
            logoutDialog(this)
        }

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    //write your implementation here
                    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        drawerLayout.closeDrawer(GravityCompat.START)
                    }

                    true
                }
                R.id.nav_logout -> {
                    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        drawerLayout.closeDrawer(GravityCompat.START)
                    }
                    logoutDialog(this)
                    true
                }
                else -> {
                    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        drawerLayout.closeDrawer(GravityCompat.START)
                    }
                    false
                }
            }
        }
    }

}
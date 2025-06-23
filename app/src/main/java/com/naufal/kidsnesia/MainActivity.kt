package com.naufal.kidsnesia

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.naufal.kidsnesia.main_features.presentation.dashboard.DashboardViewModel
import com.naufal.kidsnesia.databinding.ActivityMainBinding
import com.naufal.kidsnesia.ui.welcome.WelcomeActivity
import org.koin.android.ext.android.get

class       MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: DashboardViewModel = get()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val navView = binding.navView
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

//        val navController = findNavController(R.id.navHostFragment) // Perbaikan di sini

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_video,
                R.id.navigation_event,
                R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

//        observeSession()
    }

//    private fun observeSession() {
//        viewModel.getSession().observe(this) { user ->
//            Log.d("SessionCheck", "User login status retrieved: ${user.isLogin}")
//            if (!user.isLogin) {
//                Log.d("SessionCheck", "User not logged in, navigating to Welcome Screen")
//                navigateToWelcomeScreen()
//            } else {
//                Toast.makeText(this, "Selamat datang kembali!", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    private fun navigateToWelcomeScreen() {
//        val intent = Intent(this, WelcomeActivity::class.java)
//        startActivity(intent)
//        finish()
//    }
}

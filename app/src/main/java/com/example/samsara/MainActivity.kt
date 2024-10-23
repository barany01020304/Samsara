package com.example.samsara



import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.samsara.databinding.ActivityMainBinding
import com.example.samsara.screens.main.MainFragmentDirections
import com.example.samsara.screens.onboarding.OnBoardingFragmentDirections
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding
    lateinit  var navHostFragment:NavHostFragment
    lateinit var  navController:NavController
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var networkCallback: ConnectivityManager.NetworkCallback
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        enableEdgeToEdge()
        binding=DataBindingUtil.setContentView(this,R.layout.activity_main)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
         navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
         navController = navHostFragment.navController
        val currentDestination = navController.currentDestination?.id
        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        setupNetworkCallback()

        if (currentDestination == R.id.onBoardingFragment  && !isNetworkAvailable(this) &&Firebase.auth.currentUser!=null) {
            navController.navigate(OnBoardingFragmentDirections.actionOnBoardingFragmentToNoInternetFragment())
        }else if (currentDestination == R.id.onBoardingFragment&&Firebase.auth.currentUser!=null){
            navController.navigate(OnBoardingFragmentDirections.actionOnBoardingFragmentToMainFragment())

        }

    }
    private fun setupNetworkCallback() {
        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onLost(network: Network) {
                super.onLost(network)
                // Network is lost, check the current destination and navigate to NoInternetFragment
                runOnUiThread {
                    if (navController.currentDestination?.id != R.id.noInternetFragment) {
                        navController.navigate(R.id.noInternetFragment)
                    }
                }
            }

            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                // Network is available, you can handle reconnection here if needed
                runOnUiThread {
                    if (navController.currentDestination?.id == R.id.noInternetFragment) {
                        navController.popBackStack()  // Dismiss the NoInternetFragment when connection is restored
                    }
                }
            }
        }

        // Register the callback with the ConnectivityManager
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    }
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Unregister the network callback to avoid leaks
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    }

package com.example.spoti5.presentations.feature.auth

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.spoti5.BuildConfig
import com.example.spoti5.R
import com.example.spoti5.constants.Constants.REQUEST_CODE
import com.example.spoti5.databinding.ActivityMainBinding
import com.example.spoti5.presentations.feature.auth.ViewModel.LoginViewModel
import com.example.spoti5.utils.SharePrefUtils
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.sdk.android.auth.AuthorizationClient
import com.spotify.sdk.android.auth.AuthorizationResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy(mode = LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }
    //nav
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)


        // Define navHostFragment to navController to manage the screens
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        Log.e(TAG, "Token: ${SharePrefUtils(this).saveAccessToken}")
        observeDestinationChange()

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun observeDestinationChange() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment -> {
                    // Hide the action bar when on the login fragment
                    supportActionBar?.hide()
                }
                else -> {
                    // Show the action bar for other fragments
                    supportActionBar?.show()
                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            val response = AuthorizationClient.getResponse(resultCode, data)
            when (response.type) {
                AuthorizationResponse.Type.TOKEN -> {

                    SharePrefUtils(this).saveAccessToken = response.accessToken
                    Log.e(TAG, "onActivityResult: Token: ${response.accessToken}")

                    navigateMainFragment()
                }
                AuthorizationResponse.Type.ERROR -> {
                    Log.e(TAG, "onActivityResult: Error ${response.error}")
                }
                else -> {
                    Log.e(TAG, "onActivityResult: ${response.code} ${response.error}")
                }
            }
        }
    }

    private fun navigateMainFragment() {
        try {
            navController.navigate(R.id.action_loginFragment_to_homeFragment)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}
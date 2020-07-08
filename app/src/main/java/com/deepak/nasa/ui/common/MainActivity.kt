package com.deepak.nasa.ui.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.deepak.nasa.R
import com.deepak.nasa.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun initializeDaggerComponent(){

    }


    private fun init() {
        val navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController
    }

//    open fun goBack() = onBackPressed()
//
//    override fun onBackPressed() {
//        if(supportFragmentManager.backStackEntryCount > 0)
//            supportFragmentManager.popBackStackImmediate()
//        super.onBackPressed()
//    }

    override fun onNavigateUp(): Boolean {
        return navController.navigateUp() || super.onNavigateUp()
    }
}
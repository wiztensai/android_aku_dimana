package com.jetwiz.akudimana.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.jetwiz.akudimana.R
import com.jetwiz.akudimana.base.CST
import com.jetwiz.akudimana.databinding.AMainBinding
import wazma.punjabi.helper.U_Prefs

class A_Main :AppCompatActivity() {

    lateinit var bind:AMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = AMainBinding.inflate(layoutInflater)
        setContentView(bind.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val flagMsg = U_Prefs(this).getPrefs().getBoolean(CST.SHOW_FIRST_MSG, true)
        if (flagMsg == true) {
            navController.navigate(R.id.nav_f_Opening)
        }
    }
}
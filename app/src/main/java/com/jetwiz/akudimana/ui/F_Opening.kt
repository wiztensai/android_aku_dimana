package com.jetwiz.akudimana.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.jetwiz.akudimana.R
import com.jetwiz.akudimana.base.CST
import com.jetwiz.akudimana.databinding.FOpeningBinding
import wazma.punjabi.helper.U_Prefs

class F_Opening:Fragment() {

    lateinit var bind:FOpeningBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bind = FOpeningBinding.inflate(layoutInflater)
        return bind.root
    }

    override fun onStart() {
        super.onStart()

        bind.btnSubmit.setOnClickListener {
            if (findNavController().currentDestination!!.label!!.equals("F_Map")) {
                return@setOnClickListener
            }

            if (bind.cbInfo.isChecked) {
                U_Prefs(requireContext()).setData(CST.SHOW_FIRST_MSG,false)
            }

            findNavController().navigate(R.id.nav_f_Map)
        }
    }
}
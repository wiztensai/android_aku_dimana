package com.jetwiz.akudimana.dialog

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.Constraints
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.jetwiz.akudimana.R
import com.jetwiz.akudimana.base.CST
import com.jetwiz.akudimana.databinding.DfResultBinding
import com.jetwiz.akudimana.domain.Result
import com.jetwiz.akudimana.util.U_DpPxConverter
import com.jetwiz.akudimana.viewmodel.VM_Map
import wazma.punjabi.helper.U_Prefs


@SuppressLint("ValidFragment")
class DF_Result(var result: Result = Result(), var viewmodel:VM_Map) : DialogFragment() {

    lateinit var bind: DfResultBinding
    lateinit var prefs:U_Prefs

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = DfResultBinding.inflate(layoutInflater)
        return bind.root
    }

    override fun onStart() {
        super.onStart()
        if (dialog != null) {
            prefs = U_Prefs(bind.btnGoHere.context)

            dialog!!.getWindow()!!.setLayout(Constraints.LayoutParams.MATCH_PARENT, Constraints.LayoutParams.WRAP_CONTENT) // full width dialog
            val back = ColorDrawable(Color.TRANSPARENT)
            val inset = InsetDrawable(back, U_DpPxConverter.dpToPixel(16, bind.btnGoHere.context))
            dialog!!.window!!.setBackgroundDrawable(inset)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (dialog!!.window != null) {
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        result.name?.let {
            bind.tvName.text = it
        }

        result.vicinity?.let {
            bind.tvAddress.text = it
        }

        result.photos?.let {
            if (it.isNotEmpty()) {
                bind.ivPhoto.visibility = View.VISIBLE

                val key = resources.getString(R.string.google_maps_key)
                val url = "${CST.BASE_MAPS_URL}place/photo?maxwidth=400&photoreference=${it[0].photoReference}&key=${key}"

                Glide.with(this).load(url).into(bind.ivPhoto)
            }
        }?: kotlin.run {
            bind.ivPhoto.visibility = View.GONE
        }


        bind.btnCancel.setOnClickListener {
            dismiss()
        }

        bind.btnGoHere.setOnClickListener {
            result.geometry?.let {
                viewmodel.resetPlaces()

                /**
                https://developers.google.com/maps/documentation/urls/android-intents#launch-turn-by-turn-navigation
                d for driving (default)
                b for bicycling
                l for two-wheeler
                w for walking */

//                val gmmIntentUri = Uri.parse("google.navigation:q=${it.location!!.lat},${it.location!!.lng}&mode=l")
//                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
//                mapIntent.setPackage("com.google.android.apps.maps")
//                startActivity(mapIntent)

                val url = "https://www.google.com/maps/dir/?api=1&destination=${it.location!!.lat},${it.location!!.lng}&travelmode=two-wheeler"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            }
//            dismiss()
        }
    }


}
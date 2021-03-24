package com.jetwiz.akudimana.dialog

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.view.ContextThemeWrapper
import androidx.constraintlayout.widget.Constraints
import androidx.core.view.forEach
import androidx.fragment.app.DialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable
import com.google.android.material.slider.Slider
import com.jetwiz.akudimana.R
import com.jetwiz.akudimana.base.CST
import com.jetwiz.akudimana.databinding.DfFilterBinding
import com.jetwiz.akudimana.util.U_DpPxConverter
import timber.log.Timber
import wazma.punjabi.helper.U_Prefs

@SuppressLint("ValidFragment")
class DF_Filter() : DialogFragment() {

    private lateinit var bind: DfFilterBinding
    private var filterType = "park" // default
    private lateinit var prefs:U_Prefs

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = DfFilterBinding.inflate(layoutInflater)
        prefs = U_Prefs(bind.btnSubmit.context)
        return bind.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            it.getWindow()!!.setLayout(Constraints.LayoutParams.MATCH_PARENT, Constraints.LayoutParams.WRAP_CONTENT) // full width dialog
            val back = ColorDrawable(Color.TRANSPARENT)
            val inset = InsetDrawable(back, U_DpPxConverter.dpToPixel(16, it.context))
            it.window!!.setBackgroundDrawable(inset)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (dialog!!.window != null) {
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        bind.btnCancel.setOnClickListener {
            dismiss()
        }

        val radius = prefs.getPrefs().getInt(CST.RADIUS_I, CST.DEF_RADIUS)
        bind.sliderRadius.value = radius.toFloat()
        bind.tvRadius.text = (bind.sliderRadius.value / 1000).toInt().toString() +" Km"

        bind.sliderRadius.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
                // nothing
            }

            override fun onStopTrackingTouch(slider: Slider) {
                bind.tvRadius.text = (slider.value / 1000).toInt().toString() +" Km"
            }
        })

        val type = prefs.getPrefs().getString(CST.FILTER_TYPE_S, CST.DEF_TYPE)

        val types = resources.getStringArray(R.array.field_types)
        types.sort()
        types.forEach {
            // set chip style programatically: https://stackoverflow.com/a/55925632/5855782
            val chip = Chip(bind.chipGroupFilter.context)
            val drawable = ChipDrawable.createFromAttributes(bind.chipGroupFilter.context, null, 0, R.style.Widget_MaterialComponents_Chip_Choice)
            chip.setChipDrawable(drawable)

            val title = it.replace("_", " ")
            chip.text = title.capitalize()

            chip.isCheckable = true
            chip.isClickable = true
            chip.tag = it
            bind.chipGroupFilter.addView(chip)
        }

        bind.chipGroupFilter.forEach {child ->
            if (child.tag.toString().equals(type)) {
                (child as Chip).setChecked(true)
                filterType = child.tag.toString()
            }
        }

        bind.chipGroupFilter.forEach {child ->
            (child as? Chip)?.setOnCheckedChangeListener { _, _ ->
                filterType = registerFilterChanged()
            }
        }

        bind.btnSubmit.setOnClickListener {
            if (filterType.isEmpty()) {
                Toast.makeText(context,"Please choose one filter criteria!", Toast.LENGTH_SHORT).show()
            }

            prefs.setData(CST.FILTER_TYPE_S, filterType)
                    .setData(CST.RADIUS_I, bind.sliderRadius.value.toInt())

            dismiss()
        }
    }

    private fun registerFilterChanged():String {
        val ids = bind.chipGroupFilter.getCheckedChipIds()

        val tags = mutableListOf<CharSequence>()

        ids.forEach { id ->
            val text = (bind.chipGroupFilter.findViewById(id) as Chip).tag.toString()
            tags.add(text)
        }

        val text = if (tags.isNotEmpty()) {
            tags.joinToString(",")
        } else {
            ""
        }

        return text
    }
}
package com.jetwiz.akudimana.ui

import android.app.ProgressDialog
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.jetwiz.akudimana.R
import com.jetwiz.akudimana.databinding.FMapBinding
import com.jetwiz.akudimana.dialog.DF_Filter
import com.jetwiz.akudimana.dialog.DF_Result
import com.jetwiz.akudimana.util.NetworkState
import com.jetwiz.akudimana.util.Status
import com.jetwiz.akudimana.util.U_Maps
import com.jetwiz.akudimana.viewmodel.VM_Map
import timber.log.Timber

class F_Map:Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var bind: FMapBinding
    private lateinit var uMaps: U_Maps
    private var mLocation: Location? = null
    private lateinit var viewmodel: VM_Map
    private lateinit var progressDialog:ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = FMapBinding.inflate(layoutInflater)
        return bind.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewmodel = ViewModelProviders.of(this, VM_Map.VMFactory(requireActivity().application)).get(VM_Map::class.java)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Loading")

        onListener()
        onObserve()
    }

    private fun onObserve() {
        viewmodel.placeData.observe(viewLifecycleOwner, Observer {
            progressDialog.dismiss()

            if (it.places.isEmpty()) {
                if (it.networkState.status == Status.FAILED) {
                    it.networkState.msg?.let {
                        Toast.makeText(bind.root.context, it, Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                val filtered = it.places.filter {
                    it.businessStatus.equals("OPERATIONAL")
                }

                val rnds = (0..filtered.size-1).random()
                val place = filtered.get(rnds)

                DF_Result(place, viewmodel).show(childFragmentManager, null)
            }
        })
    }

    private fun onListener() {
        bind.btnCurrentPlace.setOnClickListener {
            if (::mMap.isInitialized) {
                uMaps.getLocation()
            } else {
                Toast.makeText(activity,"Please wait, map is loading", Toast.LENGTH_SHORT).show()
            }
        }

        bind.btnFind.setOnClickListener {
            mLocation?.let {
                progressDialog.show()
                viewmodel.findPlace(it)
            }
        }

        bind.btnFilter.setOnClickListener {
            DF_Filter().show(childFragmentManager, "")
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        uMaps = U_Maps(this, viewLifecycleOwner.lifecycle, object : U_Maps.MapsUtilListener {
            override fun onLocationChanged(location: Location?) {
                super.onLocationChanged(location)
                location?.let {
                    Timber.d("latitude" + location.latitude.toString())
                    Timber.d("longitude" + location.longitude.toString())

                    mLocation = location
                    val currentLoc = LatLng(location.latitude, location.longitude)

                    if (::mMap.isInitialized) {
                        mMap.clear()
                        mMap.addMarker(MarkerOptions().position(currentLoc).title("Current Location"))
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 18.0f))
                    }
                }
            }
        })

        uMaps.getLocation()
    }



    @CallSuper
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)

        uMaps.locationManager!!.onActivityResult(requestCode, resultCode, data)
    }

    @CallSuper
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        uMaps.locationManager!!.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
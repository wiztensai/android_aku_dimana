package com.jetwiz.akudimana.viewmodel

import android.app.Application
import android.content.Context
import android.location.Location
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.jetwiz.akudimana.domain.PlacesModel
import com.jetwiz.akudimana.domain.Result
import com.jetwiz.akudimana.repo.RepoMap
import com.jetwiz.akudimana.util.NetworkState
import com.jetwiz.akudimana.util.U_Prefs
import kotlinx.coroutines.launch
import timber.log.Timber

data class PlaceData(var places : MutableList<Result>, var networkState: NetworkState)

class VM_Map(context: Context) : ViewModel() {

    private var prefs: U_Prefs
    private var repo: RepoMap
    var placeData = MutableLiveData<PlaceData>()
        private set
    private var _places = mutableListOf<Result>() // temp

    init {
        prefs = U_Prefs(context)
        repo = RepoMap(context)
        placeData.value = PlaceData(mutableListOf(), NetworkState.LOADING)
    }

    fun findPlace(mLocation: Location) {
        viewModelScope.launch {
            try {
                val firstResult = repo.findPlace(mLocation)

                firstResult.nextPageToken?.also {
                    _places = firstResult.results
                    getNextPage(it)
                }?: kotlin.run {
                    if (firstResult.results.isEmpty()) {
                        placeData.value = PlaceData(firstResult.results, NetworkState.error("Place not found!"))
                    } else {
                        placeData.value = PlaceData(firstResult.results, NetworkState.LOADED)
                    }
                }

            } catch (t: Throwable) {

                Timber.e(t, "ERROR")
                placeData.value = PlaceData(mutableListOf(), NetworkState.LOADED)
            }
        }
    }

    fun getNextPage(pageToken:String) {
        viewModelScope.launch {
            try {
                val nextPageResult = repo.nextPlace(pageToken)
                _places.addAll(nextPageResult.results)

                nextPageResult.nextPageToken?.also {
                    getNextPage(it)
                }?: kotlin.run {
                    placeData.value = PlaceData(_places, NetworkState.LOADED)
                }

            } catch (t: Throwable) {

                Timber.e(t, "ERROR")
                placeData.value = PlaceData(mutableListOf(), NetworkState.LOADED)
            }
        }
    }

    fun resetPlaces() {
        placeData.value = PlaceData(mutableListOf(), NetworkState.LOADING)
    }

    class VMFactory constructor(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(VM_Map::class.java)) {
                VM_Map(application) as T
            } else {
                throw IllegalArgumentException("ViewModel Not Found")
            }
        }
    }
}
package com.anwesha.anweshascan

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventsViewModel: ViewModel() {
    var eventList: MutableLiveData<ArrayList<EventsList>> = MutableLiveData()

    fun getEventListObserver(): MutableLiveData<ArrayList<EventsList>>{
        return eventList
    }

    fun makeApiCall(){

        val call = QRScanResult.scanapi.getAllEvents()
        call.enqueue(object : Callback<MutableList<EventsList>>{
            override fun onResponse(
                call: Call<MutableList<EventsList>>,
                response: Response<MutableList<EventsList>>
            ){
                Log.d("response", response.code().toString())
                Log.d("response", response.message())
                if(response.isSuccessful) {
                    eventList.postValue(response.body() as ArrayList)
                }
            }

            override fun onFailure(call: Call<MutableList<EventsList>>, t: Throwable) {
                Log.d("inside failure", "could not call that api")
            }
        })
    }

}
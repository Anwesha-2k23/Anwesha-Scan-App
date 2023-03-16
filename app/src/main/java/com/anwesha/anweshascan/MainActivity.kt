package com.anwesha.anweshascan

import android.R
import android.content.pm.PackageManager
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat.OrientationMode
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anwesha.anweshascan.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.zxing.client.android.Intents
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.ArrayList


class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var eventId: String
    private lateinit var eventName: String
    private lateinit var eventViewModel: EventsViewModel
    private lateinit var eventRecyclerView: RecyclerView
    private lateinit var newEventList: ArrayList<EventsList>
    private lateinit var adapter: EventsAdapter




    private val barcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents == null) {
            val originalIntent = result.originalIntent
            if (originalIntent == null) {
                Log.d("MainActivity", "Cancelled scan")
                Toast.makeText(applicationContext, "Cancelled", Toast.LENGTH_LONG).show()
            } else if (originalIntent.hasExtra(Intents.Scan.MISSING_CAMERA_PERMISSION)) {
                Log.d("MainActivity", "Cancelled scan due to missing camera permission")
                Toast.makeText(
                    applicationContext,
                    "Cancelled due to missing camera permission",
                    Toast.LENGTH_LONG
                ).show()
            }
            val fragMan = supportFragmentManager
            fragMan.popBackStack()
        }
        else {

            CoroutineScope(Dispatchers.IO).launch {
                Log.d("signature", result.contents)
                val response = QRScanResult.scanapi.getUserById(ScanApiData(result.contents, eventId))
                val repbody = response.body()
                if(response.isSuccessful) {
                    val bundle = Bundle()
                    bundle.putString("eventName", eventName)
                    bundle.putString("eventId", eventId)
                    bundle.putString("anweshaId", repbody!!.anwesha_id)
                    bundle.putString("userName", repbody.username)
                    bundle.putString("entry_status", if(repbody.has_entered == true) "IN" else "OUT")

                    val fragmentManager = supportFragmentManager.beginTransaction()
                    fragmentManager.addToBackStack(null)
                    val scannedDataFragment = ScannedDataFragment()
                    scannedDataFragment.arguments = bundle
                    fragmentManager.replace(binding.frameContainer.id, scannedDataFragment)
                    fragmentManager.commit()
//                    binding.userName.text = repbody!!.username
//                    binding.anweshaId.text = repbody.anwesha_id
//                    binding.currEntryStatus.text = if(repbody.entry_status == true) "IN" else "OUT"
                }
                else {
                    CoroutineScope(Dispatchers.Main).launch {
                        if(response.code() == 400) {
                            Toast.makeText(applicationContext, "Invalid QR Code", Toast.LENGTH_SHORT).show()
                        }
                        else if (response.code() == 402) {
                            Toast.makeText(applicationContext, "Invalid Event ID", Toast.LENGTH_SHORT).show()
                        }
                        else { // error code 401
                            Toast.makeText(applicationContext, "${response.code()} User/ Team Not registered", Toast.LENGTH_SHORT).show()
                        }
                        val fragMan = supportFragmentManager
                        fragMan.popBackStack()
                    }
                }
            }



        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        newEventList = arrayListOf()
        eventViewModel = ViewModelProvider(this)[EventsViewModel::class.java]



        loadEvents()


//        //Sets status bar color to white
//        window.statusBarColor = Color.WHITE
//        window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
//
//        //Launches qr code scanner
//        scanQrCode.launch(null)
//

    }

    private fun loadEvents() {

        eventRecyclerView = binding.eventsRecycler
        eventRecyclerView.layoutManager = LinearLayoutManager(applicationContext)
        adapter = EventsAdapter(applicationContext)
        eventRecyclerView.adapter = adapter
        getEvents()

    }

    private fun getEvents() {
        eventViewModel.getEventListObserver().observe(this) {

            if (it != null) {
                newEventList = it
                adapter.setEvents(it)
                adapter.notifyDataSetChanged()
                adapter.setOnItemClickListener(object : EventsAdapter.OnItemClickListener {
                    override fun onItemClicked(event: EventsList) {          //when any event from the recycler view is clicked

                        val scanOptions = ScanOptions().apply {
                            setDesiredBarcodeFormats(ScanOptions.QR_CODE)
                            setPrompt("Scan QR code")
                            setOrientationLocked(false)
                        }
                        eventId = event.id!!
                        eventName = event.name!!
                        barcodeLauncher.launch(scanOptions)

                    }
                })


            } else {
                Toast.makeText(applicationContext, "Error in getting Events", Toast.LENGTH_SHORT).show()
            }

        }

        eventViewModel.makeApiCall()
    }






    // Function launched on qr code result
//    private fun qrResult(result: QRResult) {
//        val text = when (result) {
//            is QRResult.QRSuccess -> result.content.rawValue
//            QRResult.QRUserCanceled -> "User canceled"
//            QRResult.QRMissingPermission -> "Missing permission"
//            is QRResult.QRError -> "${result.exception.javaClass.simpleName}: ${result.exception.localizedMessage}"
//        }
//
//        Toast.makeText(this,text,Toast.LENGTH_SHORT).show()
//    }
//
    // On Success
//    private fun onSuccess(){
//        val dialog = MaterialAlertDialogBuilder(
//            this,
////            R.style.MyRounded
//        ) // for fragment you can use getActivity() instead of this
//            .setView(R.layout.dialog_layout_success) // custom layout is here
//        dialog.show()
//    }
//
//    // On Failure
//    private fun onFailure(){
//        val dialog = MaterialAlertDialogBuilder(
//            this,
//            R.style.MyRounded
//        ) // for fragment you can use getActivity() instead of this
//            .setView(R.layout.dialog_layout_failure) // custom layout is here
//        dialog.show()
//    }
//



}
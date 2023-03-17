package com.anwesha.anweshascan

import android.graphics.Color
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.anwesha.anweshascan.databinding.FragmentScannedDataBinding
import com.google.zxing.client.android.Intents
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ScannedDataFragment : Fragment() {

    private lateinit var binding: FragmentScannedDataBinding
    private lateinit var eventId: String



//    private val barcodeLauncher = registerForActivityResult(
//        ScanContract()
//    ) { result: ScanIntentResult ->
//        if (result.contents == null) {
//            val originalIntent = result.originalIntent
//            if (originalIntent == null) {
//                Log.d("MainActivity", "Cancelled scan")
//                Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show()
//            } else if (originalIntent.hasExtra(Intents.Scan.MISSING_CAMERA_PERMISSION)) {
//                Log.d("MainActivity", "Cancelled scan due to missing camera permission")
//                Toast.makeText(
//                    context,
//                    "Cancelled due to missing camera permission",
//                    Toast.LENGTH_LONG
//                ).show()
//            }
//            val fragMan = requireActivity().supportFragmentManager
//            fragMan.popBackStack()
//        }
//        else {
//
//            CoroutineScope(Dispatchers.IO).launch {
//                val response = QRScanResult.scanapi.getUserById(ScanApiData(result.contents, eventId))
//                val repbody = response.body()
//                if(response.isSuccessful) {
//                    binding.userName.text = repbody!!.username
//                    binding.anweshaId.text = repbody.anwesha_id
//                    binding.currEntryStatus.text = if(repbody.entry_status == true) "IN" else "OUT"
//                }
//                else {
//                    CoroutineScope(Dispatchers.Main).launch {
//                        if(response.code() == 400) {
//                            Toast.makeText(context, "Invalid QR Code", Toast.LENGTH_SHORT).show()
//                        }
//                        else if (response.code() == 402) {
//                            Toast.makeText(context, "Invalid Event ID", Toast.LENGTH_SHORT).show()
//                        }
//                        else {
//                            Toast.makeText(context, "User/ Team Not registered", Toast.LENGTH_SHORT).show()
//                        }
//                        val fragMan = requireActivity().supportFragmentManager
//                        fragMan.popBackStack()
//                    }
//                }
//            }
//
//
//
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




    }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        val scanOptions = ScanOptions().apply {
//            setDesiredBarcodeFormats(ScanOptions.QR_CODE)
//            setPrompt("Scan QR code")
//            setOrientationLocked(false)
//        }
//
//
//        barcodeLauncher.launch(scanOptions)
        binding = FragmentScannedDataBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val scanOptions = ScanOptions().apply {
//            setDesiredBarcodeFormats(ScanOptions.QR_CODE)
//            setPrompt("Scan QR code")
//            setOrientationLocked(false)
//        }
//
//
//        barcodeLauncher.launch(scanOptions)

        arguments.let {
            if(it == null) return@let
            binding.eventName.text = it.getString("eventName")
            binding.anweshaId.text = it.getString("anweshaId")
            binding.userName.text = it.getString("userName")
            binding.currEntryStatus.text = it.getString("entry_status")
            eventId = it.getString("eventId")!!
        }

        if(binding.currEntryStatus.text == "UNMARK") {
            binding.currEntryStatus.setBackgroundColor(Color.RED)

        }
        else {
            binding.currEntryStatus.setBackgroundColor(Color.GREEN)
        }


        binding.submitBtn.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                val checked = binding.entryStatus.checkedRadioButtonId
                val status = checked == binding.entryIn.id
                val response = QRScanResult.scanapi.postUserCurrentInfo(CurrentUserStatus(binding.anweshaId.text.toString(), eventId, status))
                CoroutineScope(Dispatchers.Main).launch {
                    if (response.isSuccessful) {
                        Toast.makeText(context, "Successfully updated", Toast.LENGTH_SHORT).show()
                        val player = MediaPlayer.create(context, R.raw.success_sound)
                        player.setOnPreparedListener {
                            it.start()
                        }
                        val fragMan = requireActivity().supportFragmentManager
                        fragMan.popBackStack()
                    } else {
                        val player = MediaPlayer.create(context, R.raw.error_sound)
                        player.setOnPreparedListener {
                            it.start()
                        }
                        Toast.makeText(context, "${response.code()} Oops.. Something occured", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }

        }
    }



}
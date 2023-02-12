package com.anwesha.anweshascan

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.anwesha.anweshascan.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.github.g00fy2.quickie.QRResult
import io.github.g00fy2.quickie.ScanQRCode


class MainActivity : AppCompatActivity() {
    private lateinit var b : ActivityMainBinding
    private val scanQrCode = registerForActivityResult(ScanQRCode(), ::qrResult)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        //Sets status bar color to white
        window.statusBarColor = Color.WHITE
        window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)

        //Launches qr code scanner
        scanQrCode.launch(null)

        b.submitBtn.setOnClickListener {
            onSuccess()
        }
    }


    // Function launched on qr code result
    private fun qrResult(result: QRResult) {
        val text = when (result) {
            is QRResult.QRSuccess -> result.content.rawValue
            QRResult.QRUserCanceled -> "User canceled"
            QRResult.QRMissingPermission -> "Missing permission"
            is QRResult.QRError -> "${result.exception.javaClass.simpleName}: ${result.exception.localizedMessage}"
        }

        Toast.makeText(this,text,Toast.LENGTH_SHORT).show()
    }

    // On Success
    private fun onSuccess(){
        val dialog = MaterialAlertDialogBuilder(
            this,
            R.style.MyRounded
        ) // for fragment you can use getActivity() instead of this
            .setView(R.layout.dialog_layout_success) // custom layout is here
        dialog.show()
    }

    // On Failure
    private fun onFailure(){
        val dialog = MaterialAlertDialogBuilder(
            this,
            R.style.MyRounded
        ) // for fragment you can use getActivity() instead of this
            .setView(R.layout.dialog_layout_failure) // custom layout is here
        dialog.show()
    }

}
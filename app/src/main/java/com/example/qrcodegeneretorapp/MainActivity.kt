package com.example.qrcodegeneretorapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.qrcodegeneretorapp.ui.theme.QrCodeGeneretorAppTheme
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QrCodeGeneretorAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    RenderUi(baseContext)
                }
            }
        }
    }
}

@Composable
private fun RenderUi(context: Context) {
    val qrValue = remember { mutableStateOf("QR Value") }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Button(
                onClick = {
                    configureCodeScanner(context = context) {
                        qrValue.value = it
                    }
                }
            ) {
                Text(text = "Scan QR Code")
            }
            Text(text = qrValue.value)
        }
    }
}

private fun configureCodeScanner(
    context: Context,
    qrValue: (String) -> Unit
) {
    val options = GmsBarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_QR_CODE,
            Barcode.FORMAT_AZTEC
        )
        .build()

    val scanner = GmsBarcodeScanning.getClient(context, options)

    scanner.startScan()
        .addOnSuccessListener { barcode ->
            // Task completed successfully
            val rawValue: String? = barcode.rawValue
            qrValue(rawValue.orEmpty())
        }
        .addOnCanceledListener {
            // Task canceled
        }
        .addOnFailureListener { e ->
            // Task failed with an exception
        }
}

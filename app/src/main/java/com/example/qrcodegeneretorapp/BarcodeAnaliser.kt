package com.example.qrcodegeneretorapp

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

@androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
class BarcodeAnalyser(
    val callback: () -> Unit
) : ImageAnalysis.Analyzer {
    override fun analyze(imageProxy: ImageProxy) {
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_QR_CODE,
                Barcode.FORMAT_AZTEC
            )
            .enableAllPotentialBarcodes() // to return all potential barcodes even if they cannot be decoded
            .build()

        val scanner = BarcodeScanning.getClient(options)
        val mediaImage = imageProxy.image
        mediaImage?.let {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    if (barcodes.size > 0) {
                        callback()
                    }
                }
                .addOnFailureListener {
                    // Task failed with an exception
                    // ...
                }
        }
        imageProxy.close()
    }
}


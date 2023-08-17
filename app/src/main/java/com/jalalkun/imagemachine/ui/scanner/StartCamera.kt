package com.jalalkun.imagemachine.ui.scanner

import android.content.Context
import android.view.Surface
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.jalalkun.imagemachine.utils.BarcodeAnalyser

fun Context.startCamera(
    lifecycleOwner: LifecycleOwner,
    cameraSelector: CameraSelector,
    previewView: PreviewView,
    value: (String) -> Unit
): ImageCapture {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
    val cameraProvider = cameraProviderFuture.get()
    cameraProvider.unbindAll()
    val preview = Preview.Builder()
        .build()
        .apply { setSurfaceProvider(previewView.surfaceProvider) }
    val imageCapture = ImageCapture.Builder()
        .setTargetRotation(Surface.ROTATION_0)
        .build()
    val imageAnalyzer = ImageAnalysis.Builder()
        .build()
        .also {
            it.setAnalyzer(ContextCompat.getMainExecutor(this), BarcodeAnalyser { v ->
                v?.let { value(v) }
                cameraProvider.unbindAll()
            })
        }
    cameraProvider.bindToLifecycle(
        lifecycleOwner,
        cameraSelector,
        preview,
        imageCapture,
        imageAnalyzer
    )
    return imageCapture
}
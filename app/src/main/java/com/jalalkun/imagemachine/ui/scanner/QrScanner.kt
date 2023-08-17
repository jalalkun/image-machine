package com.jalalkun.imagemachine.ui.scanner

import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import com.jalalkun.imagemachine.navigation.Navigate.toDetail
import com.jalalkun.imagemachine.utils.FeatureCameraPermission
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun QrScannerScreen(
    viewModel: QrScannerViewModel,
    navHostController: NavHostController,
    main: CoroutineDispatcher = Dispatchers.Main
) {
    val coroutine = rememberCoroutineScope()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView: PreviewView = remember { PreviewView(context) }
    LaunchedEffect(previewView) {
        context.startCamera(
            lifecycleOwner = lifecycleOwner,
            cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA,
            previewView = previewView
        ) {
            viewModel.findData(
                it,
                dataFound = { id ->
                    coroutine.launch(main) {
                        Toast.makeText(context, "data found", Toast.LENGTH_SHORT).show()
                        navHostController.toDetail(id)
                    }
                }, dataNotFound = {
                    coroutine.launch(main) {
                        Toast.makeText(context, "data not found", Toast.LENGTH_SHORT).show()
                        navHostController.popBackStack()
                    }
                }
            )
        }
    }
    FeatureCameraPermission {
        Box(modifier = Modifier.fillMaxSize()) {
            AndroidView(
                factory = {
                    previewView
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 45.dp)
                    .align(Alignment.TopCenter)
            )
        }
    }
}
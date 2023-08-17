package com.jalalkun.imagemachine.ui.machine_data.add_data

import android.content.ContentResolver
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.util.DebugLogger
import com.jalalkun.imagemachine.R
import com.jalalkun.imagemachine.navigation.Navigate.toMachineDataScreen
import com.jalalkun.imagemachine.utils.ResultState
import com.jalalkun.imagemachine.widget.FullScreenImage
import com.jalalkun.imagemachine.widget.Header
import com.jalalkun.imagemachine.widget.Loading
import com.jalalkun.imagemachine.widget.textForm.Form
import org.koin.androidx.compose.koinViewModel
import kotlin.math.ceil


@Composable
fun AddMachineDataScreen(
    navHostController: NavHostController,
    viewModel: AddDataViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(10)) {
            val resolver: ContentResolver = context.contentResolver
            it.forEach { u ->
                resolver.takePersistableUriPermission(
                    u,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
            viewModel.addImages(it)
        }
    Box(modifier = Modifier.fillMaxSize()) {
        Header(
            title = stringResource(id = R.string.add_machine_data),
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            viewModel.clearData()
            navHostController.popBackStack()
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, top = 66.dp, bottom = 40.dp, end = 24.dp)
                .align(Alignment.TopCenter),
            content = {
                item {
                    DetailMachineData(viewModel = viewModel) {
                        galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    }
                }
            }
        )
        Button(
            onClick = {
                viewModel.saveData(context)
            }, modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            Text(text = stringResource(id = R.string.save))
        }
    }

    when (
        val state = viewModel.saveDataState.collectAsStateWithLifecycle().value
    ) {
        is ResultState.Loading -> {
            Loading()
        }

        is ResultState.Success -> {
            viewModel.clearData()
            navHostController.popBackStack()
            navHostController.toMachineDataScreen()
        }

        is ResultState.Error -> {
            Toast.makeText(context, state.e.localizedMessage, Toast.LENGTH_SHORT).show()
            viewModel.dismiss()
        }

        is ResultState.Content -> {
            // do nothing
        }
    }

    BackHandler {
        viewModel.clearData()
        navHostController.popBackStack()
    }
}


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun DetailMachineData(viewModel: AddDataViewModel, pickImage: () -> Unit) {
    val images = viewModel.images.collectAsStateWithLifecycle().value
    val context = LocalContext.current
    var imagePath by remember {
        mutableStateOf("")
    }
    var showImage by remember {
        mutableStateOf(false)
    }
    if (showImage) {
        FullScreenImage(image = imagePath) {
            showImage = false
        }
    }
    Column {
        Form(state = viewModel.stateValueMachineData, modifier = Modifier.fillMaxWidth())
        CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
            val h =
                (if (images.isNotEmpty())
                    ceil(
                        (if (images.size < 10) images.size.toDouble() + 1
                        else
                            images.size.toDouble()) / 3.toDouble()
                    ).toInt()
                else 1) * 220
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .height(h.dp)
                    .padding(
                        start = 8.dp,
                        end = 8.dp,
                        bottom = 8.dp
                    )
            ) {
                items(
                    if (images.size < 10) {
                        images.size + 1
                    } else images.size
                ) { pos ->
                    if (pos < images.size) {
                        OutlinedCard(
                            onClick = {
                                imagePath = images[pos]
                                showImage = true
                            },
                            modifier = Modifier
                                .height(200.dp)
                                .padding(8.dp)
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                AsyncImage(
                                    modifier = Modifier.align(Alignment.Center),
                                    model = images[pos], contentDescription = "",
                                    imageLoader = ImageLoader.Builder(context)
                                        .logger(DebugLogger())
                                        .diskCachePolicy(CachePolicy.DISABLED)
                                        .memoryCachePolicy(CachePolicy.DISABLED)
                                        .build(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    } else {
                        OutlinedCard(
                            onClick = {
                                pickImage()
                            },
                            modifier = Modifier
                                .height(200.dp)
                                .padding(8.dp)
                        ) {
                            Box(modifier = Modifier.fillMaxSize()) {
                                AsyncImage(
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .size(42.dp),
                                    model = R.drawable.baseline_add_circle_outline_24,
                                    contentDescription = ""
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

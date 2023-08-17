package com.jalalkun.imagemachine.ui.machine_data.detail

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.request.CachePolicy
import coil.util.DebugLogger
import com.jalalkun.imagemachine.R
import com.jalalkun.imagemachine.navigation.Navigate.toMachineDataScreen
import com.jalalkun.imagemachine.navigation.Route.detailMachineDataScreen
import com.jalalkun.imagemachine.navigation.Route.machineDataScreenRoute
import com.jalalkun.imagemachine.ui.machine_data.models.ModelMachineData
import com.jalalkun.imagemachine.utils.ResultState
import com.jalalkun.imagemachine.widget.FullScreenImage
import com.jalalkun.imagemachine.widget.Header
import com.jalalkun.imagemachine.widget.Loading
import com.jalalkun.imagemachine.widget.textForm.Form
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.ceil

@Composable
fun DetailMachineDataScreen(
    navHostController: NavHostController,
    id: String,
    viewModel: MachineDetailViewModel
) {
    val context = LocalContext.current
    var editMode by remember {
        mutableStateOf(false)
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Header(
            title = stringResource(id = R.string.detail_machine_data),
            modifier = Modifier.align(Alignment.TopCenter)
        ) {
            back(navHostController, viewModel)
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, top = 66.dp, bottom = 40.dp, end = 24.dp)
                .align(Alignment.TopCenter),
            content = {
                item {
                    DetailMachineData(viewModel = viewModel, editMode = editMode)
                }
            }
        )
        Row(
            Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            SaveEditButton(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp, end = 8.dp),
                editMode = editMode,
                viewModel = viewModel
            ) { e ->
                editMode = e
            }
            DeleteButton(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp, end = 8.dp),
                editMode = editMode,
                viewModel = viewModel,
                backAction = {
                    back(navHostController, viewModel)
                }
            )
        }
    }

    when (
        val state = viewModel.dataState.collectAsStateWithLifecycle().value
    ) {
        is ResultState.Loading -> {
            Loading()
        }

        is ResultState.Success -> {
            viewModel.setData(state.data as ModelMachineData)
            viewModel.dismiss()
        }

        is ResultState.Error -> {
            Toast.makeText(context, state.e.message, Toast.LENGTH_SHORT).show()
        }

        is ResultState.Content -> {
            // do nothing
        }
    }

    LaunchedEffect(key1 = Unit, block = {
        delay(500)
        viewModel.getDetailData(
            try {
                id.toLong()
            } catch (e: Exception) {
                0
            }
        )
    })
    BackHandler {
        back(navHostController, viewModel)
    }
}

@Composable
private fun SaveEditButton(
    modifier: Modifier,
    editMode: Boolean,
    viewModel: MachineDetailViewModel,
    action: (editMode: Boolean) -> Unit
) {
    Button(
        onClick = {
            if (editMode) {
                action(false)
                viewModel.update {
                    viewModel.getDetailData()
                }
                viewModel.readOnlyTextField(true)
            } else {
                action(true)
                viewModel.readOnlyTextField(false)
            }
        },
        modifier = modifier
    ) {
        Text(text = stringResource(id = if (editMode) R.string.save else R.string.edit))
    }
}

@Composable
private fun DeleteButton(
    modifier: Modifier,
    editMode: Boolean,
    viewModel: MachineDetailViewModel,
    backAction: () -> Unit
) {
    Button(
        onClick = {
            if (editMode) {
                viewModel.deleteImage()
            } else {
                viewModel.deleteData {
                    backAction()
                }
            }
        }, modifier = modifier
    ) {
        Text(text = stringResource(id = if (editMode) R.string.delete_image else R.string.delete))
    }
}

private fun back(navHostController: NavHostController, viewModel: MachineDetailViewModel) {
    CoroutineScope(Dispatchers.Main).launch {
        viewModel.resetData()
        navHostController.clearBackStack(detailMachineDataScreen)
        navHostController.popBackStack(machineDataScreenRoute, false)
        navHostController.toMachineDataScreen()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun DetailMachineData(viewModel: MachineDetailViewModel, editMode: Boolean) {
    val images = remember {
        viewModel.images
    }
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
    Form(state = viewModel.stateValueMachineData, modifier = Modifier.fillMaxWidth())
    OutlinedTextField(
        modifier = Modifier
            .padding(
                top = 8.dp,
                bottom = 8.dp
            ),
        value = viewModel.qrCode.value,
        onValueChange = {
        },
        readOnly = true,
        enabled = false
    )
    CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .height(rowHigh(images))
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
                        modifier = Modifier
                            .height(200.dp)
                            .padding(8.dp),
                    ) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            var checked by remember {
                                mutableStateOf(false)
                            }
                            Image(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .clickable {
                                        if (editMode) {
                                            checked = !checked
                                            if((viewModel.listDeleteImage.find { it == images[pos] }) == null){
                                                viewModel.addToDeleteList(images[pos])
                                            }else{
                                                viewModel.removeFromDeleteList(images[pos])
                                            }
                                        } else {
                                            imagePath = images[pos].path
                                            showImage = true
                                        }
                                    },
                                painter = rememberAsyncImagePainter(
                                    model = images[pos].path,
                                    imageLoader = ImageLoader.Builder(context)
                                        .logger(DebugLogger())
                                        .diskCachePolicy(CachePolicy.DISABLED)
                                        .memoryCachePolicy(CachePolicy.DISABLED)
                                        .build()
                                ),
                                contentDescription = "",
                                contentScale = ContentScale.Crop
                            )
                            if (editMode) {
                                Checkbox(
                                    checked = checked,
                                    onCheckedChange = {
                                        checked = it
                                        if ((viewModel.listDeleteImage.find { d -> d == images[pos] }) == null) {
                                            viewModel.addToDeleteList(images[pos])
                                        } else {
                                            viewModel.removeFromDeleteList(images[pos])
                                        }
                                    },
                                    modifier = Modifier.align(Alignment.TopEnd)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun rowHigh(list: List<*>): Dp {
    return ((if (list.isNotEmpty())
        ceil(
            (if (list.size < 10) list.size.toDouble() + 1
            else
                list.size.toDouble()) / 3.toDouble()
        ).toInt()
    else 1) * 220).dp
}
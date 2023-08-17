package com.jalalkun.imagemachine.ui.machine_data

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.jalalkun.imagemachine.navigation.Navigate.toAddMachineDataScreen
import com.jalalkun.imagemachine.navigation.Navigate.toDetail
import com.jalalkun.imagemachine.navigation.Navigate.toHome
import com.jalalkun.imagemachine.navigation.Route.homeRoute
import com.jalalkun.imagemachine.navigation.Route.machineDataScreenRoute
import com.jalalkun.imagemachine.widget.Header
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun MachineDataScreen(
    navHostController: NavHostController,
    viewModel: MachineDataViewModel
) {
    BackHandler {
        back(navHostController)
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Header(title = "Machine Data") {
            back(navHostController)
        }
        MachineDataContent(
            modifier = Modifier
                .padding(
                    top = 66.dp,
                    bottom = 40.dp,
                    start = 24.dp,
                    end = 24.dp
                )
                .align(Alignment.TopCenter)
                .fillMaxWidth(),
            viewModel = viewModel,
            navHostController = navHostController
        )
        Button(
            onClick = { navHostController.toAddMachineDataScreen() },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            Text(text = "Add Machine Data")
        }
    }

    val coroutine = rememberCoroutineScope()
    LaunchedEffect(key1 = Unit, block = {
        viewModel.getMachineData(FilterType.DEFAULT) {
            coroutine.launch {
                if (viewModel.listMachineData.isEmpty()) navHostController.toAddMachineDataScreen()
            }
        }
    })
}

private fun back(navHostController: NavHostController){
    CoroutineScope(Dispatchers.Main).launch {
        navHostController.clearBackStack(machineDataScreenRoute)
        navHostController.popBackStack(homeRoute, false)
        navHostController.toHome()
    }
}

@Composable
private fun MachineDataContent(
    modifier: Modifier,
    viewModel: MachineDataViewModel,
    navHostController: NavHostController
) {
    val data = remember{
        viewModel.listMachineData
    }
    var expandedFilter by remember { mutableStateOf(false) }
    if (data.isNotEmpty()) LazyColumn(
        modifier = modifier
    ) {
        item {
            Box(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    TextButton(
                        onClick = {
                            expandedFilter = true
                        },
                    ) {
                        Text(text = "Short by")
                    }
                    DropdownMenu(
                        expanded = expandedFilter,
                        onDismissRequest = { expandedFilter = false }) {
                        DropdownMenuItem(
                            text = { Text("Default") },
                            onClick = {
                                viewModel.getMachineData(FilterType.DEFAULT)
                                expandedFilter = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Name Asc") },
                            onClick = {
                                viewModel.getMachineData(FilterType.NAME_ASC)
                                expandedFilter = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Name Desc") },
                            onClick = {
                                viewModel.getMachineData(FilterType.NAME_DESC)
                                expandedFilter = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Type Asc") },
                            onClick = {
                                viewModel.getMachineData(FilterType.TYPE_ASC)
                                expandedFilter = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Type Desc") },
                            onClick = {
                                viewModel.getMachineData(FilterType.TYPE_DESC)
                                expandedFilter = false
                            }
                        )
                    }
                }
            }
        }
        items(
            items = data.toList(),
            key = {
                it.id
            },
            itemContent = {
                OutlinedCard(
                    modifier = Modifier
                        .padding(top = 8.dp, bottom = 8.dp)
                        .clickable {
                            navHostController.toDetail(it.id)
                        }
                ) {
                    Box(
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth()
                    ) {
                        Text(text = it.name, modifier = Modifier.align(Alignment.CenterStart))
                        Text(text = it.type, modifier = Modifier.align(Alignment.Center))
                        Text(
                            text = "${it.images.size} Images",
                            modifier = Modifier.align(Alignment.CenterEnd)
                        )
                    }
                }
            }
        )
    }
}
package com.jalalkun.imagemachine.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.jalalkun.imagemachine.R
import com.jalalkun.imagemachine.navigation.Navigate.toMachineDataScreen
import com.jalalkun.imagemachine.navigation.Navigate.toQrScanner

@Composable
fun HomeScreen(navHostController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(id = R.string.app_name),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 52.dp)
        )

        Row(
            modifier = Modifier.align(Alignment.Center)
        ) {
            OutlinedCard(
                modifier = Modifier
                    .padding(12.dp)
                    .clickable {
                        navHostController.toMachineDataScreen()
                    }
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = R.drawable.outline_image,
                        contentDescription = stringResource(
                            id = R.string.machine_data
                        ),
                        modifier = Modifier.size(42.dp)
                    )
                    Text(text = stringResource(id = R.string.machine_data))
                }
            }
            OutlinedCard(
                modifier = Modifier
                    .padding(12.dp)
                    .clickable {
                        navHostController.toQrScanner()
                    },
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = R.drawable.outline_photo_camera,
                        contentDescription = stringResource(
                            id = R.string.code_reader
                        ),
                        modifier = Modifier.size(42.dp)
                    )
                    Text(text = stringResource(id = R.string.code_reader))
                }
            }
        }
    }
}
package com.jalalkun.imagemachine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jalalkun.imagemachine.navigation.Route.addMachineDataScreen
import com.jalalkun.imagemachine.navigation.Route.detailMachineDataScreen
import com.jalalkun.imagemachine.navigation.Route.homeRoute
import com.jalalkun.imagemachine.navigation.Route.machineDataScreenRoute
import com.jalalkun.imagemachine.navigation.Route.qrScannerScreen
import com.jalalkun.imagemachine.ui.home.HomeScreen
import com.jalalkun.imagemachine.ui.machine_data.MachineDataScreen
import com.jalalkun.imagemachine.ui.machine_data.MachineDataViewModel
import com.jalalkun.imagemachine.ui.machine_data.add_data.AddMachineDataScreen
import com.jalalkun.imagemachine.ui.machine_data.detail.DetailMachineDataScreen
import com.jalalkun.imagemachine.ui.machine_data.detail.MachineDetailViewModel
import com.jalalkun.imagemachine.ui.scanner.QrScannerScreen
import com.jalalkun.imagemachine.ui.scanner.QrScannerViewModel
import com.jalalkun.imagemachine.ui.theme.ImageMachineTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImageMachineTheme {
                // A surface container using the 'background' color from the theme
                SetNavHost()
            }
        }
    }
}

@Composable
private fun SetNavHost(
    detailViewModel: MachineDetailViewModel = koinViewModel(),
    machineDataViewModel: MachineDataViewModel = koinViewModel(),
    qrScannerViewModel: QrScannerViewModel = koinViewModel()
){
    val navHost = rememberNavController()
    NavHost(navController = navHost, startDestination = homeRoute){
        composable(homeRoute){
            HomeScreen(navHostController = navHost)
        }
        composable(machineDataScreenRoute){
            MachineDataScreen(navHostController = navHost, machineDataViewModel)
        }
        composable(addMachineDataScreen){
            AddMachineDataScreen(navHostController = navHost)
        }
        composable(detailMachineDataScreen){
            val id = it.arguments?.getString("id")
            if (id != null) {
                DetailMachineDataScreen(navHostController = navHost, id = id, detailViewModel)
            }
        }
        composable(qrScannerScreen){
            QrScannerScreen(navHostController = navHost, viewModel = qrScannerViewModel)
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ImageMachineTheme {
        Greeting("Android")
    }
}
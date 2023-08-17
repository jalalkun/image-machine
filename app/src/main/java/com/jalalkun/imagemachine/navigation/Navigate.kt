package com.jalalkun.imagemachine.navigation

import androidx.navigation.NavHostController
import com.jalalkun.imagemachine.navigation.Route.addMachineDataScreen
import com.jalalkun.imagemachine.navigation.Route.detailMachineDataScreen
import com.jalalkun.imagemachine.navigation.Route.homeRoute
import com.jalalkun.imagemachine.navigation.Route.machineDataScreenRoute
import com.jalalkun.imagemachine.navigation.Route.qrScannerScreen

object Navigate {
    fun NavHostController.toHome(){
        navigate(homeRoute)
    }

    fun NavHostController.toMachineDataScreen(){
        navigate(machineDataScreenRoute)
    }

    fun NavHostController.toAddMachineDataScreen(){
        navigate(addMachineDataScreen)
    }
    
    fun NavHostController.toDetail(id: Long){
        navigate(detailMachineDataScreen.replace("{id}", "$id"))
    }

    fun NavHostController.toQrScanner(){
        navigate(qrScannerScreen)
    }
}
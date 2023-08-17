package com.jalalkun.imagemachine.di

import com.jalalkun.imagemachine.ui.machine_data.MachineDataViewModel
import com.jalalkun.imagemachine.ui.machine_data.add_data.AddDataViewModel
import com.jalalkun.imagemachine.ui.machine_data.detail.MachineDetailViewModel
import com.jalalkun.imagemachine.ui.scanner.QrScannerViewModel
import org.koin.dsl.module

val viewModelModule = module {
    single { MachineDataViewModel(get()) }
    single { AddDataViewModel(get()) }
    single { QrScannerViewModel(get()) }
    single { MachineDetailViewModel(get()) }
}
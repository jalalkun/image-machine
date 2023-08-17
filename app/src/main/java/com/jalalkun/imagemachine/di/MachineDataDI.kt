package com.jalalkun.imagemachine.di

import androidx.room.Room
import com.jalalkun.imagemachine.data.db.MachineDataDb
import com.jalalkun.imagemachine.data.implementation.ImageMachineRepositoryImpl
import com.jalalkun.imagemachine.data.implementation.MachineDataRepositoryImpl
import com.jalalkun.imagemachine.data.repo.ImageMachineRepository
import com.jalalkun.imagemachine.data.repo.MachineDataRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module

val machineDataDbModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            MachineDataDb::class.java,
            "machine-data-db"
        ).build()
    }
}

val machineDataDaoModule = module {
    includes(machineDataDbModule)
    single { get<MachineDataDb>().machineDataDao() }
    single { get<MachineDataDb>().imageMachineDao() }
}

val machineDataRepoModule = module {
    single { MachineDataRepositoryImpl(get(), get()) } bind MachineDataRepository::class
    single { ImageMachineRepositoryImpl(get()) } bind ImageMachineRepository::class
}
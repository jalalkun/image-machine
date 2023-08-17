package com.jalalkun.imagemachine.data.db

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.jalalkun.imagemachine.data.dao.MachineDataDao
import com.jalalkun.imagemachine.data.entities.ImageMachineEntity
import com.jalalkun.imagemachine.data.entities.MachineDataEntity
import com.jalalkun.imagemachine.utils.DateConverter

@Database(
    entities = [
        MachineDataEntity::class,
        ImageMachineEntity::class
    ],
    version = 1
//    autoMigrations = [
//        AutoMigration (from = 1, to = 2)
//    ]
)
@TypeConverters(DateConverter::class)
abstract class MachineDataDb: RoomDatabase() {
    abstract fun machineDataDao(): MachineDataDao
    abstract fun imageMachineDao(): ImageMachineDao
}
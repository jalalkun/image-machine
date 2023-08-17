package com.jalalkun.imagemachine.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ImageMachineEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val path: String,
    val machineId: Long
){
    companion object{
        fun createInsertData(
            path: String,
            machineId: Long
        ): ImageMachineEntity{
            return ImageMachineEntity(
                path = path,
                machineId = machineId
            )
        }
    }
}
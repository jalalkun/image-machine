package com.jalalkun.imagemachine.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.jalalkun.imagemachine.data.entities.ImageMachineEntity
import com.jalalkun.imagemachine.ui.machine_data.models.ModelImageMachineData

@Dao
interface ImageMachineDao {
    @Insert
    fun insert(imageMachineEntity: ImageMachineEntity): Long

    @Query("DELETE FROM ImageMachineEntity WHERE id = :id")
    fun delete(id: Long)

    @Query("SELECT * FROM ImageMachineEntity WHERE machineId = :machineId")
    fun fetchByMachineId(machineId: Long): List<ImageMachineEntity>
}

fun List<ImageMachineEntity>.toModel(): List<ModelImageMachineData> {
    val list = mutableListOf<ModelImageMachineData>()
    this.forEach {
        list.add(
            ModelImageMachineData(
                id = it.id,
                path = it.path
            )
        )
    }
    return list.toList()
}
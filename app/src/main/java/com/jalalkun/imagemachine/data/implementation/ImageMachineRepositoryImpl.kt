package com.jalalkun.imagemachine.data.implementation

import com.jalalkun.imagemachine.data.db.ImageMachineDao
import com.jalalkun.imagemachine.data.entities.ImageMachineEntity
import com.jalalkun.imagemachine.data.repo.ImageMachineRepository

class ImageMachineRepositoryImpl(private val dao: ImageMachineDao):ImageMachineRepository {
    override fun insert(imageMachineEntiti: ImageMachineEntity): Long {
        TODO("Not yet implemented")
    }

    override fun delete(id: Long) {
        TODO("Not yet implemented")
    }

}
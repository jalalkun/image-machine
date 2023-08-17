package com.jalalkun.imagemachine.data.repo

import com.jalalkun.imagemachine.data.entities.ImageMachineEntity

interface ImageMachineRepository {
    fun insert(imageMachineEntiti: ImageMachineEntity): Long
    fun delete(id: Long)
}
package com.jalalkun.imagemachine.ui.machine_data.models

import java.util.Date

data class ModelMachineData(
    val id: Long,
    val name: String,
    val type: String,
    val qrCodeNumber: Int,
    val updatedAt: Date,
    val images: List<ModelImageMachineData>
){
    companion object{
        fun EMPTY() = ModelMachineData(
            id = 0,
            name = "",
            type = "",
            qrCodeNumber = 0,
            updatedAt = Date(),
            images = listOf()
        )
    }
}

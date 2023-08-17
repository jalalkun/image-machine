package com.jalalkun.imagemachine.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jalalkun.imagemachine.data.db.toModel
import com.jalalkun.imagemachine.ui.machine_data.models.ModelImageMachineData
import com.jalalkun.imagemachine.ui.machine_data.models.ModelMachineData
import java.util.Date

@Entity
data class MachineDataEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val type: String,
    val qrCodeNumber: Int,
    val updatedAt: Date,
){
    companion object{
        fun createInsertData(
            name: String,
            type: String,
            qrCodeNumber: Int,
            date: Date?
        ): MachineDataEntity{
            return MachineDataEntity(
                name = name,
                type = type,
                qrCodeNumber = qrCodeNumber,
                updatedAt = date ?: Date()
            )
        }

        fun createUpdateData(
            history: ModelMachineData
        ): MachineDataEntity{
            return MachineDataEntity(
                id = history.id,
                name = history.name,
                type = history.type,
                qrCodeNumber = history.qrCodeNumber,
                updatedAt = history.updatedAt
            )
        }

        fun MachineDataEntity.toModel(
            images: List<ModelImageMachineData>
        ): ModelMachineData {
            return ModelMachineData(
                id = this.id,
                name = this.name,
                type = this.type,
                qrCodeNumber = this.qrCodeNumber,
                updatedAt = this.updatedAt,
                images = images
            )
        }
    }
}
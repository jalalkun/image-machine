package com.jalalkun.imagemachine.data.repo

import com.jalalkun.imagemachine.data.entities.MachineDataEntity
import com.jalalkun.imagemachine.ui.machine_data.models.ModelImageMachineData
import com.jalalkun.imagemachine.ui.machine_data.models.ModelMachineData
import com.jalalkun.imagemachine.utils.ResultState
import java.util.Date
import kotlinx.coroutines.flow.Flow

interface MachineDataRepository {
    fun insert(machineDataEntity: MachineDataEntity): Long
    fun update(machineDataEntity: MachineDataEntity, listDeleteImage: List<ModelImageMachineData>): Boolean
    fun delete(id: Long): Boolean
    fun fetch(id: Long): Flow<ResultState>
    fun fetchByQr(code: Int): Long
    fun fetchAll(): List<ModelMachineData>
    fun fetchNameAsc(): List<ModelMachineData>
    fun fetchNameDesc(): List<ModelMachineData>
    fun fetchTypeAsc(): List<ModelMachineData>
    fun fetchTypeDesc(): List<ModelMachineData>
    fun saveMachineData(
        name: String,
        type: String,
        date: Date?,
        qrCodeNumber: Int,
        images: List<String>
    ): Flow<ResultState>
}
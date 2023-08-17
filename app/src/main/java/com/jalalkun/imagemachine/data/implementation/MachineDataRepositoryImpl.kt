package com.jalalkun.imagemachine.data.implementation

import android.util.Log
import com.jalalkun.imagemachine.data.dao.MachineDataDao
import com.jalalkun.imagemachine.data.db.ImageMachineDao
import com.jalalkun.imagemachine.data.db.toModel
import com.jalalkun.imagemachine.data.entities.ImageMachineEntity
import com.jalalkun.imagemachine.data.entities.MachineDataEntity
import com.jalalkun.imagemachine.data.entities.MachineDataEntity.Companion.toModel
import com.jalalkun.imagemachine.data.repo.MachineDataRepository
import com.jalalkun.imagemachine.ui.machine_data.models.ModelImageMachineData
import com.jalalkun.imagemachine.ui.machine_data.models.ModelMachineData
import com.jalalkun.imagemachine.utils.ResultState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.Date

class MachineDataRepositoryImpl(
    private val dao: MachineDataDao,
    private val imageDao: ImageMachineDao
) : MachineDataRepository {
    override fun insert(machineDataEntity: MachineDataEntity): Long {
        return dao.insert(machineDataEntity)
    }

    override fun update(
        machineDataEntity: MachineDataEntity,
        listDeleteImage: List<ModelImageMachineData>
    ): Boolean {
        return try {
            dao.update(machineDataEntity)
            listDeleteImage.forEach {
                imageDao.delete(it.id)
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    override fun delete(id: Long): Boolean {
        return try {
            dao.delete(id)
            true
        } catch (e: Exception) {
            false
        }
    }

    override fun fetch(id: Long) = flow {
        emit(ResultState.Loading)
        try {
            val a = dao.fetch(id)
            val b = imageDao.fetchByMachineId(a.id).toModel()
            emit(ResultState.Success(a.toModel(b)))
        } catch (e: Exception) {
            emit(ResultState.Error(e))
        }
    }.flowOn(Dispatchers.IO)

    override fun fetchByQr(code: Int): Long {
        return dao.fetchByQrCode(code)
    }

    override fun fetchAll(): List<ModelMachineData> {
        val list = mutableListOf<ModelMachineData>()
        dao.fetchAll().forEach {
            val b = imageDao.fetchByMachineId(it.id)
            list.add(
                it.toModel(b.toModel())
            )
        }
        return list.toList()
    }

    override fun fetchNameAsc(): List<ModelMachineData> {
        val list = mutableListOf<ModelMachineData>()
        dao.fetchAllAsc().forEach {
            val b = imageDao.fetchByMachineId(it.id)
            list.add(
                it.toModel(b.toModel())
            )
        }
        return list.toList()
    }

    override fun fetchNameDesc(): List<ModelMachineData> {
        val list = mutableListOf<ModelMachineData>()
        dao.fetchAllDesc().forEach {
            val b = imageDao.fetchByMachineId(it.id)
            list.add(
                it.toModel(b.toModel())
            )
        }
        return list.toList()
    }

    override fun fetchTypeAsc(): List<ModelMachineData> {
        val list = mutableListOf<ModelMachineData>()
        dao.fetchAllTypeAsc().forEach {
            val b = imageDao.fetchByMachineId(it.id)
            list.add(
                it.toModel(b.toModel())
            )
        }
        return list.toList()
    }

    override fun fetchTypeDesc(): List<ModelMachineData> {
        val list = mutableListOf<ModelMachineData>()
        dao.fetchAllTypeDesc().forEach {
            val b = imageDao.fetchByMachineId(it.id)
            list.add(
                it.toModel(b.toModel())
            )
        }
        return list.toList()
    }


    override fun saveMachineData(
        name: String,
        type: String,
        date: Date?,
        qrCodeNumber: Int,
        images: List<String>
    ) = flow {
        emit(ResultState.Loading)
        try {
            insert(
                MachineDataEntity.createInsertData(
                    name, type, qrCodeNumber, date
                )
            ).apply {
                images.forEach {
                    imageDao.insert(
                        ImageMachineEntity.createInsertData(
                            it,
                            this
                        )
                    )
                }
            }
            emit(ResultState.Success())
        } catch (e: Exception) {
            ResultState.Error(e)
        }
    }.flowOn(Dispatchers.IO)

}
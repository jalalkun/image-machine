package com.jalalkun.imagemachine.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.jalalkun.imagemachine.data.entities.MachineDataEntity

@Dao
interface MachineDataDao {
    @Insert
    fun insert(machineDataEntity: MachineDataEntity): Long
    @Update
    fun update(machineDataEntity: MachineDataEntity)
    @Query("DELETE FROM MachineDataEntity WHERE id = :id")
    fun delete(id: Long)
    @Query("SELECT * FROM MachineDataEntity WHERE id = :id")
    fun fetch(id: Long): MachineDataEntity
    @Query("SELECT id FROM MachineDataEntity WHERE qrCodeNumber = :code LIMIT 1")
    fun fetchByQrCode(code: Int):Long
    @Query("SELECT * FROM MachineDataEntity")
    fun fetchAll():List<MachineDataEntity>
    @Query("SELECT * FROM MachineDataEntity ORDER BY name DESC")
    fun fetchAllDesc():List<MachineDataEntity>
    @Query("SELECT * FROM MachineDataEntity ORDER BY name ASC")
    fun fetchAllAsc():List<MachineDataEntity>
    @Query("SELECT * FROM MachineDataEntity ORDER BY type DESC")
    fun fetchAllTypeDesc():List<MachineDataEntity>
    @Query("SELECT * FROM MachineDataEntity ORDER BY type ASC")
    fun fetchAllTypeAsc():List<MachineDataEntity>
}
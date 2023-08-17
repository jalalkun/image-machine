package com.jalalkun.imagemachine.ui.machine_data

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.jalalkun.imagemachine.data.repo.MachineDataRepository
import com.jalalkun.imagemachine.ui.machine_data.models.ModelMachineData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MachineDataViewModel(
    private val machineDataRepository: MachineDataRepository,
    private val io: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    companion object {
        private const val TAG = "MachineDataViewModel"
    }

    private val _listMachineData = mutableStateListOf<ModelMachineData>()
    val listMachineData = _listMachineData
    fun getMachineData(filterType: FilterType, done: (() -> Unit)? = null) {
        viewModelScope.launch(io) {
            _listMachineData.clear()
            val a = when(filterType){
                FilterType.DEFAULT -> {
                    machineDataRepository.fetchAll()
                }
                FilterType.NAME_ASC -> {
                    machineDataRepository.fetchNameAsc()
                }
                FilterType.NAME_DESC -> {
                    machineDataRepository.fetchNameDesc()
                }
                FilterType.TYPE_ASC -> {
                    machineDataRepository.fetchTypeAsc()
                }
                FilterType.TYPE_DESC -> {
                    machineDataRepository.fetchTypeDesc()
                }
            }
            _listMachineData.addAll(a)
            if (done != null) {
                done()
            }
        }
    }
}

enum class FilterType {
    DEFAULT, NAME_ASC, NAME_DESC, TYPE_ASC, TYPE_DESC
}
package com.jalalkun.imagemachine.ui.scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jalalkun.imagemachine.data.repo.MachineDataRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class QrScannerViewModel(private val machineDataRepository: MachineDataRepository) : ViewModel() {
    fun findData(
        value: String,
        io: CoroutineDispatcher = Dispatchers.IO,
        dataFound: (id: Long) -> Unit,
        dataNotFound: () -> Unit
    ) {
        viewModelScope.launch(io) {
            val a = machineDataRepository.fetchByQr(
                try {
                    value.toInt()
                } catch (e: Exception) {
                    0
                }
            )
            if (a<1)dataNotFound()
            else dataFound(a)
        }
    }
}
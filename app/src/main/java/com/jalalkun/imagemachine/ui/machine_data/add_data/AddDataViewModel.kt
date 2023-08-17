package com.jalalkun.imagemachine.ui.machine_data.add_data

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jalalkun.imagemachine.R
import com.jalalkun.imagemachine.data.repo.MachineDataRepository
import com.jalalkun.imagemachine.utils.DATE_FORMAT
import com.jalalkun.imagemachine.utils.QrCodeGenerator
import com.jalalkun.imagemachine.utils.ResultState
import com.jalalkun.imagemachine.utils.convertMillisToStringDate
import com.jalalkun.imagemachine.utils.toDate
import com.jalalkun.imagemachine.widget.textForm.FormState
import com.jalalkun.imagemachine.widget.textForm.TextField
import com.jalalkun.imagemachine.widget.textForm.TextFieldName.TF_DATE
import com.jalalkun.imagemachine.widget.textForm.TextFieldName.TF_NAME
import com.jalalkun.imagemachine.widget.textForm.TextFieldName.TF_TYPE
import com.jalalkun.imagemachine.widget.textForm.Validator
import com.jalalkun.imagemachine.widget.textForm.ValidatorState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Date

class AddDataViewModel(
    private val machineDataRepository: MachineDataRepository,
    private val io: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {
    val stateValueMachineData = FormState().apply {
        fields = listFieldMachineData()
    }

    fun clearData() {
        viewModelScope.launch {
            _images.value.clear()
            stateValueMachineData.clear()
        }
    }

    private fun listFieldMachineData() = listOf(
        TextField(
            TF_NAME,
            R.string.name,
            Validator(listOf(ValidatorState.NOT_EMPTY)),
        ),
        TextField(
            TF_TYPE,
            R.string.type,
            Validator(listOf(ValidatorState.NOT_EMPTY)),
        ),
        TextField(
            TF_DATE,
            R.string.date,
            Validator(listOf(ValidatorState.NOT_EMPTY)),
            readOnly = true,
            isDatePicker = true
        )
    )

    private val _images = MutableStateFlow(
        mutableStateListOf<String>()
    )
    val images = _images.asStateFlow()
    fun addImages(list: List<@JvmSuppressWildcards Uri>) {
        list.forEach {
            if (_images.value.size < 10) _images.value.add(it.toString())
        }
    }


    private val _saveDataState = MutableStateFlow<ResultState>(ResultState.Content)
    val saveDataState = _saveDataState.asStateFlow()
    fun dismiss(){
        viewModelScope.launch {
            _saveDataState.emit(ResultState.Content)
        }
    }
    fun saveData(context: Context) {
        viewModelScope.launch(io) {
            if (stateValueMachineData.validate(context)){
                val stateData = stateValueMachineData.getData()
                machineDataRepository.saveMachineData(
                    name = stateData[TF_NAME] ?: "",
                    type = stateData[TF_TYPE] ?: "",
                    date = (stateData[TF_DATE] ?: "").ifEmpty { convertMillisToStringDate(Date().time, DATE_FORMAT) }.toDate(),
                    qrCodeNumber = QrCodeGenerator().generate(),
                    images = _images.value
                ).collect{
                    _saveDataState.emit(it)
                }
            }
        }
    }
}
package com.jalalkun.imagemachine.ui.machine_data.detail

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jalalkun.imagemachine.R
import com.jalalkun.imagemachine.data.entities.MachineDataEntity
import com.jalalkun.imagemachine.data.repo.MachineDataRepository
import com.jalalkun.imagemachine.ui.machine_data.models.ModelImageMachineData
import com.jalalkun.imagemachine.ui.machine_data.models.ModelMachineData
import com.jalalkun.imagemachine.utils.DATE_FORMAT
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

class MachineDetailViewModel(
    private val machineDataRepository: MachineDataRepository
) : ViewModel() {

    val stateValueMachineData = FormState().apply {
        fields = listFieldMachineData()
    }

    private fun listFieldMachineData() = listOf(
        TextField(
            TF_NAME,
            R.string.name,
            Validator(listOf(ValidatorState.NOT_EMPTY)),
            readOnly = true
        ),
        TextField(
            TF_TYPE,
            R.string.type,
            Validator(listOf(ValidatorState.NOT_EMPTY)),
            readOnly = true
        ),
        TextField(
            TF_DATE,
            R.string.date,
            Validator(listOf(ValidatorState.NOT_EMPTY)),
            readOnly = true,
        )
    )
    var qrCode = mutableStateOf(TextFieldValue(""))
    fun setData(data: ModelMachineData) {
        localData = data
        qrCode.value = TextFieldValue(data.qrCodeNumber.toString())
        val hashMap = hashMapOf<String, String>()
        hashMap[TF_NAME] = data.name
        hashMap[TF_TYPE] = data.type
        hashMap[TF_DATE] = convertMillisToStringDate(data.updatedAt.time, DATE_FORMAT)
        stateValueMachineData.setValue(hashMap)
        hashMap.clear()
        images.clear()
        images.addAll(data.images)
    }

    private var localData = ModelMachineData.EMPTY()

    fun resetData(
        io: CoroutineDispatcher = Dispatchers.IO
    ) {
        viewModelScope.launch(io) {
            stateValueMachineData.clear()
            images.clear()
            localData = ModelMachineData.EMPTY()
        }
    }

    private val _dataState = MutableStateFlow<ResultState>(ResultState.Content)
    val dataState = _dataState.asStateFlow()
    val images = mutableStateListOf<ModelImageMachineData>()
    fun getDetailData(id: Long? = null) {
        if (id != null)resetData()
        viewModelScope.launch {
            machineDataRepository.fetch(id ?: localData.id)
                .collect {
                    _dataState.emit(it)
                }
        }
    }

    fun dismiss() {
        viewModelScope.launch {
            _dataState.emit(ResultState.Content)
        }
    }

    fun update(
        io: CoroutineDispatcher = Dispatchers.IO,
        success: (success: Boolean) -> Unit
    ) {
        viewModelScope.launch(io) {
            val fieldData = stateValueMachineData.getData()
            success(
                machineDataRepository.update(
                    MachineDataEntity.createUpdateData(
                        history = ModelMachineData(
                            id = localData.id,
                            name = fieldData[TF_NAME] ?: "",
                            type = fieldData[TF_TYPE] ?: "",
                            updatedAt = (fieldData[TF_DATE].toDate()) ?: Date(),
                            qrCodeNumber = localData.qrCodeNumber,
                            images = images
                        )
                    ),
                    listDeleteImage
                )
            )
        }
    }

    fun deleteImage() {
        viewModelScope.launch {
            listDeleteImage.forEach {
                images.remove(it)
            }
        }
    }

    fun deleteData(
        io: CoroutineDispatcher = Dispatchers.IO,
        success: (success: Boolean) -> Unit
    ) {
        viewModelScope.launch(io) {
            success(
                machineDataRepository.delete(localData.id)
            )
        }
    }

    val listDeleteImage = mutableListOf<ModelImageMachineData>()
    fun addToDeleteList(data: ModelImageMachineData) {
        listDeleteImage.add(data)
    }

    fun removeFromDeleteList(data: ModelImageMachineData) {
        listDeleteImage.remove(data)
    }

    fun readOnlyTextField(readOnly: Boolean) {
        viewModelScope.launch {
            stateValueMachineData.setRead(readOnly)
        }
    }
}
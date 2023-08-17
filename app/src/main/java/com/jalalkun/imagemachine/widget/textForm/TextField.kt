package com.jalalkun.imagemachine.widget.textForm

import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.jalalkun.imagemachine.R
import com.jalalkun.imagemachine.utils.DATE_FORMAT
import com.jalalkun.imagemachine.utils.convertMillisToStringDate
import java.util.Date

class TextField(
    val name: String,
    private val label: Int,
    private val validator: Validator,
    private val keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    readOnly: Boolean = false,
    private val isDatePicker: Boolean = false,
    private val isSingleLine: Boolean = false
) {
    var text by mutableStateOf(TextFieldValue(""))
    private var error by mutableStateOf(false)
    private var listError = mutableListOf<String>()
    var justRead = readOnly


    @Composable
    fun Content(
        value: TextFieldValue,
        readOnly: Boolean = justRead
    ) {
        if (isDatePicker) {
            DatePickerField()
        } else {
            if (value.text == "null") text = TextFieldValue("")
            OutlinedTextField(
                value = value,
                onValueChange = {
                    hideError()
                    text = it
                },
                label = {
                    Text(text = stringResource(id = label))
                },
                modifier = Modifier
                    .padding(
                        top = 8.dp,
                        bottom = 8.dp
                    )
                ,
                isError = error,
                supportingText = {
                    listError.forEach { Text(text = it) }
                },
                readOnly = readOnly,
                enabled = true,
                keyboardOptions = keyboardOptions,
                singleLine = isSingleLine
            )

        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun DatePickerField() {
        var showDatePicker by remember {
            mutableStateOf(false)
        }
        var data by remember {
            mutableStateOf(TextFieldValue(""))
        }
        val state = rememberDatePickerState(initialDisplayMode = DisplayMode.Picker)
        if (showDatePicker) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            data = TextFieldValue(
                                convertMillisToStringDate(
                                    state.selectedDateMillis ?: Date().time,
                                    DATE_FORMAT
                                )
                            )
                            text = data
                            showDatePicker = false
                            hideError()
                        }
                    ) {
                        Text(stringResource(id = R.string.save))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text(
                            text = stringResource(id = R.string.cancel),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            ) {
                DatePicker(state = state)
            }
        }
        val interactionSource = remember { MutableInteractionSource() }
        val isPressed: Boolean by interactionSource.collectIsPressedAsState()
        LaunchedEffect(isPressed) {
            if (isPressed) {
                showDatePicker = true
            }
        }
        OutlinedTextField(
            value = data,
            onValueChange = {
                hideError()
            },
            label = {
                Text(text = stringResource(id = label))
            },
            modifier = Modifier
                .padding(
                    top = 8.dp,
                    bottom = 8.dp
                )
                .clickable { showDatePicker = true }
            ,
            isError = error,
            supportingText = {
                listError.forEach { Text(text = it) }
            },
            readOnly = true,
            enabled = true,
            keyboardOptions = keyboardOptions,
            interactionSource = interactionSource,
            trailingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.baseline_today_24),
                    contentDescription = ""
                )
            }
        )
    }

    fun setTextDefault(string: String) {
        text = TextFieldValue(string)
    }

    fun setReadOnly(readOnly: Boolean) {
        justRead = readOnly
    }

    fun clear() {
        text = TextFieldValue("")
        error = false
        listError.clear()
    }

    private fun hideError() {
        error = false
        listError.clear()
    }

    fun validate(context: Context, valid: (Boolean) -> Unit) {
        validator.isValid(text.text, context) { lists ->
            listError.clear()
            listError.addAll(lists)
            error = listError.isNotEmpty()
            valid(listError.isEmpty())
        }
    }
}
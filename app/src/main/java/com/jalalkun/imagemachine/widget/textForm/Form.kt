package com.jalalkun.imagemachine.widget.textForm

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier


@Composable
fun Form(modifier: Modifier = Modifier, state: FormState) {
    state.fields.distinctBy { it.name }
    val list by remember {
        mutableStateOf(state.fields)
    }
    Column(modifier = modifier) {
        list.forEach {
            it.Content(
                value = it.text
            )
        }
    }
}

class FormState {

    var fields: List<TextField> = listOf()

    fun validate(context: Context): Boolean {
        var valid = true
        fields.forEach {
            it.validate(context) { b ->
                if (valid) valid = b
            }
        }
        return valid
    }

    fun clear() {
        fields.forEach {
            it.clear()
        }
    }

    fun clear(list: List<String>) {
        list.forEach { n ->
            val a = fields.find {
                it.name == n
            }
            a?.clear()
        }
    }

    fun setValue(hashMap: HashMap<String, String>) {
        fields.forEach {
            it.setTextDefault(hashMap[it.name].toString())
        }
    }

    fun setRead(hashMap: HashMap<String, Boolean>) {
        fields.forEach {
            val a = hashMap[it.name]
            if (it.justRead != a) a?.let { it1 -> it.setReadOnly(it1) }
        }
    }

    fun setRead(readOnly : Boolean){
        fields.forEach {
            it.setReadOnly(readOnly = readOnly)
        }
    }

    fun getData(): Map<String, String> = fields.associate { it.name to it.text.text }
}
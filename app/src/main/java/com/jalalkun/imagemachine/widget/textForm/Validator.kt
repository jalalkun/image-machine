package com.jalalkun.imagemachine.widget.textForm

import android.content.Context
import com.jalalkun.imagemachine.R

enum class ValidatorState {
    NOT_EMPTY
}

class Validator(
    private val validations: List<ValidatorState>) {
    fun isValid(
        input: String,
        context: Context,
        result: (List<String>) -> Unit
    ) {
        result(ValidationField(input, validations, context).messages())
    }
}

class ValidationField(
    input: String,
    private val validations: List<ValidatorState>,
    context: Context
) {
    private val notEmptyValidator = NotEmptyValidator(
        input = input,
        messageError = context.getString(R.string.validator_cant_empty)
    )


    fun messages(): List<String> {
        val messages = mutableListOf<String>()
        validations.forEach {
            when (it) {
                ValidatorState.NOT_EMPTY -> notEmptyValidator.apply {
                    this.isValid()
                    if (this.message.isNotEmpty()) messages.add(this.message)
                }
            }
        }
        return messages
    }
}

abstract class InputValidator(
    private val input: String?,
    private val messageError: String,
) {
    var message: String = ""
    open fun isValid(): Boolean {
        if (!check(input)) {
            message = String.format(messageError)
            return false
        }
        message = ""
        return true
    }

    abstract fun check(input: String?): Boolean
}

private class NotEmptyValidator(input: String?, messageError: String) : InputValidator(
    input, messageError
) {
    override fun check(input: String?): Boolean {
        return !input.isNullOrEmpty()
    }
}


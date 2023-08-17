package com.jalalkun.imagemachine.utils

sealed class ResultState{
    data object Loading : ResultState()
    class Success(val data: Any? = null) : ResultState()
    class Error(val e: Throwable) : ResultState()
    data object Content : ResultState()
}

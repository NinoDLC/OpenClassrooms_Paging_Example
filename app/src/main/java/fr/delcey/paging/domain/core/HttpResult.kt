package fr.delcey.paging.domain.core

sealed class HttpResult<out T> {

    data class Success<out T>(val data: T) : HttpResult<T>()

    sealed class HttpError : HttpResult<Nothing>() {
        object IO : HttpError()
        object Critical : HttpError()
    }
}

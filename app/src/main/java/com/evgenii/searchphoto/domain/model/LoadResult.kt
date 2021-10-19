package com.evgenii.searchphoto.domain.model

sealed class LoadResult

class SuccessResult<T>(
    val data: T
) : LoadResult()

class ErrorResult(
    val throwable: Throwable
) : LoadResult()

object PendingResult : LoadResult()
object EmptyResult : LoadResult()


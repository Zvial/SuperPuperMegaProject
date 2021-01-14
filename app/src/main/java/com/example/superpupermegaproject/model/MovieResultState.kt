package com.example.superpupermegaproject.model

sealed class MovieResultState {
    object Sucess: MovieResultState()
    object Loading: MovieResultState()
    object LoadingNextPage: MovieResultState()
    class Error(val t: Throwable): MovieResultState()
    class ErrorNextPage(val t: Throwable): MovieResultState()
    object TerminalError: MovieResultState()
}
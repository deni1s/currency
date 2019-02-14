package com.example.currency.utils.mvp

interface BaseView<out T : BasePresenter<*>> {

    fun showError(error: String)

    val presenter: T
}
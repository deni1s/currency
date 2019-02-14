package com.example.currency.utils.mvp

interface BasePresenter<V> {

    fun subscribe(view: V) {
        this.view = view
    }

    fun unSubscribe()

    var view : V?

}
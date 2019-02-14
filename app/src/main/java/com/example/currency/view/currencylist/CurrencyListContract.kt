package com.example.currency.view.currencylist

import com.example.currency.models.CurrencyPair
import com.example.currency.utils.mvp.BasePresenter
import com.example.currency.utils.mvp.BaseView


interface CurrencyListContract {
    interface View : BaseView<Presenter> {
        fun updateCurrencyList(currencyPairList: List<CurrencyPair>)
        fun updateCurrencyList(currencyPairList: List<CurrencyPair>, firstVisiblePosition : Int, lastVisiblePosition: Int)
        fun showProgressBar()
        fun hideProgressBar()
    }

    interface Presenter : BasePresenter<View> {
        fun loadCurrencyList()
        fun reloadData()
        fun updateCurrencyPairs(currencyPairs : List<CurrencyPair>, firstVisiblePosition : Int, lastVisiblePosition : Int)
    }
}
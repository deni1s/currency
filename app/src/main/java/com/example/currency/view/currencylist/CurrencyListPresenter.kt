package com.example.currency.view.currencylist

import com.example.currency.api.CurrencyApiInterface
import com.example.currency.models.CurrencyPair
import com.example.currency.utils.mvp.BasePresenter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class CurrencyListPresenter(val coroutineScope: CoroutineScope, val currencyApiInterface: CurrencyApiInterface) :
    BasePresenter<CurrencyListContract.View>, CurrencyListContract.Presenter {

    override fun unSubscribe() {
        view = null
        job.cancel()
    }

    override var view: CurrencyListContract.View? = null
    private lateinit var job: Job


    override fun loadCurrencyList() {
        if (isLoading || isAllLoaded) {
            return
        }
        loadData()
    }

    var isLoading: Boolean = false
    private var isAllLoaded: Boolean = false

    override fun reloadData() {
        isAllLoaded = false
        loadData()
    }

    private fun loadData() {
        job = coroutineScope.launch(Dispatchers.Main) {
            isLoading = true
            view!!.showProgressBar()
            try {
                val currencyPairList = currencyApiInterface.loadCurrencyPairs().await()
                view!!.updateCurrencyList(currencyPairList)
                view!!.hideProgressBar()
            } catch (e: Exception) {
                view!!.hideProgressBar()
                view!!.showError(e.localizedMessage)
            }
            isLoading = false
        }
    }

    override fun updateCurrencyPairs(currencyPairs: List<CurrencyPair>, firstVisiblePosition: Int, lastVisiblePosition: Int) {
        view!!.showProgressBar()
        val listToUpdate = prepareCurrencyPairStringToUpdate(currencyPairs)
        if (!listToUpdate.isEmpty()) {
            job = coroutineScope.launch(Dispatchers.Main) {
                isLoading = true
                try {
                    val currencyPairList = currencyApiInterface.loadCurrencyPairs(listToUpdate).await()
                    view!!.updateCurrencyList(currencyPairList, firstVisiblePosition, lastVisiblePosition)
                    view!!.hideProgressBar()
                } catch (e: Exception) {
                    view!!.hideProgressBar()
                    view!!.showError(e.localizedMessage)
                }
            }
        }
    }

    private fun wasUpdatedMoreThanTwoSecondsAgo(currencyPair: CurrencyPair): Boolean {
        return currencyPair.getSecondsPassed() > 2
    }

    private fun prepareCurrencyPairStringToUpdate(currencyPairs: List<CurrencyPair>): String {
        val listToUpdate = arrayListOf<String>()
        currencyPairs.forEach {
            if (wasUpdatedMoreThanTwoSecondsAgo(it)) {
                listToUpdate.add(it.symbol)
            }
        }
        return listToUpdate.joinToString(",")
    }

}
package com.example.currency.view.currencylist

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SimpleItemAnimator
import android.view.View
import android.widget.Toast
import com.example.currency.R
import com.example.currency.models.CurrencyPair
import com.example.currency.utils.coroutines.EventBus
import com.example.currency.utils.view.ItemDecoration
import kotlinx.android.synthetic.main.currency_list_activity.*
import kotlinx.android.synthetic.main.currency_list_content.*
import kotlinx.coroutines.GlobalScope
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf
import java.util.*


class CurrencyListActivity : AppCompatActivity(), CurrencyListContract.View {

    private lateinit var currencyPairAdapter: CurrencyRecyclerViewAdapter
    override val presenter: CurrencyListContract.Presenter by inject { parametersOf(GlobalScope) }
    val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.currency_list_activity)
        presenter.subscribe(this)
        prepareViews()
    }

    private fun prepareViews() {
        setSupportActionBar(toolbar)
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerViewCurrency.layoutManager = linearLayoutManager
        currencyPairAdapter = CurrencyRecyclerViewAdapter(this)
        recyclerViewCurrency.adapter = currencyPairAdapter
        (recyclerViewCurrency.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        recyclerViewCurrency!!.addOnScrollListener(getScrollListener(linearLayoutManager))

        val itemDecoration = ItemDecoration(this, R.drawable.divider_drawable)
        recyclerViewCurrency.addItemDecoration(itemDecoration)
        presenter.loadCurrencyList()
    }

    private fun getScrollListener(linearLayoutManager: LinearLayoutManager): RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    updateRecyclerViewContent(linearLayoutManager)
                }
            }
        }
    }

    private fun updateRecyclerViewContent(linearLayoutManager: LinearLayoutManager) {
        val firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition()
        val lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition()
        if (firstVisibleItemPosition >= 0) {
            val pairListToUpdate = arrayListOf<CurrencyPair>()
            for (position in firstVisibleItemPosition..lastVisibleItemPosition) {
                pairListToUpdate.add(currencyPairAdapter.getItem(position))
            }
            presenter.updateCurrencyPairs(pairListToUpdate, firstVisibleItemPosition, lastVisibleItemPosition)
        }
    }

    override fun updateCurrencyList(
        currencyPairList: List<CurrencyPair>,
        firstVisiblePosition: Int,
        lastVisiblePosition: Int
    ) {
        setNewCurrencyPairToList(
            currencyPairList,
            currencyPairAdapter.currencyPairList,
            firstVisiblePosition,
            lastVisiblePosition
        )
        currencyPairAdapter.notifyItemRangeChanged(firstVisiblePosition, lastVisiblePosition)
    }

    private fun setNewCurrencyPairToList(
        newCurrencyPairList: List<CurrencyPair>, fullPairList: ArrayList<CurrencyPair>, firstVisiblePosition: Int,
        lastVisiblePosition: Int
    ) {
//        newCurrencyPairList.forEach {
//            for (position in firstVisiblePosition..lastVisiblePosition) {
//                if (it.equals(this.currencyPairList.get(position))) {
//                    this.currencyPairList.set(position, it)
//                    break
//                }
//            }
//        }
        for (position in firstVisiblePosition..lastVisiblePosition) {
            val currencyPairPositionInNewList = position - firstVisiblePosition
            if (newCurrencyPairList.size > currencyPairPositionInNewList) {
                val newCurrencyPair = newCurrencyPairList.get(currencyPairPositionInNewList)
                if (fullPairList.get(position).equals(newCurrencyPair)) {
                    fullPairList.set(position, newCurrencyPair)
                }
            } else {
                break
            }
        }
    }

    override fun updateCurrencyList(currencyPairList: List<CurrencyPair>) {
        currencyPairAdapter.setCurrencyPairList(currencyPairList)
        startUpdateTimeHandler()
    }

    override fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    override fun showError(error: String) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
    }

    fun startUpdateTimeHandler() {
        val delay: Long = 1000 //1000 milliseconds = 1 sec
        handler.postDelayed(object : Runnable {
            override fun run() {
                // update time passed in adapter
                EventBus.send(EventSecondPassed())
                handler.postDelayed(this, delay)
            }
        }, delay)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.unSubscribe()
        stopUpdateTimeHandler()
    }

    fun stopUpdateTimeHandler() {
        handler.removeMessages(0)
    }
}

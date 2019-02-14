package com.example.currency.view.currencylist

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.example.currency.R
import com.example.currency.models.CurrencyPair
import com.example.currency.utils.coroutines.EventBus
import com.example.currency.view.currencylist.viewholders.CurrencyPairViewHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch


class CurrencyRecyclerViewAdapter(context: Context) : RecyclerView.Adapter<CurrencyPairViewHolder>() {

    private var context: Context

    init {
        this.context = context
    }

    var currencyPairList = ArrayList<CurrencyPair>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyPairViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_holder_currency_pair, parent, false)
        return CurrencyPairViewHolder(view)
    }

    override fun onBindViewHolder(holder: CurrencyPairViewHolder, position: Int) {
        val item = currencyPairList[position]
        var diffTime = item.getSecondsPassed()
        holder.textViewLastUpdate.text = String.format(context.getString(R.string.string_was_upated), diffTime)
        holder.textViewAsk.text = String.format(context.getString(R.string.string_ask), item.ask)
        holder.textViewBid.text = String.format(context.getString(R.string.string_bid), item.bid)
        holder.textViewCurrencyPrice.text = item.price.toString()
        holder.textViewCurrencyTitle.text = item.symbol

        if (holder.timeSubscription != null) {
            holder.timeSubscription!!.cancel()
            holder.timeSubscription = null
        }
        holder.timeSubscription = EventBus.asChannel<EventSecondPassed>()
        GlobalScope.launch(Dispatchers.Main) {
            holder.timeSubscription!!.consumeEach { event ->
                diffTime = diffTime + 1
                updateTimePassed(holder.textViewLastUpdate, diffTime)
            }
        }
    }

    private fun updateTimePassed(textViewLastUpdate: TextView, diffTime: Long) {
        (context as Activity).runOnUiThread {
            textViewLastUpdate.text =
                    String.format(context.getString(R.string.string_was_upated), diffTime)
        }
    }

    override fun getItemCount(): Int = currencyPairList.size

    fun setCurrencyPairList(list: List<CurrencyPair>) {
        this.currencyPairList.addAll(list)
        notifyDataSetChanged()
    }

    fun getItem(position: Int): CurrencyPair {
        return currencyPairList.get(position)
    }
}

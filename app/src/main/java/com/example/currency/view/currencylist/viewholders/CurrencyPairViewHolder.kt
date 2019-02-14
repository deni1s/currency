package com.example.currency.view.currencylist.viewholders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.example.currency.R
import com.example.currency.view.currencylist.EventSecondPassed
import kotlinx.coroutines.channels.ReceiveChannel

class CurrencyPairViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val textViewCurrencyTitle: TextView
    val textViewCurrencyPrice: TextView
    val textViewLastUpdate: TextView
    val textViewBid: TextView
    val textViewAsk: TextView
    var timeSubscription : ReceiveChannel<EventSecondPassed>? = null

    init {
        textViewCurrencyTitle = itemView.findViewById(R.id.text_view_currency_title)
        textViewCurrencyPrice = itemView.findViewById(R.id.text_view_currency_price)
        textViewLastUpdate = itemView.findViewById(R.id.text_view_last_update)
        textViewBid = itemView.findViewById(R.id.text_view_bid)
        textViewAsk = itemView.findViewById(R.id.text_view_ask)
    }
}
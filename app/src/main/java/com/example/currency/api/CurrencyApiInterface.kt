package com.example.currency.api

import com.example.currency.models.CurrencyPair
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApiInterface {

    @GET("quotes?api_key=dnLFRdMvscUXe4teduTHEypvdSLsgg29")
    fun loadCurrencyPairs(): Deferred<List<CurrencyPair>>

    @GET("quotes?api_key=dnLFRdMvscUXe4teduTHEypvdSLsgg29")
    fun loadCurrencyPairs(@Query("pairs") pairs: String): Deferred<List<CurrencyPair>>
}
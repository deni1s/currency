package com.example.currency.modules


import com.example.currency.api.CurrencyApiInterface
import com.example.currency.view.currencylist.CurrencyListContract
import com.example.currency.view.currencylist.CurrencyListPresenter
import com.example.currency.utils.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.CoroutineScope
import okhttp3.OkHttpClient

import org.koin.dsl.module.module
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val viewModule = module {
    single { createJsonApi(getNewHttpClient()) }
    factory { (coroutineScope: CoroutineScope) ->
        CurrencyListPresenter(
            coroutineScope,
            get()
        ) as CurrencyListContract.Presenter
    }
}

fun getNewHttpClient(): OkHttpClient {
    val client = OkHttpClient.Builder()
        .followRedirects(true)
        .followSslRedirects(true)
        .retryOnConnectionFailure(true)
        .cache(null)
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)

    return client.build()
}

fun createJsonApi(okHttpClient: OkHttpClient): CurrencyApiInterface {

    val retrofit = retrofit2.Retrofit.Builder()
        .baseUrl("https://forex.1forge.com/1.0.3/")
        .client(okHttpClient)
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    return retrofit.create(CurrencyApiInterface::class.java)
}
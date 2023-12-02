package com.example.currency.converter.di

import androidx.room.Room
import com.example.currency.converter.R
import com.example.currency.converter.data.db.AppDb
import com.example.currency.converter.data.db.DbCallback
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val DB_NAME = "database.db"

val systemModule = module {

    single { GsonBuilder().create() }

    single {

        Room.databaseBuilder(androidApplication(), AppDb::class.java, DB_NAME)
            .addCallback(DbCallback())
            .build()

    }

    single {

        val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }

        val httpClient = OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()

        Retrofit.Builder()
            .baseUrl(androidContext().getString(R.string.base_url))
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }
}
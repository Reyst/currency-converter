package com.example.currency.converter.di

import com.example.currency.converter.data.db.AppDb
import org.koin.dsl.module

val dbModule = module {
    factory { get<AppDb>().accountDao() }
    factory { get<AppDb>().ratesDao() }
}
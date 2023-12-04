package com.example.currency.converter.di

import com.example.currency.converter.data.repositories.DefaultAccountRepository
import com.example.currency.converter.data.repositories.DefaultOperationRepository
import com.example.currency.converter.data.repositories.DefaultRatesRepository
import com.example.currency.converter.data.repositories.LocalAccountDataSource
import com.example.currency.converter.data.repositories.LocalOperationDataSource
import com.example.currency.converter.data.repositories.LocalRatesDataSource
import com.example.currency.converter.data.repositories.RemoteRatesDataSource
import com.example.currency.converter.data.repositories.RetrofitRatesDataSource
import com.example.currency.converter.data.repositories.RoomAccountDataSource
import com.example.currency.converter.data.repositories.RoomOperationDataSource
import com.example.currency.converter.data.repositories.RoomRatesDataSource
import com.example.currency.converter.domain.repositories.AccountRepository
import com.example.currency.converter.domain.repositories.OperationRepository
import com.example.currency.converter.domain.repositories.RatesRepository
import org.koin.dsl.module

val dataSourceModule = module {

    factory<LocalAccountDataSource> { RoomAccountDataSource(get()) }
    factory<LocalRatesDataSource> { RoomRatesDataSource(get()) }
    factory<RemoteRatesDataSource> { RetrofitRatesDataSource(get()) }
    factory<LocalOperationDataSource> { RoomOperationDataSource(get()) }

}

val repositoryModule = module {

    includes(dataSourceModule)

    single<AccountRepository> { DefaultAccountRepository(get()) }
    single<RatesRepository> { DefaultRatesRepository(get(), get()) }
    single<OperationRepository> { DefaultOperationRepository(get())  }
}


package com.example.currency.converter.mappers

import com.example.currency.converter.data.db.entities.CurrencyRateDbEntity

fun Map<String, Double>.toDbEntities() =
    entries.map { (key, value) -> CurrencyRateDbEntity(key, value) }
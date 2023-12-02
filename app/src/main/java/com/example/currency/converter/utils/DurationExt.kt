package com.example.currency.converter.utils

import kotlin.time.DurationUnit
import kotlin.time.toDuration

fun Int.seconds() = toDuration(DurationUnit.SECONDS)
package com.example.currency.converter.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

val ViewGroup.inflater: LayoutInflater
    get() = LayoutInflater.from(context)

fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
    return inflater.inflate(layoutId, this, attachToRoot)
}

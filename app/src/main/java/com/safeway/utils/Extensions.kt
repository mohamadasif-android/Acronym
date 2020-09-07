package com.safeway.utils

import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

fun TextView.getTextChangedStateFlow(): StateFlow<String> {
    val query = MutableStateFlow("")

    doOnTextChanged { text, start, before, count ->
        query.value = text.toString()
    }

    return query
}
package com.gregrdm.accukipweatherme.ui.utils

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

fun <T : ViewDataBinding> Fragment.bindData(inflater: LayoutInflater, container: ViewGroup?, @LayoutRes layout: Int): T =
    DataBindingUtil.inflate<T>(inflater, layout, container, false).apply { lifecycleOwner = this@bindData }

fun matchesWithMyRegex(input: String): Boolean {
    return input.matches(Regex("([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z]+)"))
}

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

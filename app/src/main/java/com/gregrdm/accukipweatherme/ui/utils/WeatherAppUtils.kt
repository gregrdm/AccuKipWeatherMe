package com.gregrdm.accukipweatherme.ui.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

fun <T : ViewDataBinding> Fragment.bindData(inflater: LayoutInflater, container: ViewGroup?, @LayoutRes layout: Int): T =
    DataBindingUtil.inflate<T>(inflater, layout, container, false).apply { lifecycleOwner = this@bindData }

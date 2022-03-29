package com.bink.localhero.utils

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseViewHolder<T>(viewDataBinding: ViewBinding) :
    RecyclerView.ViewHolder(viewDataBinding.root) {
    open fun bind(item: T) {}
    open fun bind(item: T, isLastItem: Boolean) {}
}

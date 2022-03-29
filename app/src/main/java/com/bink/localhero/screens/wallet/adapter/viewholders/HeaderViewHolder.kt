package com.bink.localhero.screens.wallet.adapter.viewholders

import com.bink.localhero.databinding.HeaderItemBinding
import com.bink.localhero.utils.BaseViewHolder

class HeaderViewHolder(
    private val binding: HeaderItemBinding
) :
    BaseViewHolder<String>(binding) {

    override fun bind(title: String) {
        with(binding) {
            tvTitle.text = title
        }

    }

}
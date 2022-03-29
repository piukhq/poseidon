package com.bink.localhero.screens.wallet.adapter.viewholders

import android.view.View
import com.bink.localhero.databinding.PaymentItemBinding
import com.bink.localhero.model.wallet.PaymentCard
import com.bink.localhero.utils.BaseViewHolder

class PaymentCardViewHolder(
    private val binding: PaymentItemBinding,
    private val onClickListener: (Any) -> Unit = {}
) :
    BaseViewHolder<PaymentCard>(binding) {

    override fun bind(item: PaymentCard, isLastItem: Boolean) {
        with(binding) {
            tvNameOnCard.text = item.nameOnCard ?: item.cardNickname
            tvLastFourDigits.text = "**** ${item.lastFourDigits}"
            tvProvider.text = item.provider

            root.setOnClickListener {
                onClickListener(item)
            }

            vwSplitter.visibility = if(isLastItem) View.INVISIBLE else View.VISIBLE
        }

    }

}

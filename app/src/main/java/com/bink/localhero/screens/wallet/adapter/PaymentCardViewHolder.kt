package com.bink.localhero.screens.wallet.adapter

import com.bink.localhero.databinding.PaymentItemBinding
import com.bink.localhero.model.wallet.PaymentCard
import com.bink.localhero.utils.BaseViewHolder

class PaymentCardViewHolder(
    val binding: PaymentItemBinding,
    val onClickListener: (Any) -> Unit = {}
) :
    BaseViewHolder<PaymentCard>(binding) {

    override fun bind(item: PaymentCard) {
        with(binding){
            tvNameOnCard.text = item.nameOnCard
            tvExpiry.text = "${item.expiryMonth}/${item.expiryYear}"
            tvStatus.text = item.status

            root.setOnClickListener {
                onClickListener(item)
            }
        }

    }

}

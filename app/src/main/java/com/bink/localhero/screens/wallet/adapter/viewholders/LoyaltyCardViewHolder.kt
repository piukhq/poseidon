package com.bink.localhero.screens.wallet.adapter.viewholders

import android.view.View
import com.bink.localhero.databinding.LoyaltyItemBinding
import com.bink.localhero.model.wallet.LoyaltyCard
import com.bink.localhero.utils.BaseViewHolder

class LoyaltyCardViewHolder(
    private val binding: LoyaltyItemBinding,
    private val onClickListener: (Any) -> Unit = {}
) :
    BaseViewHolder<LoyaltyCard>(binding) {

    override fun bind(item: LoyaltyCard, isLastItem: Boolean) {
        with(binding) {
            tvLoyaltyCardName.text = item.loyaltyPlanName
            tvLoyaltyCardBalance.text = item.balance?.currentDisplayValue ?: "N/A"

            root.setOnClickListener {
                onClickListener(item)
            }

            vwSplitter.visibility = if (isLastItem) View.INVISIBLE else View.VISIBLE
        }

    }

}
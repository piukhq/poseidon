package com.bink.localhero.screens.wallet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bink.localhero.databinding.HeaderItemBinding
import com.bink.localhero.databinding.LoyaltyItemBinding
import com.bink.localhero.databinding.PaymentItemBinding
import com.bink.localhero.model.wallet.*
import com.bink.localhero.screens.wallet.adapter.viewholders.HeaderViewHolder
import com.bink.localhero.screens.wallet.adapter.viewholders.LoyaltyCardViewHolder
import com.bink.localhero.screens.wallet.adapter.viewholders.PaymentCardViewHolder
import com.bink.localhero.utils.BaseViewHolder
import com.bink.localhero.utils.PAYMENT_HEADER

class UserWalletAdapter(
    val onClickListener: (Any) -> Unit = {}
) :
    RecyclerView.Adapter<BaseViewHolder<*>>() {

    companion object {
        private const val JOIN = 0
        private const val LOYALTY_TITLE = 1
        private const val LOYALTY = 2
        private const val PAYMENT_TITLE = 3
        private const val PAYMENT = 4
    }

    private var userWallet: UserWallet? = null

    fun setData(userWallet: UserWallet) {
        this.userWallet = userWallet
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        userWallet?.asList()?.get(position)?.let { item ->
            return when (item) {
                is String -> {
                    if (item == PAYMENT_HEADER) {
                        PAYMENT_TITLE
                    } else {
                        LOYALTY_TITLE
                    }
                }
                is Join -> JOIN
                is LoyaltyCard -> LOYALTY
                else -> {
                    PAYMENT
                }
            }
        }

        return PAYMENT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            PAYMENT_TITLE, LOYALTY_TITLE -> HeaderViewHolder(
                HeaderItemBinding.inflate(inflater, parent, false)
            )

            PAYMENT -> PaymentCardViewHolder(
                PaymentItemBinding.inflate(inflater, parent, false), onClickListener
            )

            else -> {
                LoyaltyCardViewHolder(
                    LoyaltyItemBinding.inflate(inflater, parent, false), onClickListener
                )
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        userWallet?.asList()?.let { wallet ->
            wallet[position].let {
                when (holder) {
                    is HeaderViewHolder -> holder.bind(it as String)
                    is PaymentCardViewHolder -> holder.bind(
                        (it as PaymentCard),
                        isLastItem(position)
                    )
                    is LoyaltyCardViewHolder -> holder.bind(
                        (it as LoyaltyCard),
                        isLastItem(position)
                    )
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return userWallet?.asList()?.size ?: 0
    }

    private fun isLastItem(pos: Int): Boolean {
        try {
            userWallet?.asList()?.get(pos)?.let {
                return when (it) {
                    is LoyaltyCard -> {
                        (userWallet?.loyaltyCards?.last()?.id == it.id)
                    }
                    else -> {
                        (userWallet?.paymentAccounts?.last()?.id == (it as PaymentCard).id)
                    }
                }
            }
        } catch (e: IndexOutOfBoundsException) {
            return false
        }


        return false
    }

}
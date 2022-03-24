package com.bink.localhero.screens.wallet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bink.localhero.databinding.PaymentItemBinding
import com.bink.localhero.model.wallet.*
import com.bink.localhero.utils.BaseViewHolder

class UserWalletAdapter(
    val onClickListener: (Any) -> Unit = {}
) :
    RecyclerView.Adapter<BaseViewHolder<*>>() {

    companion object {
        private const val JOIN = 0

        private const val LOYALTY = 1

        private const val PAYMENT = 2
    }

    private var userWallet: UserWallet? = null

    fun setData(userWallet: UserWallet) {
        this.userWallet = userWallet
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            //TODO: Add Join and Loyalty viewholders
            JOIN -> PaymentCardViewHolder(
                PaymentItemBinding.inflate(inflater, parent, false), onClickListener
            )

            LOYALTY -> PaymentCardViewHolder(
                PaymentItemBinding.inflate(inflater, parent, false), onClickListener
            )

            else -> {
                PaymentCardViewHolder(
                    PaymentItemBinding.inflate(inflater, parent, false), onClickListener
                )
            }
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        userWallet?.asList()?.let { wallet ->
            wallet[position].let {
                when (holder) {
                    is PaymentCardViewHolder -> holder.bind(it as PaymentCard)
                }
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        userWallet?.asList()?.get(position)?.let { item ->
            return when (item) {
                is Join -> JOIN
                is LoyaltyCard -> LOYALTY
                else -> {
                    PAYMENT
                }
            }
        }

        return PAYMENT
    }

    override fun getItemCount(): Int {
        val size = userWallet?.asList()?.size ?: 0
        return size
    }

    override fun getItemId(position: Int) = position.toLong()

}
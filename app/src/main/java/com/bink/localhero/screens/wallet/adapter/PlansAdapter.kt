package com.bink.localhero.screens.wallet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bink.localhero.databinding.PlanItemBinding
import com.bink.localhero.model.loyalty_plan.LoyaltyPlan

class PlansAdapter(var planItems: List<LoyaltyPlan>) :
    RecyclerView.Adapter<PlansAdapter.WalletViewHolder>() {

    fun setData(plans: List<LoyaltyPlan>) {
        planItems = plans
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WalletViewHolder {
        val binding = PlanItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WalletViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WalletViewHolder, position: Int) {
        holder.bind(planItems[position])
    }

    override fun getItemCount(): Int {
        return planItems.size
    }

    inner class WalletViewHolder(private val binding: PlanItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(loyaltyPlan: LoyaltyPlan) {
            binding.tvPlanName.text = loyaltyPlan.planDetails?.planName
        }
    }

}
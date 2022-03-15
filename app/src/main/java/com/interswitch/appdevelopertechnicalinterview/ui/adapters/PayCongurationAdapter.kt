package com.interswitch.appdevelopertechnicalinterview.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.interswitch.appdevelopertechnicalinterview.data.model.PayConfiguration
import com.interswitch.appdevelopertechnicalinterview.databinding.SinglePayConfigurationBinding

class PayCongurationAdapter(val context: Context, var list: MutableList<PayConfiguration>)
    : RecyclerView.Adapter<PayCongurationAdapter.PayConfigHolder>() {

    inner class PayConfigHolder(val binding: SinglePayConfigurationBinding): RecyclerView.ViewHolder(binding.root) {
        fun setItemData(payConfiguration: PayConfiguration) {

            binding.source.text = payConfiguration.source
            binding.payType.text = payConfiguration.payType
            binding.payValue.text = payConfiguration.payValue.toString()
            binding.maximumFeeBorn.text = payConfiguration.maximumFeeBorn.toString()
            binding.minimumFeeBorn.text = payConfiguration.minimumFeeBorn.toString()
            binding.bandCode.text = payConfiguration.bandCode
            binding.hasExcise.text = payConfiguration.hasExcise.toString()
            binding.isPayVAT.text = payConfiguration.isPayVAT.toString()
            binding.hasWithholdingTax.text = payConfiguration.hasWithholdingTax.toString()
            binding.hasServiceCharge.text = payConfiguration.hasServiceCharge.toString()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PayConfigHolder {
        return PayConfigHolder(SinglePayConfigurationBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: PayConfigHolder, position: Int) {
        holder.setItemData(list[position])
    }

    override fun getItemCount(): Int = list.size
}
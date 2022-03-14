package com.interswitch.appdevelopertechnicalinterview.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.interswitch.appdevelopertechnicalinterview.data.model.FeeGroup
import com.interswitch.appdevelopertechnicalinterview.databinding.SingleFeeGroupBinding

class FeeGroupAdapter(val context: Context, var list: MutableList<FeeGroup>): RecyclerView.Adapter<FeeGroupAdapter.FeeGroupHolder>() {

    inner class FeeGroupHolder(val binding: SingleFeeGroupBinding): RecyclerView.ViewHolder(binding.root) {
        fun setItemData(group: FeeGroup) {
            binding.name.text = group.name
            binding.description.text = group.description
            binding.issueDate.text = group.issueDate
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeeGroupHolder {
        return FeeGroupHolder(SingleFeeGroupBinding.inflate(LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: FeeGroupHolder, position: Int) {
        holder.setItemData(list[position])
    }

    override fun getItemCount(): Int = list.size
}
package com.example.codechallenge.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.codechallenge.model.RatesModel
import com.market.codechallenge.databinding.ListItemRatesBinding

class RatesAdapter(private var rates: ArrayList<RatesModel>) : RecyclerView.Adapter<RatesAdapter.RatesViewHolder>() {

    private var exchangeRate: Double = 1.0
    private var amount: Double = 1.0

    fun setExchange(exchangeRate: Double) {
        this.exchangeRate = exchangeRate
        notifyDataSetChanged()
    }

    fun setAmount(amount: Double) {
        this.amount = amount
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatesViewHolder {
        return RatesViewHolder(ListItemRatesBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return rates.size
    }

    override fun onBindViewHolder(holder: RatesViewHolder, position: Int) {
        holder.bind(rates[position])
    }

    inner class RatesViewHolder(var binding: ListItemRatesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(ratesModel: RatesModel) {
            with(binding) {
                tvName.text = ratesModel.name
                tvRate.text = ((amount * ratesModel.exchangeRate / exchangeRate).toString())
            }
        }
    }
}
package com.app.mvvm.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.mvvm.databinding.AdapterCountryCodeBinding
import com.app.mvvm.model.response.CountryCodes

class CountryCodeAdapter(
    val activity: Activity, var list: ArrayList<CountryCodes.CountryCodesItem>
) : RecyclerView.Adapter<CountryCodeAdapter.ViewHolder>() {
    class ViewHolder(val b: AdapterCountryCodeBinding) : RecyclerView.ViewHolder(b.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val b =
            AdapterCountryCodeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(b)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.b.tvCountryNCode.text = data.code
        holder.b.tvCountryName.text = data.name
        holder.b.tvCountryCode.text = data.dial_code
    }

    fun filterList(filterList: ArrayList<CountryCodes.CountryCodesItem>) {
        list = filterList
        notifyDataSetChanged()
    }
}
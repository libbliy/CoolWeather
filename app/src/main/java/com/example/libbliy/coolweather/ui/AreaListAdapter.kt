package com.example.libbliy.coolweather.ui

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.libbliy.coolweather.databinding.AreaListItemBinding

/**
 * Created by libbly on 2018/3/20.
 */
class AreaListAdapter(val list: List<String>, val onClickListener: (View, Int) -> Unit) : RecyclerView.Adapter<AreaListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = AreaListItemBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding.root)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = DataBindingUtil.getBinding<AreaListItemBinding>(holder.mItemView!!)
        binding!!.text = list[position]
        holder.mItemView.setOnClickListener {
            onClickListener(holder.mItemView, position)
        }
    }

    class ViewHolder(val mItemView: View?) : RecyclerView.ViewHolder(mItemView)
}
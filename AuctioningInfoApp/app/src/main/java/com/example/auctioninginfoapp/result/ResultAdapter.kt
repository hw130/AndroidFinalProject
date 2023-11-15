package com.example.auctioninginfoapp.result

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.auctioninginfoapp.databinding.ListItemFreshBinding
import com.example.auctioninginfoapp.model.FreshData
import com.example.auctioninginfoapp.search.SearchAdapter

class ResultAdapter : RecyclerView.Adapter<ItemViewHolder>() {

    var freshList: List<FreshData> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ListItemFreshBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ItemViewHolder,
        position: Int
    ) {
        holder.bindItems(freshList[position])
    }

    override fun getItemCount(): Int = freshList.size


}

class SaveAdapter : PagingDataAdapter<FreshData, ItemViewHolder>(DIFF_CALLBACK){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ListItemFreshBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bindItems(getItem(position))
        Log.d("GETITEM", "${getItem(position)}, SaveAdapter")
    }

    companion object{
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FreshData>(){
            override fun areItemsTheSame(oldItem: FreshData, newItem: FreshData): Boolean = oldItem.id == newItem.id


            override fun areContentsTheSame(oldItem: FreshData, newItem: FreshData): Boolean = oldItem.id == newItem.id

        }
    }
}

class ItemViewHolder(private val binding: ListItemFreshBinding) : RecyclerView.ViewHolder(binding.root){
    fun bindItems(fresh: FreshData?){
        fresh?.let {
            binding.txtGongpanInfo.text = fresh.cprName
            binding.txtUnit.text = "${fresh.mname} > ${fresh.sname} (등급:${fresh.grade})"
            binding.txtMinPrice.text = "최저: " + String.format("%,d", fresh.minPrice.toInt())
            binding.txtMaxPrice.text = "최고: " + String.format("%,d", fresh.maxPrice.toInt())
            binding.txtAvgPrice.text = "거래량: " + String.format("%,d", fresh.tradeAmt.toInt()) + fresh.uname
        }
    }
}
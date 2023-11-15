package com.example.auctioninginfoapp.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.auctioninginfoapp.R
import com.example.auctioninginfoapp.databinding.FragmentSearchBinding
import com.example.auctioninginfoapp.databinding.ListItemSaveitemBinding
import com.example.auctioninginfoapp.model.FreshDao
import com.example.auctioninginfoapp.model.SaveItem

class SearchAdapter(val freshDao: FreshDao) : PagingDataAdapter<SaveItem, SearchAdapter.ItemViewHolder>(DIFF_CALLBACK) {
    override fun onBindViewHolder(holder: SearchAdapter.ItemViewHolder, position: Int) {
        holder.bindItems(getItem(position))
        Log.d("GETITEM", "${getItem(position)}, SearchAdapter")
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        val binding = ListItemSaveitemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    inner class ItemViewHolder(private val binding: ListItemSaveitemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bindItems(saveItem: SaveItem?){
            binding.txtSaveSubject.text = saveItem?.saveTitle

            binding.btnDelete.setOnClickListener {
                saveItem?.id?.let { freshDao.deleteSaveData(it) }
            }

            binding.txtSaveSubject.setOnClickListener {
                Navigation.findNavController(itemView).navigate(
                    R.id.action_searchFragment_to_saveFragment,
                    Bundle().apply {
                        putLong("SAVE_ID", saveItem!!.id!!)
                    }
                )
            }
        }
    }

    companion object{
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<SaveItem>(){
            override fun areContentsTheSame(oldItem: SaveItem, newItem: SaveItem): Boolean = oldItem.id == newItem.id


            override fun areItemsTheSame(oldItem: SaveItem, newItem: SaveItem): Boolean = oldItem.id == newItem.id

        }
    }
}
package com.naufal.kidsnesia.main_features.presentation.nota.detail_nota.merch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.naufal.kidsnesia.databinding.ItemDetailNotaEventBinding
import com.naufal.kidsnesia.databinding.ItemDetailNotaMerchBinding
import com.naufal.kidsnesia.main_features.data.source.remote.response.DetailMerchItem
import com.naufal.kidsnesia.main_features.data.source.remote.response.DetailNotaEventItem
import com.naufal.kidsnesia.main_features.data.source.remote.response.NotaPembelianMerch

class DetailNotaMerchAdapter : RecyclerView.Adapter<DetailNotaMerchAdapter.ViewHolder> (){

    private val items = mutableListOf<DetailMerchItem>()

    fun submitList(data: List<DetailMerchItem?>) {
        items.clear()
        items.addAll(data.filterNotNull())
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemDetailNotaMerchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DetailMerchItem) {
            binding.tvNamaMerch.text = "${item.namaMerch}"
            binding.tvJumlah.text = "   ${item.jumlahMerch}"
            binding.tvHarga.text = "${item.hargaMerch}"
            binding.tvSubtotal.text = "${item.subtotalMerch}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDetailNotaMerchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])
    override fun getItemCount(): Int = items.size
}

package com.naufal.kidsnesia.purchase.presentation.cart.merch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.naufal.kidsnesia.databinding.ItemMerchCartBinding
import com.naufal.kidsnesia.purchase.data.source.remote.response.ListCartMerchItem

class MerchCartAdapter(
    private val list: List<ListCartMerchItem>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<MerchCartAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemMerchCartBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListCartMerchItem) {
            val firstItem = item.cartMerchItem?.firstOrNull()

            binding.tvNamaMerch.text = firstItem?.namaMerch ?: "-"
            binding.tvJumlahBarang.text = "x${firstItem?.jumlahMerch ?: 0}"
            binding.tvTotal.text = "Rp ${firstItem?.hargaMerch ?: 0}"
            binding.tvStatus.text = item.statusPembelianMerch ?: "-"

            Glide.with(itemView.context)
                .load(firstItem?.fotoMerchandise)
                .into(binding.ivMerchImage)

            itemView.setOnClickListener {
                item.idPembelianMerch?.let {
                    onItemClick(it.toString())
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMerchCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}


package com.naufal.kidsnesia.main_features.presentation.nota.nota_merch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.naufal.kidsnesia.R
import com.naufal.kidsnesia.databinding.ItemNotaMerchBinding
import com.naufal.kidsnesia.main_features.data.source.remote.response.ListNotaPembelianMerchItem

class NotaMerchAdapter(
    private val onItemClick: (ListNotaPembelianMerchItem) -> Unit
) : RecyclerView.Adapter<NotaMerchAdapter.ViewHolder>() {

    private val items = mutableListOf<ListNotaPembelianMerchItem>()

    fun submitList(data: List<ListNotaPembelianMerchItem>) {
        items.clear()
        items.addAll(data)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemNotaMerchBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListNotaPembelianMerchItem) {
            binding.tvTanggal.text = item.tanggalPembelianMerch ?: "-"
            val status = item.statusPembayaranMerch ?: "-"
            binding.tvStatus.text = "$status"
            val context = binding.root.context
            when (status.lowercase()) {
                "berhasil" -> binding.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.hijau_status))
                "gagal" -> binding.tvStatus.setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark))
                "menunggu verifikasi" -> binding.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.kuning_status))
                else -> binding.tvStatus.setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray))
            }
            binding.tvTotal.text = "Rp ${item.totalPembelianMerch ?: 0}"

            binding.root.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNotaMerchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount() = items.size
}

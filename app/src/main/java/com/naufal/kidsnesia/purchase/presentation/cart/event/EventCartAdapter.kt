package com.naufal.kidsnesia.purchase.presentation.cart.event

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.naufal.kidsnesia.databinding.ItemEventCartBinding
import com.naufal.kidsnesia.purchase.data.source.remote.response.ListEventCartItem

class EventCartAdapter(
    private val list: List<ListEventCartItem>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<EventCartAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemEventCartBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListEventCartItem) {
            val firstItem = item.cartEventItem?.firstOrNull()

            binding.tvNamaEvent.text = firstItem?.namaEvent ?: "-"
            binding.tvJumlahTiket.text = "x${firstItem?.jumlahTiket ?: 0}"
            binding.tvTotal.text = "Rp ${item.totalPembelianEvent ?: 0}"
            binding.tvStatus.text = item.statusPembelianEvent ?: "-"

            Glide.with(itemView.context)
                .load(firstItem?.fotoEvent)
                .into(binding.ivEventImage)

            itemView.setOnClickListener {
                item.idPembelianEvent?.let {
                    onItemClick(it.toString())
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEventCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}


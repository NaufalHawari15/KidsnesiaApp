package com.naufal.kidsnesia.main_features.presentation.nota.detail_nota.event

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.naufal.kidsnesia.databinding.ItemDetailNotaEventBinding
import com.naufal.kidsnesia.main_features.data.source.remote.response.DetailNotaEventItem

class DetailNotaEventAdapter : RecyclerView.Adapter<DetailNotaEventAdapter.ViewHolder>() {

    private val items = mutableListOf<DetailNotaEventItem>()

    fun submitList(data: List<DetailNotaEventItem?>) {
        items.clear()
        items.addAll(data.filterNotNull())
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemDetailNotaEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DetailNotaEventItem) {
            binding.tvNamaEvent.text = "${item.namaEvent}"
            binding.tvTanggalEvent.text = "${item.tanggalEvent}"
            binding.tvJadwal.text = "${item.jadwalEvent} WIB"
            binding.tvJumlah.text = "   ${item.jumlahTiket}"
            binding.tvHarga.text = "${item.hargaEvent}"
            binding.tvSubtotal.text = "${item.subtotalEvent}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemDetailNotaEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])
    override fun getItemCount(): Int = items.size
}

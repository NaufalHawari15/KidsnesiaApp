package com.naufal.kidsnesia.main_features.presentation.event

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.naufal.kidsnesia.R
import com.naufal.kidsnesia.main_features.data.source.remote.response.ListEventItem

class EventAdapter(
    private val events: List<ListEventItem>,
    private val onItemClick: (ListEventItem) -> Unit) :
    RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    inner class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgEvent: ImageView = view.findViewById(R.id.imgEvent)
        val tvEventName: TextView = view.findViewById(R.id.tvEventName)
        val tvEventPrice: TextView = view.findViewById(R.id.tvEventPrice)
        val tvEventKuota: TextView = view.findViewById(R.id.tvEventKuota)

        fun bind(event: ListEventItem) {
            tvEventName.text = event.namaEvent
            tvEventPrice.text = "Harga: ${event.hargaEvent ?: "Gratis"}"
            tvEventKuota.text = "Kuota: ${event.kuota ?: "-"}"

            Glide.with(itemView.context)
                .load(event.fotoEvent)
                .placeholder(R.drawable.ic_maskot_kidsnesia)
                .into(imgEvent)

            itemView.setOnClickListener { onItemClick(event) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount() = events.size
}


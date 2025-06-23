package com.naufal.kidsnesia.main_features.presentation.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.naufal.kidsnesia.R
import com.naufal.kidsnesia.main_features.data.source.remote.response.ListMerchandiseItem

class MerchandiseAdapter(
    private val productList: List<ListMerchandiseItem>,
    private val onClick: (ListMerchandiseItem) -> Unit
) : RecyclerView.Adapter<MerchandiseAdapter.MerchandiseViewHolder>() {

    class MerchandiseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageProduct: ImageView = itemView.findViewById(R.id.imageProduct)
        val textProductName: TextView = itemView.findViewById(R.id.textProductName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MerchandiseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_merchandise, parent, false)
        return MerchandiseViewHolder(view)
    }

    override fun onBindViewHolder(holder: MerchandiseViewHolder, position: Int) {
        val product = productList[position]
        holder.textProductName.text = product.namaMerchandise

        // Load gambar dari URL (gunakan Glide atau Picasso)
        Glide.with(holder.itemView.context)
            .load(product.fotoMerchandise)
            .placeholder(R.drawable.ic_maskot_kidsnesia)
            .into(holder.imageProduct)

        holder.itemView.setOnClickListener {
            onClick(product)
        }
    }

    override fun getItemCount(): Int = productList.size
}


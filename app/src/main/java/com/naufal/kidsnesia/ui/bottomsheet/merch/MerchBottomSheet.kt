package com.naufal.kidsnesia.ui.bottomsheet.merch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.naufal.kidsnesia.R
import com.naufal.kidsnesia.databinding.BottomSheetMerchBinding

class MerchBottomSheet(
    private val imageUrl: String?,
    private val maxMerch: Int,
    private val onPesanClickWithCallback: (Int, () -> Unit, (String) -> Unit) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetMerchBinding? = null
    private val binding get() = _binding!!
    private var merchCount = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetMerchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme

    override fun onStart() {
        super.onStart()
        val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.layoutParams?.height = ViewGroup.LayoutParams.MATCH_PARENT
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(requireContext())
            .load(imageUrl)
            .placeholder(R.drawable.ic_maskot_kidsnesia)
            .into(binding.ivBottomMerchImage)

        binding.tvMerchCount.text = "Jumlah Produk: $merchCount"
        updatePesanButtonState()

        binding.btnPlus.setOnClickListener {
            if (merchCount < maxMerch) {
                merchCount++
                binding.tvMerchCount.text = "Jumlah Produk: $merchCount"
                updatePesanButtonState()
            } else {
                Toast.makeText(requireContext(), "Jumlah produk melebihi kuota", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnMinus.setOnClickListener {
            if (merchCount > 0) {
                merchCount--
                binding.tvMerchCount.text = "Jumlah Produk: $merchCount"
                updatePesanButtonState()
            }
        }

        binding.btnPesan.setOnClickListener {
            showLoading(true)
            onPesanClickWithCallback(
                merchCount,
                {
                    showLoading(false)
                    dismiss()
                },
                {
                    showLoading(false)
                    Toast.makeText(requireContext(), "Gagal: $it", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loadingOverlayBottom.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun updatePesanButtonState() {
        val enabled = merchCount > 0
        binding.btnPesan.isEnabled = enabled

        // Ganti warna background & teks berdasarkan status
        binding.btnPesan.setBackgroundColor(
            if (enabled) resources.getColor(R.color.sky_blue, null)
            else resources.getColor(android.R.color.darker_gray, null)
        )
        binding.btnPesan.setTextColor(
            if (enabled) resources.getColor(android.R.color.white, null)
            else resources.getColor(android.R.color.black, null)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
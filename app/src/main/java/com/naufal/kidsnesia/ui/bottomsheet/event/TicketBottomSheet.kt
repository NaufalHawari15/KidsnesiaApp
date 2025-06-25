package com.naufal.kidsnesia.ui.bottomsheet.event

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.naufal.kidsnesia.R
import com.naufal.kidsnesia.databinding.BottomSheetTicketEventBinding

class TicketBottomSheet(
    private val imageUrl: String?,
    private val maxKuota: Int,
    private val onPesanClickWithCallback: (Int, () -> Unit, (String) -> Unit) -> Unit
) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetTicketEventBinding? = null
    private val binding get() = _binding!!
    private var ticketCount = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetTicketEventBinding.inflate(inflater, container, false)
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
            .into(binding.ivBottomEventImage)

        binding.tvTicketCount.text = "Jumlah Tiket: $ticketCount"
        updatePesanButtonState()

        binding.btnPlus.setOnClickListener {
            if (ticketCount < maxKuota) {
                ticketCount++
                binding.tvTicketCount.text = "Jumlah Tiket: $ticketCount"
                updatePesanButtonState()
            } else {
                Toast.makeText(requireContext(), "Jumlah tiket melebihi kuota", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnMinus.setOnClickListener {
            if (ticketCount > 0) {
                ticketCount--
                binding.tvTicketCount.text = "Jumlah Tiket: $ticketCount"
                updatePesanButtonState()
            }
        }

        binding.btnPesan.setOnClickListener {
            showLoading(true)
            onPesanClickWithCallback(
                ticketCount,
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
        val enabled = ticketCount > 0
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


package com.naufal.kidsnesia.ui.bottomsheet.membership

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.naufal.kidsnesia.R
import com.naufal.kidsnesia.auth.data.source.local.AuthLocalDataSource
import com.naufal.kidsnesia.databinding.BottomSheetMembershipBinding
import com.naufal.kidsnesia.purchase.data.source.remote.response.PilihBankMembershipRequest
import com.naufal.kidsnesia.purchase.domain.usecase.PurchaseUseCase
import com.naufal.kidsnesia.purchase.presentation.transaksi.membership.TransaksiMembershipActivity
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MembershipBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetMembershipBinding? = null
    private val binding get() = _binding!!

    private val authLocalDataSource: AuthLocalDataSource by inject()
    private val purchaseUseCase: PurchaseUseCase by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetMembershipBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnJoinNow.setOnClickListener {
            val intent = Intent(requireContext(), TransaksiMembershipActivity::class.java)
            startActivity(intent)
            dismiss() // agar BottomSheet tertutup
        }

        binding.btnClose.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
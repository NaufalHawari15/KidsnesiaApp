package com.naufal.kidsnesia.main_features.presentation.video

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.naufal.kidsnesia.R
import com.naufal.kidsnesia.databinding.FragmentVideoBinding

class VideoFragment : Fragment() {

    private var _binding: FragmentVideoBinding? = null
    private val binding get() = _binding!!

    private var isMember: Boolean = false // Ganti ini dengan logika pengecekan sesungguhnya

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoBinding.inflate(inflater, container, false)

        if (isMember) {
            binding.contentLayout.visibility = View.VISIBLE
            binding.lockedLayout.visibility = View.GONE
        } else {
            binding.contentLayout.visibility = View.GONE
            binding.lockedLayout.visibility = View.VISIBLE

            binding.btnDaftarMembership.setOnClickListener {
                Toast.makeText(requireContext(), "Arahkan ke halaman daftar membership", Toast.LENGTH_SHORT).show()
                // Bisa pakai Navigation component atau Intent ke activity daftar
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}


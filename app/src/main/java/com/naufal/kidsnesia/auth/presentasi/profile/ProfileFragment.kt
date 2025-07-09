package com.naufal.kidsnesia.auth.presentasi.profile

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.LiveData
import com.naufal.kidsnesia.MainActivity
import com.naufal.kidsnesia.R
import com.naufal.kidsnesia.auth.data.Resource
import com.naufal.kidsnesia.auth.domain.model.UserModel
import com.naufal.kidsnesia.ui.about.AboutActivity
import com.naufal.kidsnesia.databinding.FragmentProfileBinding
import com.naufal.kidsnesia.main_features.presentation.nota.NotaActivity
import com.naufal.kidsnesia.ui.welcome.WelcomeActivity
import org.koin.android.ext.android.get

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel = get()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.profile.observe(viewLifecycleOwner) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    binding.progressBarProfile.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBarProfile.visibility = View.GONE
                    resource.data?.message?.let { pelanggan ->
                        binding.textNameProfile.text = pelanggan.namaPelanggan ?: "N/A"
                        binding.textEmailProfile.text = pelanggan.email ?: "N/A"
                        binding.textPhoneProfile.text = pelanggan.noHpPelanggan ?: "N/A"
                    }
                }
                is Resource.Error -> {
                    binding.progressBarProfile.visibility = View.GONE
                    Toast.makeText(requireContext(), resource.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        setupAction()
    }

    private fun setupAction() {
        binding.buttonAbout.setOnClickListener {
            startActivity(Intent(requireContext(), AboutActivity::class.java))
        }

        binding.buttonFollowedEvent.setOnClickListener {
            startActivity(Intent(requireContext(), NotaActivity::class.java))
        }

        binding.buttonExit.setOnClickListener {
            viewModel.logout()
            startActivity(Intent(requireContext(), WelcomeActivity::class.java))
            requireActivity().finish() // Menutup aktivitas agar tidak bisa kembali dengan tombol back
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

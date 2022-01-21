package com.marchbreeze.scheder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.marchbreeze.scheder.databinding.FragmentOwnerRequestBinding

class OwnerRequestFragment : Fragment() {
    lateinit var binding: FragmentOwnerRequestBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOwnerRequestBinding.inflate(inflater, container, false)
        return binding.root
    }
}
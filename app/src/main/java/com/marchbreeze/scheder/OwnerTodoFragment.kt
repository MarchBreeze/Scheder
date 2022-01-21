package com.marchbreeze.scheder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.marchbreeze.scheder.databinding.FragmentOwnerTodoBinding

class OwnerTodoFragment : Fragment() {
    lateinit var binding: FragmentOwnerTodoBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOwnerTodoBinding.inflate(inflater, container, false)
        return binding.root
    }
}
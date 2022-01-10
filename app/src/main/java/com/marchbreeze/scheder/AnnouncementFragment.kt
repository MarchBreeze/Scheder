package com.marchbreeze.scheder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.marchbreeze.scheder.databinding.FragmentAnnouncementBinding

class AnnouncementFragment : Fragment() {
    lateinit var binding: FragmentAnnouncementBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAnnouncementBinding.inflate(inflater, container, false)
        return binding.root
    }
}
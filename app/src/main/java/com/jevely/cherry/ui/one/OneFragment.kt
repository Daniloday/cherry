package com.jevely.cherry.ui.one

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.jevely.cherry.R
import com.jevely.cherry.databinding.FragmentOneBinding
import com.jevely.cherry.ui.game.GameViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class OneFragment : Fragment(R.layout.fragment_one) {

    private val binding by viewBinding(FragmentOneBinding::bind)
    private val oneViewModel : OneViewModel by viewModel()
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_one, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        oneViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.playButton.setOnClickListener {
            findNavController().navigate(R.id.action_nav_one_to_nav_game)
        }
    }
}
package com.jevely.cherry.ui.result

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
import com.jevely.cherry.databinding.FragmentGameBinding
import com.jevely.cherry.databinding.FragmentOneBinding
import com.jevely.cherry.databinding.FragmentResultBinding
import com.jevely.cherry.ui.game.GameViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

const val KEY1 = "key1"
const val KEY2 = "key2"
const val KEY3 = "key3"

class ResultFragment : Fragment() {

    private val resultViewModel : ResultViewModel by viewModel()
    private val binding by viewBinding(FragmentResultBinding::bind)

    var first : Int? = null
    var second : Int? = null
    var third : Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            first = getInt(KEY1)
            second = getInt(KEY2)
            third = getInt(KEY3)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_result, container, false)
        val textView: TextView = root.findViewById(R.id.text_slideshow)
        resultViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageView1.setImageResource(getImage(first!!))
        binding.imageView2.setImageResource(getImage(second!!))
        binding.imageView3.setImageResource(getImage(third!!))
        if (first == second && third == second)
            binding.textResult.text = "Победа!!!"
        else
            binding.textResult.text = "Проигрыш!"
        binding.restartButton.setOnClickListener {
            findNavController().navigate(R.id.action_nav_result_to_nav_one)
            resultViewModel.using()
        }
    }

    companion object{
        fun newInstance(first : Int, second : Int, third : Int) = Bundle().apply {
            putInt(KEY1,first)
            putInt(KEY2,second)
            putInt(KEY3,third)
        }
    }

    fun getImage(id : Int): Int {
        return when(id){
            1 -> R.drawable.f
            2 -> R.drawable.s
            3 -> R.drawable.t
            else -> R.drawable.fo
        }
    }
}
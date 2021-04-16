package com.jevely.cherry.ui.game

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.CountDownTimer
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
import com.jevely.cherry.ui.result.ResultFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class GameFragment : Fragment(R.layout.fragment_game) {


    private val binding by viewBinding(FragmentGameBinding::bind)
    var timer : CountDownTimer? = null
    var first : Int? = null
    var second : Int? = null
    var third : Int? = null
    private val viewModel : GameViewModel by viewModel()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        timer = object : CountDownTimer(1000*1000.toLong()!!, 100){
            override fun onFinish() {
                //todo string to values

            }

            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                first = (1..4).random()
                second = (1..4).random()
                third = (1..4).random()
                binding.imageView1.setImageResource(getImage(first!!))
                binding.imageView2.setImageResource(getImage(second!!))
                binding.imageView3.setImageResource(getImage(third!!))
            }
        }
        (timer as CountDownTimer).start()
        binding.stopButton.setOnClickListener {
            timer?.cancel()
            findNavController().navigate(R.id.action_nav_game_to_nav_result,ResultFragment.newInstance(first!!,second!!,third!!))
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
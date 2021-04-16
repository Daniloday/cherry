package com.jevely.cherry.ui.game

import android.support.v4.os.IResultReceiver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jevely.cherry.repository.IScoreRepository

class GameViewModel(val repository: IScoreRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is gallery Fragment"
    }
    val text: LiveData<String> = _text
}
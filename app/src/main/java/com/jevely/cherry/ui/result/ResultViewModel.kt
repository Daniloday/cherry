package com.jevely.cherry.ui.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jevely.cherry.repository.IScoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ResultViewModel(val repository: IScoreRepository) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is slideshow Fragment"
    }
    val text: LiveData<String> = _text

    fun using(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll(5,"chev")
            repository.deleteScore()
            repository.geScoreId(15)
            repository.getScores()
        }
    }
}
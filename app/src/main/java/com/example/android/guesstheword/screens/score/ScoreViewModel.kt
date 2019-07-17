package com.example.android.guesstheword.screens.score

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScoreViewModel(finalScoreParameter: Int): ViewModel() {
    private val _finalScore = MutableLiveData<Int>()
    val finalScore: LiveData<Int>
        get() = _finalScore

    private val _tryAgain = MutableLiveData<Boolean>()
    val tryAgain: LiveData<Boolean>
        get() = _tryAgain

    init {
        _finalScore.value = finalScoreParameter
        _tryAgain.value = false
    }

    fun tryAgainButton(){
        _tryAgain.value = true
    }
    fun tryAgainHandled(){
        _tryAgain.value = false
    }
}
package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.util.TimeUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import timber.log.Timber


private val CORRECT_BUZZ_PATTERN = longArrayOf(100, 100, 100, 100, 100, 100)
private val PANIC_BUZZ_PATTERN = longArrayOf(0, 200)
private val GAME_OVER_BUZZ_PATTERN = longArrayOf(0, 2000)
private val NO_BUZZ_PATTERN = longArrayOf(1)

enum class BuzzType(val pattern: LongArray) {
    CORRECT(CORRECT_BUZZ_PATTERN),
    GAME_OVER(GAME_OVER_BUZZ_PATTERN),
    COUNTDOWN_PANIC(PANIC_BUZZ_PATTERN),
    NO_BUZZ(NO_BUZZ_PATTERN)
}

class GameViewModel: ViewModel() {

    companion object{
        const val DONE = 0L
        const val ONE_SECOND = 1000L
        const val COUNTDOWN_TIME = 10000L
    }


    // The current word
    private val _word = MutableLiveData<String>()
    val word: LiveData<String>
        get() = _word

    // The current score
    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    private val _eventGameFinished = MutableLiveData<Boolean>()
    val eventGameFinished: LiveData<Boolean>
        get() = _eventGameFinished

    private val _eventCorrect = MutableLiveData<Boolean>()
    val eventCorrect: LiveData<Boolean>
        get() = _eventCorrect

    private val _eventSkip = MutableLiveData<Boolean>()
    val eventSkip: LiveData<Boolean>
        get() = _eventSkip

    private val _eventTime = MutableLiveData<Boolean>()
    val eventTime: LiveData<Boolean>
        get() = _eventTime

    private val _currentTime = MutableLiveData<Long>()
    val currentTime: LiveData<String> = Transformations.map(_currentTime) {
        DateUtils.formatElapsedTime(it)
    }

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    private val timer: CountDownTimer

    init{

        Timber.i("GameViewModel created!")
        resetList()
        nextWord()
        _score.value = 0
        _eventCorrect.value = false
        _eventSkip.value = false
        _eventGameFinished.value = false
        _eventTime.value = false
        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND){
            override fun onTick(millisUntilFinished: Long) {
                if(millisUntilFinished<6000L)_eventTime.value = true
                _currentTime.value = millisUntilFinished/1000
            }

            override fun onFinish() {
                _eventGameFinished.value = true
            }
        }
        timer.start()
    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("GameViewModel destroyed!")
        timer.cancel()
    }
    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList() {
        wordList = mutableListOf(
                "queen",
                "hospital",
                "basketball",
                "cat",
                "change",
                "snail",
                "soup",
                "calendar",
                "sad",
                "desk",
                "guitar",
                "home",
                "railway",
                "zebra",
                "jelly",
                "car",
                "crow",
                "trade",
                "bag",
                "roll",
                "bubble"
        )
        wordList.shuffle()
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            resetList()
        }
        _word.value = wordList.removeAt(0)
    }

    /** Methods for buttons presses **/

    fun onSkip() {
        _score.value = score.value?.minus(1)
        _eventSkip.value = true
        nextWord()
    }

    fun onCorrect() {
        _score.value = score.value?.plus(1)
        _eventCorrect.value = true
        nextWord()
    }

    fun onCorrectCompleted() {
        _eventCorrect.value = false
    }

    fun onSkipCompleted() {
        _eventSkip.value = false
    }

    fun onGameFinishedCompleted() {
        _eventGameFinished.value = false
    }

    fun onTimeCompleted() {
        _eventTime.value = false
    }




}
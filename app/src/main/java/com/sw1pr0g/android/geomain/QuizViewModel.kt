package com.sw1pr0g.android.geomain

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"



class QuizViewModel: ViewModel() {

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    var questionUserCheck = mutableListOf(false, false, false, false, false, false)
    var questionUserBank = mutableListOf(false, false, false, false, false, false)

    var currentIndex = 0

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    fun moveToNext() {
        currentIndex = (currentIndex+1) % questionBank.size
    }

    fun moveToBack() {
        val calcIndex = (currentIndex-1) % questionBank.size
        currentIndex = if (calcIndex > 0) calcIndex else 0
    }


}
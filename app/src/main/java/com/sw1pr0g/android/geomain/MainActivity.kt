package com.sw1pr0g.android.geomain

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider

private const val TAG = "MainActivityyyyyy"

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var backButton: ImageButton
    private lateinit var nextButton: ImageButton
    private lateinit var questionTextView: TextView

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProvider(this)[QuizViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        backButton = findViewById(R.id.back_button)
        nextButton = findViewById(R.id.next_button)
        questionTextView = findViewById(R.id.question_text_view)

        questionTextView.setOnClickListener {
            viewNextQuestion()
        }

        trueButton.setOnClickListener {
            checkAnswer(true)
            checkEnabledButtons()
            correctPercentage()
        }

        falseButton.setOnClickListener {
            checkAnswer(false)
            checkEnabledButtons()
            correctPercentage()
        }

        backButton.setOnClickListener {
            quizViewModel.moveToBack()
            updateQuestion()
            checkEnabledButtons()
        }

        nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
            checkEnabledButtons()
        }

        updateQuestion()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "OnStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "OnResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "OnPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "OnPause() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "OnPause() called")
    }

    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer

        val messageResId: Int
        if (userAnswer == correctAnswer) {
            messageResId = R.string.correct_toast
            quizViewModel.questionUserBank[quizViewModel.currentIndex] = true
        } else {
            messageResId = R.string.incorrect_toast
            quizViewModel.questionUserBank[quizViewModel.currentIndex] = false
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
        quizViewModel.questionUserCheck[quizViewModel.currentIndex] = true
    }

    private fun checkEnabledButtons(){
        if (quizViewModel.questionUserCheck[quizViewModel.currentIndex]) {
            trueButton.isEnabled = false
            falseButton.isEnabled = false
        } else {
            trueButton.isEnabled = true
            falseButton.isEnabled = true
        }
    }

    private fun viewNextQuestion() {
        quizViewModel.moveToNext()
        updateQuestion()
    }

    private fun correctPercentage() {
        if (quizViewModel.questionUserCheck.count { it } == 6) {
            val trueAnswers = quizViewModel.questionUserBank.count { it }
            Toast.makeText(this, "You have get answer to all, your percentage - ${(trueAnswers/6)*100}",
                Toast.LENGTH_SHORT).show()
        }
    }
}
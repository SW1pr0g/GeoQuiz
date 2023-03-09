package com.sw1pr0g.android.geomain

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

private const val TAG = "MainActivityyyyyy"

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var backButton: ImageButton
    private lateinit var nextButton: ImageButton
    private lateinit var questionTextView: TextView

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )
    private var currentIndex = 0

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
        }

        falseButton.setOnClickListener {
            checkAnswer(false)
        }

        backButton.setOnClickListener {
            val calcIndex = (currentIndex-1) % questionBank.size
            currentIndex = if (calcIndex > 0) calcIndex else 0
            updateQuestion()
        }

        nextButton.setOnClickListener {
            viewNextQuestion()
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
        val questionTextResId = questionBank[currentIndex].textResId
        questionTextView.setText(questionTextResId)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = questionBank[currentIndex].answer

        val messageResId = if (userAnswer == correctAnswer) R.string.correct_toast
                                                        else R.string.incorrect_toast

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }

    private fun viewNextQuestion() {
        currentIndex = (currentIndex+1) % questionBank.size
        updateQuestion()
    }
}
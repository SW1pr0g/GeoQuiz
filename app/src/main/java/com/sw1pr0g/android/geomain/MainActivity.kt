package com.sw1pr0g.android.geomain

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider

private const val TAG = "MainActivityyyyyy"
private const val KEY_INDEX = "index"
private const val KEY_QUESTION_USER_CHECK = "userCheckList"
private const val KEY_QUESTION_USER_BANK = "userBankList"
private const val REQUEST_CODE_CHEAT = 0

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var cheatButton: Button
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

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        val questionUserCheck = savedInstanceState?.getBooleanArray(KEY_QUESTION_USER_CHECK) ?:
                            booleanArrayOf(false, false, false, false, false, false)
        val questionUserBank = savedInstanceState?.getBooleanArray(KEY_QUESTION_USER_BANK) ?:
        booleanArrayOf(false, false, false, false, false, false)
        quizViewModel.currentIndex = currentIndex
        quizViewModel.questionUserCheck = questionUserCheck.toMutableList()
        quizViewModel.questionUserBank = questionUserBank.toMutableList()

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        cheatButton = findViewById(R.id.cheat_button)
        backButton = findViewById(R.id.back_button)
        nextButton = findViewById(R.id.next_button)
        questionTextView = findViewById(R.id.question_text_view)

        checkEnabledButtons()

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

        cheatButton.setOnClickListener {
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            startActivityForResult(intent, REQUEST_CODE_CHEAT)
            startActivity(intent)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }

        if (requestCode == REQUEST_CODE_CHEAT) {
            quizViewModel.isCheater = data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
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

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
        savedInstanceState.putBooleanArray(KEY_QUESTION_USER_CHECK, quizViewModel.questionUserCheck.toBooleanArray())
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
        when (userAnswer) {
            quizViewModel.isCheater -> {
                messageResId = R.string.judgment_toast
                quizViewModel.questionUserBank[quizViewModel.currentIndex] = true
            }
            (userAnswer == correctAnswer) -> {
                messageResId = R.string.correct_toast
                quizViewModel.questionUserBank[quizViewModel.currentIndex] = true
            }
            else -> {
                messageResId = R.string.incorrect_toast
                quizViewModel.questionUserBank[quizViewModel.currentIndex] = false
            }
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
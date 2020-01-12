package com.android.braintrainer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import androidx.core.view.forEach
import kotlinx.android.synthetic.main.activity_game.*
import kotlin.math.sign
import kotlin.random.Random

class GameActivity : AppCompatActivity() {

    private val signals = arrayOf("+","-","\u00F7","x")
    private var firstNumber = 0
    private var secondNumber = 0
    private var signal = ""
    private var answer = -1
    private var attempts = 0
    private var correctAnswers = 0

    private fun generateRandomAnswers(): ArrayList<Int> {
        val randomArray = ArrayList<Int>()
        randomArray.add(answer)
        while(randomArray.size < 4){
            val randomNumber = ((answer-20)..(answer+20)).shuffled().first()
            if(!randomArray.contains(randomNumber))
                randomArray.add(randomNumber)
        }
        return randomArray
    }

    private fun refreshGridView() {
        val randomTags = (0..3).shuffled()
        val randomAnswers = generateRandomAnswers()
        randomTags.forEach{
            val teste = gridLayout.getChildAt(it) as TextView
            teste.text = randomAnswers.first().toString()
            randomAnswers.removeAt(0)
        }
    }

    fun validateAnswer(view: View){
        val textView = view as TextView
        if(textView.text.toString().toInt() == answer) {
            attempts++
            correctAnswers++
        } else {
            attempts++
        }
        scoreTextView.text = "$correctAnswers/$attempts"
        generateExpression()
        refreshGridView()
    }

    private fun generateExpression() {
        firstNumber = (1..100).shuffled().first()
        secondNumber = (1..100).shuffled().first()
        signal = signals[(0..3).shuffled().first()]
        answer = when(signal) {
            "+" -> (firstNumber+secondNumber)
            "-" -> (firstNumber-secondNumber)
            "÷" -> (firstNumber/secondNumber)
            "x" -> (firstNumber*secondNumber)
            else -> (-1)
        }
        randNumberTextView.text = "$firstNumber $signal $secondNumber"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        generateExpression()
        refreshGridView()

        val timer = object : CountDownTimer(31000, 1000){

            override fun onTick(millisUntilFinished: Long) {
                timeTextView.text = "${(millisUntilFinished / 1000)}s"
            }

            override fun onFinish() {
                playAgainButton.visibility = View.VISIBLE
                gridLayout.forEach {
                    it.isClickable = false
                }
            }

        }.start()

        playAgainButton.setOnClickListener {
            timer.start()
            generateExpression()
            refreshGridView()
            scoreTextView.text = "0/0"
        }
    }
}

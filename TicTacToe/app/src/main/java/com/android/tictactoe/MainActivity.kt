package com.android.tictactoe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    // 0: yellow, 1: red, 2: none
    var activePlayer = 0

    // game board
    var gameBoard = intArrayOf(2,2,2,2,2,2,2,2,2)

    val winningPositions = arrayOf(intArrayOf(0,1,2), intArrayOf(3,4,5), intArrayOf(6,7,8),
                                                intArrayOf(0,3,6), intArrayOf(1,4,7), intArrayOf(2,5,8),
                                                intArrayOf(2,4,6), intArrayOf(0,4,8))

    var gameActive = true

    fun dropIn(view: View) {
        val newPiece = view as ImageView
        val positionClicked = newPiece.tag.toString().toInt()

        if(gameBoard[positionClicked] != 2 || !gameActive)
            return

        newPiece.translationY = -1500f
        gameBoard[positionClicked] = activePlayer

        if(activePlayer == 0){
            newPiece.setImageResource(R.drawable.yellow)
            activePlayer++
        } else {
            newPiece.setImageResource(R.drawable.red)
            activePlayer--
        }

        newPiece.animate().translationYBy(1500f).rotation(3600f).duration = 400

        for(winningPosition in winningPositions){
            if(gameBoard[winningPosition[0]] == gameBoard[winningPosition[1]] && gameBoard[winningPosition[1]] == gameBoard[winningPosition[2]] && gameBoard[winningPosition[0]] != 2){
                var winner: String
                if(activePlayer == 1){
                    winner = "Yellow"
                } else {
                    winner = "Red"
                }
                gameActive = false
                winnerTextView.text = "$winner pieces has won!"
                winnerTextView.visibility = View.VISIBLE
                playButton.visibility = View.VISIBLE
            }
        }
    }

    fun playAgain(view: View){
        winnerTextView.visibility = View.INVISIBLE
        playButton.visibility = View.INVISIBLE
        for(i in 0 until gridLayout.childCount){
            val piece = gridLayout.getChildAt(i) as ImageView
            piece.setImageDrawable(null)
        }
        activePlayer = 0
        gameBoard = intArrayOf(2,2,2,2,2,2,2,2,2)
        gameActive = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}

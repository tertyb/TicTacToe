package com.android.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.android.tictactoe.ui.theme.TicTacToeTheme
import android.view.View
import android.graphics.Color
import android.widget.Button
import android.widget.TextView


class MainActivity : ComponentActivity() {

    private lateinit var buttons: Array<Array<Button>>
    private lateinit var statusTextView: TextView
    private var isPlayerXTurn = true
    private var board = Array(3) { CharArray(3) { ' ' } }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        enableEdgeToEdge()


        buttons = Array(3) { row ->
            Array(3) { col ->
                findViewById(resources.getIdentifier("button${row * 3 + col + 1}", "id", packageName))
            }
        }

        statusTextView = findViewById(R.id.statusTextView)

        findViewById<Button>(R.id.resetButton).setOnClickListener { resetGame() }

        initializeGame()
    }

    private fun initializeGame() {
        for (i in 0..2) {
            for (j in 0..2) {
                buttons[i][j].apply {
                    text = ""
                    isEnabled = true
                    setOnClickListener { onButtonClick(this, i, j) }
                }
                buttons[i][j].setBackgroundColor(Color.GRAY)
            }
        }
        findViewById<Button>(R.id.resetButton).visibility = View.GONE
        isPlayerXTurn = true
        board = Array(3) { CharArray(3) { ' ' } }
        statusTextView.text = "Player X's Turn"
    }

    private fun onButtonClick(button: Button, row: Int, col: Int) {
        if (board[row][col] != ' ') return

        board[row][col] = if (isPlayerXTurn) 'X' else 'O'
        button.text = board[row][col].toString()
        button.isEnabled = false

        if (isPlayerXTurn) {
            button.setTextColor(Color.WHITE) // Player X text color
            button.setBackgroundColor(Color.BLUE) // Player X background color
        } else {
            button.setTextColor(Color.WHITE) // Player O text color
            button.setBackgroundColor(Color.RED) // Player O background color
        }

        if (checkWin()) {
            statusTextView.text = "Player ${board[row][col]} Wins!"
            disableAllButtons()
            findViewById<Button>(R.id.resetButton).visibility = View.VISIBLE
        } else if (isBoardFull()) {
            statusTextView.text = "It's a Draw!"
            findViewById<Button>(R.id.resetButton).visibility = View.VISIBLE
        } else {
            isPlayerXTurn = !isPlayerXTurn
            statusTextView.text = "Player ${if (isPlayerXTurn) "X" else "O"}'s Turn"
        }
    }

    private fun checkWin(): Boolean {
        // Check rows and columns
        for (i in 0..2) {
            if ((board[i][0] == board[i][1] && board[i][1] == board[i][2] && board[i][0] != ' ') ||
                (board[0][i] == board[1][i] && board[1][i] == board[2][i] && board[0][i] != ' ')
            ) {
                return true
            }
        }

        // Check diagonals
        if ((board[0][0] == board[1][1] && board[1][1] == board[2][2] && board[0][0] != ' ') ||
            (board[0][2] == board[1][1] && board[1][1] == board[2][0] && board[0][2] != ' ')
        ) {
            return true
        }

        return false
    }

    private fun isBoardFull(): Boolean {
        return board.all { row -> row.all { cell -> cell != ' ' } }
    }

    private fun disableAllButtons() {
        for (i in 0..2) {
            for (j in 0..2) {
                buttons[i][j].isEnabled = false
            }
        }
    }

    private fun resetGame() {
        initializeGame()
    }
}


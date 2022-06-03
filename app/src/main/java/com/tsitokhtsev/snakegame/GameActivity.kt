package com.tsitokhtsev.snakegame

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.tsitokhtsev.snakegame.databinding.ActivityGameBinding
import com.tsitokhtsev.snakegame.model.CellType
import com.tsitokhtsev.snakegame.model.Direction
import com.tsitokhtsev.snakegame.view.GameView

class GameActivity : AppCompatActivity() {
    private val handler = Handler(Looper.getMainLooper())

    private lateinit var binding: ActivityGameBinding
    private lateinit var gameView: GameView

    private var score: Int = 0
    private var isGameOver: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        gameView = binding.gameView
        gameView.init()

        binding.gameScore.post {
            binding.gameScore.text = getString(R.string.game_score, score.toString())
        }

        addListeners()
        startGame()
    }

    private fun addListeners() {
        binding.buttonUp.setOnClickListener {
            gameView.setDirection(Direction.UP)
        }

        binding.buttonDown.setOnClickListener {
            gameView.setDirection(Direction.DOWN)
        }

        binding.buttonLeft.setOnClickListener {
            gameView.setDirection(Direction.LEFT)
        }

        binding.buttonRight.setOnClickListener {
            gameView.setDirection(Direction.RIGHT)
        }
    }

    private fun startGame() {
        Thread {
            while (!isGameOver) {
                try {
                    Thread.sleep(400)
                    next()
                    handler.post(gameView::invalidate)
                } catch (ignored: InterruptedException) {
                }
            }

            val intent = Intent(this, GameOverActivity::class.java)
            intent.putExtra("score", score)
            startActivity(intent)
            finish()
        }.start()
    }

    private fun next() {
        val snake = gameView.getSnake()
        val firstCell = snake.first
        val nextCell = gameView.getNext(firstCell)

        when (nextCell.type) {
            CellType.EMPTY -> {
                nextCell.type = CellType.SNAKE
                snake.addFirst(nextCell)
                snake.last.type = CellType.EMPTY
                snake.removeLast()
            }
            CellType.APPLE -> {
                nextCell.type = CellType.SNAKE
                snake.addFirst(nextCell)
                gameView.randomApple()

                score++
                binding.gameScore.post {
                    binding.gameScore.text = getString(R.string.game_score, score.toString())
                }
            }
            CellType.SNAKE -> {
                isGameOver = true
            }
        }
    }
}
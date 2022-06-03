package com.tsitokhtsev.snakegame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.tsitokhtsev.snakegame.databinding.ActivityGameOverBinding

class GameOverActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameOverBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameOverBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        val score = intent.getIntExtra("score", 0)
        binding.gameOverScore.text = getString(R.string.game_over_score, score.toString())

        binding.tryAgainButton.setOnClickListener {
            val intent = Intent(this, GameActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
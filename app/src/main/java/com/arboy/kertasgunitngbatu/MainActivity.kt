package com.arboy.kertasgunitngbatu

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.BounceInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import java.util.Locale

enum class Choice(val drawableId: Int) {
    ROCK(R.drawable.ic_batu),
    PAPER(R.drawable.ic_kertas),
    SCISSORS(R.drawable.ic_gunting)
}

enum class GameResult {
    WIN, LOSE, DRAW
}

sealed class GameState {
    object Idle : GameState()
    data class PlayerSelected(val choice: Choice) : GameState()
    data class Revealing(val playerChoice: Choice, val comChoice: Choice) : GameState()
    data class ShowingResult(
        val playerChoice: Choice,
        val comChoice: Choice,
        val result: GameResult
    ) : GameState()

    data class GameOver(val result: GameResult, val playerScore: Int, val comScore: Int) : GameState()
}

data class ScoreState(val playerWins: Int = 0, val comWins: Int = 0, val round: Int = 0)

class MainActivity : AppCompatActivity() {

    private lateinit var scorePlayer: TextView
    private lateinit var scoreCom: TextView
    private lateinit var ivPlayerChoice: ImageView
    private lateinit var ivComChoice: ImageView
    private lateinit var resultText: TextView
    private lateinit var btnRock: LinearLayout
    private lateinit var btnPaper: LinearLayout
    private lateinit var btnScissor: LinearLayout
    private lateinit var roundLabel: TextView
    private lateinit var roundProgress: ProgressBar
    private lateinit var resetBtn: ImageView
    private lateinit var gameOverOverlay: ConstraintLayout
    private lateinit var gameOverText: TextView
    private lateinit var finalScoreText: TextView
    private lateinit var playAgainBtn: TextView
    private lateinit var tvPlayerLabel: TextView
    private lateinit var tvComLabel: TextView
    private lateinit var chooseLabel: TextView
    private lateinit var btnEn: TextView
    private lateinit var btnId: TextView
    private lateinit var btnZh: TextView

    private var currentState: GameState = GameState.Idle
    private var scoreState = ScoreState()
    private var isAnimating = false
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindViews()
        setupClickListeners()
        updateLanguageButtonStyles()
        updateUI()
    }

    private fun bindViews() {
        scorePlayer = findViewById(R.id.score_player)
        scoreCom = findViewById(R.id.score_com)
        ivPlayerChoice = findViewById(R.id.iv_player_choice)
        ivComChoice = findViewById(R.id.iv_com_choice)
        resultText = findViewById(R.id.result_text)
        btnRock = findViewById(R.id.btn_rock)
        btnPaper = findViewById(R.id.btn_paper)
        btnScissor = findViewById(R.id.btn_scissor)
        roundLabel = findViewById(R.id.round_label)
        roundProgress = findViewById(R.id.round_progress)
        resetBtn = findViewById(R.id.reset_btn)
        gameOverOverlay = findViewById(R.id.game_over_overlay)
        gameOverText = findViewById(R.id.game_over_text)
        finalScoreText = findViewById(R.id.final_score_text)
        playAgainBtn = findViewById(R.id.play_again_btn)
        tvPlayerLabel = findViewById(R.id.tv_player_label)
        tvComLabel = findViewById(R.id.tv_com_label)
        chooseLabel = findViewById(R.id.choose_label)
        btnEn = findViewById(R.id.btn_en)
        btnId = findViewById(R.id.btn_id)
        btnZh = findViewById(R.id.btn_zh)
    }

    private fun setupClickListeners() {
        btnRock.setOnClickListener { onPlayerChoice(Choice.ROCK) }
        btnPaper.setOnClickListener { onPlayerChoice(Choice.PAPER) }
        btnScissor.setOnClickListener { onPlayerChoice(Choice.SCISSORS) }
        resetBtn.setOnClickListener { resetGame() }
        playAgainBtn.setOnClickListener { resetGame() }

        btnEn.setOnClickListener { setAppLocale("en") }
        btnId.setOnClickListener { setAppLocale("in") }
        btnZh.setOnClickListener { setAppLocale("zh") }
    }

    private fun onPlayerChoice(choice: Choice) {
        if (isAnimating) return
        if (currentState is GameState.GameOver) return
        if (scoreState.round >= 5) return

        isAnimating = true

        currentState = GameState.PlayerSelected(choice)
        playBounceAnimation(
            when (choice) {
                Choice.ROCK -> btnRock
                Choice.PAPER -> btnPaper
                Choice.SCISSORS -> btnScissor
            }
        )

        ivPlayerChoice.setImageResource(choice.drawableId)

        handler.postDelayed({
            val comChoice = Choice.values().random()
            currentState = GameState.Revealing(choice, comChoice)
            revealComChoice(comChoice)
        }, 300)
    }

    private fun revealComChoice(comChoice: Choice) {
        ivComChoice.rotationY = 0f
        ivComChoice.animate()
            .rotationY(360f)
            .setDuration(500)
            .setInterpolator(DecelerateInterpolator())
            .withEndAction {
                ivComChoice.setImageResource(comChoice.drawableId)
                evaluateGame()
            }
            .start()
    }

    private fun evaluateGame() {
        val state = currentState
        if (state !is GameState.Revealing) return

        val result = when {
            state.playerChoice == state.comChoice -> GameResult.DRAW
            state.playerChoice == Choice.ROCK && state.comChoice == Choice.SCISSORS -> GameResult.WIN
            state.playerChoice == Choice.SCISSORS && state.comChoice == Choice.PAPER -> GameResult.WIN
            state.playerChoice == Choice.PAPER && state.comChoice == Choice.ROCK -> GameResult.WIN
            else -> GameResult.LOSE
        }

        currentState = GameState.ShowingResult(state.playerChoice, state.comChoice, result)

        when (result) {
            GameResult.WIN -> {
                scoreState = scoreState.copy(playerWins = scoreState.playerWins + 1)
        resultText.setTextColor(androidx.core.content.ContextCompat.getColor(this, R.color.win_green))
                resultText.text = getString(R.string.win)
            }
            GameResult.LOSE -> {
                scoreState = scoreState.copy(comWins = scoreState.comWins + 1)
                resultText.setTextColor(androidx.core.content.ContextCompat.getColor(this, R.color.player_red))
                resultText.text = getString(R.string.lose)
            }
            GameResult.DRAW -> {
                resultText.setTextColor(androidx.core.content.ContextCompat.getColor(this, R.color.draw_gold))
                resultText.text = getString(R.string.draw)
            }
        }

        scoreState = scoreState.copy(round = scoreState.round + 1)
        slideInResult(result)
    }

    private fun slideInResult(result: GameResult) {
        resultText.translationY = 100f
        resultText.alpha = 0f
        resultText.animate()
            .translationY(0f)
            .alpha(1f)
            .setDuration(400)
            .setInterpolator(DecelerateInterpolator())
            .withEndAction {
                popScore(result)
            }
            .start()
    }

    private fun popScore(result: GameResult) {
        if (result == GameResult.DRAW) {
            updateUI()
            isAnimating = false
            checkGameOver()
            return
        }

        val target = if (result == GameResult.WIN) scorePlayer else scoreCom
        target.animate()
            .scaleX(1.5f)
            .scaleY(1.5f)
            .setDuration(200)
            .setInterpolator(AccelerateInterpolator())
            .withEndAction {
                updateUI()
                target.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(200)
                    .setInterpolator(BounceInterpolator())
                    .withEndAction {
                        isAnimating = false
                        checkGameOver()
                    }
                    .start()
            }
            .start()
    }

    private fun checkGameOver() {
        if (scoreState.playerWins >= 3 || scoreState.comWins >= 3 || scoreState.round >= 5) {
            val finalResult = when {
                scoreState.playerWins > scoreState.comWins -> GameResult.WIN
                scoreState.comWins > scoreState.playerWins -> GameResult.LOSE
                else -> GameResult.DRAW
            }
            currentState = GameState.GameOver(finalResult, scoreState.playerWins, scoreState.comWins)
            showGameOver(finalResult)
        }
    }

    private fun showGameOver(result: GameResult) {
        gameOverOverlay.visibility = View.VISIBLE
        gameOverOverlay.alpha = 0f
        gameOverOverlay.scaleX = 0.8f
        gameOverOverlay.scaleY = 0.8f

        gameOverText.text = when (result) {
            GameResult.WIN -> getString(R.string.win)
            GameResult.LOSE -> getString(R.string.lose)
            GameResult.DRAW -> getString(R.string.draw)
        }
        gameOverText.setTextColor(
            when (result) {
                GameResult.WIN -> androidx.core.content.ContextCompat.getColor(this, R.color.win_green)
                GameResult.LOSE -> androidx.core.content.ContextCompat.getColor(this, R.color.player_red)
                GameResult.DRAW -> androidx.core.content.ContextCompat.getColor(this, R.color.draw_gold)
            }
        )

        finalScoreText.text = "${getString(R.string.player)} ${scoreState.playerWins} - ${scoreState.comWins} ${getString(R.string.com)}"

        gameOverOverlay.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(500)
            .setInterpolator(DecelerateInterpolator())
            .start()
    }

    private fun resetGame() {
        isAnimating = false
        currentState = GameState.Idle
        scoreState = ScoreState()
        ivComChoice.setImageResource(R.drawable.ic_refresh)
        ivPlayerChoice.setImageResource(R.drawable.ic_batu)

        gameOverOverlay.animate()
            .alpha(0f)
            .setDuration(200)
            .withEndAction {
                gameOverOverlay.visibility = View.GONE
            }
            .start()

        updateUI()
    }

    private fun updateUI() {
        val player = getString(R.string.player)
        val com = getString(R.string.com)
        scorePlayer.text = "$player  ${scoreState.playerWins}"
        scoreCom.text = "${scoreState.comWins}  $com"
        roundLabel.text = getString(R.string.round_of, scoreState.round + 1)
        roundProgress.progress = scoreState.round
        resultText.text = getString(R.string.vs)
        resultText.setTextColor(androidx.core.content.ContextCompat.getColor(this, R.color.text_primary))
        tvPlayerLabel.text = player
        tvComLabel.text = com
    }

    private fun playBounceAnimation(view: View) {
        view.animate()
            .scaleX(1.15f)
            .scaleY(1.15f)
            .setDuration(150)
            .setInterpolator(AccelerateInterpolator())
            .withEndAction {
                view.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(150)
                    .setInterpolator(BounceInterpolator())
                    .start()
            }
            .start()
    }

    private fun updateLanguageButtonStyles() {
        val currentLang = getCurrentLocale().language
        val isEn = currentLang == "en"
        val isId = currentLang == "in" || currentLang == "id"
        val isZh = currentLang == "zh"

        btnEn.setBackgroundResource(if (isEn) R.drawable.bg_active else 0)
        btnId.setBackgroundResource(if (isId) R.drawable.bg_active else 0)
        btnZh.setBackgroundResource(if (isZh) R.drawable.bg_active else 0)
    }

    private fun getCurrentLocale(): Locale {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            resources.configuration.locales[0]
        } else {
            resources.configuration.locale
        }
    }

    private fun setAppLocale(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration(resources.configuration)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale)
        } else {
            config.locale = locale
        }
        resources.updateConfiguration(config, resources.displayMetrics)

        getSharedPreferences("settings", Context.MODE_PRIVATE)
            .edit()
            .putString("language", language)
            .apply()

        recreate()
    }

    override fun attachBaseContext(newBase: Context) {
        val prefs = newBase.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val lang = prefs.getString("language", "in") ?: "in"
        val locale = Locale(lang)
        Locale.setDefault(locale)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val config = Configuration(newBase.resources.configuration)
            config.setLocale(locale)
            super.attachBaseContext(newBase.createConfigurationContext(config))
        } else {
            val config = Configuration(newBase.resources.configuration)
            config.locale = locale
            newBase.resources.updateConfiguration(config, newBase.resources.displayMetrics)
            super.attachBaseContext(newBase)
        }
    }
}

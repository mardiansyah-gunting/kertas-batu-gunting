# KGB Redesign Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Transform the KGB (Rock Paper Scissors) Android app with playful visual theme, 3-language support, best-of-5 scoring, and smooth animations.

**Architecture:** Single-activity Android app using XML layouts + Kotlin. Game state managed as sealed class. Language via locale switching. Animations via ViewPropertyAnimator/ObjectAnimator. No new dependencies.

**Tech Stack:** Kotlin 1.7.20, Android SDK 33, AndroidX AppCompat 1.6.0, Material 1.7.0, ConstraintLayout 2.1.4

## Global Constraints

- Must compile with AGP 7.3.1, Gradle 7.4, Kotlin 1.7.20
- No additional library dependencies
- Must support Android 5.0+ (minSdk 21)
- Language preference persisted via SharedPreferences
- All animations use built-in Android animator framework only
- Keep existing PNG assets (ic_batu, ic_kertas, ic_gunting, ic_refresh, tt_kgb)

---

### Task 1: Colors, Theme & Background Drawable

**Files:**
- Modify: `app/src/main/res/values/colors.xml`
- Modify: `app/src/main/res/values/themes.xml`
- Create: `app/src/main/res/drawable/bg_gradient.xml`
- Modify: `app/src/main/res/drawable/bg_active.xml`

**Interfaces:**
- Produces: Color constants `player_red`, `com_blue`, `win_green`, `draw_gold`; gradient background drawable; updated bg_active.xml

- [ ] **Step 1: Update colors.xml**

Write to `app/src/main/res/values/colors.xml`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <color name="dark_navy_start">#1A1A2E</color>
    <color name="dark_navy_end">#16213E</color>
    <color name="player_red">#E94560</color>
    <color name="com_blue">#0F3460</color>
    <color name="win_green">#4EEC6C</color>
    <color name="draw_gold">#FFD700</color>
    <color name="white">#FFFFFFFF</color>
    <color name="black">#FF000000</color>
    <color name="card_bg">#2A2A4A</color>
    <color name="text_primary">#FFFFFFFF</color>
    <color name="text_secondary">#B0B0B0</color>
    <color name="score_bg">#2A2A4A</color>
    <color name="transparent_white_20">#33FFFFFF</color>
</resources>
```

- [ ] **Step 2: Update themes.xml**

Write to `app/src/main/res/values/themes.xml`:

```xml
<resources xmlns:tools="http://schemas.android.com/tools">
    <style name="Theme.KertasGunitngBatu" parent="Theme.MaterialComponents.DayNight.NoActionBar">
        <item name="colorPrimary">@color/player_red</item>
        <item name="colorPrimaryVariant">@color/dark_navy_end</item>
        <item name="colorOnPrimary">@color/white</item>
        <item name="colorSecondary">@color/com_blue</item>
        <item name="colorSecondaryVariant">@color/dark_navy_end</item>
        <item name="colorOnSecondary">@color/white</item>
        <item name="android:statusBarColor">@color/dark_navy_start</item>
        <item name="android:navigationBarColor">@color/dark_navy_start</item>
        <item name="android:windowBackground">@drawable/bg_gradient</item>
    </style>
</resources>
```

- [ ] **Step 3: Create gradient background drawable**

Write to `app/src/main/res/drawable/bg_gradient.xml`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <gradient
        android:type="linear"
        android:angle="135"
        android:startColor="@color/dark_navy_start"
        android:endColor="@color/dark_navy_end" />
</shape>
```

- [ ] **Step 4: Update bg_active.xml**

Write to `app/src/main/res/drawable/bg_active.xml`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<shape xmlns:android="http://schemas.android.com/apk/res/android">
    <solid android:color="@color/transparent_white_20" />
    <stroke android:width="2dp" android:color="@color/player_red" />
    <corners android:radius="16dp" />
</shape>
```

- [ ] **Step 5: Commit**

```
git add app/src/main/res/values/colors.xml app/src/main/res/values/themes.xml app/src/main/res/drawable/bg_gradient.xml app/src/main/res/drawable/bg_active.xml
git commit -m "feat: update color palette, theme, and background drawable"
```

---

### Task 2: String Resources (3 Languages)

**Files:**
- Modify: `app/src/main/res/values/strings.xml` (Indonesian - default)
- Create: `app/src/main/res/values-en/strings.xml` (English)
- Create: `app/src/main/res/values-zh/strings.xml` (Mandarin)

**Interfaces:**
- Produces: String resources accessible via `getString(R.string.xxx)` in code

- [ ] **Step 1: Write Indonesian strings.xml**

Write to `app/src/main/res/values/strings.xml`:

```xml
<resources>
    <string name="app_name">KGB (Kertas Gunting Batu)</string>
    <string name="player">Pemain</string>
    <string name="com">COM</string>
    <string name="rock">Batu</string>
    <string name="paper">Kertas</string>
    <string name="scissors">Gunting</string>
    <string name="vs">VS</string>
    <string name="win">Kamu Menang!</string>
    <string name="lose">Kamu Kalah!</string>
    <string name="draw">Seri!</string>
    <string name="round_of">Ronde %d dari 5</string>
    <string name="score">Skor</string>
    <string name="play_again">Main Lagi</string>
    <string name="game_over">Game Over</string>
    <string name="en">EN</string>
    <string name="id">ID</string>
    <string name="zh">中文</string>
    <string name="choose_move">Pilih gerakanmu!</string>
</resources>
```

- [ ] **Step 2: Write English strings.xml**

Write to `app/src/main/res/values-en/strings.xml`:

```xml
<resources>
    <string name="app_name">KGB Game</string>
    <string name="player">Player</string>
    <string name="com">COM</string>
    <string name="rock">Rock</string>
    <string name="paper">Paper</string>
    <string name="scissors">Scissors</string>
    <string name="vs">VS</string>
    <string name="win">You Win!</string>
    <string name="lose">You Lose!</string>
    <string name="draw">Draw!</string>
    <string name="round_of">Round %d of 5</string>
    <string name="score">Score</string>
    <string name="play_again">Play Again</string>
    <string name="game_over">Game Over</string>
    <string name="en">EN</string>
    <string name="id">ID</string>
    <string name="zh">中文</string>
    <string name="choose_move">Choose your move!</string>
</resources>
```

- [ ] **Step 3: Write Mandarin strings.xml**

Write to `app/src/main/res/values-zh/strings.xml`:

```xml
<resources>
    <string name="app_name">石头剪刀布</string>
    <string name="player">玩家</string>
    <string name="com">电脑</string>
    <string name="rock">石头</string>
    <string name="paper">布</string>
    <string name="scissors">剪刀</string>
    <string name="vs">VS</string>
    <string name="win">你赢了！</string>
    <string name="lose">你输了！</string>
    <string name="draw">平局！</string>
    <string name="round_of">第%d轮共5轮</string>
    <string name="score">分数</string>
    <string name="play_again">再玩一次</string>
    <string name="game_over">游戏结束</string>
    <string name="en">EN</string>
    <string name="id">ID</string>
    <string name="zh">中文</string>
    <string name="choose_move">选择你的手势！</string>
</resources>
```

- [ ] **Step 4: Commit**

```
git add app/src/main/res/values/strings.xml app/src/main/res/values-en/strings.xml app/src/main/res/values-zh/strings.xml
git commit -m "feat: add string resources for ID, EN, ZH"
```

---

### Task 3: Layout XML

**Files:**
- Modify: `app/src/main/res/layout/activity_main.xml`

**Interfaces:**
- Produces: Layout with IDs: `score_player`, `score_com`, `btn_en`, `btn_id`, `btn_zh`, `player_choice_card`, `com_choice_card`, `iv_player_choice`, `iv_com_choice`, `result_text`, `btn_rock`, `btn_paper`, `btn_scissor`, `round_progress`, `round_label`, `reset_btn`, `game_over_overlay`, `game_over_text`, `play_again_btn`

- [ ] **Step 1: Write the layout**

Write to `app/src/main/res/layout/activity_main.xml`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:fillViewport="true"
    tools:context=".MainActivity">

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:padding="16dp">

        <!-- Score Bar -->
        <LinearLayout
            android:id="@+id/score_bar"
            android:layout_width="0dp"
            android:layout_height="56dp"
            android:background="@drawable/bg_active"
            android:gravity="center"
            android:orientation="horizontal"
            android:paddingHorizontal="16dp"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent">

            <TextView
                android:id="@+id/score_player"
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:fontFamily="@font/rr_fonts"
                android:gravity="center"
                android:text="@string/player"
                android:textColor="@color/player_red"
                android:textSize="18sp"
                android:textStyle="bold" />

            <TextView
                android:id="@+id/score_vs"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:fontFamily="@font/rr_fonts"
                android:text="@string/vs"
                android:textColor="@color/text_primary"
                android:textSize="16sp"
                android:textStyle="bold" />

            <TextView
                android:id="@+id/score_com"
                android:layout_width="0dp"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:fontFamily="@font/rr_fonts"
                android:gravity="center"
                android:text="@string/com"
                android:textColor="@color/com_blue"
                android:textSize="18sp"
                android:textStyle="bold" />
        </LinearLayout>

        <!-- Language Toggle -->
        <LinearLayout
            android:id="@+id/language_bar"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="8dp"
            android:orientation="horizontal"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintTop_toBottomOf="@id/score_bar">

            <TextView
                android:id="@+id/btn_en"
                android:layout_width="40dp"
                android:layout_height="28dp"
                android:background="@drawable/bg_active"
                android:fontFamily="@font/rr_fonts"
                android:gravity="center"
                android:text="@string/en"
                android:textColor="@color/text_primary"
                android:textSize="12sp"
                android:textStyle="bold" />

            <TextView
                android:id="@+id/btn_id"
                android:layout_width="40dp"
                android:layout_height="28dp"
                android:layout_marginStart="4dp"
                android:background="@drawable/bg_active"
                android:fontFamily="@font/rr_fonts"
                android:gravity="center"
                android:text="@string/id"
                android:textColor="@color/text_primary"
                android:textSize="12sp"
                android:textStyle="bold" />

            <TextView
                android:id="@+id/btn_zh"
                android:layout_width="40dp"
                android:layout_height="28dp"
                android:layout_marginStart="4dp"
                android:background="@drawable/bg_active"
                android:fontFamily="@font/rr_fonts"
                android:gravity="center"
                android:text="@string/zh"
                android:textColor="@color/text_primary"
                android:textSize="12sp"
                android:textStyle="bold" />
        </LinearLayout>

        <!-- Game Arena -->
        <androidx.constraintlayout.widget.ConstraintLayout
            android:id="@+id/game_arena"
            android:layout_width="0dp"
            android:layout_height="200dp"
            android:layout_marginTop="16dp"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@id/language_bar">

            <!-- Player Card -->
            <LinearLayout
                android:id="@+id/player_choice_card"
                android:layout_width="100dp"
                android:layout_height="140dp"
                android:background="@drawable/bg_active"
                android:gravity="center"
                android:orientation="vertical"
                android:padding="8dp"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toTopOf="parent">

                <ImageView
                    android:id="@+id/iv_player_choice"
                    android:layout_width="64dp"
                    android:layout_height="64dp"
                    android:src="@drawable/ic_batu"
                    android:contentDescription="@string/rock" />

                <TextView
                    android:id="@+id/tv_player_label"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="4dp"
                    android:fontFamily="@font/rr_fonts"
                    android:text="@string/player"
                    android:textColor="@color/player_red"
                    android:textSize="12sp"
                    android:textStyle="bold" />
            </LinearLayout>

            <!-- VS / Result Text -->
            <TextView
                android:id="@+id/result_text"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:fontFamily="@font/rr_fonts"
                android:text="@string/vs"
                android:textColor="@color/text_primary"
                android:textSize="28sp"
                android:textStyle="bold"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintEnd_toStartOf="@id/com_choice_card"
                app:layout_constraintStart_toEndOf="@id/player_choice_card"
                app:layout_constraintTop_toTopOf="parent" />

            <!-- COM Card -->
            <LinearLayout
                android:id="@+id/com_choice_card"
                android:layout_width="100dp"
                android:layout_height="140dp"
                android:background="@drawable/bg_active"
                android:gravity="center"
                android:orientation="vertical"
                android:padding="8dp"
                app:layout_constraintBottom_toBottomOf="parent"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintTop_toTopOf="parent">

                <ImageView
                    android:id="@+id/iv_com_choice"
                    android:layout_width="64dp"
                    android:layout_height="64dp"
                    android:src="@drawable/ic_refresh"
                    android:contentDescription="@string/com" />

                <TextView
                    android:id="@+id/tv_com_label"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="4dp"
                    android:fontFamily="@font/rr_fonts"
                    android:text="@string/com"
                    android:textColor="@color/com_blue"
                    android:textSize="12sp"
                    android:textStyle="bold" />
            </LinearLayout>
        </androidx.constraintlayout.widget.ConstraintLayout>

        <!-- Player Controls -->
        <TextView
            android:id="@+id/choose_label"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="16dp"
            android:fontFamily="@font/rr_fonts"
            android:text="@string/choose_move"
            android:textColor="@color/text_secondary"
            android:textSize="14sp"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@id/game_arena" />

        <LinearLayout
            android:id="@+id/player_controls"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_marginTop="12dp"
            android:gravity="center"
            android:orientation="horizontal"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@id/choose_label">

            <LinearLayout
                android:id="@+id/btn_rock"
                android:layout_width="0dp"
                android:layout_height="100dp"
                android:layout_weight="1"
                android:layout_marginEnd="8dp"
                android:background="@drawable/bg_active"
                android:gravity="center"
                android:orientation="vertical"
                android:padding="8dp"
                android:clickable="true"
                android:focusable="true">

                <ImageView
                    android:layout_width="48dp"
                    android:layout_height="48dp"
                    android:src="@drawable/ic_batu"
                    android:contentDescription="@string/rock" />

                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="4dp"
                    android:fontFamily="@font/rr_fonts"
                    android:text="@string/rock"
                    android:textColor="@color/text_primary"
                    android:textSize="12sp"
                    android:textStyle="bold" />
            </LinearLayout>

            <LinearLayout
                android:id="@+id/btn_paper"
                android:layout_width="0dp"
                android:layout_height="100dp"
                android:layout_weight="1"
                android:layout_marginEnd="8dp"
                android:background="@drawable/bg_active"
                android:gravity="center"
                android:orientation="vertical"
                android:padding="8dp"
                android:clickable="true"
                android:focusable="true">

                <ImageView
                    android:layout_width="48dp"
                    android:layout_height="48dp"
                    android:src="@drawable/ic_kertas"
                    android:contentDescription="@string/paper" />

                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="4dp"
                    android:fontFamily="@font/rr_fonts"
                    android:text="@string/paper"
                    android:textColor="@color/text_primary"
                    android:textSize="12sp"
                    android:textStyle="bold" />
            </LinearLayout>

            <LinearLayout
                android:id="@+id/btn_scissor"
                android:layout_width="0dp"
                android:layout_height="100dp"
                android:layout_weight="1"
                android:background="@drawable/bg_active"
                android:gravity="center"
                android:orientation="vertical"
                android:padding="8dp"
                android:clickable="true"
                android:focusable="true">

                <ImageView
                    android:layout_width="48dp"
                    android:layout_height="48dp"
                    android:src="@drawable/ic_gunting"
                    android:contentDescription="@string/scissors" />

                <TextView
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:layout_marginTop="4dp"
                    android:fontFamily="@font/rr_fonts"
                    android:text="@string/scissors"
                    android:textColor="@color/text_primary"
                    android:textSize="12sp"
                    android:textStyle="bold" />
            </LinearLayout>
        </LinearLayout>

        <!-- Round Progress -->
        <TextView
            android:id="@+id/round_label"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginTop="16dp"
            android:fontFamily="@font/rr_fonts"
            android:text="@string/round_of"
            android:textColor="@color/text_secondary"
            android:textSize="12sp"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@id/player_controls" />

        <ProgressBar
            android:id="@+id/round_progress"
            style="?android:attr/progressBarStyleHorizontal"
            android:layout_width="0dp"
            android:layout_height="8dp"
            android:layout_marginTop="8dp"
            android:max="5"
            android:progress="0"
            android:progressDrawable="@drawable/bg_active"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@id/round_label" />

        <!-- Reset Button -->
        <ImageView
            android:id="@+id/reset_btn"
            android:layout_width="40dp"
            android:layout_height="40dp"
            android:layout_marginTop="16dp"
            android:padding="8dp"
            android:src="@drawable/ic_refresh"
            android:contentDescription="@string/play_again"
            android:clickable="true"
            android:focusable="true"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintTop_toBottomOf="@id/round_progress" />

        <!-- Game Over Overlay -->
        <androidx.constraintlayout.widget.ConstraintLayout
            android:id="@+id/game_over_overlay"
            android:layout_width="0dp"
            android:layout_height="0dp"
            android:background="#CC000000"
            android:visibility="gone"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent">

            <TextView
                android:id="@+id/game_over_text"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:fontFamily="@font/rr_fonts"
                android:text="@string/game_over"
                android:textColor="@color/win_green"
                android:textSize="36sp"
                android:textStyle="bold"
                app:layout_constraintBottom_toTopOf="@id/play_again_btn"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toTopOf="parent"
                app:layout_constraintVerticalChainStyle="centered" />

            <TextView
                android:id="@+id/final_score_text"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginTop="16dp"
                android:fontFamily="@font/rr_fonts"
                android:textColor="@color/text_primary"
                android:textSize="20sp"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toBottomOf="@id/game_over_text" />

            <TextView
                android:id="@+id/play_again_btn"
                android:layout_width="wrap_content"
                android:layout_height="48dp"
                android:layout_marginTop="24dp"
                android:background="@drawable/bg_active"
                android:fontFamily="@font/rr_fonts"
                android:gravity="center"
                android:paddingHorizontal="32dp"
                android:text="@string/play_again"
                android:textColor="@color/win_green"
                android:textSize="18sp"
                android:textStyle="bold"
                android:clickable="true"
                android:focusable="true"
                app:layout_constraintEnd_toEndOf="parent"
                app:layout_constraintStart_toStartOf="parent"
                app:layout_constraintTop_toBottomOf="@id/final_score_text" />
        </androidx.constraintlayout.widget.ConstraintLayout>

    </androidx.constraintlayout.widget.ConstraintLayout>

</ScrollView>
```

- [ ] **Step 2: Commit**

```
git add app/src/main/res/layout/activity_main.xml
git commit -m "feat: redesign layout with score bar, game arena, language toggle, overlay"
```

---

### Task 4: MainActivity - Game Engine & State Logic

**Files:**
- Modify: `app/src/main/java/com/arboy/kertasgunitngbatu/MainActivity.kt`

**Interfaces:**
- Consumes: R.id.* from layout, R.string.* from strings
- Produces: Game state sealed class, Choice enum, Result sealed class, LocaleHelper object, evaluate() function

- [ ] **Step 1: Write the complete MainActivity.kt**

Write to `app/src/main/java/com/arboy/kertasgunitngbatu/MainActivity.kt`:

```kotlin
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
        isAnimating = true
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
                scoreState = scoreState.copy(round = scoreState.round + 1)
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
        val config = Configuration(newBase.resources.configuration)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale)
        } else {
            config.locale = locale
        }
        super.attachBaseContext(newBase.createConfigurationContext(config))
    }
}
```

**Important note about `attachBaseContext`:** The `createConfigurationContext` approach is for API 24+. For minSdk 21 support, the above code handles both paths. However, `createConfigurationContext` returns a new ContextWrapper — the proper pattern is to override `attachBaseContext` to wrap it. The code above does this correctly.

- [ ] **Step 2: Commit**

```
git add app/src/main/java/com/arboy/kertasgunitngbatu/MainActivity.kt
git commit -m "feat: implement game engine, scoring, language switching, and animations"
```

---

## Self-Review Check

1. **Spec coverage:** Every section of the spec has a corresponding task — visual theme (Task 1), strings (Task 2), layout (Task 3), animations/game logic/scoring/language (Task 4).
2. **Placeholder scan:** No TBD, TODO, or placeholders. All code is complete.
3. **Type consistency:** `Choice`, `GameResult`, `GameState`, `ScoreState` types used consistently. Method signatures match across the plan.
4. **Scope check:** Focused entirely on the KGB redesign. No scope creep.

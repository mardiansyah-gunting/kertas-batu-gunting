# ✂️ KertasGuntingBatu (Rock Paper Scissors)

Rock Paper Scissors game with a playful dark theme, 3-language support, and best-of-5 scoring system.

## Features

- 🎮 **Best of 5** — First to 3 wins takes the match
- 🌍 **3 Languages** — English, Indonesia, 中文 (Mandarin)
- 🎬 **Smooth Animations** — Bounce on tap, flip reveal, slide results, pop score
- 🏆 **Game Over Overlay** — Win/Lose/Draw with final score
- 🌙 **Dark Theme** — Gradient background with vibrant accents

## Screenshots

| Game Arena | Result | Game Over |
|---|---|---|
| *(screenshot needed)* | *(screenshot needed)* | *(screenshot needed)* |

## Tech Stack

- **Language:** Kotlin 1.7.20
- **Min SDK:** 21
- **Target SDK:** 33
- **Architecture:** Single Activity, XML layouts, sealed class state machine
- **Dependencies:** AndroidX AppCompat, Material, ConstraintLayout

## How to Build

```bash
./gradlew assembleDebug
```

## How to Play

1. Select **Rock**, **Paper**, or **Scissors**
2. Watch the COM choice reveal with a flip animation
3. See the result (Win/Lose/Draw) with score update
4. First to **3 wins** wins the game
5. If tied after 5 rounds, higher score wins
6. Tap **Play Again** to restart

## Language

Tap **EN** / **ID** / **中文** in the top-right corner to switch languages. Your preference is saved.

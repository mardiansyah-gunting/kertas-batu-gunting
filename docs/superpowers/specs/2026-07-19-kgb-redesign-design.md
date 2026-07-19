# Kertas Gunting Batu (KGB) Redesign

## Overview
Revamp the existing Android Rock-Paper-Scissors game with playful/fun visual theme, 3-language support, best-of-5 scoring system, and smooth animations — all within the existing XML + Kotlin architecture.

## Approach
**A — Enhance Existing XML** (no Jetpack Compose, no MotionLayout)
Keep the current XML layout approach. Add animations via `ViewPropertyAnimator` + `ObjectAnimator`. Add language switching via locale configuration. Add score tracking and best-of-5 game logic.

## Visual Theme & Layout

### Color Palette
| Element | Color | Hex |
|---------|-------|-----|
| Background | Dark navy gradient | `#1A1A2E` → `#16213E` |
| Player card accent | Red-pink | `#E94560` |
| COM card accent | Deep blue | `#0F3460` |
| Win indicator | Bright green | `#4EEC6C` |
| Draw indicator | Gold | `#FFD700` |
| Text | White | `#FFFFFF` |

### Layout Structure (top to bottom)
1. **Header bar** — Score display (Player vs COM), Language toggle (EN | ID | 中文)
2. **Game arena** — Central area showing player choice vs COM choice, VS/result text
3. **Player controls** — Three cards (Batu/Paper/Gunting) with rounded corners + shadow
4. **Footer** — Round progress indicator (Round X of 5), Reset button

### Key Layout Changes
- Remove three separate ConstraintLayout rows; use a single central arena
- COM choice shows as a card that flips on reveal
- Player choices are horizontal row of 3 cards with equal weight
- Score bar shows animated number with player/COM labels
- Language toggle as 3 small buttons in top-right

## Animations
All use `ViewPropertyAnimator` and `ObjectAnimator` — no extra dependencies.

| Event | Animation | Duration |
|-------|-----------|----------|
| Player taps choice | Scale 1.0→1.15→1.0 with bounce interpolator | 300ms |
| COM reveals choice | RotateY 0→180→360 (3D flip) via ObjectAnimator | 500ms |
| Result text appears | Slide in from bottom + fade alpha 0→1 | 400ms |
| Score increments | Scale 1.0→1.5→1.0 + color flash | 400ms |
| Round progress | Progress bar width animation | 300ms |
| Game Over overlay | Fade in with scale 0.8→1.0 | 500ms |
| Reset | All cards fade out (200ms) then fade in (200ms) | 400ms |

## Language System

### Implementation
- 3 `strings.xml` files in `values/` (ID default), `values-en/` (English), `values-zh/` (Mandarin)
- Language toggle buttons in header call `setLocale()`
- `attachBaseContext()` override to apply locale at activity creation
- Locale preference saved to `SharedPreferences`

### Key Strings
| Key | EN | ID | ZH |
|-----|----|----|-----|
| app_name | KGB Game | KGB (Kertas Gunting Batu) | 石头剪刀布 |
| player | Player | Pemain | 玩家 |
| computer | Computer | COM | 电脑 |
| rock | Rock | Batu | 石头 |
| paper | Paper | Kertas | 布 |
| scissors | Scissors | Gunting | 剪刀 |
| win | You Win! | Kamu Menang! | 你赢了！ |
| lose | You Lose! | Kamu Kalah! | 你输了！ |
| draw | Draw! | Seri! | 平局！ |
| round_of | Round %d of 5 | Ronde %d dari 5 | 第%d轮共5轮 |
| score | Score | Skor | 分数 |

## Score System (Best of 5)

### Game Flow
1. Player selects Rock/Paper/Scissors
2. COM randomly selects (revealed with flip animation)
3. Result determined (Win/Lose/Draw) with slide animation
4. Score updated (with pop animation) if not a draw
5. Check win condition: first to 3 wins wins the game
6. If neither has 3 after 5 rounds, higher score wins
7. If tied after 5 rounds, it's a draw game
8. Game Over overlay with "Play Again" button

### State Machine
```
IDLE → PLAYER_SELECTS → REVEALING → SHOW_RESULT → SCORE_UPDATE
                                                         ↓
                                              CHECK_WINNER → GAME_OVER
                                                         ↓ (no winner yet)
                                                     NEXT_ROUND
```

## Code Architecture

### Files to Modify
| File | Changes |
|------|---------|
| `MainActivity.kt` | Complete rewrite — add game state, scoring, animations, language handling |
| `activity_main.xml` | Rewrite layout — new structure, score bar, language toggle, round indicator |
| `values/strings.xml` | Update with all Indonesian strings |
| `values-en/strings.xml` | New — English translations |
| `values-zh/strings.xml` | New — Mandarin translations |
| `values/colors.xml` | New color palette |
| `values/themes.xml` | Update to match new palette |

### Files to Keep (unchanged)
- All drawable PNG assets (ic_batu, ic_kertas, ic_gunting, ic_refresh, tt_kgb)
- `bg_active.xml` (may need minor color update)
- Font file (rr_fonts.ttf)
- Gradle/build files
- AndroidManifest.xml

### Game Logic (Kotlin)
- `GameState` sealed class: `Idle`, `PlayerTurn`, `Revealing`, `Result`, `GameOver`
- `GameEngine` object: `evaluate(choice1, choice2) -> Result`
- `ScoreTracker` data class: `playerWins`, `comWins`, `roundsPlayed`
- Winner check: `playerWins >= 3 || comWins >= 3 || roundsPlayed >= 5`

## Testing
- Run on emulator/device to verify all animations play smoothly
- Test language switching (EN → ID → 中文) for all strings
- Play through full best-of-5 game to verify scoring and win conditions
- Verify reset after game over
- Test rotation handling (save/restore state)

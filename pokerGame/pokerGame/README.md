# Texas Hold'em Poker game

## Overview

This project is a Texas Hold'em poker simulator built using **Java** and **JavaFX**.  
The application separates poker game logic from the graphical user interface using a modular design.

---

# System Architecture

The application consists of three main components:

- `App.java`
- `Poker.java`
- `Card.java`

These components work together to manage card generation, game rules, score evaluation, and UI rendering.

---

# Components

## Card.java

### Purpose

Represents individual playing cards.

### Main Functions

- Stores:
  - Card rank
  - Suit
  - Value

- Uses immutable fields with `final`
- Returns card colors based on suit:
  - Red for hearts and diamonds
  - Black for clubs and spades

---

## Poker.java

### Purpose

Handles poker game logic and score evaluation.

### Main Functions

- Creates and shuffles a 52-card deck
- Evaluates poker hands:
  - Pair
  - Straight
  - Flush
  - Full House
  - etc.

- Uses:
  - `Collections.shuffle()`
  - `HashMap`
  - `ArrayList`

- Converts scores into hand names

---

## App.java

### Purpose

Controls the JavaFX interface and game phases.

### Main Functions

- Manages game flow using a `phase` variable
- Deals:
  - Player cards
  - Dealer cards
  - Community cards

- Renders cards dynamically using JavaFX layouts
- Displays final winner during showdown

---

# Game Workflow

| Phase | Action |
|---|---|
| 0 | Deal player and dealer cards |
| 1 | Deal the Flop |
| 2 | Deal Turn and River cards |
| 3 | Reveal winner and reset game |

---

# Technologies Used

| Technology | Purpose |
|---|---|
| Java | Core programming |
| JavaFX | Graphical User Interface |
| Collections API | Deck shuffling and storage |
| OOP | Modular application design |

---



---

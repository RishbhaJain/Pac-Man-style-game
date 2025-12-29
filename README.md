# Save the Josh - Pac-Man Style Game

A Java-based Pac-Man style adventure game featuring procedurally generated worlds, enemy AI, collectibles, and immersive audio.

## Features

### Gameplay
- **Procedurally Generated Worlds**: Each game generates unique room-based worlds with interconnected hallways
- **Player Movement**: Navigate through the world using keyboard controls
- **Enemy AI**: Enemies that patrol and chase the player with timed movement intervals
- **Collectible System**: Gather collectibles scattered throughout the world
- **Health System**: Player has a maximum of 10 health points
- **Save/Load System**: Save your progress and resume later

### Audio
The game includes immersive audio features:
- Menu background music
- Exploration music during gameplay
- Footstep sound effects
- Collection sound effects
- Hit/damage sound effects

### User Interface
- Main menu with options:
  - New Game (N)
  - Load Game (L)
  - Quit (Q)
- HUD display showing game stats
- Tile-based rendering engine

## Project Structure

```
proj5/
├── src/
│   ├── core/          # Core game logic
│   │   ├── Main.java           # Entry point
│   │   ├── Game.java           # Main game controller
│   │   ├── World.java          # World generation
│   │   ├── User.java           # Player character
│   │   ├── Enemy.java          # Enemy AI
│   │   ├── Collectible.java   # Collectible items
│   │   ├── GameAudio.java      # Audio management
│   │   ├── SaveLoad.java       # Save/load functionality
│   │   ├── GameState.java      # Game state management
│   │   ├── Room.java           # Room generation
│   │   ├── HallwayGenerator.java
│   │   ├── DisjointSets.java
│   │   └── Edge.java
│   ├── tileengine/    # Rendering engine
│   │   ├── TERenderer.java
│   │   ├── TETile.java
│   │   └── Tileset.java
│   └── demo/          # Demo files
├── test/              # Test files
sounds/                # Audio files
├── menu_music.wav
├── exploration_music.wav
├── footstep.wav
├── collect.wav
└── hit.wav
```

## How to Run

1. Ensure you have Java installed on your system
2. Compile the project:
   ```bash
   javac proj5/src/core/Main.java
   ```
3. Run the game:
   ```bash
   java core.Main
   ```

## Controls

- **N**: Start a new game
- **L**: Load saved game
- **Q**: Quit the game
- **Arrow Keys/WASD**: Move the player character
- **:Q**: Save and quit during gameplay

## Technical Details

- Built using Java
- Uses Princeton's StdDraw library for graphics
- Implements Disjoint Sets data structure for world generation
- Tile-based rendering system
- Real-time enemy movement with 500ms update intervals

## Game Stats

- Maximum Collectibles: 10
- Maximum Enemies: 10
- Player Max Health: 10
- World Dimensions: Configurable width and height with HUD overlay

## Author

Rishbha Jain

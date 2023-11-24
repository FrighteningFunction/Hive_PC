# Hive Game Application Specification

## Introduction
This document outlines the development of a digital version of the board game "Hive" using Java and Swing for the graphical user interface (GUI). The purpose of this project is to provide a virtual experience of Hive, enabling two players to engage in the game on a single computer. The game will replicate the traditional Hive experience while leveraging the advantages of a digital platform, such as automated rule enforcement, game state management, and a visually appealing interface.

### Brief Description
Hive is a two-player, turn-based strategy game where the objective is to surround the opponent's Queen Bee tile with a mix of their own and the opponent's tiles. Each tile represents a different insect, with its own unique movement pattern. The board is dynamically formed by the placement of these hexagonal tiles, and the game does not require a fixed board. The digital version aims to mimic these gameplay mechanics and provide an intuitive and interactive user experience.

## Use Cases
### Player Interactions:
1. **Start Game**: Players can start a new game, reset the board, and choose their tile color (black or white).
2. **Place Tile**: During their turn, players choose a tile from their collection and place it on the board adhering to the game's placement rules.
3. **Move Tile**: Players can move a tile according to the specific movement rules of that insect.
4. **View Rules/Help**: Users can view the rules of the game or get help about the game mechanics.
5. **Save/Load Game**: Players can save the current game state or load a previously saved game.
6. **Quit Game**: Exit the current game session and close the application.

### Application Features:
1. **Game History**: Tracks and displays the sequence of moves.
2. **Undo Move**: Allows a player to undo their last move.
3. **Game Timer**: Times the duration of the game or each move, enforcing time limits if set.

## Solution Outline

### Technological Solutions
1. **Swing GUI**: The interface will be built using Java Swing, ensuring a responsive and intuitive user experience. Key components:
    - `JFrame` for the main window.
    - Custom drawing on `JPanel` for the game board using low-level graphics routines (Graphics class) to render hexagonal tiles and insects.
    - `JMenu` for the main menu, including options like New Game, Save/Load, Exit.
    - `JOptionPane` for prompts and messages.
    - `JComboBox` for selecting tile color (black/white) before starting a new game.

2. **Collections Framework**: Utilize Java Collections Framework for managing the game state, such as a `List` for tracking the sequence of moves and a `Set` or `Map` for storing the board state and the positions of the tiles.

3. **File I/O**: Game state serialization for saving and loading games, using either Java's built-in serialization or JSON processing with libraries like Gson or Jackson.

4. **JUnit Testing**: Test critical components of the game logic, including tile placement rules, move validation, and game state management. At least 10 methods across 3 different classes (e.g., `GameBoard`, `Tile`, `GameManager`) will be tested.

### File Formats
- **Game Save File**: JSON/XML format, storing the current state of the game, including board configuration, sequence of moves, and time elapsed.

### Testing Strategy
- **Game Logic Tests**: Ensure that the movement and placement rules for each insect type are correctly implemented.
- **GUI Tests**: Validate that the GUI correctly reflects the game state and responds to user inputs.
- **Serialization Tests**: Confirm that saving and loading functionality works as intended, with accurate recreation of game state.

## Conclusion
This application will offer a digital rendition of the Hive game with an emphasis on usability and adherence to the original game's rules. By fulfilling the mandatory functionalities and focusing on a robust testing strategy, the project aims to deliver a reliable and enjoyable Hive gaming experience.

## Rules

1. **Queen Bee:**
    - Moves one space at a time.
    - Cannot move if completely surrounded by other pieces.

2. **Spider:**
    - Moves exactly three spaces around the hive.
    - Must move in a single unbroken path.

3. **Beetle:**
    - Moves one space at a time like the Queen Bee.
    - Can move on top of other pieces, and while on top, it controls the space underneath it.

4. **Grasshopper:**
    - Jumps over a line of one or more pieces.
    - Must land in the first empty space directly on the opposite side.

5. **Ant:**
    - Can move any number of spaces around the hive's edge.
    - Offers the most mobility among all pieces.

6. **Ladybug (if using expansion):**
    - Moves exactly three spaces: two on top of the hive and one down.
    - This allows it to quickly traverse the board.

7. **Mosquito (if using expansion):**
    - Adopts the movement characteristics of any adjacent piece it touches at the start of its move.
    - Can change its movement type each turn based on its position.

8. **Pillbug (if using expansion):**
    - Moves one space like the Queen Bee.
    - Has a special ability to move an adjacent piece (friend or foe) by rolling it to an empty space around the Pillbug.

In "Hive," each insect's movement is designed to mimic, in an abstract way, their real-life counterparts' behavior. The strategy in the game hinges on effectively using these movement patterns to surround the opponent's Queen Bee while protecting your own.

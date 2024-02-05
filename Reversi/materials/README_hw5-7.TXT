Overview: This codebase implements a game of Reversi through interactions with the model.IReversi
class. The currently playable implementation is a hexagonal version of reversi, which can be played
by instantiating a model.HexReversi object.

The user should have an understanding of the public model.IReversi methods available, in order to
interact with the model. They also need to know the rules of reversi to prevent invalid move
exceptions from the model. Last, an understanding of the coordinate system used, in order to
execute moves.
Our model uses cubic coordinates which can be constructed either directly, or with coordinates
corresponding to a 2D array representation of the board (which can be converted to cubic).

We envisioned future versions of this code could have different rules surrounding legality or
board dimensions/shape and the code allows for these kinds of abstractions. The interface
enforces that each IReversi can move, pass, return the results of a move, calculate a score,
get the state of a board tile, or get the board size (in terms of rings of tiles).
How these happen are up to specific implementations, leaving room for different rules in
the future. While our code does allow for these changes, it does not abstract unnecessarily.

Quick Start:

model.IReversi myGame = new model.HexReversi(4); // constructs a game of size 4 HexReversi
model.ITile.State blackPiece = model.ITile.State.BLACK; // black piece color
model.ITile.State whitePiece = model.ITile.State.WHITE; // white piece color
myGame.move(new CubeCoord(q, r, s, false), model.ITile.State.WHITE); // call move with cubic
                                                                     coordinates and a given color
myGame.pass(); // switch to the next color's turn

Key Components:
The main component of this project is the model class, HexReversi, which consists of two
major components: the board and the rules.
The board stores all the information about the current state of the game, including the tiles in
play, the color of the last turn, and if the last player to go passed.
The game is played through interactions with the board through the model. The rule-keeping is done
within the methods of the model, by throwing exceptions when invalid input is provided
preventing it from being applied to the board. It enforces valid coordinate input, and keeps
track of moves and turn order.
Another key component is the CubeCoord class, which represents a cubic coordinate with the origin
(0, 0, 0) at the center of the board. This allows the board representation to be
accessible to players that do not need to understand how the board is physically represented in the
model itself while still being able to understand the spatial positions of the tiles.
For details on how to use the cube coordinate system, see the CubeCoord class in the model package.

    Key Subcomponents:
    Our only major subcomponent is the ITile interface. This represents the board
    state through returning the enum State that represents the state of the tile, BLACK, WHITE,
    or NONE. The enums are immutable, but the State at a given position may be changed by moves.


Source Organization:
We store the classes related to the model inside the "model" package. These include the
 ITile and IReversi interfaces, and CubeCoord, HexReversi, and HexTile classes. Our tile
 states are represented as an Enum, which can be found in the ITile interface.
Classes related to the players, including human and ai players, the player interface, and the player
 features interface, are located inside the "player" package.
Strategy classes are located inside the "strategy" package, including the interface, the abstract
 class, and both implemented strategies.
The Controller class and the listener interfaces are located in the "controller" package.
All mocks used for testing purposes are located in a separate "mocks" package.
Finally, view-related classes that implement TextualView and ViewPub interfaces, are located in the
"view" package.


** To avoid the model tracking active players and their colors, our move methods take in a
model.ITile.State enum representing the color of the move to determine which turn it is. Additionally,
since we're using States instead of Players, pass simply flips the "lastWent" State.


Changes for part 2:
The model is now split into two interfaces, ReadOnlyReversi and IReversi. ReadOnlyReversi has
observational methods, like the score, results of a certain move, or if the game is over.
IReversi is the full implementation of the game, adding the move and pass methods onto the
functionality of ReadOnlyReversi.
Additionally, the VisualView interface was added, and its implementation, GraphicView.
Users can click to select or deselect a cell, and hit enter to indicate they want to move
or "p" to pass. Our GraphicView class extends JFrame and HexPanel extends JPanel to visualize and
interact with the board. To draw the tiles, locations are iterated over and are positioned relative
to the window size using trigonometry.
The VisualView interface has no methods, since the graphical view has no behavior outside
of what is extended from the JFrame class. The TextView interface is a separate interface, as the
text and graphical views currently have no shared behavior.

******* EXTRA CREDIT: AvoidAroundCorners class in package "strategy" *******

We also added an IStrategy interface and an abstract class AStrategy that contains some functionality
for picking moves, such as choosing the highest scoring or upper left (to help deal with ties).
HighestScore and AvoidAroundCorners extend AStrategy. HighestScore returns the coordinates of the
move with the highest score, and AvoidAroundCorners returns a move that avoids corners, and either
picks the highest score or a fallback strategy (if provided).


CHANGES FOR PART 3:
For part 3, we redesigned the interface structure related to views in order to accommodate players.
Previously, the GraphicView class only implemented the VisualView interface, which contained
no methods (since there was no player behavior yet to be abstracted). For part 3, we replaced
VisualView with ViewPub, our view features interface, which contains methods for a view to
communicate with a controller. This way, new views can be introduced without having to modify
the controller to accommodate.

ViewPub also extends PlayerActionPub, which is a new interface that abstracts player communication
with a controller. IReversiPlayer is another interface that extends PlayerActionPub to represent
players and containing player-exclusive behavior, currently only getColor. This way, views and
players can interact with the controller in the same way, since an AI player and graphic view both
need to provide moves to the controller. By separating additional methods to ViewPub and IReversiPlayer,
more type-specific behavior including getColor and setVisible can be added. However, combining
them enables potential future objects that may not behave like a view or a player, but still
need to be able to communicate moves to the controller.

The ModelStatusPub interface, which IReversi extends, was also added. This interface contains
methods for a model to communicate with controllers via notifyTurnChange. This enables new models
to communicate without having the change the controller. Additional changes to the model include
pulling hasMoves to ReadOnlyReversi and adding a startGame method to IReversi.

For the controller itself, two new interfaces were added: ModelStatusListener and
PlayerActionListener. Since the model itself is unconcerned with exactly what it is notifying
and what its subscribers are doing with the information, it only needs to ensure its subscribers
are able to receive its specific notifications, by implementing ModelStatusListener. This enables the
addition of any kind of model listeners, not just a reversi controller. The same applies for the
other interface, PlayerActionListener, which represents listeners for players and player-like
objects. The controller, since it must listen to the model, players, and view, implements both
these interfaces.

From the user's perspective, gameplay remains the same, with visual feedback instead of print
statements. Two windows appear, one for each player, with the color of the player in the window
header. Players can select a tile by clicking on it and deselecting by clicking it again, attempt
a move by hitting "enter", or pass by hitting "p". The view now responds to invalid moves with
a popup indicating if a move is invalid or it is not that player's turn. The view will also
indicate to the player turn if they have no valid moves. The player playing as white will always go
first, regardless of whether any players are human or not. When the game is over (which only
occurs when both players have passed), a "game over" popup will appear and indicate which color won.

To launch a game of reversi from the command line, the first value is an integer indicating board
size, followed by up to two keywords for the players. The player keywords are "human", "highscore"
(which creates an AI that uses the highest-score strategy), "avoidcorners" (creating an AI with the
avoiding corners strategy), or "combo" (creating an AI with avoiding corners stragegy plus highest
score as a backup strategy). If less than two player keywords are included, human players will be
added for a total of 2 players. If no command line input is given, a size four game with 2 human
players will be constructed. However, if player keywords are given, a size value MUST be provided,
and it must be the first argument.
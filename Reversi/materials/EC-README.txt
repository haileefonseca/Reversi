All five levels have been implemented successfully in our model. The changes for each level are
as follows:

Level 0: Hints
    Our view design did not previously support any kind of decorator pattern, so as instructed on
Piazza, we redesigned our HexPanel class. Previously, our panel handled all shape-drawing and
determining the state of each tile, so it was impossible to use the decorator pattern to add hints.

    With the new design, we simply pass in a Painter object upon instantiation of view, which is
responsible for accepting coordinates and model information and drawing the given Polygon. With
this, the same Painter objects can draw both hexagons and squares, while the panel class is
responsible for scaling and positioning them based on the type of board. Thus, we have two panel
classes, SquarePanel and HexPanel, which can both be treated as ordinary JPanels in the frame.

    To add the hints, we created a Painter decorator that stores a ShapePainter and draws a score value
if hints are enabled after calling its stored ShapePainter's draw method.

Level 1: Square Reversi
    To implement square reversi, we abstracted HexReversi. Since our board was physically
represented as a square 2D array of tiles, most of the model's behavior did not depend on a
hexagonal representation.

Level 2: View
	To implement the view for this, we needed to add a new class to our view package,
SquarePanel, that would draw Polygons with four vertices instead of six. Due to our view
restructuring, this was the only additional file needed. It functions mostly the same, but with
different board offsets and different position calculations.

Level 3: Controller
	Nothing was changed to be able to implement the controller with both Square and Hex,
and we were able to successfully test communication between our MVC. Our controller did not rely
on any particular board implementation.

Level 4: Strategic Square Reversi
	All we needed to be able to implement SquareReversi was to add some observational
 methods. Our strategies previously assumed a Hex representation but by changing that to
request observations about the state of the model to make its decision, we were able to have
them all function as before.


To play a game of reversi from the command line, there are 3 parts to include:
1) a size value. Size must be 2 or greater, and must be even for a square game of reversi.
2) "hex" or "square", indicating the board shape.
3) up to two player specifications. "human" for human, or one of "highscore", "avoidcorners", or
"combo" for an AI player.
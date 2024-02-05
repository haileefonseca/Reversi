We were able to get all features working, simultaneously running our MVC alongside theirs. A game
could be played with both views and both our and the providers' strategies. Their strategies
have a bug that does not allow it to complete a game past a few moves, where it will continue
to attempt invalid moves. The game will still run without throwing but will need
human input to perform moves. We were able to maintain complete functionality
of our code, and can run our MVC with or without adapters. Their code does not support multiple
board sizes, so if we use their view, we are unable to specify size. Still, if only using our views,
sizes can be anything greater than two. Because of this, we needed to remove the command-line
support for multiple board sizes, so it would need to be explicitly constructed.

Command-line arguments now accept up to two arguments. Defaults are our human players, but users
may enter any of the following:
human, highscore, avoidcorners, combo, providerCaptureMost, providerMinimax, providerAvoidCorners,
providerChooseCorners, and providerTryTwo.
EXAMPLE: "highscore providerTryTwo"
The first player will always have our view, and the player two will always have the providers.
Our providers implementation does not support selecting a tile and handles all clicks as move
attempts, so there is some difference between how our view and theirs behaves.
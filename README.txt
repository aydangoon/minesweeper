Minesweeper README

The classes are:

Constants:
	
	Doesn't actually have an instance. This class just stores a bunch of constants I use in the game
	such a Space width or Game width or number of bombs, etc. I figured it was good practice to store
	all these values in one place. Plus it was smarter to store these values in a final variable because
	then if I ever wanted to globally change a value I would only have to change it in this file and
	it would change everywhere.
	
Game:
	
	An extension of the JPanel class. This is where all game logic and the game GUI is handled. It also
	implements mouselisener so that mouse interacts can be handled in the game. 
	
GameStateHandler:

	This is a class that handles the state of the game including variables such as time, whether the game
	is being played currently, etc. It also has a GUI component so it extends JPanel. Here is where I read
	and write High Scores to highscores.txt

MinesweeperMain:

	This is where we actually have the main method. It extends JFrame and then we add our Game and GameStateHandler
	objects onto that JFrame.
	
Score:

	A simple class. This is an object that represents a name int pair, that is a highscore.
	
Space:

	A class that represents a space. Since a space's position is implicitly stored in the 2D array of board, 
	all this stores is whether a space has a bomb, is flagged, is hidden, and what its number is.
	
----------------------------------------------------------------------------------------------------------------------------

Concepts:

Recursion:

	I use recursion in the method explode found in the Game class. Explode reveals all adjacent 0s and their neighbors
	should a 0 spot be clicked upon by a user. Since if a 0s neighbor is also a 0 the process is identical for that 
	neighbor, I decided to make this method recursive. To make the method as efficient as possible, it only gets
	called on spaces that aren't yet revealed.
	
2D Array:

	I store Spaces representing the board in a 2D array of spaces called board. It takes the form board[column][row]
	since columns are like x values and rows are like y values on a graph.
	
File I/O:

	I store high scores in a file called highscores.txt and read and write to it when appropriate. This allows for global
	high scores to be kept track of regardless of whether the program is open or closed. Every time the game is opened,
	the file is read from and a list of high scores is created. As the program runs this list is changed and every time
	it is changed the file is overwritten with this new list. 
	
Collections:

	I used a LinkedList to store a list of high scores. This list is created every time when the highscores.txt file is
	read from, and a score is added if either the list is less than 10 elements long or if the new high score fits 
	somewhere in the top 10. Since I always add elements in order, the list is implicitly sorted. 

	
package mazeGenerator;

import datatypes.Maze;
import datatypes.Wall;
import datatypes.Node;

public abstract class MazeGen {
	
	protected Maze maze;
	protected long seed;
	protected MazeDrawer drawer;
	protected int startX = 0, 
				  startY = 0;
	protected boolean animate = true;
	protected int animationDelay = 0;
	protected boolean timer = false;
	protected long startTime = -1;
	protected long endTime;
	
	/**
	 * Constructs a MazeGenerator with maze width 
	 * and height given in number of nodes.
	 * @param width measured in nodes
	 * @param height measured in nodes
	 * @param drawer of type MazeDrawer, 
	 * 		  the class which handles the drawing of the maze.
	 */
	public MazeGen(int width, int height, MazeDrawer drawer) {
		this.maze = new Maze(width, height);
		this.drawer = drawer;
		startX = width/2;
		startY = height/2;
	}
	
	/**
	 * Constructs a MazeGenerator with maze width 
	 * and height given in number of nodes.
	 * @param width measured in nodes
	 * @param height measured in nodes
	 * @param drawer of type MazeDrawer, 
	 * 		  the class which handles the drawing of the maze.
	 * @param animate boolean, indicating whether the maze generation process 
	 * 		  should be animated.
	 * @param animation delay integer defining the length in milliseconds
	 * 		  for the delay each animation step. (i.e. each time the maze
	 * 		  is redrawn.)
	 */
	public MazeGen(int width, int height, MazeDrawer drawer, 
				   boolean animate, int animationDelay) {
		this(width, height, drawer);
		this.animate = animate;
		this.animationDelay = animationDelay;
	}
	
	/**
	 * Generates a maze with just walls
	 */
	public void generate() {
		setAllWalls();
		setAllNodes();
	}
	
	/**
	 * Gets the seed used to create the maze
	 * @return long seed which can be passed to recreate the maze
	 */
	public long getSeed() {
		return seed;
	}

	/**
	 * Pauses execution for the delay duration 
	 * specified in the delayDuration variable
	 */
	public void delay() {
		try {
			Thread.sleep(animationDelay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void setAnimationDelay(int animationDelay) {
		if (animationDelay < 0) {
			System.err.println("Animation delay cannot be negative!");
			animationDelay = 0;
		}
	}
	
	/**
	 * Creates all possible walls in the maze.
	 */
	public void setAllWalls() {
		int mazeWidth = maze.getWidth();
		int mazeHeight = maze.getHeight();
		for (int y = 0; y < mazeHeight; y++) {
			for (int x = 0; x < mazeWidth; x++) {
				Node currentNode = new Node(x, y);
				Node[] neighbours = currentNode.getNeighboursArray(mazeWidth, 
																   mazeHeight);
				for (int i = 0; i < neighbours.length; i++) {
					Node neighbour = neighbours[i];
					if (neighbour != null) {
						maze.addWall(currentNode, neighbour);
					}
				}
			}
		}
	}
	
	/**
	 * Gives all nodes in the maze a value of 1, 
	 * indicating that they have not yet been visited
	 */
	public void setAllNodes() {
		for (int y = 0; y < maze.getHeight(); y++) {
			for (int x = 0; x < maze.getWidth(); x++) {
				maze.setNodeValue(x, y, 1);
			}
		}
	}

	/**
	 * Fetches the Maze-instance for this MazeGenerator
	 * @return maze object representing the current maze
	 */
	public Maze getMaze() {
		return this.maze;
	}
	
	/**
	 * Is called when the maze is changed and needs updating.
	 * Delays and then updates the drawn maze.
	 */
	public void mazeChanged() {
		if (animate) {
			delay();
			this.drawer.updateMaze(this.maze);
		}
	}
	
	public void startTimer() {
		startTime = System.currentTimeMillis();
	}
	
	public long endTimer() {
		if (startTime < 0) {
			throw new IllegalStateException("Attempted to stop timer before"
					+ "timer was started!");
		}
		endTime = System.currentTimeMillis();
		long elapsed = endTime - startTime;
		startTime = endTime = 0;
		return elapsed;
	}

	/**
	 * Fetches the wall-iterator from the current maze and returns it.
	 * @return iterator containing all the walls in the current maze
	 */
	public Iterable<Wall> getWallsIterator() {
		return maze.getWallsIterator();
	}
	
	@Override
	public String toString() {
		return maze.toString();
	}

}





























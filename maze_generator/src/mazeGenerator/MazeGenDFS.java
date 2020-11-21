package mazeGenerator;

import java.util.Random;
import java.util.Stack;

import datatypes.Node;
import datatypes.NodeCollection;
import program.MazeGenerator;

public class MazeGenDFS extends MazeGen {
	
	private Random rnd = new Random();
	
	/**
	 * Constructs a maze generator which uses the randomized DFS algorithm
	 * @param width of the maze measured in number of nodes
	 * @param height of the maze measured in number of nodes
	 * @param drawer of type MazeDrawer, 
	 * 		  the class which handles the drawing of the maze.
	 * @param animate boolean, indicating whether the maze generation process 
	 * 		  should be animated.
	 * @param animation delay integer defining the length in milliseconds
	 * 		  for the delay each animation step. (i.e. each time the maze
	 * 		  is redrawn.)
	 * @param timer boolean, indicating whether the maze generation process
	 * 		  should be timed and printed to the console.
	 */
	public MazeGenDFS(int width, int height, MazeDrawer drawer,
					  boolean animate, int animationDelay) {
		super(width, height, drawer, animate, animationDelay);
		seed = rnd.nextLong();
		rnd.setSeed(seed);
	}
	
	/**
	 * Constructs a maze generator which uses the randomized DFS algorithm
	 * @param width of the maze measured in number of nodes
	 * @param height of the maze measured in number of nodes
	 * @param drawer of type MazeDrawer, 
	 * 		  the class which handles the drawing of the maze.
	 * @param seed to generate a maze identical each time
	 * @param animate boolean, indicating whether the maze generation process 
	 * 		  should be animated.
	 * @param animation delay integer defining the length in milliseconds
	 * 		  for the delay each animation step. (i.e. each time the maze
	 * 		  is redrawn.)
	 * @param timer boolean, indicating whether the maze generation process
	 * 		  should be timed and printed to the console.
	 */
	public MazeGenDFS(int width, int height, MazeDrawer drawer,
					  long seed, boolean animate, int animationDelay) {
		this(width, height, drawer, animate, animationDelay);
		if (seed != 0) {
			rnd.setSeed(seed);
		}
	}

	/**
	 * Generates a maze with randomized-DFS
	 */
	@Override
	public void generate() {
		super.generate();
		int startX = maze.getWidth()/2;
		int startY = maze.getHeight()/2;
		if (MazeGenerator.DEBUG) {
			startTimer();
		}
		Node startNode = new Node(startX, startY);
		NodeCollection visited = new NodeCollection();
		visited.add(startNode);
		maze.setNodeValue(startNode, 0);
		mazeChanged();
		Stack<Node> stack = new Stack<Node>();
		stack.push(startNode);
		Node currentNode, chosenNode;
		while (!stack.isEmpty()) {
			currentNode = stack.pop();
			NodeCollection neighbours = currentNode.getNeighbours(maze.getWidth(), maze.getHeight());
			NodeCollection unvisitedNeighbours = new NodeCollection();
			for (Node neighbour : neighbours) {
				if (!visited.contains(neighbour)) {
					unvisitedNeighbours.add(neighbour);
				}
			}
			if (unvisitedNeighbours.size() > 0) {
				stack.push(currentNode);
				int randint = rnd.nextInt(unvisitedNeighbours.size());
				chosenNode = unvisitedNeighbours.get(randint);
				maze.removeWall(currentNode, chosenNode);
				visited.add(chosenNode);
				maze.setNodeValue(chosenNode, 0);
				stack.push(chosenNode);
				mazeChanged();
			}
		}
		if (MazeGenerator.DEBUG) {
			long elapsed = endTimer();
			System.out.println("Generation: " + elapsed + "ms");
		}
	}

}





























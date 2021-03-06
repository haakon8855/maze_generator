package program;

import mazeGenerator.MazeDrawer;
import mazeGenerator.MazeGen;
import mazeGenerator.MazeGenDFS;
import mazeGenerator.MazeGenPrim;
import mazeGenerator.SwingWorkerGenerate;

public class MazeGenerator {
	
	public static boolean DEBUG = false;
	
	private int width, height, animationDelay, minDelay, maxDelay;
	private MazeType mazeType;
	private long seed;
	private boolean timer, animate;
	
	private MazeDrawer drawer;
	private MazeGen gen;
	
	public void init(String config) {
		MazeGeneratorReadConfig cfg = new MazeGeneratorReadConfig(config);

		this.width = cfg.getPositiveInteger("width");
		this.height = cfg.getPositiveInteger("height");
		setDimensions(width, height);

		this.mazeType = cfg.getMazeType();

		this.animate = cfg.getBoolean("animate");
		this.animationDelay = cfg.getPositiveInteger("animationDelay");
		this.minDelay = cfg.getPositiveInteger("animationSliderMin");
		this.maxDelay = cfg.getPositiveInteger("animationSliderMax");

		this.timer = cfg.getBoolean("timer");
		
		this.seed = cfg.getLong("seed");

		this.drawer = new MazeDrawer(width, height, animationDelay, animate,
									 seed, this);
		this.drawer.setWidthValue(width);
		this.drawer.setHeightValue(height);
	}
	
	/**
	 * Set the maze dimensions
	 * @param width in blocks
	 * @param height in blocks
	 */
	public void setDimensions(int width, int height) {
		width = Math.max(5, width);
		height = Math.max(5, height);
		width = Math.min(width, 3*height);
		height = Math.min(height, 3*width);
		this.width = width;
		this.height = height;
	}
	
	/**
	 * Getter for maze Width
	 * @return Width of the maze in blocks
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Getter for maze height
	 * @return Height of the maze in blocks
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Gets the currently selected maze type
	 * @return String representing the currently selected type
	 */
	public MazeType getMazeType() {
		return mazeType;
	}
	
	/**
	 * Sets the maze type
	 * @param String mazeType representing the currently selected type
	 */
	public void setMazeType(MazeType mazeType) {
		this.mazeType = mazeType;
	}
	
	/**
	 * Sets the seed to use for next generation
	 * @param seed
	 */
	public void setSeed(long seed) {
		this.seed = seed;
	}
	
	/**
	 * Sets whether animations are to be shown or not
	 * @param showAnimations, boolean
	 */
	public void setAnimation(boolean showAnimations) {
		this.animate = showAnimations;
	}
	
	/**
	 * Sets the speed at which animations are to be drawn
	 * @param float animationSpeed, number between 0 and 1 indicating slider val
	 */
	public void setAnimationSpeed(float animationSpeed) {
		float inverse = 1 - animationSpeed;
		this.animationDelay = Math.round((inverse * (maxDelay-minDelay)) + minDelay);
	}
	
	/**
	 * Gets the speed at which animations are to be drawn
	 * @return A float between 0 and 1 representing the animation speed
	 */
	public float getAnimationSpeed() {
		float inverse = ((float) this.animationDelay - minDelay) / (maxDelay-minDelay);
		float speed = 1 - inverse;
		return speed;
	}

	/**
	 * Creates a new maze generator corresponding to the currently selected maze type
	 * @return A MazeGen instance
	 */
	private MazeGen makeNewGenerator() {
		switch (mazeType) {
			case DFS:
				gen = new MazeGenDFS(width, height, drawer, seed,
									 animate, animationDelay);
				break;
			case PRIM:
				gen = new MazeGenPrim(width, height, drawer, seed,
									  animate, animationDelay);
				break;
			default:
				System.out.println("Invalid maze type, check README.md for guide");
				break;
		}
		return gen;
	}
	
	/**
	 * Generates the maze itself, can be called multiple times to create new.
	 * @param worker of SwingWorker type to run maze generation on separate thread.
	 */
	public void generate(SwingWorkerGenerate worker) throws InterruptedException {
		makeNewGenerator();
		drawer.deactivateGenerationBtn();
		drawer.setSeedValue(gen.getSeed());
		if (gen != null) {
			long startTime = 0;
			long endTime = 0;
			long elapsed;
			if (timer) {
				startTime = System.currentTimeMillis();
			}
			gen.generate(worker);
			if (timer) {
				endTime = System.currentTimeMillis();
				elapsed = endTime - startTime;
				drawer.showTime();
				drawer.setTime(elapsed);
				System.out.println("Time: " + elapsed + "ms");
			}
			drawer.updateMaze(gen.getMaze());
			drawer.activateGenerationBtn();
		}
	}

	public static void main(String[] args) {
		MazeGenerator generator = new MazeGenerator();
		generator.init("config.conf");
	}

}

public class OLAGraph {
	private int rows;
	private int cols;
	private int[] layout;
	private int[][] connectionMatrix;

	public OLAGraph(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		layout = new int[rows * cols];
		connectionMatrix = new int[rows * cols][rows * cols];
	}

	public OLAGraph(int rows, int cols, int[] layout, int[][] connectionMatrix) {
		this.rows = rows;
		this.cols = cols;
		this.layout = layout;
		this.connectionMatrix = connectionMatrix;
	}

	public int findFitness() {
		
	}
}
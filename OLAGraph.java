public class OLAGraph {
	private int rows;
	private int cols;
	private int[] layout;
	private int[][] connectionMatrix;
	private int fitness;

	public OLAGraph(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		layout = new int[rows * cols];
		connectionMatrix = new int[rows * cols][rows * cols];
		fitness = findFitness();
	}

	public OLAGraph(int rows, int cols, int[] layout, int[][] connectionMatrix) {
		this.rows = rows;
		this.cols = cols;
		this.layout = layout;
		this.connectionMatrix = connectionMatrix;
		fitness = findFitness();
	}

	public int getFitness() {
		return fitness;
	}

	public int getLength() {
		return rows * cols;
	}

	public int[] getLayout() {
		return layout;
	}

	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return cols;
	}

	public int[][] getConnectionMatrix() {
		return connectionMatrix;
	}

	private int findFitness() {
		int fitness = 0;
		int offset = 0;
		for (int vertex = 0; vertex < layout.length; vertex++) {
			for (int vertexToConnect = vertex; vertexToConnect < layout.length; vertexToConnect++) {
				int connections = connectionMatrix[vertex][vertexToConnect];
				int distance = computeDistance(layout[vertex], layout[vertexToConnect]);
				fitness += connections * distance;
			}
		}
		return fitness;
	}

	private int computeDistance(int index1, int index2) {
		int index1X = index1 % cols;
		int index1Y = index1 / rows;
		int index2X = index2 % cols;
		int index2Y = index2 / rows;
		return Math.abs(index2X - index1X) + Math.abs(index2Y - index1Y);
	}


	public String toString() {
		String info = "Fitness: " + fitness + " Rows: " + rows + "Columns: " + cols + " Layout: ";
		for(int i = 0; i < layout.length; i++) {
			if(i % cols == 0)
				info += "\n";
			info += layout[i];
			if(i != layout.length)
				info += ",";
		}
		return info;
	}

	public OLAGraph copy() {
		return new OLAGraph(rows, cols, layout, connectionMatrix);
	}

	public static int[][] generateConnectionMatrix(int rows, int cols, int minConnections, int maxConnections) {
		int length = rows * cols;
		int[][] generatedMatrix = new int[length][length];

		for (int vertex = 0; vertex < length; vertex++) {
			for (int vertexToConnect = vertex + 1; vertexToConnect < length; vertexToConnect++) {
				int connections = (int)(Math.random() * (maxConnections - minConnections + 1)) + minConnections;
				generatedMatrix[vertex][vertexToConnect] = connections;
				generatedMatrix[vertexToConnect][vertex] = connections;
			}
		}
		return generatedMatrix;
	}

}
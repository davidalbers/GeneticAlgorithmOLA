public class OLAGraph implements Comparable<OLAGraph> {
	private int rows;
	private int cols;
	private int[] layout;
	private int[][] connectionMatrix;
	private int fitness = -1;

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

	public int getFitness() {
		if(fitness == -1)
			fitness = findFitness2();
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

	private int findFitness2() {
		int fitness = 0;
		for (int pos1 = 0; pos1 < layout.length; pos1++) {
			for(int pos2 = pos1; pos2 < layout.length; pos2++) {
				int vertex = layout[pos1];
				int vertexToConnect = layout[pos2];
				int connections = connectionMatrix[vertex][vertexToConnect];
				int distance = computeDistance(pos1, pos2);
				fitness += connections * distance;
			}
		}
		return fitness;
	}

	private int computeDistance(int index1, int index2) {
		int index1X = index1 % cols;
		int index1Y = index1 / cols;
		int index2X = index2 % cols;
		int index2Y = index2 / cols;
		return Math.abs(index2X - index1X) + Math.abs(index2Y - index1Y);
	}
	
	public static int[] computeDirectionVector(int index1, int index2, int cols) {
		int index1X = index1 % cols;
		int index1Y = index1 / cols;
		int index2X = index2 % cols;
		int index2Y = index2 / cols;
		return new int[]{(index2X - index1X), (index2Y - index1Y)};
	}
	
	 public static int[] indexToCoordinates(int index, int cols) {
	 	int indexX = index % cols;
		int indexY = index / cols;
		return new int[]{indexX, indexY};
	 }

	public String toString() {
		String info = "Fitness: " + getFitness() + " Rows: " + rows + "Columns: " + cols + " Layout: ";
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
		for(int i = 0; i < (rows * cols) / 10; i++) {
			generatedMatrix[(int)(Math.random() * length)][(int)(Math.random() * length)] *= 10;
		}
		for(int i = 0; i < (rows * cols) / 10; i++) {
			generatedMatrix[(int)(Math.random() * length)][(int)(Math.random() * length)] = 0;
		}
		return generatedMatrix;
	}

	public int[] generateReverseLookup() {
		int[] reverse = new int[layout.length];
		for(int i = 0; i < reverse.length; i++) {
			reverse[layout[i]] = i;
		}
		return reverse;
	}

	public int compareTo(OLAGraph toCompare) {
		if(getFitness() < toCompare.getFitness()) 
			return -1;
		else if(getFitness() > toCompare.getFitness()) 
			return 1;
		else return 0;
	}

}

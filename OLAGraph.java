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
			fitness = findFitness();
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
		for (int pos1 = 0; pos1 < layout.length; pos1++) {
			for(int pos2 = pos1; pos2 < layout.length; pos2++) {
				int vertex = layout[pos1];
				int vertexToConnect = layout[pos2];
				if(vertex != -1 && vertexToConnect != -1) {
					int connections = connectionMatrix[vertex][vertexToConnect];
					int distance = computeDistance(pos1, pos2);
					fitness += connections * distance;
				}
			}
		}
		return fitness;
	}

	/**
	* Compute the Euclidean distance between two indices in the layout array
	*/
	private int computeDistance(int index1, int index2) {
		int index1X = index1 % cols;
		int index1Y = index1 / cols;
		int index2X = index2 % cols;
		int index2Y = index2 / cols;
		return Math.abs(index2X - index1X) + Math.abs(index2Y - index1Y);
	}
	
	/**
	* Given two vertices, determine a direction vector from one vertex to another
	*/
	public static int[] computeDirectionVector(int index1, int index2, int cols) {
		int index1X = index1 % cols;
		int index1Y = index1 / cols;
		int index2X = index2 % cols;
		int index2Y = index2 / cols;
		return new int[]{(index2X - index1X), (index2Y - index1Y)};
	}
	
	/**
	* Convert index in array to 2-dimensional coordinates
	*/
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

	public static String connectionMatrixToString(int[][] connection) {
		int sum = 0;
		String matrixString = "{\n";
		for(int i = 0; i < connection.length; i++) {
			matrixString += "\t{";
			for (int j = 0; j < connection[i].length; j++) {
				matrixString += connection[i][j];
				sum += connection[i][j];
				if(j != connection[i].length - 1)
					matrixString += ",";
			}
			matrixString += "}";
			if(i != connection.length -1)
				matrixString += ",";
			matrixString += "\n";
		}
		matrixString += "}";
		int averageNumConn = (int)((double)sum / (connection.length * connection[0].length) + .5);
		matrixString += "\n Average number connections: " + averageNumConn;
		return matrixString; 
	}

	public OLAGraph copy() {
		return new OLAGraph(rows, cols, layout, connectionMatrix);
	}

	/**
	* Randomly generate a connection matrix
	*/
	public static int[][] generateConnectionMatrix(int rows, int cols, int minConnections, int maxConnections, int weightedness) {
		int length = rows * cols;
		int[][] generatedMatrix = new int[length][length];
		for (int vertex = 0; vertex < length; vertex++) {
			for (int vertexToConnect = vertex + 1; vertexToConnect < length; vertexToConnect++) {
				int connections = generateWeightedRandomNumber(minConnections, maxConnections, weightedness); 
				generatedMatrix[vertex][vertexToConnect] = connections;
				generatedMatrix[vertexToConnect][vertex] = connections;
			}
		}
		return generatedMatrix;
	}

	/**
	* Generate a random number that is weighted/biased towards smaller numbers
	* For more bias make weight larger. 1 is no bias.
	*/
	public static int generateWeightedRandomNumber(int min, int max, int weightedness) {
		//Generate a random number and look it up in a table defined by
		// for(index x in range min to max)
		//    y = ((i+1) / (max - min))^(1/weightedness)
		// for weigtedness = 2, min =0, max = 4
		// x|y  (if random <= y then )
		// --- 
		// 0|0.447
		// 1|0.632
		// 2|0.774
		// 3|0.894
		// 4|1
		int rand =  (int)Math.ceil( Math.pow(Math.random(), weightedness) * (max - min + 1) - 1);
		if(rand == -1) //account for the fact that numbers like -9.99e-19 == -1 because of precision
			rand = 0;
		return rand;
	}

	/**
	* Make an array where arr[i] = j
	* where i is the vertex and j is its placement
	*/
	public int[] generateReverseLookup() {
		int[] reverse = new int[layout.length];
		for(int i = 0; i < reverse.length; i++) {
			reverse[layout[i]] = i;
		}
		return reverse;
	}

	/**
	* Compare graphs based on fitness 
	*/
	public int compareTo(OLAGraph toCompare) {
		if(getFitness() < toCompare.getFitness()) 
			return -1;
		else if(getFitness() > toCompare.getFitness()) 
			return 1;
		else return 0;
	}

}

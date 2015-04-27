public class OLAGreedyAlgorithm {

	private int[][] connectionMatrix;
	private int rows;
	private int cols;
	private boolean[] addedConnections;
	private int[] layout;
	private boolean firstPlacement = true;
	private int fitnessEvals = 0;
	private int totalFitnessEvals;
	private long startTime;

// 	PPPGreedy:
// where g is an empty graph of size mxn
// where c is a list of vertices to place of size m*n
// where m is a connection matrix of size m*n x m*n
// remove vertex with maximum total connections from c
// place maximally connected vertex in middle of g
// while(c not empty):
// find vertex cm in c with most connections to vertices in g
// remove cm from c
// try all possible placements for cm 
// choose the optimal placement using fitness function with m
// place cm in optimal placement
// done.
// For n comp
	public OLAGreedyAlgorithm(int rows, int cols, int[][] connectionMatrix) {
		this.connectionMatrix = connectionMatrix;
		this.rows = rows;
		this.cols = cols;
		layout = new int[rows*cols];
		addedConnections = new boolean[rows*cols];
		totalFitnessEvals = (rows*cols - 1)*(rows*cols)/2;
	}

	public void run() {
		startTime = System.currentTimeMillis();
		for(int i = 0; i < rows*cols; i++) {
				layout[i] = -1;
		} 
		int connectionsMade = 0;
		System.out.print("Greedy algorithm percent: 0%");
		while(connectionsMade != rows * cols) {
			if(firstPlacement) {
				//find maximally connected edge and place it in middle
				int maxEdge = findMaxConnectedVertex();
				int row = rows / 2;
				int col = cols /2;
				int placement = row * cols + col;
				layout[placement] = maxEdge;
				firstPlacement = false;
				addedConnections[maxEdge] = true;
			}
			else {
				//vertex with most connections to vertices which have already been placed
				int maxEdge = findNextMaxConnectedVertex();
				//find the optimal placement for this vertex
				int minPlacement = findOptimalPlacementForVertex(maxEdge);
				//place the vertex
				layout[minPlacement]=maxEdge;
				addedConnections[maxEdge] = true;
			}
			connectionsMade++;
			//print progess updates
			if(connectionsMade == (rows*cols/4))
				System.out.print(" 25%");
			if(connectionsMade == (rows*cols/2))
				System.out.print(" 50%");
			if(connectionsMade == (3*rows*cols/4))
				System.out.print(" 75%");
		}

		OLAGraph newGraph = new OLAGraph(rows, cols, layout, connectionMatrix);
		System.out.println("\nGreedy algorithm found this graph:\n" + newGraph.toString() + "\n in " + (System.currentTimeMillis() - startTime) + "ms");
	}

	public  int findMaxConnectedVertex() {
		int maxConnections = -1;
		int maxEdge = 0;
		//iterate overconnection matrix and find max
		for(int edge = 0; edge < rows*cols; edge++) {
			int sum = 0;
			for(int j = 0; j< rows*cols; j++) {
				sum += connectionMatrix[edge][j];
			}
			if(sum > maxConnections) {
				maxConnections = sum;
				maxEdge = edge;
			}
		}
		return maxEdge; 
	}

	public  int findNextMaxConnectedVertex() {
		int maxEdge = 0;
		int maxConnections = -1;
		//iterate over connections
		for(int edge = 0; edge < rows*cols; edge++) {
			int sum = 0;
			//if connection has not been added find how many connections is has to vertices that have been added
			if(!addedConnections[edge]) {
				for(int existingEdge = 0; existingEdge < rows*cols; existingEdge++){
					if(addedConnections[existingEdge]) {
						sum += connectionMatrix[edge][existingEdge];
					}
				} 
			}
			if(sum > maxConnections) {
				maxEdge = edge;
				maxConnections = sum;
			}
		}
		return maxEdge;
	}

	public  int findOptimalPlacementForVertex(int edgeToPlace) {
		int minxyPlacement = 0;
		int minFitness = -1;
		//try all possible placements for the new vertex and choose the one which has the lowest fitness
		for(int i = 0; i < rows*cols; i++) {
			if(layout[i] == -1) {
				layout[i] = edgeToPlace;
				OLAGraph newGraph = new OLAGraph(rows, cols, layout, connectionMatrix);
				if(newGraph.getFitness() < minFitness || minFitness == -1) {
					minFitness = newGraph.getFitness();
					minxyPlacement = i;
				}
				layout[i] = -1;
				fitnessEvals++;
			}
		}
		return minxyPlacement;
	}
	

}

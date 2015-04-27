import java.util.*;
public class OLABruteForce {
	private int minFitness = -1;
	private int[][] connectionMatrix;
	private int rows;
	private int cols; 
	private int[] minLayout;
	private int count = 0;
	private long startTime;


	public OLABruteForce(int rows, int cols, int[][] connectionMatrix) {
		this.rows = rows;
		this.cols = cols;
		this.connectionMatrix = connectionMatrix;
	}

	public void run() {
		this.startTime = System.currentTimeMillis();

		//build list of vertices
		int[] ns = new int[rows * cols];
		for(int i = 0; i < rows * cols; i++) 
			ns[i] = i;
		//permuate the list and find best permuation
		permute(ns, ns.length);
		System.out.println("***Brute force determined min fitness: " + minFitness + " with layout\n" + Arrays.toString(minLayout));
	}

	/**
	 * Permute algorithm from:
	 * http://lucitworks.com/Snippets/Algorithms/permutation.htm
	 */

	//swap i and j
	private static void swap(int[] v, int i, int j) {
		int t = v[i]; v[i] = v[j]; v[j] = t;
	}
 
 	/**
 	*Find all permuations of a list v recursively
 	*/
	public void permute(int[] v, int n) {
		if (n == 1) {
			//found a new permutation, evaluate the fitness
			OLAGraph permGraph = new OLAGraph(rows, cols, v, connectionMatrix);
			if(permGraph.getFitness() < minFitness || minFitness == -1) {
				minFitness = permGraph.getFitness();
				minLayout = new int[v.length];
				System.arraycopy(v, 0, minLayout, 0, v.length );
			}
			count++;

		} else {
			for (int i = 0; i < n; i++) {
				permute(v, n-1);
				if (n % 2 == 1) {
					swap(v, 0, n-1);
				} else {
					swap(v, i, n-1);
				}
			}
		}
	}
	 

	 
 
}
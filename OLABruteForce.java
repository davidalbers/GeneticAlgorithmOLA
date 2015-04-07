import java.util.*;
public class OLABruteForce {
	public static void main(String [] args) {
		int rows = 3;
		int cols = 3;
		int[][] connectionMatrix = OLAGraph.generateConnectionMatrix(rows, cols, 0, 100, 2);
		ArrayList<Integer> vertices = new ArrayList<Integer>();
		for(int i = 0; i < rows * cols; i++)
			vertices.add(i);
		ArrayList<ArrayList<Integer>> permutations = generatePerm(vertices);
		int min = 0;
		int minFitness = 0;
		for(int j = 0; j < permutations.size(); j++) {
			ArrayList<Integer> perm = permutations.get(j);
			//System.out.println("permuation " + j + " of " + permutations.size());
			int[] permArr = new int[perm.size()];
			for(int i = 0; i < perm.size(); i++) {
				permArr[i] = perm.get(i);
			}
			OLAGraph permGraph = new OLAGraph(rows, cols, permArr, connectionMatrix);
			int fitness = permGraph.getFitness();
			if(fitness < minFitness || j == 0) {
				min = j;
				minFitness = fitness;
			}
		}
		System.out.println("min fitness : " + minFitness + "\n");
		ArrayList<Integer> minGraph = permutations.get(min);
		for(int i : minGraph)
			System.out.print(i + ",");
		System.out.println("\nConnection\n" + OLAGraph.connectionMatrixToString(connectionMatrix));
	}

	public static ArrayList<ArrayList<Integer>> generatePerm(ArrayList<Integer> original) {
     if (original.size() == 0) { 
       ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
       result.add(new ArrayList<Integer>());
       return result;
     }
     Integer firstElement = original.remove(0);
     ArrayList<ArrayList<Integer>> returnValue = new ArrayList<ArrayList<Integer>>();
     ArrayList<ArrayList<Integer>> permutations = generatePerm(original);
     for (ArrayList<Integer> smallerPermutated : permutations) {
       for (int index=0; index <= smallerPermutated.size(); index++) {
         ArrayList<Integer> temp = new ArrayList<Integer>(smallerPermutated);
         temp.add(index, firstElement);
         returnValue.add(temp);
       }
     }
     return returnValue;
   }
}
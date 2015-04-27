import java.util.ArrayList;
import java.util.Collections;
import java.io.PrintWriter;
public class HillClimbing{

	private int rows;
	private int cols;
	private int[][] connectionMatrix;
	private double initialTemperature;
	private int initialIterations;
	private int maxTimeMs;
	private double alpha;
	private double beta;
	private int maxIterations;
	private String name;
	private int perturbationFunction;

	public HillClimbing(int rows, int cols, int[][] connectionMatrix, int maxIterations, int maxTimeMs, int perturbationFunction, String name) {
		this.rows = rows;
		this.cols = cols;
		this.connectionMatrix = connectionMatrix;
		this.maxIterations = maxIterations;
		this.maxTimeMs = maxTimeMs;
		this.perturbationFunction = perturbationFunction;
		this.name = name;
	}

	public void runHillClimb() {
		OLAGraph initialSolution = generateSolution(rows, cols, connectionMatrix);
		OLAGraph solutionN = initialSolution;
		int perturbationCount = 0;
		int minFitness = initialSolution.getFitness();
		String tag = name + ", ";
		if(perturbationFunction == 0)
			tag += "pairwise";
		else
			tag += "cycle";
		long startTime = System.currentTimeMillis();
		System.out.println(name + ", initial fitness " + initialSolution.getFitness());

		ArrayList<Integer> fitnesses = new ArrayList<Integer>();
		ArrayList<Integer> fitnessesOccurances = new ArrayList<Integer>();
		fitnesses.add(minFitness);
		fitnessesOccurances.add(0);

		while(perturbationCount != maxIterations && (System.currentTimeMillis() - startTime) < maxTimeMs) {
			OLAGraph newSolution;
			//perturb
			if(perturbationFunction == 0)
				newSolution = pairwiseExchange(solutionN);
			else
				newSolution = cycleLength3(solutionN);
			//choose only better solutions
			if(newSolution.getFitness() < solutionN.getFitness()) {
				solutionN = newSolution;
				minFitness = solutionN.getFitness();
				fitnesses.add(minFitness);
				fitnessesOccurances.add(perturbationCount);
				System.out.println(name + " pertn count " + perturbationCount + " fitness " + solutionN.getFitness());
			}
			perturbationCount++;
		}

		//save logged data
		try {
			PrintWriter writer = new PrintWriter(tag+System.currentTimeMillis()+".ola", "UTF-8");
			writer.println("finished in " + (System.currentTimeMillis() - startTime) + "ms and " + perturbationCount + " iterations.");
			writer.println("resulting layout " + solutionN.toString());
			writer.println();
			writer.println("fitnesses");
			for(int i : fitnesses)
				writer.print(i + ",");
			writer.println();
			writer.println("fitnessesOccurances");
			for(int i : fitnessesOccurances)
				writer.print(i + ",");
			writer.close();
		}
		catch(Exception ex) {
				System.out.println("error writing output.");
		}
	}
	
	/**
	* Swap two alleles randomly
	*/
	public OLAGraph pairwiseExchange(OLAGraph oldSoln) {
		//System.out.println("before exchange\n" + oldSoln.toString());
		int[] layout = oldSoln.copy().getLayout();

		int swapIndex1 = (int)(Math.random() * layout.length);
		int swapIndex2 = (int)(Math.random() * layout.length);
		int swapTemp = layout[swapIndex1];

		layout[swapIndex1] = layout[swapIndex2];
		layout[swapIndex2] = swapTemp;
		OLAGraph newSoln = new OLAGraph(oldSoln.getRows(), oldSoln.getColumns(), layout, oldSoln.getConnectionMatrix());
		//System.out.println("after exchange\n" + newSoln.toString());
		return newSoln;
	}
	
	/**
	* Choose 3 alleles randomly, swap them in a cycle
	*/
	public OLAGraph cycleLength3(OLAGraph oldSoln) {
		
		int[] layout = oldSoln.copy().getLayout();

		int cycleI = (int)(Math.random() * layout.length);
		int cycleJ = (int)(Math.random() * layout.length);
		while(cycleJ == cycleI) {
			cycleJ = (int)(Math.random() * layout.length);
		}
		int cycleK = (int)(Math.random() * layout.length);
		while(cycleK == cycleI || cycleK == cycleJ) {
			cycleK = (int)(Math.random() * layout.length);
		}
		
		//System.out.println(cycleI + "," + cycleJ + "," + cycleK + " before exchange\n" + oldSoln.toString());
		int cycleINew = layout[cycleK];
		int cycleKNew = layout[cycleJ];
		int cycleJNew = layout[cycleI];
		
		layout[cycleI] = cycleINew;
		layout[cycleJ] = cycleJNew;
		layout[cycleK] = cycleKNew;
		OLAGraph newSoln = new OLAGraph(oldSoln.getRows(), oldSoln.getColumns(), layout, oldSoln.getConnectionMatrix());
		//System.out.println("after exchange\n" + newSoln.toString());
		return newSoln;
	}
	
	/**
	* Generate a random solution
	*/
	public OLAGraph generateSolution(int rows, int cols, int[][] connMatrix) {
		ArrayList<Integer> availablePositions = new ArrayList<Integer>();
		//list of 1,2,3,...n
		for(int i = 0; i < rows * cols; i++) {
			availablePositions.add(i);
		}
		//remove the next element in list and place it in an empty spot randomly
		int[] randomLayout = new int[rows * cols];
		for(int i = 0; i < randomLayout.length; i++) {
			int vertexPosition = availablePositions.remove( (int)(Math.random() * availablePositions.size()) );
			randomLayout[i] = vertexPosition;
		}
		return new OLAGraph(rows, cols, randomLayout, connMatrix);
	}
}
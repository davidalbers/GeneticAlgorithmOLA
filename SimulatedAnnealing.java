import java.util.ArrayList;
import java.util.Collections;
import java.io.PrintWriter;
public class SimulatedAnnealing {

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

	public SimulatedAnnealing(int rows, int cols, int[][] connectionMatrix, double initialTemperature, 
		int initialIterations, double alpha, double beta, int maxIterations, int maxTimeMs, int perturbationFunction, String name) {
		this.rows = rows;
		this.cols = cols;
		this.connectionMatrix = connectionMatrix;
		this.initialTemperature = initialTemperature;
		this.initialIterations = initialIterations;
		this.alpha = alpha;
		this.beta = beta;
		this.maxIterations = maxIterations;
		this.maxTimeMs = maxTimeMs;
		this.perturbationFunction = perturbationFunction;
		this.name = name;
	}
	
	public void runSA() {
		OLAGraph initialSolution = generateSolution(rows, cols, connectionMatrix);
		OLAGraph solutionN = initialSolution;
		double temperature = initialTemperature;
		double iterations = initialIterations;
		int count = 0;
		int perturbationCount = 0;
		int leastFitParentCount = 0;
		int fitParentCount = 0;
		int minFitness = initialSolution.getFitness();
		//Variables used for logging important data
		ArrayList<Integer> fitnesses = new ArrayList<Integer>();
		ArrayList<Integer> fitnessesOccurances = new ArrayList<Integer>();
		fitnesses.add(minFitness);
		fitnessesOccurances.add(0);
		String tag = name + ", ";
		if(perturbationFunction == 0)
			tag += "pairwise";
		else
			tag += "cycle";
		long startTime = System.currentTimeMillis();
		System.out.println(name + ", initial fitness " + initialSolution.getFitness());

		while(count != maxIterations && (System.currentTimeMillis() - startTime) < maxTimeMs) {
			int innerLoopCount = 0;
			while(innerLoopCount < iterations) {
				OLAGraph newSolution;
				//perturb
				if(perturbationFunction == 0)
					newSolution = pairwiseExchange(solutionN);
				else
					newSolution = cycleLength3(solutionN);
				perturbationCount++;
				//choose better solution
				if(newSolution.getFitness() < solutionN.getFitness()) {
					fitParentCount++;
					solutionN = newSolution;
				}//or choose worse solution, maybe...
				else if (acceptLeastFitSolution(solutionN, newSolution, temperature)) {
					solutionN = newSolution;
					leastFitParentCount++;
				}
				innerLoopCount++;
			}
			//update alpha and beta
			temperature = temperature * alpha; 
			iterations = iterations * beta;

			if(solutionN.getFitness() < minFitness) {
				minFitness = solutionN.getFitness();
				fitnesses.add(minFitness);
				fitnessesOccurances.add(count);
				System.out.println(name + ", p " + perturbationCount + " f " + solutionN.getFitness() + " t " + temperature + " r " + ((double)leastFitParentCount/fitParentCount));
			}
			count++;
		}

		//save logged data
		try {
			PrintWriter writer = new PrintWriter(tag+System.currentTimeMillis()+".ola", "UTF-8");
			writer.println("finished in " + (System.currentTimeMillis() - startTime) + "ms and " + count + " iterations.");
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
				System.out.println("error writing log for SA called: " + name + ".");
		}
	}
	
	/**
	* Randomly swap 2 alleles
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
	* Choose 3 alleles, swap them in a cycle randomly
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
	* Determine whether or not to accept a worse solution
	*/
	public boolean acceptLeastFitSolution(OLAGraph oldSoln, OLAGraph newSoln, double temp) {
		return Math.random() <  Math.exp((oldSoln.getFitness() - newSoln.getFitness())/temp);
	}
	
	/**
	* Generate a random solution
	*/
	public OLAGraph generateSolution(int rows, int cols, int[][] connMatrix) {
		ArrayList<Integer> availablePositions = new ArrayList<Integer>();
		for(int i = 0; i < rows * cols; i++) {
			availablePositions.add(i);
		}
		int[] randomLayout = new int[rows * cols];
		for(int i = 0; i < randomLayout.length; i++) {
			int vertexPosition = availablePositions.remove( (int)(Math.random() * availablePositions.size()) );
			randomLayout[i] = vertexPosition;
		}
		return new OLAGraph(rows, cols, randomLayout, connMatrix);
	}
}

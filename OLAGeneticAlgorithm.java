import java.util.ArrayList;

public class OLAGeneticAlgorithm {
	static int[] layout = {3,2,0,1};
	static int[][] connectionMatrix = 
					{
						{0,6,5,8},
						{6,0,9,9},
						{5,9,0,7},
						{8,9,7,0},
					};
	private ArrayList<OLAGraph> population;
	public static void main(String[] args) {
		// OLAGraph olaGraph = new OLAGraph(2,2,layout,connectionMatrix);
		// System.out.println(olaGraph.findFitness() + "");
		OLAGeneticAlgorithm geneticAlgorithm = new OLAGeneticAlgorithm();
		geneticAlgorithm.run();
	}

	public void run() {
		population = generatePopulation(24, 2, 2);
		int sum = 0;
		for(OLAGraph graph : population) {
			// System.out.println(graph.toString() + "\nFitness: " + graph.getFitness());
			sum += graph.getFitness();
		}
		System.out.println("Avg fitness of parents: " + ( (double)sum / population.size()));
		sum = 0;
		ArrayList<OLAGraph> children = performTournament(population, .75);
		for(OLAGraph graph : children) {
			// System.out.println(graph.toString() + "\nFitness: " + graph.getFitness());
			sum += graph.getFitness();
		}
		System.out.println("Avg fitness of children: " + ( (double)sum / population.size()));
	}

	public ArrayList<OLAGraph> generatePopulation(int popSize, int rows, int cols) {
		ArrayList<OLAGraph> population = new ArrayList<OLAGraph>();
		while(population.size() < popSize) {
			ArrayList<Integer> availablePositions = new ArrayList<Integer>();
			for(int i = 0; i < rows * cols; i++) {
				availablePositions.add(i);
			}
			int[] randomLayout = new int[rows * cols];
			for(int i = 0; i < randomLayout.length; i++) {
				int vertexPosition = availablePositions.remove( (int)(Math.random() * availablePositions.size()) );
				randomLayout[i] = vertexPosition;
			}
			population.add(new OLAGraph(rows, cols, randomLayout, connectionMatrix));
		}
		return population;
	}

	public ArrayList<OLAGraph> performTournament(ArrayList<OLAGraph> parentPopulation, double k) {
		ArrayList<OLAGraph> selectedPopulation = new ArrayList<OLAGraph>();
		int fitCount = 0;
		int unfitCount = 0;
		while(selectedPopulation.size() < parentPopulation.size()) {
			int parent1 = (int)(Math.random() * parentPopulation.size());
			int parent2 = (int)(Math.random() * parentPopulation.size());
			OLAGraph fittestParent;
			OLAGraph unfitParent;
			if(parentPopulation.get(parent1).getFitness() <= parentPopulation.get(parent2).getFitness()) {
				fittestParent = parentPopulation.get(parent1).copy();
				unfitParent = parentPopulation.get(parent2).copy();
			}
			else {
				fittestParent = parentPopulation.get(parent2).copy();
				unfitParent = parentPopulation.get(parent1).copy();
			}
			// System.out.println("\niteration" + selectedPopulation.size() + "\nfittestParent:\n" + fittestParent.toString() + "\nLeast Fit:\n" + unfitParent.toString());
			if(Math.random() < k) {
				selectedPopulation.add(fittestParent);
				fitCount++;
			}
			else {
				selectedPopulation.add(unfitParent);
				unfitCount++;
			}
		}
		// System.out.println("Added " + fitCount + " fit parents and " + unfitCount + " unfit parents");
		return selectedPopulation;
	}

	public ArrayList<OLAGraph> order1Crossover(OLAGraph parent1, OLAGraph parent2) {
		int crossoverPoint1 = (int)(Math.random * (parent1.getLength() - 1));
		int crossoverPoint2 = (int)(Math.random * (parent1.getLength() - crossoverPoint1));
		
	}
}
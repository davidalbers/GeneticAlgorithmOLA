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
		for(OLAGraph graph : population) {
			System.out.println(graph.toString() + "\nFitness: " + graph.getFitness());
		}
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
		ArrayList<OLAGraph> childPopulation = new ArrayList<OLAGraph>();
		while(childPopulation.size() < parentPopulation.size()) {
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
			if(Math.random() < k) 
				childPopulation.add(fittestParent);
			else
				childPopulation.add(unfitParent);
		}
		return childPopulation;
	}
}
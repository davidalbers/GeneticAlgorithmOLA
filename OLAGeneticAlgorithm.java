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
		// population = generatePopulation(24, 2, 2);
		// int sum = 0;
		// for(OLAGraph graph : population) {
		// 	// System.out.println(graph.toString() + "\nFitness: " + graph.getFitness());
		// 	sum += graph.getFitness();
		// }
		// System.out.println("Avg fitness of parents: " + ( (double)sum / population.size()));
		// sum = 0;
		// ArrayList<OLAGraph> children = performTournament(population, .75);
		// for(OLAGraph graph : children) {
		// 	// System.out.println(graph.toString() + "\nFitness: " + graph.getFitness());
		// 	sum += graph.getFitness();
		// }
		// System.out.println("Avg fitness of children: " + ( (double)sum / population.size()));
		long start = System.currentTimeMillis();
		int[][] testConnection = OLAGraph.generateConnectionMatrix(3, 4, 1, 9);
		System.out.println("Generated connection matrix\n");
		for(int i = 0; i < testConnection.length; i++) {
			for (int j = 0; j < testConnection[i].length; j++) {
				System.out.print(testConnection[i][j] + ",");
			}
			System.out.println("");
		}
		ArrayList<OLAGraph> testPopulation = generatePopulation(500, 2, 3, testConnection);
		ArrayList<OLAGraph> crossedKids = order1Crossover(testPopulation.get(0), testPopulation.get(1));
		System.out.println("Done in " + (System.currentTimeMillis() - start) + "ms");
	}

	public ArrayList<OLAGraph> generatePopulation(int popSize, int rows, int cols, int[][] connMatrix) {
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
			population.add(new OLAGraph(rows, cols, randomLayout, connMatrix));
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
		
		int[] parent1layout = parent1.getLayout();
		int[] parent2layout = parent2.getLayout();
		//pick two crossover points randomly
		int crossoverPoint1 = (int)(Math.random() * (parent1.getLength() - 2));
		int crossoverPoint2 = (int)(Math.random() * (parent1.getLength() - crossoverPoint1)) + crossoverPoint1;
		System.out.println("Crossing parent1:\n" + parent1.toString() + "\nparent2\n"+ parent2.toString() + "\ncrossover points: " + crossoverPoint1 + "," + crossoverPoint2);
		int[] child1layout = new int[parent1.getLength()];
		int[] child2layout = new int[parent2.getLength()];
		//set all child data to -1 to keep track of which indices have been set
		for(int i = 0; i < child1layout.length; i++) {
			child1layout[i] = -1;
			child2layout[i] = -1;
		}
		//fill in the "middle" part of the children chromosomes
		for(int i = crossoverPoint1; i <= crossoverPoint2; i++) {
			child1layout[i] = parent2layout[i];
			child2layout[i] = parent1layout[i];
		}
		System.out.println("child1layout");
		for(int i = 0; i < child1layout.length; i++) {
			System.out.print(child1layout[i] + ",");
		}
		System.out.println();
		System.out.println("child2layout");
		for(int i = 0; i < child2layout.length; i++) {
			System.out.print(child2layout[i] + ",");
		}
		System.out.println();
		//determine what genetic information is missing from each parent
		int[] parent1leftover = new int[parent1layout.length - (crossoverPoint2 - crossoverPoint1 + 1)];
		int[] parent2leftover = new int[parent1layout.length - (crossoverPoint2 - crossoverPoint1 + 1)];
		int leftoverCount = 0;
		for(int i = 0; i < child1layout.length; i++) {
			int positionToFind = parent1layout[i];
			boolean found = false;
			//check if position in parent is already in child 
			for(int searchIndex = crossoverPoint1; searchIndex <= crossoverPoint2; searchIndex++) {
				if(positionToFind == child1layout[searchIndex]) {
					found = true;
					break;
				}
			}
			if(!found) { //if not in child, add it to left over
				parent1leftover[leftoverCount] = positionToFind;
				leftoverCount++;
			}
		}
		//do the same for child2
		leftoverCount = 0;
		for(int i = 0; i < child1layout.length; i++) {
			int positionToFind = parent2layout[i];
			boolean found = false;
			for(int searchIndex = crossoverPoint1; searchIndex <= crossoverPoint2; searchIndex++) {
				if(positionToFind == child2layout[searchIndex]) {
					found = true;
					break;
				}
			}
			if(!found) {
				parent2leftover[leftoverCount] = positionToFind;
				leftoverCount++;
			}
		}
		System.out.println("parent1leftover");
		for(int i = 0; i < parent1leftover.length; i++) {
			System.out.print(parent1leftover[i] + ",");
		}
		System.out.println();
		System.out.println("parent2leftover");
		for(int i = 0; i < parent2leftover.length; i++) {
			System.out.print(parent2leftover[i] + ",");
		}
		System.out.println();

		//fill in empty positions
		for(int i = 0; i < parent1leftover.length; i++) {
			int childPosition = i;
			//skip over "middle" part
			if(childPosition >= crossoverPoint1) {
				childPosition += (crossoverPoint2 - crossoverPoint1 + 1);
			}
			child1layout[childPosition] = parent1leftover[i];
			child2layout[childPosition] = parent2leftover[i];
		}
		ArrayList<OLAGraph> children = new ArrayList<OLAGraph>();
		children.add(new OLAGraph(parent1.getRows(), parent1.getColumns(), child1layout, parent1.getConnectionMatrix()));
		children.add(new OLAGraph(parent1.getRows(), parent1.getColumns(), child2layout, parent1.getConnectionMatrix()));
		System.out.println("Resultant child1\n" + children.get(0).toString() + "\nchild2\n"+ children.get(1).toString());
		return children;
	}
}
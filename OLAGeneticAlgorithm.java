import java.util.ArrayList;

public class OLAGeneticAlgorithm {
	// static int[] layout = {3,2,0,1};
	// static int[][] connectionMatrix = 
	// 				{
	// 					{0,6,5,8},
	// 					{6,0,9,9},
	// 					{5,9,0,7},
	// 					{8,9,7,0},
	// 				};
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
		int[][] testConnection = OLAGraph.generateConnectionMatrix(20, 19, 1, 20);
		// System.out.println("Generated connection matrix");
		// for(int i = 0; i < testConnection.length; i++) {
		// 	for (int j = 0; j < testConnection[i].length; j++) {
		// 		System.out.print(testConnection[i][j] + ",");
		// 	}
		// 	System.out.println("");
		// }
		ArrayList<OLAGraph> testPopulation = generatePopulation(5000, 20, 19, testConnection);
		int count = 0;
		int stop = 10;
		double mutation = .05;
		int minGeneration = 0;
		int minFitness = 0;
		while(count < stop) {
			long avgSum = 0;
			for(OLAGraph graph : testPopulation) {
				avgSum += graph.getFitness();
			}
			System.out.println("Avg before selection\t\t" + (double)avgSum / testPopulation.size());
			long time = System.currentTimeMillis();
			ArrayList<OLAGraph> selected = performTournament(testPopulation, .825);

			avgSum = 0;
			// for(OLAGraph graph : selected) {
			// 	avgSum += graph.getFitness();
			// }
			System.out.println("Avg after selection\t\t" + (double)avgSum / testPopulation.size() + " took " + (System.currentTimeMillis() - time));
			time = System.currentTimeMillis();

			ArrayList<OLAGraph> crossed = order1Crossover(selected);

			avgSum = 0;
			// for(OLAGraph graph : crossed) {
			// 	avgSum += graph.getFitness();
			// }
			System.out.println("Avg after cross\t\t\t" + (double)avgSum / testPopulation.size() + " took " + (System.currentTimeMillis() - time));
			time = System.currentTimeMillis();
			for(int i = 0; i < (int)(crossed.size() * mutation); i++) {
				OLAGraph mutated = mutate(crossed.remove((int)(Math.random() * crossed.size())));
				crossed.add(mutated);
			}

			avgSum = 0;
			// for(OLAGraph graph : crossed) {
			// 	avgSum += graph.getFitness();
			// }
			System.out.println("Avg after mutate\t\t" + (double)avgSum / testPopulation.size()+ " took " + (System.currentTimeMillis() - time));
			testPopulation = crossed;
			

			long sum = 0;
			int min = testPopulation.get(0).getFitness();
			int max = 0;
			for(OLAGraph graph : testPopulation) {
				sum += graph.getFitness();
				if(graph.getFitness() < min)
					min = graph.getFitness();
				if(graph.getFitness() > max)
					max = graph.getFitness();
			}
			double avg = ((double)sum / testPopulation.size());
			if(count == 0) {
				minFitness = min;
			}
			if(min < minFitness) {
				minFitness = min;
				minGeneration = count;
			}
			System.out.println("Generation " + count + " avg fitness: " + avg + " min " + min + ", max " + max + " total min: " + minFitness + " at Generation" + minGeneration);
			count++;
		}
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
				fittestParent = parentPopulation.get(parent1);
				unfitParent = parentPopulation.get(parent2);
			}
			else {
				fittestParent = parentPopulation.get(parent2);
				unfitParent = parentPopulation.get(parent1);
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
	public ArrayList<OLAGraph> order1Crossover(ArrayList<OLAGraph> parents) {
		int parentIndex = 0;
		ArrayList<OLAGraph> children = new ArrayList<OLAGraph>();
		while(parentIndex < parents.size()) {
			OLAGraph parent1 = parents.get(parentIndex);
			parentIndex++;
			OLAGraph parent2 = parents.get(parentIndex);
			parentIndex++;
			int[] parent1layout = parent1.getLayout();
			int[] parent2layout = parent2.getLayout();
			//pick two crossover points randomly
			int crossoverPoint1 = (int)(Math.random() * (parent1.getLength() - 2));
			int crossoverPoint2 = (int)(Math.random() * (parent1.getLength() - crossoverPoint1)) + crossoverPoint1;
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

			//determine what genetic information is missing from each parent
			int[] parent1leftover = new int[parent1layout.length - (crossoverPoint2 - crossoverPoint1 + 1)];
			int[] parent2leftover = new int[parent1layout.length - (crossoverPoint2 - crossoverPoint1 + 1)];
			int leftoverCount = 0;
			int index = (crossoverPoint2 + 1) % child1layout.length;
			while(leftoverCount < parent1leftover.length) {
				int positionToFind = parent1layout[index];
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
				index++;
				index = index % child1layout.length;
			}
			//do the same for child2
			leftoverCount = 0;
			index = (crossoverPoint2 + 1) % child1layout.length;
			while(leftoverCount < parent1leftover.length) {
				int positionToFind = parent2layout[index];
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
				index++;
				index = index % child1layout.length;
			}

			int placementIndex = (crossoverPoint2 + 1) % child1layout.length;
			int leftoverIndex = 0;
			//fill in empty positions
			while(placementIndex != crossoverPoint1) {
				child1layout[placementIndex] = parent1leftover[leftoverIndex];
				child2layout[placementIndex] = parent2leftover[leftoverIndex];
				placementIndex++;
				leftoverIndex++;
				placementIndex = placementIndex % child1layout.length;
			}
			children.add(new OLAGraph(parent1.getRows(), parent1.getColumns(), child1layout, parent1.getConnectionMatrix()));
			children.add(new OLAGraph(parent1.getRows(), parent1.getColumns(), child2layout, parent1.getConnectionMatrix()));
		}
		return children;
	}


	public OLAGraph mutate(OLAGraph graph) {
		int[] layout = graph.getLayout();

		int swapIndex1 = (int)(Math.random() * layout.length);
		int swapIndex2 = (int)(Math.random() * layout.length);
		int swapTemp = layout[swapIndex1];

		layout[swapIndex1] = layout[swapIndex2];
		layout[swapIndex2] = swapTemp;

		return new OLAGraph(graph.getRows(), graph.getColumns(), layout, graph.getConnectionMatrix());
	}
}
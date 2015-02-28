import java.util.ArrayList;
import java.util.Collections;
public class OLAGeneticAlgorithm {
	// static int[] layout = {3,2,0,1};
	static int[][] contrivedMatrix = 
			{
	  //{0,  1,   2,  3,   4   ,5,   6,  7,   8, 9,0,1,2,3,4,5,6,7}
   /*0*/{0,  0,   0,  0,   500 ,0,   0,  0,   0, 0,0,0,0,0,0,0,0,0},
   /*1*/{0,  0,   0,  0,   5000,0,   0,  0,   0, 0,0,0,0,0,0,0,0,0},
   /*2*/{0,  0,   0,  0,   500 ,0,   0,  0,   0, 0,0,0,0,0,0,0,0,0},
   /*3*/{0,  0,   0,  0,   5000,0,   0,  0,   0, 0,0,0,0,0,0,0,0,0},
   /*4*/{500,5000,500,5000,0,   5000,500,5000,500, 0,0,0,0,0,0,0,0,0},
   /*5*/{0,  0,   0,  0,   5000,0,0,0,0, 0,   0,0,0,0,0,0,0,0},
   /*6*/{0,  0,   0,  0,   500 ,0,0,0,0, 0,   0,0,0,0,0,0,0,0},
   /*7*/{0,  0,   0,  0,   5000,0,0,0,0, 0,   0,0,0,0,0,0,0,0},
   /*8*/{0,  0,   0,  0,   500 ,0,0,0,0, 0,   0,0,0,0,0,0,0,0},
   /*9*/{0,  0,   0,  0,0,0,0,0,0,            0  ,0   ,0  ,0   ,500 ,0   ,0  ,0,   0},
   /*0*/{0,  0,   0,  0,0,0,0,0,0,            0  ,0   ,0  ,0   ,5000,0   ,0  ,0,   0},
   /*1*/{0,  0,   0,  0,0,0,0,0,0,    		  0  ,0   ,0  ,0   ,500 ,0   ,0  ,0,   0},
   /*2*/{0,  0,   0,  0,0,0,0,0,0,    		  0  ,0   ,0  ,0   ,5000,0   ,0  ,0,   0},
   /*3*/{0,  0,   0,  0,0,0,0,0,0,    		  500,5000,500,5000,0   ,5000,500,5000,500},
   /*4*/{0,  0,   0,  0,0,0,0,0,0,    		  0  ,0   ,0  ,0   ,5000,0   ,0  ,0,   0},
   /*5*/{0,  0,   0,  0,0,0,0,0,0,    		  0  ,0   ,0  ,0   ,500 ,0   ,0  ,0,   0},
   /*6*/{0,  0,   0,  0,0,0,0,0,0,    		  0  ,0   ,0  ,0   ,5000,0   ,0  ,0,   0},
   /*7*/{0,  0,   0,  0,0,0,0,0,0,      	  0  ,0   ,0  ,0   ,500 ,0   ,0  ,0,   0}
	  //{0,  1,   2,  3,4,5,6,7,8,            9,  0,  1,   2,   3,   4,  5,   6,   7}
			};
	private ArrayList<OLAGraph> population;
	public static void main(String[] args) {
		// OLAGraph olaGraph = new OLAGraph(2,2,layout,connectionMatrix);
		// System.out.println(olaGraph.findFitness() + "");
		OLAGeneticAlgorithm geneticAlgorithm = new OLAGeneticAlgorithm();
		geneticAlgorithm.run();
	}

	public void run() {
		int rows = 6;
		int cols = 3;
		int[][] testConnection = OLAGraph.generateConnectionMatrix(rows, cols, 1, 10);
		// System.out.println("Generated connection matrix");
		// for(int i = 0; i < testConnection.length; i++) {
		// 	for (int j = 0; j < testConnection[i].length; j++) {
		// 		System.out.print(testConnection[i][j] + ",");
		// 	}
		// 	System.out.println("");
		// }

		ArrayList<OLAGraph> testPopulation = generatePopulation(10001, rows, cols, testConnection);
		ArrayList<OLAGraph> testPopulation2 = new ArrayList<OLAGraph>();
		ArrayList<OLAGraph> testPopulation3 = new ArrayList<OLAGraph>();
		ArrayList<OLAGraph> testPopulation4 = new ArrayList<OLAGraph>();

		for(OLAGraph graph : testPopulation) {
			testPopulation2.add(graph.copy());
			testPopulation3.add(graph.copy());
			testPopulation4.add(graph.copy());
		}

		(new GAThread(testPopulation, 100000, .05, 0, 0)).start();
		(new GAThread(testPopulation2, 100000, .05, 0, 1)).start();
		(new GAThread(testPopulation3, 100000, .05, 1, 0)).start();
		(new GAThread(testPopulation4, 100000, .05, 1, 1)).start();
	}

	class GAThread extends Thread {
         ArrayList<OLAGraph> population;
         int stop;
         double mutation;
         int selectionAlg;
         int crossAlg;
         GAThread(ArrayList<OLAGraph> population, int stop, double mutation, int selectionAlg, int crossAlg) {
             this.population = population;
             this.stop = stop;
             this.mutation = mutation;
             this.selectionAlg = selectionAlg;
             this.crossAlg = crossAlg;
         }

         public void run() {
         	runGA(population, stop, mutation, selectionAlg, crossAlg);
         }
     }

	public void runGA(ArrayList<OLAGraph> population, int stop, double mutation, int selectionAlg, int crossAlg) {
		int count = 0;
		int minGeneration = 0;
		int minFitness = 0;
		long start = System.currentTimeMillis();
		String tag = "";
		if(selectionAlg == 0) 
			tag += "tournament,";
		else
			tag += "rank,";
		if(crossAlg == 0)
			tag += "order1";
		else
			tag += "cycle";
		System.out.println("Starting GA " + tag);
		while(count < stop && minFitness != 48000) {
			OLAGraph minParent = population.remove(populationMinIndex(population));
			int newFitness = minParent.getFitness();
			if(newFitness < minFitness || count == 0) {
				// System.out.println("Gen " + count + " min:\n" + minParent.toString());
				minFitness = newFitness;
			}
			ArrayList<OLAGraph> selected;
			if(selectionAlg == 0)
				selected = performTournament(population, .75);
			else
				selected = performRank(population);

			ArrayList<OLAGraph> crossed;
			if(crossAlg == 0)
				crossed = order1Crossover(selected);
			else
				crossed = cycle(selected);

			for(int i = 0; i < (int)(crossed.size() * mutation); i++) {
				OLAGraph mutated = mutate(crossed.remove((int)(Math.random() * crossed.size())));
				crossed.add(mutated);
			}

			population = crossed;

			if(count%100 == 0)
				System.out.println(tag + " gen " + count + "min " + minFitness + " took " + (System.currentTimeMillis() - start));
			
			population.add(minParent);
			count++;
		}
		System.out.println(tag + " finished in " + (System.currentTimeMillis() - start) + "ms and " + count + "generations");
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
				selectedPopulation.add(fittestParent.copy());
				fitCount++;
			}
			else {
				selectedPopulation.add(unfitParent.copy());
				unfitCount++;
			}
		}
		// System.out.println("Added " + fitCount + " fit parents and " + unfitCount + " unfit parents");
		return selectedPopulation;
	}

	public ArrayList<OLAGraph> performRank(ArrayList<OLAGraph> parentPopulation) {
		Collections.sort(parentPopulation);
		Collections.reverse(parentPopulation);
		// n(n+1)/2
		int totalFitness = parentPopulation.size() * (parentPopulation.size()+1) / 2;
		ArrayList<OLAGraph> selectedPopulation = new ArrayList<OLAGraph>();
		while(selectedPopulation.size() < parentPopulation.size()) {
			double rouletteSpin = Math.random();
			//given a "spin" which is random double between 0 and 1
			//equation for rank which the roulette ball landed on is
			// floor(.5 * (sqrt(8pt + 1) + 1)) where p = spin, t = total fitness
			int rank = (int)(.5 * (Math.sqrt(8 * rouletteSpin * totalFitness + 1) + 1));
			selectedPopulation.add(parentPopulation.get(rank - 1).copy());
		}
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

	public ArrayList<OLAGraph> cycle(ArrayList<OLAGraph> parents) {
		ArrayList<OLAGraph> children = new ArrayList<OLAGraph>();
		int parentIndex = 0;
		while(children.size() != parents.size()) {
			OLAGraph parent1 = parents.get(parentIndex);
			parentIndex++;
			OLAGraph parent2 = parents.get(parentIndex);
			parentIndex++;

			int[] parent1layout = parent1.getLayout();
			int[] parent2layout = parent2.getLayout();
			//for this algorithm we search for where a certain value 
			//is in a chromosome to make this easier, make a reverse lookup 
			//reverseLookup[value] = index in layout
			int[] parent1reverse = parent1.generateReverseLookup();
			int[] parent2reverse = parent2.generateReverseLookup();
			int[] child1layout = new int[parent1layout.length];
			int[] child2layout = new int[parent1layout.length];
			//set all child data to -1 to keep track of which indices have been set
			for(int i = 0; i < child1layout.length; i++) {
				child1layout[i] = -1;
				child2layout[i] = -1;
			}
			//select a random start index
			int startIndex = (int)(Math.random() * parent1layout.length);
			int cycleIndex = startIndex;
			//fill the first index
			child1layout[startIndex] = parent1layout[startIndex];
			cycleIndex = parent2reverse[parent1layout[startIndex]];
			//cycle until you reach the start
			while(cycleIndex != startIndex) {
				child1layout[cycleIndex] = parent1layout[cycleIndex];
				cycleIndex = parent2reverse[parent1layout[cycleIndex]];
			}
			//fill in missing values
			for(int i = 0; i < child1layout.length; i++) {
				if(child1layout[i] == -1) {
					child1layout[i] = parent2layout[i];
				}
			}
			//repeat for child2
			cycleIndex = startIndex;
			child2layout[startIndex] = parent2layout[startIndex];
			cycleIndex = parent1reverse[parent2layout[startIndex]];
			while(cycleIndex != startIndex) {
				child2layout[cycleIndex] = parent2layout[cycleIndex];
				cycleIndex = parent1reverse[parent2layout[cycleIndex]];
			}
			for(int i = 0; i < child2layout.length; i++) {
				if(child2layout[i] == -1) {
					child2layout[i] = parent1layout[i];
				}
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

	public double populationAverage(ArrayList<OLAGraph> population) {
		long sum = 0;
		for(OLAGraph graph : population) {
			sum += graph.getFitness();
		}
		return ((double)sum/population.size());
	}

	public int populationMin(ArrayList<OLAGraph> population) {
		int min = population.get(0).getFitness();
		for(OLAGraph graph : population) {
			if(graph.getFitness() < min)
				min = graph.getFitness();
		}
		return min;
	}
	public int populationMax(ArrayList<OLAGraph> population) {
		int max = 0;
		for(OLAGraph graph : population) {
			if(graph.getFitness() > max)
				max = graph.getFitness();
		}
		return max;
	}

	public int populationMinIndex(ArrayList<OLAGraph> population) {
		int min = population.get(0).getFitness();
		int minIndex = 0;
		for(int i = 0; i < population.size(); i++) {
			if(population.get(i).getFitness() < min) {
				min = population.get(i).getFitness();
				minIndex = i;
			}
		}
		return minIndex;
	}
}
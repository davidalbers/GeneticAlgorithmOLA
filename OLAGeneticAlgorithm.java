import java.util.ArrayList;
import java.util.Collections;
import java.io.PrintWriter;
public class OLAGeneticAlgorithm {
	// static int[] layout = {3,2,0,1};test
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
		int[][] testConnection = OLAGraph.generateConnectionMatrix(rows, cols, 0, 9);
		// System.out.println("Generated connection matrix");
		// for(int i = 0; i < testConnection.length; i++) {
		// 	for (int j = 0; j < testConnection[i].length; j++) {
		// 		System.out.print(testConnection[i][j] + ",");
		// 	}
		// 	System.out.println("");
		// }
		//int[] optimalLayout = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17};
		//OLAGraphDrawer drawer = new OLAGraphDrawer(optimalLayout, contrivedMatrix, rows, cols);

		
		
		ArrayList<OLAGraph> testPopulation = generatePopulation(501, rows, cols, testConnection);
		ArrayList<OLAGraph> testPopulation2 = new ArrayList<OLAGraph>();
		ArrayList<OLAGraph> testPopulation3 = new ArrayList<OLAGraph>();
		ArrayList<OLAGraph> testPopulation4 = new ArrayList<OLAGraph>();
		ArrayList<OLAGraph> testPopulation5 = new ArrayList<OLAGraph>();	
		
		for(OLAGraph graph : testPopulation) {
			testPopulation2.add(graph.copy());
			testPopulation3.add(graph.copy());
			testPopulation4.add(graph.copy());
			testPopulation5.add(graph.copy());
		}
		
		(new GAThread(testPopulation, -1, .05, 0, 0, .75, "t1")).start();
		(new GAThread(testPopulation2, -1, .05, 0, 0, .75, "t2")).start();
		(new GAThread(testPopulation3, -1, .05, 0, 0, .75, "t3")).start();
		(new GAThread(testPopulation4, -1, .05, 0, 0, .75, "t4")).start();
		(new GAThread(testPopulation5, -1, .05, 0, 0, .75, "t5")).start();
	}

	class GAThread extends Thread {
         ArrayList<OLAGraph> population;
         int stop;
         double mutation;
         int selectionAlg;
         int crossAlg;
         double k;
	String name;
         GAThread(ArrayList<OLAGraph> population, int stop, double mutation, int selectionAlg, int crossAlg, double k, String name) {
             this.population = population;
             this.stop = stop;
             this.mutation = mutation;
             this.selectionAlg = selectionAlg;
             this.crossAlg = crossAlg;
             this.k = k;
		this.name = name;
         }

         public void run() {
         	runGA(population, stop, mutation, selectionAlg, crossAlg, k, name);
         }
     }

	public void runGA(ArrayList<OLAGraph> population, int stop, double mutation, int selectionAlg, int crossAlg, double k, String name) {
		int count = 0;
		int minGeneration = 0;
		int minFitness = 0;
		long start = System.currentTimeMillis();
		String tag = name;
		ArrayList<Integer> fitnesses = new ArrayList<Integer>();
		ArrayList<Integer> fitnessesOccurances = new ArrayList<Integer>();
		int[] blankLayout = new int[population.get(0).getLayout().length];
		//OLAGraphDrawer drawer = new OLAGraphDrawer(blankLayout, contrivedMatrix, population.get(0).getRows(), population.get(0).getColumns());
		// if(selectionAlg == 0) 
		// 	tag += "tournament,";
		// else
		// 	tag += "rank,";
		// if(crossAlg == 0)
		// 	tag += "order1";
		// else if(crossAlg == 1)
		// 	tag += "cycle";
		// else 
			// tag += "2d";
		//tag += " mut " + mutation;
		//tag += " k " + k;
		tag+=population.size();
		while(count != stop) {
			OLAGraph minParent = population.remove(populationMinIndex(population));
			int newFitness = minParent.getFitness();
			if(newFitness < minFitness || count == 0) {
				System.out.println(tag + " Gen " + count + "min " + minFitness);// " min:" + minParent.toString());
				//drawer.setLayout(minParent.getLayout());
				fitnesses.add(newFitness);
				fitnessesOccurances.add(count);
				minFitness = newFitness;
				minGeneration = count;
			}
			ArrayList<OLAGraph> selected;
			if(selectionAlg == 0)
				selected = performTournament(population, k);
			else
				selected = performRank(population);

			ArrayList<OLAGraph> crossed;
			if(crossAlg == 0)
				crossed = order1Crossover(selected);
			else if(crossAlg == 1)
				crossed = cycle(selected);
			else
				crossed = TwoDPointCrossover(selected, selected.get(0).getRows(), selected.get(0).getColumns());
			for(int i = 0; i < (int)(crossed.size() * mutation); i++) {
				crossed.add(mutate(crossed.remove((int)(Math.random() * crossed.size()))));
			}
			mutation = .05;
			population = crossed;

			 //if(count%25 == 0)
			 //	System.out.println(tag + " gen " + count);// + "min " + minFitness + " took " + (System.currentTimeMillis() - start));
			population.add(minParent);
			count++;

		}
		
		try {
		PrintWriter writer = new PrintWriter(tag+System.currentTimeMillis()+".ola", "UTF-8");
		writer.println("finished in " + (System.currentTimeMillis() - start) + "ms and " + count + " generations. Minimum generation " + minGeneration);
		writer.println("resulting layout " + population.get(populationMinIndex(population)).toString());
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
			// System.out.println("\niteration" + selectedPopulation.size() + "\nfittestParent:\n" + fittestParent.toString() + 
			//"\nLeast Fit:\n" + unfitParent.toString());
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
	
	//Slow, inefficient :( 
	public ArrayList<OLAGraph> TwoDPointCrossover(ArrayList<OLAGraph> parents, int rows, int cols) {
		int parentIndex = 0;
		ArrayList<OLAGraph> children = new ArrayList<OLAGraph>();
		while(children.size() < parents.size()) {
			int xCrossoverPoint1 = 0;//(int)(Math.random() * (cols - 2));
			int xCrossoverPoint2 = (int)(Math.random() * (cols - xCrossoverPoint1)) + xCrossoverPoint1;
			int yCrossoverPoint1 = 0;//(int)(Math.random() * (rows - 2));
			int yCrossoverPoint2 = 0;//(int)(Math.random() * (rows - yCrossoverPoint1)) + yCrossoverPoint1;
			int[][] connectionMatrix = parents.get(0).getConnectionMatrix();
			int[] parent1layout = parents.get(parentIndex).getLayout();
			parentIndex++;
			int[] parent2layout = parents.get(parentIndex).getLayout();
			parentIndex++;
			int[] child1layout = new int[parent1layout.length];
			int[] child2layout = new int[parent1layout.length];
			//set all child data to -1 to keep track of which indices have been set
			for(int i = 0; i < child1layout.length; i++) {
				child1layout[i] = -1;
				child2layout[i] = -1;
			}
			ArrayList<Integer> child1Duplicates = new ArrayList<Integer>();
			ArrayList<Integer> child2Duplicates = new ArrayList<Integer>();

			// 1,2,3
			// 4,5,6
			// 7,8,9
			for(int row = 0; row < rows; row++) {
				for(int col = 0; col < cols; col++) {
					if(row < yCrossoverPoint1) {
						if(col < xCrossoverPoint1) { //1
							if(findVertex(child1layout, parent1layout[row * cols + col], row * cols + col))
								child1Duplicates.add(parent1layout[row * cols + col]);
							else
								child1layout[row * cols + col] = parent1layout[row * cols + col];
							if(findVertex(child2layout, parent2layout[row * cols + col], row * cols + col))
								child2Duplicates.add(parent2layout[row * cols + col]);
							else
								child2layout[row * cols + col] = parent2layout[row * cols + col];
						}
						else if(col <= xCrossoverPoint2) { //2
							if(findVertex(child1layout, parent2layout[row * cols + col], row * cols + col))
								child1Duplicates.add(parent2layout[row * cols + col]);
							else
								child1layout[row * cols + col] = parent2layout[row * cols + col];
							if(findVertex(child2layout, parent1layout[row * cols + col], row * cols + col))
								child2Duplicates.add(parent1layout[row * cols + col]);
							else
								child2layout[row * cols + col] = parent1layout[row * cols + col];
						}
						else { //3
							if(findVertex(child1layout, parent1layout[row * cols + col], row * cols + col))
								child1Duplicates.add(parent1layout[row * cols + col]);
							else
								child1layout[row * cols + col] = parent1layout[row * cols + col];
							if(findVertex(child2layout, parent2layout[row * cols + col], row * cols + col))
								child2Duplicates.add(parent2layout[row * cols + col]);
							else
								child2layout[row * cols + col] = parent2layout[row * cols + col];
						}
					}
					else if(row <= yCrossoverPoint2) {
						if(col < xCrossoverPoint1) { //4
							if(findVertex(child1layout, parent2layout[row * cols + col], row * cols + col))
								child1Duplicates.add(parent2layout[row * cols + col]);
							else
								child1layout[row * cols + col] = parent2layout[row * cols + col];
							if(findVertex(child2layout, parent1layout[row * cols + col], row * cols + col))
								child2Duplicates.add(parent1layout[row * cols + col]);
							else
								child2layout[row * cols + col] = parent1layout[row * cols + col];
						}
						else if(col <= xCrossoverPoint2) { //5
							if(findVertex(child1layout, parent1layout[row * cols + col], row * cols + col))
								child1Duplicates.add(parent1layout[row * cols + col]);
							else
								child1layout[row * cols + col] = parent1layout[row * cols + col];
							if(findVertex(child2layout, parent2layout[row * cols + col], row * cols + col))
								child2Duplicates.add(parent2layout[row * cols + col]);
							else
								child2layout[row * cols + col] = parent2layout[row * cols + col];
						}
						else { //6
							if(findVertex(child1layout, parent2layout[row * cols + col], row * cols + col))
								child1Duplicates.add(parent2layout[row * cols + col]);
							else
								child1layout[row * cols + col] = parent2layout[row * cols + col];
							if(findVertex(child2layout, parent1layout[row * cols + col], row * cols + col))
								child2Duplicates.add(parent1layout[row * cols + col]);
							else
								child2layout[row * cols + col] = parent1layout[row * cols + col];
						}
					}
					else {
						if(col < xCrossoverPoint1) { //7
							if(findVertex(child1layout, parent1layout[row * cols + col], row * cols + col))
								child1Duplicates.add(parent1layout[row * cols + col]);
							else
								child1layout[row * cols + col] = parent1layout[row * cols + col];
							if(findVertex(child2layout, parent2layout[row * cols + col], row * cols + col))
								child2Duplicates.add(parent2layout[row * cols + col]);
							else
								child2layout[row * cols + col] = parent2layout[row * cols + col];
						}
						else if(col <= xCrossoverPoint2) { //8
							if(findVertex(child1layout, parent2layout[row * cols + col], row * cols + col))
								child1Duplicates.add(parent2layout[row * cols + col]);
							else
								child1layout[row * cols + col] = parent2layout[row * cols + col];
							if(findVertex(child2layout, parent1layout[row * cols + col], row * cols + col))
								child2Duplicates.add(parent1layout[row * cols + col]);
							else
								child2layout[row * cols + col] = parent1layout[row * cols + col];
						}
						else { //9
							if(findVertex(child1layout, parent1layout[row * cols + col], row * cols + col))
								child1Duplicates.add(parent1layout[row * cols + col]);
							else
								child1layout[row * cols + col] = parent1layout[row * cols + col];
							if(findVertex(child2layout, parent2layout[row * cols + col], row * cols + col))
								child2Duplicates.add(parent2layout[row * cols + col]);
							else
								child2layout[row * cols + col] = parent2layout[row * cols + col];
						}
					}	
				}
			}
			
			for(int i = 0; i < parent1layout.length; i++) {
				if(child1layout[i] == -1)
					child1layout[i] = child2Duplicates.remove(0);
				if(child2layout[i] == -1)
					child2layout[i] = child1Duplicates.remove(0);
			}
			/*
			System.out.println("Crossover points " + xCrossoverPoint1 + "," + xCrossoverPoint2 + "," + yCrossoverPoint1 + "," + yCrossoverPoint2);
			System.out.println("\nParent1");
			for(int i = 0; i < parent1layout.length; i++) {
				if(i%cols == 0)
					System.out.println();
				System.out.print(parent1layout[i] + ",");
				
			}
			System.out.println("\nParent2");
			for(int i = 0; i < parent1layout.length; i++) {
				if(i%cols == 0)
					System.out.println();
				System.out.print(parent2layout[i] + ",");
				
			}
			System.out.println("\nChild1");
			for(int i = 0; i < parent1layout.length; i++) {
				if(i%cols == 0)
					System.out.println();
				System.out.print(child1layout[i] + ",");
				
			}
			System.out.println("\nChild2");
			for(int i = 0; i < parent1layout.length; i++) {
				if(i%cols == 0)
					System.out.println();
				System.out.print(child2layout[i] + ",");
				
			}
			*/
			children.add(new OLAGraph(rows, cols, child1layout, connectionMatrix));
			children.add(new OLAGraph(rows, cols, child2layout, connectionMatrix));
		}
		return children;
	}
	
	public boolean findVertex(int[] layout, int vertex, int stop) {
		for(int i = 0; i <= stop; i++) {
			if(layout[i] == vertex)
				return true;
		}
		return false;
	}

	public OLAGraph mutate(OLAGraph graph) {
		int[] layout = graph.getLayout();

		int swapIndex1 = (int)(Math.random() * layout.length);
		int swapIndex2 = (int)(Math.random() * layout.length);
		int swapTemp = layout[swapIndex1];

		layout[swapIndex1] = layout[swapIndex2];
		layout[swapIndex2] = swapTemp;

		swapIndex1 = (int)(Math.random() * layout.length);
		swapIndex2 = (int)(Math.random() * layout.length);
		swapTemp = layout[swapIndex1];

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

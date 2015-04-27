import java.util.ArrayList;
import java.util.Collections;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Arrays;
public class OLAGeneticAlgorithm {

	private ArrayList<OLAGraph> population;
	private int rows;
	private int cols;
	private int[][] connectionMatrix;
	private int populationSize;
	private int selectionAlg;
	private double tournamentK;
	private int crossAlg;
	private double crossRate;
	private int mutationOp;
	private double mutationRate;
	private int maxIterations;
	private int maxTimeMs;
	private String name;


	public OLAGeneticAlgorithm(int rows, int cols, int[][] connectionMatrix, int populationSize, 
		int selectionAlg, double tournamentK, int crossAlg, double crossRate, int mutationOp, double mutationRate, int maxIterations, int maxTimeMs, String name) {
		this.rows = rows;
		this.cols = cols;
		this.connectionMatrix = connectionMatrix;
		this.populationSize = populationSize;
		this.selectionAlg = selectionAlg;
		this.tournamentK = tournamentK;
		this.crossAlg = crossAlg;
		this.crossRate = crossRate;
		this.mutationOp = mutationOp;
		this.mutationRate = mutationRate;
		this.maxIterations = maxIterations;
		this.maxTimeMs = maxTimeMs;
		this.name = name;
		population = generatePopulation(populationSize, rows, cols, connectionMatrix);
	}


	public void runGA() {
		int count = 0;
		int minGeneration = 0;
		int minFitness = 0;
		
		//log new minimum fitnesses and when they occur
		ArrayList<Integer> fitnesses = new ArrayList<Integer>();
		ArrayList<Integer> fitnessesOccurances = new ArrayList<Integer>();
		int[] blankLayout = new int[population.get(0).getLayout().length];

		//build a descriptive, readable name for this algorithm
		String tag = name + ", ";
		tag += "size " + population.size() + ",";
		if(selectionAlg == 0) 
			tag += "tour,";
		else
			tag += "rank,";
		if(crossAlg == 0)
			tag += "order1,";
		else if(crossAlg == 1)
			tag += "cycle ,";
		else
			tag += "2point,";
		tag += " mut " + mutationRate + ",";
		if(mutationOp == 0)
			tag += "pairExc,";
		else 
			tag += "cycle3 ,";
		tag += "k " + tournamentK + ",";
		
		long startTime = System.currentTimeMillis();


		//run the genetic algorithm
		while(count != maxIterations && (System.currentTimeMillis() - startTime) < maxTimeMs) {
			System.out.println(population.size() + "size");
			//remove minimum parent - elitism
			OLAGraph minParent = population.remove(populationMinIndex(population));
			//determine if minimum parent is an improvement over the previous minimum
			int newFitness = minParent.getFitness();
			if(newFitness < minFitness || count == 0) {
				fitnesses.add(newFitness);
				fitnessesOccurances.add(count);
				minFitness = newFitness;
				minGeneration = count;
				System.out.println(name + " Gen " + count + " min " + minFitness);
			}

			//perform selection
			ArrayList<OLAGraph> selected;
			if(selectionAlg == 0) //0 = tournament 
				selected = performTournament(population, tournamentK);
			else //1 = rank
				selected = performRank(population);

			
			//move over a percentage of the selected to the group which will reproduce/crossover
			ArrayList<OLAGraph> toCross = new ArrayList<OLAGraph>();
			int toCrossSize = (int)(selected.size() * crossRate + .5);
			while(toCross.size() < toCrossSize) {
				int indexToAdd = (int)(Math.random() * selected.size());
				toCross.add(selected.remove(indexToAdd));
			}

			//"reset" the population
			population.clear();
			//add anyone not crossing back to the population
			population.addAll(selected);

			//perform crossover and add crossed children to population
			if(crossAlg == 0) //0 = order 1 crossover
				population.addAll(order1Crossover(toCross));
			else if(crossAlg == 1) //1 = cycle crossover
				population.addAll(cycle(toCross));
			else  //2 = two-point crossover
				population.addAll(TwoPointCrossover(toCross, toCross.get(0).getRows(), toCross.get(0).getColumns()));

			//perform mutation
			if(mutationOp == 0) { //0 - pairwise exchange
				for(int i = 0; i < (int)(population.size() * mutationRate); i++) {
					population.add(pairwiseExchange(population.remove((int)(Math.random() * population.size()))));
				}
			}
			else { //1 - cycle of 2 
				for(int i = 0; i < (int)(population.size() * mutationRate); i++) {
					population.add(cycleLength3(population.remove((int)(Math.random() * population.size()))));
				}
			}

			population.add(minParent);
			count++;
		}
		
		//write data to a file
		try {
			PrintWriter writer = new PrintWriter(tag+System.currentTimeMillis()+".ola", "UTF-8");
			writer.println("finished in " + (System.currentTimeMillis() - startTime) + "ms and " + count + " generations. Minimum generation " + minGeneration);
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

	/**
	* Generate a population randomly
	*/
	public ArrayList<OLAGraph> generatePopulation(int popSize, int rows, int cols, int[][] connMatrix) {
		ArrayList<OLAGraph> population = new ArrayList<OLAGraph>();
		while(population.size() < popSize) {
			//make a list of available positions 1..n
			ArrayList<Integer> availablePositions = new ArrayList<Integer>();
			for(int i = 0; i < rows * cols; i++) {
				availablePositions.add(i);
			}
			//make a random layout in which those positions are randomly placed
			int[] randomLayout = new int[rows * cols];
			for(int i = 0; i < randomLayout.length; i++) {
				//remove a random available position
				int vertexPosition = availablePositions.remove( (int)(Math.random() * availablePositions.size()) );
				//place it 
				randomLayout[i] = vertexPosition;
			}
			population.add(new OLAGraph(rows, cols, randomLayout, connMatrix));
		}
		return population;
	}

	public ArrayList<OLAGraph> performTournament(ArrayList<OLAGraph> parentPopulation, double k) {
		ArrayList<OLAGraph> selectedPopulation = new ArrayList<OLAGraph>();
		while(selectedPopulation.size() < parentPopulation.size()) {
			int parent1 = (int)(Math.random() * parentPopulation.size());
			int parent2 = (int)(Math.random() * parentPopulation.size());
			//determine which parent is fittest
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
			//select either fit or unfit based on k
			if(Math.random() < k) {
				selectedPopulation.add(fittestParent.copy());
			}
			else {
				selectedPopulation.add(unfitParent.copy());
			}
		}
		return selectedPopulation;
	}

	public ArrayList<OLAGraph> performRank(ArrayList<OLAGraph> parentPopulation) {
		//order from least to greatest
		Collections.sort(parentPopulation);
		//switch to greatest to least
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
			if(parentIndex+1 == parents.size()) {
				//only one parent remaining, just add it and stop
				children.add(parents.get(parentIndex));
				break;
			}
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
		while(parentIndex < parents.size()) {
			if(parentIndex+1 == parents.size()) {
				//only one parent remaining, just add it and stop
				children.add(parents.get(parentIndex));
				break;
			}
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

	public ArrayList<OLAGraph> TwoPointCrossover(ArrayList<OLAGraph> parents, int rows, int cols) {
		int parentIndex = 0;
		ArrayList<OLAGraph> children = new ArrayList<OLAGraph>();
		while(parentIndex < parents.size()) {
			if(parentIndex+1 == parents.size()) {
				//only one parent remaining, just add it and stop
				children.add(parents.get(parentIndex));
				break;
			}
			int crossoverPoint1 = (int)(Math.random() * cols);
			int crossoverPoint2 = (int)(Math.random() * (rows - crossoverPoint1)) + crossoverPoint1;
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

			for(int i = 0; i < child1layout.length; i++) {
				int valueC1;
				int valueC2;
				if(i < crossoverPoint1 || i > crossoverPoint2) {
					valueC1 = parent2layout[i];
					valueC2 = parent1layout[i];
				}
				else {
					valueC1 = parent1layout[i];
					valueC2 = parent2layout[i];

				}
				if(findVertex(child1layout, valueC1, i))
					child1Duplicates.add(valueC1);
				else
					child1layout[i] = valueC1;
				if(findVertex(child2layout, valueC2, i))
					child2Duplicates.add(valueC2);
				else
					child2layout[i] = valueC2;
			}
			
			for(int i = 0; i < parent1layout.length; i++) {
				if(child1layout[i] == -1)
					child1layout[i] = child2Duplicates.remove(0);
				if(child2layout[i] == -1)
					child2layout[i] = child1Duplicates.remove(0);
			}
			children.add(new OLAGraph(rows, cols, child1layout, connectionMatrix));
			children.add(new OLAGraph(rows, cols, child2layout, connectionMatrix));
		}
		return children;
	}
	
	public boolean findVertex(int[] layout, int vertex, int maxIterations) {
		for(int i = 0; i <= maxIterations; i++) {
			if(layout[i] == vertex)
				return true;
		}
		return false;
	}

	public OLAGraph pairwiseExchange(OLAGraph oldSoln) {
		int[] layout = oldSoln.copy().getLayout();

		int swapIndex1 = (int)(Math.random() * layout.length);
		int swapIndex2 = (int)(Math.random() * layout.length);
		int swapTemp = layout[swapIndex1];

		layout[swapIndex1] = layout[swapIndex2];
		layout[swapIndex2] = swapTemp;
		OLAGraph newSoln = new OLAGraph(oldSoln.getRows(), oldSoln.getColumns(), layout, oldSoln.getConnectionMatrix());
		return newSoln;
	}
	
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
		
		int cycleINew = layout[cycleK];
		int cycleKNew = layout[cycleJ];
		int cycleJNew = layout[cycleI];
		
		layout[cycleI] = cycleINew;
		layout[cycleJ] = cycleJNew;
		layout[cycleK] = cycleKNew;
		OLAGraph newSoln = new OLAGraph(oldSoln.getRows(), oldSoln.getColumns(), layout, oldSoln.getConnectionMatrix());
		return newSoln;
	}

	public double populationAverage(ArrayList<OLAGraph> population) {
		long sum = 0;
		for(OLAGraph graph : population) {
			sum += graph.getFitness();
		}
		return (double)(sum/population.size());
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

	public double populationVariance(ArrayList<OLAGraph> population) {
		double avg = populationAverage(population);
		long sum = 0;
		for(OLAGraph graph : population) {
			sum += Math.pow( (graph.getFitness() - avg), 2);
		}
		return Math.sqrt((double)(sum / (population.size() -1)));
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

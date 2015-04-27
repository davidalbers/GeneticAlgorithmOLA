import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
/**
*Asks the user questions to determine what algorithms to run also generates random datasets for the user.
*/
public class OLAAlgorithmGenerator {

	public static void main(String[] args) {

		ArrayList<Thread> algorithmThreads = new ArrayList<Thread>();

		Scanner userInput = new Scanner(System.in);
		boolean stopAsking = false;
		int[][] connectionMatrix;
		int rows;
		int cols;
		
		System.out.print("Number of rows: ");
		rows = userInput.nextInt();
		System.out.print("Number of cols: ");
		cols = userInput.nextInt();
		//build random connection matrix
		System.out.print("Minimum connections: ");
		int minConn = userInput.nextInt();
		System.out.print("Maximum connections: ");
		int maxConn = userInput.nextInt();
		System.out.print("Weightedness/Bias of connections (1 for no bias): ");
		int weightedness = userInput.nextInt();
		System.out.println("Generating connection matrix...");

		connectionMatrix = OLAGraph.generateConnectionMatrix(rows, cols, minConn, maxConn, weightedness);
		System.out.println(OLAGraph.connectionMatrixToString(connectionMatrix));

		if(rows * cols <= 12) {
			//can be solved by brute force in about 10 mins or less
			//ask to run brute force
			System.out.print("Run brute force algorithm? (y/n): ");
			String runBruteForce = userInput.next();
			if(runBruteForce.equalsIgnoreCase("y")) {
				final OLABruteForce bruteForce = new OLABruteForce(rows, cols, connectionMatrix);
				System.out.println("Running brute force algorithm...");
				Thread bruteForceThread = new Thread(){
				    public void run(){
				      bruteForce.run();
				    }
				};
				algorithmThreads.add(bruteForceThread);
			}
		}

		boolean runGreedy = true;
		if(rows * cols > 100) {
			System.out.print("Run greedy algorithm? (y/n): ");
			String runGreedyInput = userInput.next();
			if(!runGreedyInput.equalsIgnoreCase("y"))
				runGreedy = false;
		}
		if(runGreedy) {
			//n = 400 ~30 sec
			//n = 625 ~2.5mins
			//n = 900 ~9 mins
			System.out.println("Running greedy algorithm, may take a minute");
			OLAGreedyAlgorithm greedyAlgorithm = new OLAGreedyAlgorithm(rows, cols, connectionMatrix);
			greedyAlgorithm.run();
		}

		while(!stopAsking) {
			System.out.println("Type \"g\" for a simple genetic algorithm.");
			System.out.println("Type \"s\" for a simulated annealing algorithm.");
			System.out.println("Type \"h\" for a hill-climbing algorithm.");
			System.out.print("Enter your choice here: ");
			String algorithmType = userInput.next();

			//System.out.println("\nConnection matrix:\n" + OLAGraph.connectionMatrixToString(connectionMatrix));

			if(algorithmType.equalsIgnoreCase("g")) {
				System.out.print("Choose selection algorithm, t=tournament, r=rank: ");
				String selectionAlgStr = userInput.next();
				int selectionAlg;
				double k = .75; //default
				if(selectionAlgStr.equalsIgnoreCase("t")) {
					selectionAlg = 0;
				}
				else 
					selectionAlg = 1;
				System.out.print("Choose crossover algorithm, o=order1, c=cycle, t=two-point");
				String crossoverAlgStr = userInput.next();
				int crossoverAlg;
				if(crossoverAlgStr.equalsIgnoreCase("o"))
					crossoverAlg = 0;
				else if (crossoverAlgStr.equalsIgnoreCase("c"))
					crossoverAlg = 1;
				else if (crossoverAlgStr.equalsIgnoreCase("t"))
					crossoverAlg = 2;
				else 
					crossoverAlg = 3;

				double crossRate = .95;

				System.out.print("Choose mutation operator, p=pairwise exchange, c=cycle of 3: ");
				String mutationOpStr = userInput.next();
				int mutationOp;
				if(mutationOpStr.equalsIgnoreCase("p"))
					mutationOp = 0;
				else
					mutationOp = 1;
				double mutationRate = .01;

				System.out.print("Choose population size: ");
				int popSize = userInput.nextInt();
				System.out.print("Terminate after how many generations: ");
				int stopIterations = userInput.nextInt();
				System.out.print("Terminate after certain amount of time (minutes): ");
				int stopTime = userInput.nextInt() * 1000 * 60;

				System.out.print("Choose a name for this algorithm: ");
				String name = userInput.next();

				//add the GA to a list of algorithms to run
				final OLAGeneticAlgorithm newGA = new OLAGeneticAlgorithm(rows, cols, connectionMatrix, popSize, selectionAlg, k, crossoverAlg, crossRate, mutationOp, mutationRate, stopIterations, stopTime, name);
				Thread gaThread = new Thread(){
				    public void run(){
				      newGA.runGA();
				    }
				};
				algorithmThreads.add(gaThread);
			}
			else if(algorithmType.equalsIgnoreCase("s")) {
				System.out.print("Choose initial temperature: ");
				double temp = userInput.nextDouble();

				System.out.print("Choose initial iterations: ");
				int iterations = userInput.nextInt();

				System.out.print("Choose alpha (less than 1): ");
				double alpha = userInput.nextDouble();

				System.out.print("Choose beta (greater than 1): ");
				double beta = userInput.nextDouble();

				System.out.print("Stop after certain number of iterations: ");
				int stopIterations = userInput.nextInt();

				System.out.print("Stop after certain amount of time (minutes): ");
				int stopTime = userInput.nextInt() * 1000 * 60;

				System.out.print("Select perturbation function, p=pairwise exchange, c=cycle of 3: ");
				String perturbationOpStr = userInput.next();
				int perturbationOp;
				if(perturbationOpStr.equalsIgnoreCase("p"))
					perturbationOp = 0;
				else
					perturbationOp = 1;

				System.out.print("Choose a name for this algorithm: ");
				String name = userInput.next();

				final SimulatedAnnealing sa = new SimulatedAnnealing(rows, cols, connectionMatrix, temp, iterations, alpha, beta, stopIterations, stopTime, perturbationOp, name);
				Thread saThread = new Thread(){
				    public void run(){
				      sa.runSA();
				    }
				};
				algorithmThreads.add(saThread);
			}
			else if(algorithmType.equalsIgnoreCase("h")) {
				System.out.print("Stop after certain number of iterations: ");
				int stopIterations = userInput.nextInt();

				System.out.print("Stop after certain amount of time (minutes): ");
				int stopTime = userInput.nextInt() * 1000 * 60;

				System.out.print("Select perturbation function, p=pairwise exchange, c=cycle of 3: ");
				String perturbationOpStr = userInput.next();
				int perturbationOp;
				if(perturbationOpStr.equalsIgnoreCase("p"))
					perturbationOp = 0;
				else
					perturbationOp = 1;

				System.out.print("Choose a name for this algorithm: ");
				String name = userInput.next();

				final HillClimbing hc = new HillClimbing(rows, cols, connectionMatrix, stopIterations, stopTime, perturbationOp, name);
				Thread hcThread = new Thread(){
				    public void run(){
				      hc.runHillClimb();
				    }
				};
				algorithmThreads.add(hcThread);
			}
			else
				System.out.println("You did not input an expected command");
			System.out.print("Add another algorithm (y/n)? ");
			if(userInput.next().equalsIgnoreCase("n"))
				stopAsking = true;
		}

		for(Thread t : algorithmThreads){
			t.start();
		}
	}
}

import java.util.Scanner;
import java.util.ArrayList;
public class OLAAlgorithmGenerator {

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



	public static void main(String[] args) {
		ArrayList<Thread> algorithmThreads = new ArrayList<Thread>();

		Scanner userInput = new Scanner(System.in);
		boolean stopAsking = false;
		System.out.print("Generate random connections or contrived connections (type r  for random or c for contrived): ");
		String randomOrContrived = userInput.next();
		int[][] connectionMatrix;
		int rows;
		int cols;
		if(randomOrContrived.equalsIgnoreCase("r")) {
			System.out.print("Number of rows: ");
			rows = userInput.nextInt();
			System.out.print("Number of cols: ");
			cols = userInput.nextInt();
	
			System.out.print("Minimum connections: ");
			int minConn = userInput.nextInt();
			System.out.print("Maximum connections: ");
			int maxConn = userInput.nextInt();
			System.out.print("Weightedness/Bias of connections (1 for no bias): ");
			int weightedness = userInput.nextInt();
			System.out.println("Generating connection matrix...");
			connectionMatrix = OLAGraph.generateConnectionMatrix(rows, cols, minConn, maxConn, weightedness);
		}
		else {
			connectionMatrix = contrivedMatrix;
			rows = 6;
			cols = 3;
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
					System.out.print("Choose k value (between 0 and 1): ");
					k = userInput.nextDouble();
				}
				else 
					selectionAlg = 1;
				System.out.print("Choose crossover algorithm, o=order1, c=cycle, t=two-point: ");
				String crossoverAlgStr = userInput.next();
				int crossoverAlg;
				if(crossoverAlgStr.equalsIgnoreCase("o"))
					crossoverAlg = 0;
				else if (crossoverAlgStr.equalsIgnoreCase("c"))
					crossoverAlg = 1;
				else
					crossoverAlg = 2;

				System.out.print("Choose crossover rate (between 0 and 1): ");
				double crossRate = userInput.nextDouble();

				System.out.print("Choose mutation operator, p=pairwise exchange, c=cycle of 3: ");
				String mutationOpStr = userInput.next();
				int mutationOp;
				if(mutationOpStr.equalsIgnoreCase("p"))
					mutationOp = 0;
				else
					mutationOp = 1;
				System.out.print("Choose mutation rate (between 0 and 1): ");
				double mutationRate = userInput.nextDouble();

				System.out.print("Choose population size: ");
				int popSize = userInput.nextInt();
				System.out.print("Terminate after how many generations: ");
				int stopIterations = userInput.nextInt();
				System.out.print("Terminate after certain amount of time (minutes): ");
				int stopTime = userInput.nextInt() * 1000 * 60;

				System.out.print("Choose a name for this algorithm: ");
				String name = userInput.next();
				System.out.println(rows + "," + cols + "," + popSize+ "," + selectionAlg + "," + k + "," + crossoverAlg + "," + crossRate + "," + mutationOp + "," + mutationRate + "," + stopIterations + "," + name);
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
			System.out.println("Add another algorithm (y/n)? ");
			if(userInput.next().equalsIgnoreCase("n"))
				stopAsking = true;
		}

		for(Thread t : algorithmThreads){
			t.start();
		}
	}
}

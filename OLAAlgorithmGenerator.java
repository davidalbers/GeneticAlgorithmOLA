import java.util.Scanner;
import java.util.ArrayList;
public class OLAAlgorithmGenerator {

		// static int[][] contrivedMatrix = 
		// 	{
	 //  //{0,  1,   2,  3,   4   ,5,   6,  7,   8, 9,0,1,2,3,4,5,6,7}
  //  /*0*/{0,  0,   0,  0,   500 ,0,   0,  0,   0, 0,0,0,0,0,0,0,0,0},
  //  /*1*/{0,  0,   0,  0,   5000,0,   0,  0,   0, 0,0,0,0,0,0,0,0,0},
  //  /*2*/{0,  0,   0,  0,   500 ,0,   0,  0,   0, 0,0,0,0,0,0,0,0,0},
  //  /*3*/{0,  0,   0,  0,   5000,0,   0,  0,   0, 0,0,0,0,0,0,0,0,0},
  //  /*4*/{500,5000,500,5000,0,   5000,500,5000,500, 0,0,0,0,0,0,0,0,0},
  //  /*5*/{0,  0,   0,  0,   5000,0,0,0,0, 0,   0,0,0,0,0,0,0,0},
  //  /*6*/{0,  0,   0,  0,   500 ,0,0,0,0, 0,   0,0,0,0,0,0,0,0},
  //  /*7*/{0,  0,   0,  0,   5000,0,0,0,0, 0,   0,0,0,0,0,0,0,0},
  //  /*8*/{0,  0,   0,  0,   500 ,0,0,0,0, 0,   0,0,0,0,0,0,0,0},
  //  /*9*/{0,  0,   0,  0,0,0,0,0,0,            0  ,0   ,0  ,0   ,500 ,0   ,0  ,0,   0},
  //  /*0*/{0,  0,   0,  0,0,0,0,0,0,            0  ,0   ,0  ,0   ,5000,0   ,0  ,0,   0},
  //  /*1*/{0,  0,   0,  0,0,0,0,0,0,    		  0  ,0   ,0  ,0   ,500 ,0   ,0  ,0,   0},
  //  /*2*/{0,  0,   0,  0,0,0,0,0,0,    		  0  ,0   ,0  ,0   ,5000,0   ,0  ,0,   0},
  //  /*3*/{0,  0,   0,  0,0,0,0,0,0,    		  500,5000,500,5000,0   ,5000,500,5000,500},
  //  /*4*/{0,  0,   0,  0,0,0,0,0,0,    		  0  ,0   ,0  ,0   ,5000,0   ,0  ,0,   0},
  //  /*5*/{0,  0,   0,  0,0,0,0,0,0,    		  0  ,0   ,0  ,0   ,500 ,0   ,0  ,0,   0},
  //  /*6*/{0,  0,   0,  0,0,0,0,0,0,    		  0  ,0   ,0  ,0   ,5000,0   ,0  ,0,   0},
  //  /*7*/{0,  0,   0,  0,0,0,0,0,0,      	  0  ,0   ,0  ,0   ,500 ,0   ,0  ,0,   0}
	 //  //{0,  1,   2,  3,4,5,6,7,8,            9,  0,  1,   2,   3,   4,  5,   6,   7}
		// 	};


	// min fitness : 1677
	// 1,3,6,
	// 2,5,4,
	// 7,0,8,
	// static int[][] contrivedMatrix = 
	// {
	// 	{0,9,17,29,7,95,13,21,38},
	// 	{9,0,84,98,0,0,41,40,31},
	// 	{17,84,0,3,1,12,4,80,2},
	// 	{29,98,3,0,15,71,44,23,7},
	// 	{7,0,1,15,0,35,98,0,57},
	// 	{95,0,12,71,35,0,70,17,42},
	// 	{13,41,4,44,98,70,0,3,28},
	// 	{21,40,80,23,0,17,3,0,7},
	// 	{38,31,2,7,57,42,28,7,0}
	// };

    //  min: 4250 layout 5, 11, 2, 0, 1, 6, 
    //			  		 4, 9, 8, 10, 7, 3
	static int[][] contrivedMatrix = {
		{0,0,3,32,50,21,89,38,66,1,59,71},
		{0,0,100,24,14,35,51,54,4,3,32,19},
		{3,100,0,0,43,49,27,0,92,34,76,86},
		{32,24,0,0,0,12,1,46,21,12,0,15},
		{50,14,43,0,0,73,0,14,58,41,0,18},
		{21,35,49,12,73,0,3,30,4,8,13,38},
		{89,51,27,1,0,3,0,22,0,24,0,0},
		{38,54,0,46,14,30,22,0,58,75,85,43},
		{66,4,92,21,58,4,0,58,0,57,73,3},
		{1,3,34,12,41,8,24,75,57,0,11,21},
		{59,32,76,0,0,13,0,85,73,11,0,11},
		{71,19,86,15,18,38,0,43,3,21,11,0}
	};

// https://www.google.com/url?sa=t&rct=j&q=&esrc=s&source=web&cd=1&cad=rja&uact=8&ved=0CCEQFjAA&url=http%3A%2F%2Fepubs.siam.org%2Fdoi%2Fpdf%2F10.1137%2F1014035&ei=ReoqVdubA47doAT3qIHwBQ&usg=AFQjCNE7ithfjlJoaaGDikghUW9sSBmlmw&sig2=rPAQBeaunCFzalqNp3DASA&bvm=bv.90491159,d.cGU

	public static void main(String[] args) {
		ArrayList<Thread> algorithmThreads = new ArrayList<Thread>();

		Scanner userInput = new Scanner(System.in);
		boolean stopAsking = false;
		System.out.print("Generate random connections or contrived connections (type r for random or c for contrived): ");
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
			rows = 2;
			cols = 6;
		}
		if(rows * cols <= 12) {
			//can be solved by brute force in about 10 mins or less
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
		OLAGreedyAlgorithm greedyAlgorithm = new OLAGreedyAlgorithm(rows, cols, connectionMatrix);
		greedyAlgorithm.run();

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
				//System.out.println(rows + "," + cols + "," + popSize+ "," + selectionAlg + "," + k + "," + crossoverAlg + "," + crossRate + "," + mutationOp + "," + mutationRate + "," + stopIterations + "," + name);
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

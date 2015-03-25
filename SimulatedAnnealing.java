import java.util.ArrayList;
import java.util.Collections;
public class SimulatedAnnealing {
	
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
		SimulatedAnnealing sa = new SimulatedAnnealing();
		sa.run();
	}
	
	public void run() {
		(new SAThread(0)).start();
		(new SAThread(1)).start();
	}
	
	public class SAThread extends Thread {
		private int perturbationFunction;
		public SAThread(int perturbationFunction) {
			this.perturbationFunction = perturbationFunction;
		}
		 public void run() {
         	runSA(perturbationFunction);
         }
	}
	
	public void runSA(int perturbationFunction) {
		int rows = 6;
		int cols = 3;
		int[][] connectionMatrix = OLAGraph.generateConnectionMatrix(rows, cols, 0, 50);
		OLAGraph initialSolution = generateSolution(rows, cols, connectionMatrix);
		OLAGraph solutionN = initialSolution;
		double initialTemperature = 100.0;
		double temperature = initialTemperature;
		double iterations = 20.0;
		double alpha = .99999;
		double beta = 1.00001;
		int count = 0;
		int leastFitParentCount = 0;
		int fitParentCount = 0;
		int stop = 10000000;
		int minFitness = initialSolution.getFitness();
		String tag = "";
		if(perturbationFunction == 0)
			tag += "pairwise";
		else
			tag += "cycle";
		while(true) {
			int innerLoopCount = 0;
			while(innerLoopCount < iterations) {
				OLAGraph newSolution;
				if(perturbationFunction == 0)
					newSolution = pairwiseExchange(solutionN);
				else
					newSolution = cycleLength3(solutionN);
				if(newSolution.getFitness() < solutionN.getFitness()) {
					fitParentCount++;
					solutionN = newSolution;
				}
				else if (acceptLeastFitSolution(solutionN, newSolution, temperature)) {
					solutionN = newSolution;
					leastFitParentCount++;
				}
				innerLoopCount++;
				count++;
			}
			temperature = temperature * alpha; 
			iterations = iterations * beta;
			if(solutionN.getFitness() < minFitness)
				minFitness = solutionN.getFitness();
			System.out.println(tag + " new solution gen " + count  + " temp " + temperature + " parent ratio "  + ((double)leastFitParentCount/fitParentCount) + leastFitParentCount + " min fitness " + minFitness + "\n" + solutionN.toString());
		}
	}
	
	public OLAGraph pairwiseExchange(OLAGraph oldSoln) {
		//System.out.println("before exchange\n" + oldSoln.toString());
		int[] layout = oldSoln.copy().getLayout();

		int swapIndex1 = (int)(Math.random() * layout.length);
		int swapIndex2 = (int)(Math.random() * layout.length);
		int swapTemp = layout[swapIndex1];

		layout[swapIndex1] = layout[swapIndex2];
		layout[swapIndex2] = swapTemp;
		OLAGraph newSoln = new OLAGraph(oldSoln.getRows(), oldSoln.getColumns(), layout, oldSoln.getConnectionMatrix());
		//System.out.println("after exchange\n" + newSoln.toString());
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
		
		//System.out.println(cycleI + "," + cycleJ + "," + cycleK + " before exchange\n" + oldSoln.toString());
		int cycleINew = layout[cycleK];
		int cycleKNew = layout[cycleJ];
		int cycleJNew = layout[cycleI];
		
		layout[cycleI] = cycleINew;
		layout[cycleJ] = cycleJNew;
		layout[cycleK] = cycleKNew;
		OLAGraph newSoln = new OLAGraph(oldSoln.getRows(), oldSoln.getColumns(), layout, oldSoln.getConnectionMatrix());
		//System.out.println("after exchange\n" + newSoln.toString());
		return newSoln;
	}
	
	public boolean acceptLeastFitSolution(OLAGraph oldSoln, OLAGraph newSoln, double temp) {
		return Math.random() <  Math.exp(oldSoln.getFitness() - newSoln.getFitness()/temp);
	}
	
	public OLAGraph generateSolution(int rows, int cols, int[][] connMatrix) {
		ArrayList<Integer> availablePositions = new ArrayList<Integer>();
		for(int i = 0; i < rows * cols; i++) {
			availablePositions.add(i);
		}
		int[] randomLayout = new int[rows * cols];
		for(int i = 0; i < randomLayout.length; i++) {
			int vertexPosition = availablePositions.remove( (int)(Math.random() * availablePositions.size()) );
			randomLayout[i] = vertexPosition;
		}
		return new OLAGraph(rows, cols, randomLayout, connMatrix);
	}
}

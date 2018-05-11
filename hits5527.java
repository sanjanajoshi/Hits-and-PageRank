/* SANJANA ANIRUDDHA JOSHI   cs610 5527 prp */

import java.io.*;
import static java.lang.Math.*;

public class hits5527 {
	// Declaration of instance variables
	int iteration;
	int initialval;
	String filename;
	int numVertices;
	int numEdges;
	int[][] adjMatrix;
	double[] hub0;
	double[] auth0;
	double errorrate = 0.00001;

	// default constructor
	hits5527() {
	}

	// Constructor to initialize instance variables with the arguments
	hits5527(int iteration, int initialval, String filename) {
		this.iteration = iteration;
		this.initialval = initialval;
		this.filename = filename;
		// To read the file
		try {
			FileReader input = new FileReader(filename);
			BufferedReader buffread = new BufferedReader(input);
			String line = null;

			try {
				if ((line = buffread.readLine()) != null) {
					String[] array = line.split(" ");
					numVertices = Integer.parseInt(array[0]);
					numEdges = Integer.parseInt(array[1]);
				}
			} catch (NumberFormatException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			// Initializing of the adjacency matrix to default value of 0
			adjMatrix = new int[numVertices][numVertices];

			try {
				while ((line = buffread.readLine()) != null) {
					String[] array = line.split(" ");
					int a = Integer.parseInt(array[0]);

					int b = Integer.parseInt(array[1]);
					adjMatrix[a][b] = 1;
					//System.out.println(a + " " + b);
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			hub0 = new double[numVertices];
			auth0 = new double[numVertices];
			switch (initialval) {
			case 0:
				for (int i = 0; i < numVertices; i++) {
					hub0[i] = 0;
					auth0[i] = 0;
				}
				break;
			case 1:
				for (int i = 0; i < numVertices; i++) {
					hub0[i] = 1;
					auth0[i] = 1;
				}
				break;
			case -1:
				for (int i = 0; i < numVertices; i++) {
					hub0[i] = 1.0 / numVertices;
					auth0[i] = 1.0 / numVertices;
				}
				break;
			case -2:
				for (int i = 0; i < numVertices; i++) {
					hub0[i] = 1.0 / Math.sqrt(numVertices);
					auth0[i] = 1.0 / Math.sqrt(numVertices);
				}
				break;
			}

		} catch (FileNotFoundException exception) {
			System.out.println(exception.getMessage());
			System.exit(0);
		}
	}

	public static void main(String[] args) {
		if (args.length != 3) {
			System.out.println("Run by providing: hits5527 <iterations> <initialvalue> <filename>");
			return;
		}
		// Parsing the command-line arguments
		int iterations = Integer.parseInt(args[0]);
		int initialvalue = Integer.parseInt(args[1]);
		String filename = args[2];

		if (!(initialvalue >= -2 && initialvalue <= 1)) {
			System.out.println("Initialvalue can only be -2, -1, 0 or 1");
			return;
		}
		hits5527 hits = new hits5527(iterations, initialvalue, filename);

		hits.hitsAlgoritm5527(); //Calling method to perform Kleinberg's HITS algorithm
	}

	boolean isEnoughPrecision5527(double[] p, double[] q) {
		for (int i = 0; i < numVertices; i++) {
			if (abs(p[i] - q[i]) > errorrate)
				return false;
		}
		return true;
	}

	public void hitsAlgoritm5527() {
		double[] hub = new double[numVertices];
		double[] auth = new double[numVertices];
		double[] authPrevious = new double[numVertices]; // the previous iteration values of auth is used to check for precision (convergence) 
		double[] hubPrevious = new double[numVertices]; // the previous iteration values of hub is used to check for precision (convergence)
														
		// If the graph is large, i.e, has number of vertices >10, then the values for iteration = 0 and initialvalue = -1
		
		if (numVertices > 10) {
			iteration = 0;
			for (int i = 0; i < numVertices; i++) {
				//setting default initial value to case -1
				hub[i] = 1.0 / numVertices;
				auth[i] = 1.0 / numVertices;
				hubPrevious[i] = hub[i];
				authPrevious[i] = auth[i];
			}
			// declaring and initializing a counter i
			int i = 0;
			do {
				for (int x = 0; x < numVertices; x++) {
					authPrevious[x] = auth[x];
					hubPrevious[x] = hub[x];
				}

				// Computation of authoritative, hub and scaling
				calBaseSet5527(auth, hub);
				// incrementing counter
				i++;
			} while (false == isEnoughPrecision5527(auth, authPrevious) || false == isEnoughPrecision5527(hub, hubPrevious));
			System.out.println("Iteration Number when converged:  " + i);
			for (int l = 0; l < numVertices; l++) {
				System.out.printf(" A/H[%d]=%.7f/%.7f\n",l,Math.round(auth[l]*10000000.0)/10000000.0,Math.round(hub[l]*10000000.0)/10000000.0);
			}
			return;
		}

		// Initialization if N is not greater than 10.
		for (int i = 0; i < numVertices; i++) {
			hub[i] = hub0[i];
			auth[i] = auth0[i];
			hubPrevious[i] = hub[i];
			authPrevious[i] = auth[i];
		}

		// Base iteration
		System.out.print("Base:    0 :");
		for (int i = 0; i < numVertices; i++) {
			System.out.printf(" A/H[%d]=%.7f/%.7f", i,auth0[i],
					hub0[i]);
		}
		//for iteration is greater than 0, small graphs.
		if (iteration > 0) {
			for (int i = 0; i < iteration; i++) { 
				// Computation of authoritative, hub and scaling
				calBaseSet5527(auth, hub); 

				System.out.println();
				System.out.print("Iter:    " + (i + 1) + " :");
				for (int l = 0; l < numVertices; l++) {
					System.out.printf(" A/H[%d]=%.7f/%.7f",l,Math.round(auth[l]*10000000.0)/10000000.0,Math.round(hub[l]*10000000.0)/10000000.0);
				}

			} 
		} 
		else {
			// for iteration less than 0 and 0
			errorrate = Math.pow(10, iteration);//errorrate for iteration less than 0, i.e, -1, -2 and so on.
			if (iteration == 0) {
				errorrate = Math.pow(10, -5);// for iteration is 0, default errorrate of 10^-5
			}
			int i = 0;
			do {
				for (int r = 0; r < numVertices; r++) {
					authPrevious[r] = auth[r];
					hubPrevious[r] = hub[r];
				}
				// Computation of authoritative, hub and scaling
				calBaseSet5527(auth, hub);
				i++;
				System.out.println();
				System.out.print("Iter:    " + i + " :");
				for (int l = 0; l < numVertices; l++) {
					System.out.printf(" A/H[%d]=%.7f/%.7f",l,Math.round(auth[l]*10000000.0)/10000000.0,Math.round(hub[l]*10000000.0)/10000000.0);
				}
			} while (false == isEnoughPrecision5527(auth, authPrevious) || false == isEnoughPrecision5527(hub, hubPrevious));
		}
		System.out.println();
	}
//Computation of base set which includes hub and authoritative values and scaling of them.
	private void calBaseSet5527(double[] auth, double[] hub) {
		double authScalingFact;
		double authSqSum;
		double hubScalingFact;
		double hubSqSum;
		//calculation of auth values
		for (int p = 0; p < numVertices; p++) {
			auth[p] = 0.0;
		}

		for (int j = 0; j < numVertices; j++) {
			for (int k = 0; k < numVertices; k++) {
				if (adjMatrix[k][j] == 1) {
					auth[j] += hub[k];
				}
			}
		}

		// Calculation of hub
		for (int p = 0; p < numVertices; p++) {
			hub[p] = 0.0;
		}

		for (int j = 0; j < numVertices; j++) {
			for (int k = 0; k < numVertices; k++) {
				if (adjMatrix[j][k] == 1) {
					hub[j] += auth[k];
				}
			}
		}

		// To scale auth values
		authScalingFact = 0.0;
		authSqSum = 0.0;
		for (int t = 0; t < numVertices; t++) {
			authSqSum += auth[t] * auth[t];
		}
		authScalingFact = Math.sqrt(authSqSum);
		for (int t = 0; t < numVertices; t++) {
			auth[t] = auth[t] / authScalingFact;
		}

		// To scale hub values
		hubScalingFact = 0.0;
		hubSqSum = 0.0;
		for (int l = 0; l < numVertices; l++) {
			hubSqSum += hub[l] * hub[l];
		}
		hubScalingFact = Math.sqrt(hubSqSum);
		for (int l = 0; l < numVertices; l++) {
			hub[l] = hub[l] / hubScalingFact;
		}
	}
}

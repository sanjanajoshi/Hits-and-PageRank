/* SANJANA ANIRUDDHA JOSHI   cs610 5527 prp */

import java.io.*;
import static java.lang.Math.*;

public class pgrk5527 {
	// Declaration of instance variables
	int iteration;
	int initialval;
	String filename;
	int numVertices;
	int numEdges;
	int[][] adjMatrix;
	final double d = 0.85;
	double errorrate = 0.00001;
	double[] auth;
	int[] C; // array for out-degree (outgoing links) of vertex Ti

	// default constructor
	pgrk5527() {
	}

	// Constructor to initialize instance variables with the arguments
	pgrk5527(int iteration, int initialval, String filename) {
		this.iteration = iteration;
		this.initialval = initialval;
		this.filename = filename;

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
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// C[i] calculation. C[i] matrix is the out-degree of vertex Ti
			C = new int[numVertices];
			for (int i = 0; i < numVertices; i++) {
				C[i] = 0;
				for (int j = 0; j < numVertices; j++) {
					C[i] += adjMatrix[i][j];

				}
			}

			auth = new double[numVertices];
			switch (initialval) {
			case 0:
				for (int i = 0; i < numVertices; i++) {
					auth[i] = 0;
				}
				break;
			case 1:
				for (int i = 0; i < numVertices; i++) {
					auth[i] = 1;
				}
				break;
			case -1:
				for (int i = 0; i < numVertices; i++) {
					auth[i] = 1.0 / numVertices;
				}
				break;
			case -2:
				for (int i = 0; i < numVertices; i++) {
					auth[i] = 1.0 / Math.sqrt(numVertices);
				}
				break;
			}
		} catch (FileNotFoundException exception) {
		}
	}

	public static void main(String[] args) {
		if (args.length != 3) {
			System.out.println("Run by providing: pgrk5527 <iterations> <initialvalue> <filename>");
			return;
		}
		// Parsing the command-line arguments
		int iterations = Integer.parseInt(args[0]);
		int initialvalue = Integer.parseInt(args[1]);
		String filename = args[2];
		
		if (!(initialvalue >= -2 && initialvalue <= 1)) {
			System.out.println("Enter -2, -1, 0 or 1 for initialvalue");
			return;
		}

		pgrk5527 pageRank = new pgrk5527(iterations, initialvalue, filename);

		pageRank.pgrankAlgorithm5527(); //Calling method to perform PageRank computations
	}

	boolean checkPrecision5527(double[] p, double[] q) {
		for (int i = 0; i < numVertices; i++) {
			if (abs(p[i] - q[i]) > errorrate)
				return false;
		}
		return true;
	}
//PageRank Algorithm, calculations done using synchronous update 
	void pgrankAlgorithm5527() {
		double[] P = new double[numVertices];
		boolean flag = true;
		// If the graph is large, i.e, has number of vertices >10, then the values for iteration = 0 and initialvalue = -1
		if (numVertices > 10) {
			iteration = 0;
			for (int i = 0; i < numVertices; i++) {
				auth[i] = 1.0 / numVertices;
			}
			int i = 0;
			do {
				if (flag == true) {
					flag = false;
				} else {
					for (int l = 0; l < numVertices; l++) {
						auth[l] = P[l];
					}
				}
				for (int l = 0; l < numVertices; l++) {
					P[l] = 0.0;
				}

				for (int j = 0; j < numVertices; j++) {
					for (int k = 0; k < numVertices; k++) {
						if (adjMatrix[k][j] == 1) {
							P[j] += auth[k] / C[k];
						}
					}
				}

				// Computation of PageRank for all vertices (web-pages)
				for (int l = 0; l < numVertices; l++) {
					P[l] = d * P[l] + (1 - d) / numVertices;
				}
				i++;
			} while (checkPrecision5527(auth, P) != true);

			// PageRank at the stopping iteration
			System.out.println("Iteration Number when converged: " + i);
			for (int l = 0; l < numVertices; l++) {
				System.out.printf("P[" + l + "] = %.7f\n", Math.round(P[l] * 10000000.0) / 10000000.0);
			}
			return;
		}
		// Base iteration
		System.out.print("Base    : 0");
		for (int j = 0; j < numVertices; j++) {
			System.out.printf(" :P[" + j + "]=%.7f", Math.round(auth[j] * 10000000.0) / 10000000.0);
		}

		if (iteration > 0) {
			for (int i = 0; i < iteration; i++) {
				for (int l = 0; l < numVertices; l++) {
					P[l] = 0.0;
				}

				for (int j = 0; j < numVertices; j++) {
					for (int k = 0; k < numVertices; k++) {
						if (adjMatrix[k][j] == 1) {
							P[j] += auth[k] / C[k];

						}
					}
				}

				// Computation of PageRank for all vertices (web-pages)
				System.out.println();
				System.out.print("Iter    : " + (i + 1));
				for (int l = 0; l < numVertices; l++) {
					P[l] = d * P[l] + (1 - d) / numVertices;
					System.out.printf(" :P[" + l + "]=%.7f", Math.round(P[l] * 10000000.0) / 10000000.0);
				}

				for (int l = 0; l < numVertices; l++) {
					auth[l] = P[l];
				}
			}
			System.out.println();
		} else {
			// if number of iteration is 0 or less than 0
			errorrate = Math.pow(10, iteration); //errorrate for iteration less than 0, i.e, -1, -2 and so on.
			if (iteration == 0) {
				errorrate = Math.pow(10, -5); // for iteration is 0, default errorrate of 10^-5
			}
			int i = 0;
			do {
				if (flag == true) {
					flag = false;
				} else {
					for (int l = 0; l < numVertices; l++) {
						auth[l] = P[l];
					}
				}
				for (int l = 0; l < numVertices; l++) {
					P[l] = 0.0;
				}

				for (int j = 0; j < numVertices; j++) {
					for (int k = 0; k < numVertices; k++) {
						if (adjMatrix[k][j] == 1) {
							P[j] += auth[k] / C[k];
						}
					}
				}

				// Computation of PageRank for all vertices (web-pages)
				System.out.println();
				System.out.print("Iter    : " + (i + 1));
				for (int l = 0; l < numVertices; l++) {
					P[l] = d * P[l] + (1 - d) / numVertices;
					System.out.printf(" :P[" + l + "]=%.7f", Math.round(P[l] * 10000000.0) / 10000000.0);
				}
				i++;
			} while (checkPrecision5527(auth, P) != true);
			System.out.println();
		}
	}
}

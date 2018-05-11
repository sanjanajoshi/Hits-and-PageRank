# Hits-and-PageRank
Implementation of Kleinberg's hits and Google's PageRank iterative algorithm.
At iteration t the pagerank values are computed using the previous iteration (t-1) and the updates are synchronous.
We either run the algorithm for fixed number of iterations or error-rate, i.e, an iteration equal to 0 corresponds to 10^-5. ( -1, -2... -6 represents 10^-1, 10^-2 and so on).
Initial value helps to set up intial values of vectors, i.e, 0 is initialized to 0, 1 initializes vectors to the value of 1 and -1 initializes 1/N, N being the number og web pages and -2 sets it to 1/√N.

A file is read which contains the input (directed) graph. The first line contains two numbers: the number of vertices followed by the number of edges. All vertices are labeled 0, . . . , N − 1. Expect N to be less than 1,000,000. 
In each line an edge (i, j) is represented by i j. 

Output: Vector values are printed to 7 decimal digits. If the graph has N GREATER than 10, then the values for iterations, initial value are automatically set to 0 and -1 respectively and the hub/authority/pageranks at the stopping iteration (i.e t) are only shown. 
Stopping condition: At each iteration the current and previous iteration values are compared for every vertex and if the difference is less than error-rate for each vertex, only then we stop computation at that iteration.

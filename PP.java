/** 
Savanna Moss & Celeste Rosendale
CSC 3380 Analysis of Algorithms
Date: 12/01/2019
**/

package FinalProject;

import java.util.*;
import java.io.*;

public class PP {

    public static void main(String[] args) throws Exception {
        // Start file setup

        // Input File:
        File inputFile = new File("graphs.txt");
        if (!inputFile.exists()) {
            System.out.println("Input file, " + inputFile + ", does not exist.");
            System.exit(0);
        }
        // Output File:
        File outputFile = new File("results.txt");

        // Make Scanner variable to read from input file, and 
        // make Printwriter variable to print to output
        Scanner in = new Scanner(inputFile);
        PrintWriter output = new PrintWriter(outputFile);

        // End file setup
        
        // Read in the number of graphs in the file.
        int m = in.nextInt();

        for (int i = 0; i < m; i++) {
            // Read in the number of vertices on the graph.
            int n = in.nextInt();

            // Set up graph as a weighted adjancency matrix.
            int[][] G = setUpGraph(n, in);

            // Do algorithm to find shortest path
            LinkedList<Edge> edges = findMST(n, G);
            int[] frequency = findFrequency(edges, n);
            int source = sourceFind(edges, frequency);
            
            // Complete cost find
            costFind(G, n, edges, frequency, source, output);

            // END ITERATION
        }

        // Close input and output files
        in.close();
        output.close();
    }

    public static int[][] setUpGraph(int n, Scanner in) {
        // Establish adjancency matrix.
        int[][] G = new int[n][n];

        // For loop for edge weight values of connections.
        // Loop over vertices.
        for (int i = 0; i < n; i++) {
            // Loop over current vertex's connections.
            for (int j = 0; j < n; j++) {
                G[i][j] = in.nextInt();
            }
        }

        // Return the weighted adjancency matrix of the current graph.
        return G;
    }

    public static boolean weightCheck(int weight, int v1, int v2, LinkedList<Edge> edges) {

        if (edges.size() > 0) {
            // Traverse until reaching the end of the list.
            int i = 0;
            while (i < edges.size()) {
                // If we find the weight we are looking for.
                if (edges.get(i).getWeight() == weight && (edges.get(i).getV1() == v1 && edges.get(i).getV2() == v2) || (edges.get(i).getV1() == v2 && edges.get(i).getV2() == v1)) {
                    return true;
                }
                // Else, continue through the LinkedList.
                ++i;
            }
        }
        return false;
    }

    public static LinkedList<Edge> findMST(int n, int[][] G) {
        LinkedList<Edge> edges = new LinkedList<>();

        //Finding the smallest edge in each line (will create the smallest MST possible).
        for (int i = 0; i < n; i++) {
            int vertex1 = 0, vertex2 = 0, min = 0;

            // Resetting minimum value we want.
            for (int j = 0; j < n; j++) {
                if (!weightCheck(G[i][j], i, j, edges) && G[i][j] != 0) {
                    min = G[i][j];
                    vertex1 = i; // First value is 0, and that breaks things.
                    vertex2 = j;
                    break;
                }
            }
            
            // Entering row to check.
            for (int j = 0; j < n; j++) {
                if (G[i][j] < min && G[i][j] != 0 && !weightCheck(G[i][j], i, j, edges)) { // less than our current min and greater than 0
                    min = G[i][j];
                    vertex1 = i;
                    vertex2 = j;
                }
            }
            Edge test = new Edge(vertex1, vertex2, min);

            // Add this edge to our linked list.
            edges.add(test);
        }
        
        return edges;
    }

    public static int[] findFrequency(LinkedList<Edge> edges, int n) {
        int[] frequency = new int[n];

        // Loop through the LinkedList and count how many times each vertex appears on either side of an edge.
        int i = 0;
        while (i < edges.size()) {
            frequency[edges.get(i).getV1()]++;
            frequency[edges.get(i).getV2()]++;

            i++;
        }

        return frequency;
    }

    public static int sourceFind(LinkedList<Edge> edges, int[] frequency) {
        /*
            Linked List -> Edge nodes
            2 vertexes and edge weight

            traverse through the linked list and count the edge's total connections
            (frequency it appears in the list)

            the edge with the most weight will be our main concern
            you pick your source based off of the vertex connected to the
            heaviest edge with the most connections
         */
        
        int starterWeight = edges.getFirst().getWeight();
        int v1 = edges.getFirst().getV1();
        int v2 = edges.getFirst().getV2();
        
        int i = 0;
        while (i < edges.size()) {
            if (edges.get(i).getWeight() > starterWeight) {
                v1 = edges.get(i).getV1();
                v2 = edges.get(i).getV2();
            }

            i++;
        }

        if (frequency[v1] > frequency[v2]) {
            return v1;
        } else {
            return v2;
        }
    }

    public static boolean vertexCheck(boolean[] visited) {
        // Loop through visited array and make sure each vertex has been visited.
        for (int i = 0; i < visited.length; i++) {
            // If a vertex has not been visited, return false.
            if (visited[i] == false) {
                return false;
            }
        }
        // If every vertex has been visited, return true.
        return true;
    }

    public static int findMIndex(int[] connectionF, int[][] MSTG, int source, boolean[] visited) {
        int minVal = -1;
        int mindex = -1;

        // Find first index that holds a value greater than 0 and set that as initial minimum.
        for (int i = 0; i < connectionF.length; i++) {
            if (connectionF[i] > 0) {
                minVal = connectionF[i];
                mindex = i;
                break;
            }
        }

        // Find minimum value of the array.
        for (int i = 1; i < connectionF.length; i++) {
            if (connectionF[i] < minVal && connectionF[i] > 0) {
                minVal = connectionF[i];
                mindex = i;
            }
        }

        int count = 0, totalConnections = 0; // total connections for a later check, just utilizing this loop
        for (int i = 0; i < connectionF.length; i++) {
            if (connectionF[i] == minVal) {
                count++;
            }
            if (connectionF[i] > 0) {
                totalConnections++;
            }
        }

        if (count > 1) {
            // loop through those that are just the minimum value, and find the larger edge
            int maxWeight = 0;
            for (int i = 0; i < connectionF.length; i++) {
                if (connectionF[i] == minVal) {
                    if (MSTG[source][i] > maxWeight) {
                        maxWeight = MSTG[source][i];
                        mindex = i;
                    }
                }
            }
        }

        if (!visited[mindex]) {
            return mindex;
        } 
        else {
            if (totalConnections == 1) {
                return mindex;
            } 
            else {
                connectionF[mindex] = 0;
                return findMIndex(connectionF, MSTG, source, visited);
            }
        }
    }

    public static void costFind(int[][] G, int n, LinkedList<Edge> edges, int[] frequency, int source, PrintWriter output) {
        /*
            1 -
            From our source, pick from our connected edges, pick the most isolated
             - If theres a tie, go with the larger weight

            Step Break Down
              1 - Find our connected edges to our Source - Come from Edges or the new Graph
              2 - Look at the connections of our connections (frequency only)
              3 - We pick the vertex that has the smallest frequency
                  - If theres a tie, go with the larger edge (now looking at proper weights)
              4 - Go to step 2.


            2 -
            Traverse to the next edge, adding the weight to our total
             - If you hit a dead end, traverse back from whence you came


            Step Break Down
              1 - Traverse to next node
              2 - Add weight we just travelled to total sum
                  - Add new vertex travelled to the integer linked list
              3 - Mark new node on visited array as true
              4 - If our new node has no other connections, head back to the node we came from, repeating 1-2

            3 - 
            Repeat until we have hit all vertices, then return to source
              - On the return to source, consider both MST and the Graph, as there may be a better path.
         */

        /*
            ACTUAL CODE BELOW
         */
        
        // Initializing a base sum for the total cost
        int sum = 0;

        // set up a graph with only the MST connections we found before
        int[][] MSTG = new int[n][n];
        
        int i = 0;
        while (i < edges.size()) { //building our graph to do costs
            MSTG[edges.get(i).getV1()][edges.get(i).getV2()] = MSTG[edges.get(i).getV2()][edges.get(i).getV1()] = edges.get(i).getWeight();
            ++i;
        }

        // set up visited array to make sure we visit every vertex at least once
        boolean[] visited = new boolean[n];

        // set up variable to save home
        int home = source;

        // set up LinkedList to keep track of traveled path as we go through.
        LinkedList<Integer> bestPath = new LinkedList<>();
        bestPath.add(home);
        int[] connectionF = new int[n];
        int nextIndex;

        while (!vertexCheck(visited)) {
            visited[source] = true;
            for (i = 0; i < MSTG.length; i++) {
                if (MSTG[source][i] != 0) {
                    connectionF[i] = frequency[i];
                }
                else {
                    connectionF[i] = 0;
                }
            }

            // finding next place to go
            nextIndex = findMIndex(connectionF, MSTG, source, visited);

            // Setting our new node to visited and adding to our path
            // visited[nextIndex] = true;
            bestPath.add(nextIndex);
            sum += MSTG[source][nextIndex];

            //'traversing'
            source = nextIndex;
        }

        // If source == home, we're already back - If there is a possible better direct path, we find it
        if (source != home) { // Finding the vertices we traveled until we hit our earliest occurance of home
            int[] pathHome = new int[n];
            i = 0;
            while (bestPath.peekLast() != home) {
                pathHome[i] = bestPath.pollLast();
                ++i;
            }
            // putting the vertices we just polled back into our path
            for (i = n - 1; i >= 0; i--) {
                if (pathHome[i] != 0) {
                    bestPath.add(pathHome[i]);
                }
            }

            int v1 = 0, v2 = 1, pPath = 0;
            while (pathHome[v2] != 0 && v2 != n) { // finding a possible path home travelling back through vertices
                pPath += MSTG[pathHome[v1]][pathHome[v2]];
                ++v1;
                ++v2; // we have the vertices, not the actual weights, so we gotta pull it from our graph
            }

            // comparing the path travelled around to a direct path home
            if (G[source][home] <= pPath) { // direct path is better or equal
                sum += G[source][home];
            } 
            else {
                sum += pPath;

                // adding our path to the print out
                for (i = 1; i < n; i++) {
                    if (pathHome[i] != 0 && pathHome[i] != home) {
                        bestPath.add(i);
                    } else {
                        break;
                    }
                }
            }
        }
        
        bestPath.pollLast();
        // print out our path
        output.println(printPath(bestPath));

        // print out path cost
        output.println(sum);
    }

    public static String printPath(LinkedList<Integer> bestPath) {

        // Convert the LinkedList to an Array for easy traversal.
        Object[] path = bestPath.toArray();

        // Initialize empty String.
        String pathString = "";

        // Loop over the array and add each value i the path to the String.
        for (int i = 0; i < path.length; i++) {
            pathString += String.format(path[i] + " ");
        }

        // Return the String.
        return pathString;
    }
}

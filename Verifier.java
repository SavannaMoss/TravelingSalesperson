/** 
Savanna Moss & Celeste Rosendale
CSC 3380 Analysis of Algorithms
Date: 12/01/2019
**/

package verifier;

import java.util.*;
import java.io.*;

public class Verifier {

    public static void main(String[] args) throws Exception {
        // Start file setup

        // Input File:
        File inputFile1 = new File("graphs.txt");
        File inputFile2 = new File("results.txt");

        if (!inputFile1.exists()) {
            System.out.println("Input file, " + inputFile1 + ", does not exist.");
            System.exit(0);
        }
        if (!inputFile2.exists()) {
            System.out.println("Input file, " + inputFile2 + ", does not exist.");
            System.exit(0);
        }

        // Make Scanner variables to read from input files
        Scanner in1 = new Scanner(inputFile1);
        Scanner in2 = new Scanner(inputFile2);

        // End file setup
        // graphs.txt input
        // Read in the number of graphs in the file.
        int m = in1.nextInt();

        boolean[] graphValidity = new boolean[m];

        for (int i = 0; i < m; i++) {
            
            // Read in the number of vertices on the graph.
            int n = in1.nextInt();

            // Set up graph as a weighted adjancency matrix.
            int[][] G = setUpGraph(n, in1);
            
            // results.txt input            
            // Account for weird thing that keeps happening with graphs after the first one.
            String first = "";            
            if (i > 0) {
                first = in2.next();
            }
            
            // Read in the path and split it into an array.
            String pathString = in2.nextLine();
            String[] pathStringArr = pathString.split(" ");
            if (i > 0) {
                pathStringArr[0] = first;
            }
            int[] path = new int[pathStringArr.length];
            for (int j = 0; j < path.length; j++) {
                path[j] = Integer.parseInt(pathStringArr[j]);                
            }

            // Read the path cost from the file.
            int pathCost1 = in2.nextInt();
            
            // Initialize new value to calculate path cost based on graph.
            int pathCost2 = 0;

            // Intialize new array to mark vertices as visited.
            boolean[] visited = new boolean[n];

            // Mark first vertex in path array as visited.
            visited[path[0]] = true;

            // Calculate path cost based on graph.
            for (int j = 1; j < path.length; j++) {
                pathCost2 += G[path[j - 1]][path[j]];
                visited[path[j]] = true;
            }
            
            // Add cost to go back home.
            pathCost2 += G[path[path.length-1]][path[0]];

            // Check if graph is valid.
            if (pathCost1 == pathCost2 && arrCheck(visited)) {
                graphValidity[i] = true;
            } else {
                graphValidity[i] = false;
            }
            
            // END ITERATION
        }

        // If all indices in the array have been marked true.
        if (arrCheck(graphValidity)) {
            System.out.println("All cases are valid!");
        } else {
            System.out.println("The following cases are invalid or incorrect:");
            for (int i = 0; i < graphValidity.length; i++) {
                // If a value is false, print it out.
                if (graphValidity[i] == false) {
                    System.out.println(i + 1);
                }
            }
        }

        // Close input files
        in1.close();
        in2.close();
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

    public static boolean arrCheck(boolean[] arr) {
        // Loop through array and make sure each value is true.
        for (int i = 0; i < arr.length; i++) {
            // If a value is false, return false.
            if (arr[i] == false) {
                return false;
            }
        }
        // If every value is true, return true.
        return true;
    }
}

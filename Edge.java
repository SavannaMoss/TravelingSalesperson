/** 
Savanna Moss & Celeste Rosendale
CSC 3380 Analysis of Algorithms
Date: 12/01/2019
**/

package FinalProject;

// Edge.java
// Edge Object for use in main PP calculations.

public class Edge {

    // DATA MEMBERS
    private int v1, v2, weight;

    // CONSTRUCTOR
    public Edge(int v1, int v2, int weight) {
        this.v1 = v1;
        this.v2 = v2;
        this.weight = weight;
    }

    // GETTERS
    public int getV1() {
        return v1;
    }

    public int getV2() {
        return v2;
    }

    public int getWeight() {
        return weight;
    }

    // SETTER
    public void setV1(int v1) {
        this.v1 = v1;
    }

    public void setV2(int v2) {
        this.v2 = v2;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }
}

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Vector;

/**
 * This program is an implementation of Kruskal's algorithm.
 *
 * @author Shane Humphrey humphrey.268@osu.edu
 *
 */
public final class Kruskal {

    /**
     * Private constructor so this utility class cannot be instantiated.
     */
    private Kruskal() {
    }

    private static Comparator<Map.Entry<Set<Integer>, Integer>> compWeights = new Comparator<Map.Entry<Set<Integer>, Integer>>() {
        @Override
        public int compare(Entry<Set<Integer>, Integer> o1,
                           Entry<Set<Integer>, Integer> o2) {
            return o1.getValue().compareTo(o2.getValue());
        }
    };

    private static Comparator<Map.Entry<Integer, Integer>> compNodes = new Comparator<Map.Entry<Integer, Integer>>() {
        @Override
        public int compare(Entry<Integer, Integer> o1,
                           Entry<Integer, Integer> o2) {
            return o1.getValue().compareTo(o2.getValue());
        }
    };

    public static void createMap(Map<Set<Integer>, Integer> weights,
                                 BufferedReader input) {
        String line;
        try {
            line = input.readLine();
        } catch (IOException e) {
            System.err.println("Error reading a line from input file.");
            return;
        }
        // Current node/line
        int currentNode = 1;
        while (line != null) {
            while (line.length() > 0) {
                Set<Integer> edge = new HashSet<Integer>();
                // Read the node and its associated weight
                int sep = line.indexOf(" ");
                String curNode = line.substring(0, sep);
                line = line.substring(sep + 1, line.length());
                int sep2 = line.indexOf(" ");
                String weight = "";
                if (sep2 >= 0) {
                    weight = line.substring(0, sep2);
                    line = line.substring(sep2 + 1, line.length());
                } else {
                    weight = line.substring(0, line.length());
                    line = line.substring(line.length(), line.length());
                }
                edge.add(currentNode);
                edge.add(Integer.parseInt(curNode));
                weights.put(edge, Integer.parseInt(weight));
            }
            currentNode++;
            try {
                line = input.readLine();
            } catch (IOException e) {
                System.err.println("Error reading a line from input file.");
                return;
            }
        }
    }

    public static boolean isConnected(int startNode, int soughtNode,
                                      Vector<Set<Integer>> edgeVec) {
        // For each node connected to startNode, look to see if it is connected to soughtNode.
        Vector<Integer> previousStartNodes = new Vector<Integer>();
        Vector<Integer> newStartNodes = new Vector<Integer>();
        newStartNodes.add(startNode);
        while (!newStartNodes.isEmpty()) {
            int currentNode = newStartNodes.remove(0);
            previousStartNodes.add(currentNode);
            for (Integer conNode : edgeVec.get(currentNode)) {
                if (conNode == soughtNode) {
                    return true;
                }
                if (!previousStartNodes.contains(conNode)) {
                    newStartNodes.addElement(conNode);
                }
            }
        }
        return false;
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        // Get input file
        BufferedReader inputFile;
        try {
            inputFile = new BufferedReader(new FileReader("input.txt"));
        } catch (IOException e) {
            System.err.println("Error opening file to read from.");
            return;
        }
        // Get output file
        BufferedWriter outputFile;
        try {
            outputFile = new BufferedWriter(new FileWriter("output.txt"));
        } catch (IOException e) {
            System.err.println("Error creating file to write to.");
            return;
        }

        int numNodes;
        int numEdges;
        // Get number of nodes and edges.
        String firstLine;
        try {
            firstLine = inputFile.readLine();
        } catch (IOException e) {
            System.err.println("Error reading first line from input file.");
            return;
        }
        int sep = firstLine.indexOf(" ");
        String nodes = firstLine.substring(0, sep);
        String edges = firstLine.substring(sep + 1, firstLine.length());
        numNodes = Integer.parseInt(nodes);
        numEdges = Integer.parseInt(edges);

        // Create map of weights
        Map<Set<Integer>, Integer> weights = new HashMap();
        createMap(weights, inputFile);
        Queue<Map.Entry<Set<Integer>, Integer>> sortedWeights = new PriorityQueue<>(
                compWeights);
        // Add all map entries to the priority queue
        sortedWeights.addAll(weights.entrySet());

        // Create new vector of edges
        Vector<Set<Integer>> newEdgeVector = new Vector<Set<Integer>>(numNodes);
        for (int i = 0; i < numNodes; i++) {
            Set<Integer> temp = new HashSet<Integer>();
            newEdgeVector.add(temp);
        }
        //Kruskals

        // newWeights is a vector of maps where the index of the vector is a
        // node, the key of the map is a connected node, and the value
        // is its weight
        Vector<Map<Integer, Integer>> newWeights = new Vector<Map<Integer, Integer>>(
                numNodes);
        for (int i = 0; i < numNodes; i++) {
            Map<Integer, Integer> temp = new HashMap<Integer, Integer>();
            newWeights.add(temp);
        }
        int newEdgeCount = 0;
        while (sortedWeights.size() > 0) {
            Map.Entry<Set<Integer>, Integer> edgeInfo = sortedWeights.remove();
            Set<Integer> currentEdge = edgeInfo.getKey();
            Integer[] y = currentEdge.toArray(new Integer[2]);
            int node1 = y[0] - 1;
            int node2 = y[1] - 1;
            if (!isConnected(node1, node2, newEdgeVector)) {
                //Add the edge to the edge vector
                Set<Integer> node1Set = newEdgeVector.get(node1);
                node1Set.add(node2);
                newEdgeVector.set(node1, node1Set);
                Set<Integer> node2Set = newEdgeVector.get(node2);
                node2Set.add(node1);
                newEdgeVector.set(node2, node2Set);
                //Add the edge to the newWeights map
                Map<Integer, Integer> node1Edges = newWeights.get(node1);
                if (!node1Edges.containsKey(node2)) {
                    node1Edges.put(node2, edgeInfo.getValue());
                }
                newWeights.set(node1, node1Edges);
                //Add again for other node
                Map<Integer, Integer> node2Edges = newWeights.get(node2);
                if (!node2Edges.containsKey(node1)) {
                    node2Edges.put(node1, edgeInfo.getValue());
                }
                newWeights.set(node2, node2Edges);
                newEdgeCount++;
            }
        }

        //Write to the output
        try {
            outputFile.write(numNodes + " " + newEdgeCount);
            outputFile.write("\n");
        } catch (IOException e) {
            System.err.println("Error writing to output file.");
            return;
        }
        while (newWeights.size() > 0) {
            Map<Integer, Integer> currentEdges = newWeights.remove(0);
            Queue<Map.Entry<Integer, Integer>> sortedNodes = new PriorityQueue<>(
                    compNodes);
            sortedNodes.addAll(currentEdges.entrySet());
            int spaceCounter = 0;
            while (!sortedNodes.isEmpty()) {
                Map.Entry<Integer, Integer> curEdge = sortedNodes.remove();
                try {
                    if (spaceCounter > 0) {
                        outputFile.write(" ");
                    }
                    outputFile.write(
                            (curEdge.getKey() + 1) + " " + curEdge.getValue());
                    spaceCounter++;
                } catch (IOException e) {
                    System.err.println("Error writing to output file.");
                    return;
                }
            }
            try {
                outputFile.write("\n");
            } catch (IOException e) {
                System.err.println("Error writing to output file.");
                return;
            }
        }

        /*
         * Close input and output streams
         */
        try {
            inputFile.close();
        } catch (IOException e) {
            System.err.println("Error closing the input file.");
            return;
        }
        try {
            outputFile.close();
        } catch (IOException e) {
            System.err.println("Error closing the output file.");
            return;
        }
    }

}

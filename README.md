# kruskal-implementation

Implementation of Kruskal's Algorithm from Spring 2017. I've not edited this code since submitting it for my coursework at the time. There's likely improvements that could be made in the implementation and readability of the code.

To run it, open the project in your IDE of choice and run the Kruskal.java file. I've only used the community version of IntelliJ for the code in this repository, so I recommend you to use the same.

This program will read the contents of input.txt, which is stored in the root folder of the project. The program expects the input data to be formatted as described below and to represent a connected weighted graph. The program will output a file that is similarly formatted but represents a Minimum Spanning tree of the graph provided in input.txt.

## Input Data Format
- The first line in the input file represents the number of nodes followed by the number of edges in the graph
- Subsequent lines represent details about the nodes and how they are connected to each other
	- The formatting in each of these lines should be: "nodeNumber1 lineWeight1 nodeNumber2 lineWeight2 ... nodeNumberN lineWeightN" such that each nodeNumber and lineWeight represents a connection to the node represented by that line of the input file
	- For example, the second line in the input file (the next line after the line containing the number of nodes/number of edges) represents all connections to Node 1. The third line in the input file represents all connections to Node 2 and so on.
		- If the second line contains "2 20 3 10", you can read this as "Node 1 is connected to Node 2 by a line of weight 20 and Node 1 is connected to Node 3 by a line of weight 10"
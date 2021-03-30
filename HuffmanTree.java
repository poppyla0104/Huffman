import java.io.*;
import java.util.*;
/*
Author: Tran La
Instructor: Craig Niiyama
Date: 11/22/2019
Course: CS211 
Assignment 8
Describe: This class create a Huffman Tree, includes 3 main methods:
	- Compress an input file of text to a string of its Huffman binary code
	- Decompress a text that was compressed with its encodings
	- Print a Huffman tree of the text with each node contains the input text characters and their frequency side way
 */

public class HuffmanTree {
	PriorityQueue<HuffmanNode> myQueue; // Queue that keeps HuffmanNodes sorted in order(frequency priority)
	HuffmanNode overalRoot; // the main root of HuffmanTree
	String sideWaysString = ""; // stores the side way of HuffmanTree
	Map<Character, String> binaryMap; // map contains characters with their Huffman binary code

	// constructor that takes a map(characters as keys, integers as values) to
	// create new map with original map's characters and their Huffman binary number
	// using characters frequencies
	public HuffmanTree(Map<Character, Integer> counts) {
		priorityQueue(counts);
		createTree(myQueue);
		binaryMap = new TreeMap<Character, String>();
		getBinary(overalRoot, binaryMap, "");
	}

	// as per the method presented in Chapter 17.
	public String printSideways() {
		return printSideWays(overalRoot, 0);
	}

	private String printSideWays(HuffmanNode root, int level) {
		if (root != null) {
			printSideWays(root.right, level + 1);
			for (int i = 0; i < level; i++) {
				sideWaysString += "      ";
			}
			// print "char" with the character ASCII code when it's a leaf and "count" when
			// it's a node
			if (root.isLeaf()) {
				sideWaysString += root.frequency + "=char(" + (int) root.character + ")\n\n";
			} else {
				sideWaysString += root.frequency + "=count\n\n";
			}
			printSideWays(root.left, level + 1);
		}
		return sideWaysString;
	}

	// method takes a file as parameter and return a string of its Huffman binary
	// number
	public StringBuilder compress(InputStream inputFile) throws IOException {// inputFile is a text file
		StringBuilder compressStr = new StringBuilder();
		int bytes = inputFile.read();
		while (bytes != -1) {
			// convert integer bytes number to its character
			char ch = (char) bytes;
			if (binaryMap.containsKey(ch)) {
				// add character binary number to string
				compressStr.append(binaryMap.get(ch));
			}
			bytes = inputFile.read();
		}
		// add end-of-byte binary number (when bytes = -1) to string
		compressStr.append(binaryMap.get((char) bytes));
		return compressStr;
	}

	// method takes a string of binary number(1 and 0) as parameter and return a
	// string of matching characters
	public StringBuilder decompress(StringBuilder inputString) {
		// new node that go over the tree and search
		HuffmanNode current = overalRoot;
		StringBuilder decompressStr = new StringBuilder();

		// read every single bit from input string,
		for (int i = 0; i < inputString.length(); i++) {
			// characters stored at leaves, add character to string then restart
			// the current node back to the beginning to search for new character
			if (current.isLeaf()) {
				decompressStr.append(current.character);
				current = overalRoot;
			}
			// current node move to the left node if it's 0 and right if it's 1
			if (inputString.charAt(i) == '0')
				current = current.left;
			else
				current = current.right;
		}
		return decompressStr;
	}

	// method create a sorted queue of nodes that takes a map as parameter,
	// frequency priority
	private void priorityQueue(Map<Character, Integer> counts) {
		myQueue = new PriorityQueue<>();
		for (Character ch : counts.keySet()) {
			// create new node with frequency and character
			HuffmanNode node = new HuffmanNode();
			node.frequency = counts.get(ch);
			node.character = ch;
			// add new node in sorted order
			myQueue.offer(node);
		}
	}

	// method create Huffman Tree that takes a sorted Queue of Huffman nodes as
	// parameter
	private void createTree(PriorityQueue<HuffmanNode> myQueue) {
		// keep joining nodes until we get only the overall node in queue
		while (myQueue.size() > 1) {
			// new node is parent of the first 2 nodes in queue
			// with frequency equals sum of its children's frequency
			HuffmanNode sumNode = new HuffmanNode();
			sumNode.left = myQueue.poll();
			sumNode.right = myQueue.poll();
			sumNode.frequency = sumNode.left.frequency + sumNode.right.frequency;
			// add new node to the queue to continue
			myQueue.offer(sumNode);
		}
		// copy the only root left in Queue to overalRoot
		overalRoot = myQueue.peek();
	}

	// method return a new map that takes a root, a map and String as parameters
	// new map contains parameter map characters(as keys) and their Huffman binary
	// number strings as value
	private Map<Character, String> getBinary(HuffmanNode root, Map<Character, String> m, String a) {
		if (root != null) {
			// all characters are leaves
			// add character as key and binary number string as value
			if (root.isLeaf()) {
				m.put(root.character, a);
			}
			// string a add 0 when we go left and 1 when we go right down the tree
			getBinary(root.left, m, a + "0");
			getBinary(root.right, m, a + "1");
		}
		return m;
	}

}

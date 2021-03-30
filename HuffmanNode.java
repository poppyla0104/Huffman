import java.io.*;
import java.util.*;

/*
Author: Tran La
Instructor: Craig Niiyama
Date: 11/22/2019
Course: CS211 
Assignment 8
Describe: This class create a Huffman node that stores a character, its frequency and 2 nodes left and right. 
	Class includes 2 main methods:
	- Takes a text file, count each character frequency and store them in a map with the characters as key and frequency as value
	- Check if the node is leaf or not
 */


public class HuffmanNode implements Comparable<HuffmanNode> {
	public int frequency;
	public char character;
	public HuffmanNode left;
	public HuffmanNode right;

	// method takes a text file as parameter
	// store all text's characters in a map as key set with their frequency as value
	public static Map<Character, Integer> getCounts(FileInputStream f) throws IOException {
		Map<Character, Integer> m = new HashMap<Character, Integer>();
		int bytes = f.read();
		while (bytes != -1) {
			// convert integer bytes number to its character
			char ch = (char) bytes;
			// if map already has character, increase the frequency
			if (m.containsKey(ch)) {
				int count = m.get(ch);
				m.put(ch, count + 1);
			} else // if character is new, add to map with frequency = 1
				m.put(ch, 1);
			bytes = f.read();
		}
		// add the end of file byte to map with frequency = 1
		m.put((char) bytes, 1);
		return m;
	}

	// method return whether the Huffman node is leaf
	public boolean isLeaf() {
		return (left == null && right == null);
	}

	//compare nodes by their frequency
	public int compareTo(HuffmanNode o) {
		return this.frequency - o.frequency;

	}
}

// This class represents a Huffman encoding algorithm for compressing data  
import java.util.*;
import java.io.*;

public class HuffmanCode {
    private HuffmanNode root;

    // This constructor initializes a new HuffmanCode object 
    // from an array of frequencies.
    // If there exists a character with a frequency <= 0, 
    // the character is not included in the HuffmanCode object.
    public HuffmanCode(int[] frequencies) {
        Queue<HuffmanNode> pq = new PriorityQueue<>();

        // Add new nodes to priority queue with their frequencies 
        for (int i = 0; i < frequencies.length; i++) {
            if (frequencies[i] > 0) {
                pq.add(new HuffmanNode(i, frequencies[i]));
            }
        }

        while (pq.size() > 1) {
            // Remove two smallest from priority queue
            HuffmanNode left = pq.remove();
            HuffmanNode right = pq.remove();
            // Create new node, frequency = sum of left and right
            HuffmanNode parentNode = 
            new HuffmanNode(left, right, left.frequency + right.frequency);
            pq.add(parentNode);
        }
        
        if (!pq.isEmpty()) {
            root = pq.remove();
        }
    }

    // This constructor initializes a new HuffmanCode object 
    // by reading in a previously constructed code from a .code file. 
    public HuffmanCode(Scanner input) {
        while (input.hasNextLine()) {
            int asciiValue = Integer.parseInt(input.nextLine());
            String code = input.nextLine();
            this.root = HuffmanCodeHelper(asciiValue, code, root, 0);
        }
    }

    // This is a private helper method for the HuffmanCode constructor.
    private HuffmanNode HuffmanCodeHelper(int asciiValue, String code, HuffmanNode root, int i) {
        if (i == code.length()) {
            return new HuffmanNode(asciiValue, 0);
        }

        if (i < code.length()) {
            if (root == null) {
                root = new HuffmanNode();
            } 
            
            if (code.charAt(i) == '0') {
                root.left = HuffmanCodeHelper(asciiValue, code, root.left, i + 1);
            } else if (code.charAt(i) == '1') {
                root.right = HuffmanCodeHelper(asciiValue, code, root.right, i + 1);
            }
        } 
        return root; 
    }   

    // This method stores the current Huffman Code to the given output stream.
    public void save(PrintStream output) {
        save(output, root, "");
    }

    // This is a private helper method for the save method.
    private void save(PrintStream output, HuffmanNode root, String code) {
        if (root != null) {
            if (root.data != -1) {
                output.println(root.data);
                output.println(code);
            } else {
                save(output, root.left, code + "0");
                save(output, root.right, code + "1");
            }
        }
    }

    // This method reads individual bits from the input stream 
    // and writes the corresponding characters to the given output stream.
    // It stops reading when the BitInputStream is empty.
    public void translate(BitInputStream input, PrintStream output) {
        HuffmanNode curr = root;
        while (input.hasNextBit()) {
            int bit = input.nextBit();

            if (bit == 0) {
                curr = curr.left;
            } else if (bit == 1) {
                curr = curr.right;
            }

            if (curr != null && curr.data != -1) {
                output.write((char)curr.data);
                curr = root;
            }      
        }
    }

    // Class representing each node of the HuffmanCode object. 
    public static class HuffmanNode implements Comparable<HuffmanNode> {
        public int data = -1;
        public int frequency;
        public HuffmanNode left;
        public HuffmanNode right;

        // Creates HuffmanNode with no parameters.
        public HuffmanNode() {
            this(null, null, 0);
        }

        // Creates HuffmanNode given int data and int frequency.
        public HuffmanNode(int data, int frequency) {
            this(null, null, frequency);
            this.data = data;
        }

        // Creates HuffmanNode given left node, right node, and frequency.
        public HuffmanNode(HuffmanNode left, HuffmanNode right, int frequency) {
            this.frequency = frequency;
            this.left = left;
            this.right = right;      
        }

        // Method to implement comparable. 
        // Lower frequencies are considered “less” than higher frequencies. 
        @Override
        public int compareTo(HuffmanNode other) {
            return this.frequency - other.frequency;
        }
    }
}

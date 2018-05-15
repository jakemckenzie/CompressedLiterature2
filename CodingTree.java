import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.PriorityQueue;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.*;
import java.nio.file.Paths;
import java.nio.file.Files;
//cd C:\Users\Epimetheus\Documents\GitHub\CompressedLiterature2
//javac *.java -Xlint:unchecked

/**
 * @author Jake McKenzie
 */
public class CodingTree {
    /**
     * A constructor that takes the text of a message to be compressed. 
     * The constructor is responsible for calling all methods 
     * that carry out the Huffman coding algorithm.
     * @param message the message encoded by the huffman tree
     */

    public CodingTree(String message) {
        codes = new MyHashTable<String,String>(1 << 15);
        countStrings(message);
        buildHuffmanTree(queue);
        buildBinary(root,"");
        convertToBinary();
        //decoded = decode(bits,codes);
    }
    /**
     * @param root the root to the Huffman Tree.
     */
    public HuffmanNode root = new HuffmanNode();
    /**
     * @param codes A map of characters in the message with their binary codes.
     */

    public MyHashTable<String,String> codes;
    /**
     * @param strings an array containing all the words and seperators, in their proper order from the file
     */
    public ArrayList<String> strings = new ArrayList<String>(22691);
    /**
     * @param size the size of the huffman tree
     */
    public int size = 0;
    
    /**
     * @param bits the output bits for file
     */

    public String bits;
    
    /**
     * @param decoded the decoded string
     */
    
     public String decoded;

    /**
     * @param queue the priority queue for the huffman tree
     */
    
     public PriorityQueue<HuffmanNode> queue = new PriorityQueue<HuffmanNode>();
    
     /**
     * ******************************EXTRA CREDIT**************************** 
     * This method will take the output of Huffman’s encoding and produce the original text.
     * @param bits A message encoded using Huffman codes.
     * @param codes A map of characters in the message with their binary codes.
     */

    // public String decode(String bits, MyHashTable<String,String> codes) {
    //     StringBuilder out = new StringBuilder();
    //     StringBuilder temp = new StringBuilder();
    //     //String temp = "";
    //     //HuffmanNode decNode = root;
    //     Map<String,Character> decoder = new HashMap<String,Character>();
    //     Collection<Character> keys = codes.keySet();
    //     for (char k: keys) decoder.put(codes.get(k),k);
    //     for (int i = 0; i < bits.length(); i++) {
    //         //temp += (bits.charAt(i) == '1') ? '1' : '0';            
    //         if (bits.charAt(i) == '1') {
    //             temp.append('1');
    //         } else {
    //             temp.append('0');
    //         }
    //         if (decoder.get(temp.toString()) != null) {
    //             out.append(decoder.get(temp.toString()));
    //             //temp = new StringBuilder();
    //             temp.setLength(0);//faster than making a new object apparently?
    //         }
    //     }
    //     return out.toString();
    // }
    /**
     * Builds a Huffman tree given some weights and an alphabet.
     * @param queue A priority queue of huffman nodes
     */
    
     public void buildHuffmanTree(PriorityQueue<HuffmanNode> queue) {
        do{queue.offer(new HuffmanNode(queue.poll(),queue.poll()));} while (queue.size() > 1);
        root = queue.poll();
    }
    /**
     * @param node root node -> node -> node -> ... -> leaf ... until all leaves have been meet. 
     */
    public void buildBinary(HuffmanNode node,String temp) {
        if (!node.isLeaf()) {
            buildBinary(node.R,temp + '1');
            buildBinary(node.L,temp + '0');
        } else {
            codes.put((String)node.key,temp);
        } 
    }

    public void convertToBinary() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strings.size(); i++) sb.append(codes.get(strings.get(i)));
		bits = sb.toString();
    }


    /**
     * This method will count the characters in my string. 
     * I was talking with ammon dodson a lot about bitwise
     * operations and stumbled upon this method.
     * 
     * Since all bitwise operations implicityly cast all types
     * to integers when you apply operations to them this allows
     * me to count count all 2 byte length chars in the file without
     * getting an index out of bounds error, since bytes are signed
     * in java.
     * 
     * This allows me to store the value and frequency all in one array
     * as the index of the array corresponds to the character value.
     * @param message the message encoded by the huffman tree
     */
    public int[] countChar(String message) {
        //byte[] bytes = message.getBytes(Charset.forName("US-ASCII"));
        byte[] bytes = message.getBytes();
        //byte[] bytes = message.getBytes(Charset.forName("UTF-8"));
        final int[] frequency = new int[256];
        for (byte b : bytes) frequency[b & 0xFF]++;
        return frequency;
    }
    
    public void countStrings(String message) {
        String[] words = message.split("([^\\w\\Q-\\E\\Q'\\E])+");
        String[] seperators = message.split("([\\w\\Q-\\E\\Q'\\E])+");
        //ArrayList<String> strings = new ArrayList<String>(22668);
        //I start at 3 to skip the file encoding. This shouldn't work on
        //non encoded txt files. I have to do this because of the special
        //encoding of the file. I looked and most txt files have such 
        //encodings so this is actually quite general.
        for (int i = 0; i < words.length;i++) {
            if (      (message.charAt(3) >= 'A' && message.charAt(3) <= 'Z')
                    ||(message.charAt(3) >= 'a' && message.charAt(3) <= 'z')
                    ||(message.charAt(3) >= '0' && message.charAt(3) <= '9')
                    ||(message.charAt(3) == '-')
                    ||(message.charAt(3) == '\'')) {
                strings.add(words[i]);
                char[] seperatedChars = seperators[i].toCharArray();
                for (int j = 0; j < seperatedChars.length; j++) strings.add(Character.toString(seperatedChars[j]));
            } else {
                char[] seperatedChars = seperators[i].toCharArray();
                for (int j = 0; j < seperatedChars.length; j++) strings.add(Character.toString(seperatedChars[j]));
                strings.add(words[i]);
                if (i == words.length - 1) {
                    char[] seperatedChars2 = seperators[i + 1].toCharArray();
                    for (int j = 0; j < seperatedChars2.length; j++) strings.add(Character.toString(seperatedChars2[j]));
                } 
            }
        }
        TreeMap<String,Integer> tm = new TreeMap<String,Integer>();
        for (String i : strings) {
            Integer j = tm.get(i);
            tm.put(i,j == null ? 1 : j + 1);
        }
        for (Map.Entry m : tm.entrySet()) queue.offer(new HuffmanNode((String)m.getKey(),(int)m.getValue()));
    }
}
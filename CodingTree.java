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
import java.util.Map.Entry;
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

    public CodingTree(String bits, MyHashTable<String,String> codes) {
        decoded = decode(bits,codes);
    }

    public CodingTree(String message) {
        codes = new MyHashTable<String,String>(32768);
        countStrings(message);
        buildHuffmanTree(queue);
        buildBinary(root,"");
        convertToBinary();

        /**
         * Please comment out to test decode
         */

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
    
     public MyPriorityQueue<HuffmanNode> queue = new MyPriorityQueue<HuffmanNode>();
    
     /**
     * ******************************EXTRA CREDIT**************************** 
     * This method will take the output of Huffman’s encoding and produce the original text.
     * @param bits A message encoded using Huffman codes.
     * @param codes A map of characters in the message with their binary codes.
     */

    public String decode(String bits, MyHashTable<String,String> codes) {
        StringBuilder out = new StringBuilder();
        StringBuilder temp = new StringBuilder();
        //String temp = "";
        Map<String,String> decoder = new HashMap<String,String>();
        Collection<String> keys = codes.keySet();
        for (String k: keys) decoder.put(codes.get(k),k);
        for (int i = 0; i < bits.length(); i++) {
            // long sum = 0;
            // long z = System.nanoTime();
            //temp += (bits.charAt(i) == '1') ? '1' : '0';
            //System.out.println(i);
            if (bits.charAt(i) == '1') {
                temp.append('1');
            } else {
                temp.append('0');
            }
            if (decoder.get(temp.toString()) != null) {
                out.append(decoder.get(temp.toString()));
                temp.setLength(0);//faster than making a new object apparently?
                //temp = "";
            }
            // long y = System.nanoTime();
            // sum+= (y-z);
            // if (i % 100000 == 0 )  System.out.println(sum);
        }
        return out.toString();
    }
    /**
     * Builds a Huffman tree given some weights and an alphabet.
     * @param queue A priority queue of huffman nodes
     */
    
     public void buildHuffmanTree(MyPriorityQueue<HuffmanNode> queue) {
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
            codes.put(node.key,temp);
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

    /**
     * I used the book Java 9 Regular Expressions by Anubhava Srivastava
     * to parse the novel war and peace. I have two regular expressions
     * The first one works by splitting by every character NOT in the set:
     * {a,...,z,A,...,Z,-,'} while the latter splits by that set.
     */
    
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
        MyHashTable<String,Integer> ht = new MyHashTable<String,Integer>(1<<15);
        for (String i : strings) {
            Integer j;
            if (ht.containsKey((String)i) == true) {
                j = ht.get((String)i);
                ht.put(i, new Integer(j+ 1));
            } else {
                ht.put(i, 1);
            }
            
        }
        Collection<String> keys = ht.keySet();
        for (String k: keys) queue.offer(new HuffmanNode(k,ht.get(k)));
    }
}
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.PriorityQueue;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
//cd C:\Users\Epimetheus\Documents\GitHub\CompressedLiterature2
//javac *.java -Xlint:unchecked

/**
 * @author Jake McKenzie
 */
public class CodingTree {
    /**
     * A constructor that takes the text of a message to be compressed. 
     * The constructor is responsible for calling all private methods 
     * that carry out the Huffman coding algorithm.
     * @param message the message encoded by the huffman tree
     * @TODO: Finish build huffmantree
     */

    public CodingTree(String message) {
        countString(message);
        //buildHuffmanTree(table);
        //buildBinary(root,new StringBuilder());
        buildBinary(root,"");
        convertToBinary(message);
        decoded = decode(bits,codes);
        
    }
    /**
     * @param root the root to the Huffman Tree.
     */
    public HuffmanNode root = new HuffmanNode();
    /**
     * @param codes A map of characters in the message with their binary codes.
     */

    public Map<Character,String> codes;

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
     * @param table the hash table for the huffman tree
     */
    
     public MyHashTable<String,Integer> table = new MyHashTable<String,Integer>(32768);
    
     /**
     * ******************************EXTRA CREDIT**************************** 
     * This method will take the output of Huffman’s encoding and produce the original text.
     * @param bits A message encoded using Huffman codes.
     * @param codes A map of characters in the message with their binary codes.
     */

    private String decode(String bits, Map<Character,String> codes) {
        StringBuilder out = new StringBuilder();
        StringBuilder temp = new StringBuilder();
        //String temp = "";
        //HuffmanNode decNode = root;
        Map<String,Character> decoder = new HashMap<String,Character>();
        Collection<Character> keys = codes.keySet();
        for (char k: keys) decoder.put(codes.get(k),k);
        for (int i = 0; i < bits.length(); i++) {
            //temp += (bits.charAt(i) == '1') ? '1' : '0';            
            if (bits.charAt(i) == '1') {
                temp.append('1');
            } else {
                temp.append('0');
            }
            if (decoder.get(temp.toString()) != null) {
                out.append(decoder.get(temp.toString()));
                //temp = new StringBuilder();
                temp.setLength(0);//faster than making a new object apparently?
            }
        }
        return out.toString();
    }
    /**
     * Builds a Huffman tree given some weights and an alphabet.
     * @param queue A priority queue of huffman nodes
     */
    
    //  public void buildHuffmanTree(MyPriorityQueue<HuffmanNode> queue) {
    //     do{queue.offer(new HuffmanNode(queue.poll(),queue.poll()));} while (queue.size() > 1);
    //     root = queue.poll();
    // }
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

    private void convertToBinary(String book) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < book.length(); i++) sb.append(codes.get(book.charAt(i)));
		bits = sb.toString();
    }
    /**
     * This method will count the characters in my string
     * @param message the message encoded by the huffman tree
     * 
     * This was for an earlier implementation of the program.
     * 
     * I left it in because I was learning functional programming
     * way of doing all my methods. Interesting that c needs to
     * be casted as it is being treated as an int (?) for optimization
     * purposes.
     */
    public Map<Character,Integer> tallyChar(String message) {
        HashMap<Character,Integer> count = new HashMap<>();
        message.chars().forEach(c -> {
            if (count.containsKey((char)c)) {
                count.put((char)c,count.get((char)c)+1);
            }   
            else {
                count.put((char)c,1);
            }   
        });
        return count;
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
         * This returns the maximum codelength of the current huffman tree.
         * 
         * Used for testing.
         * 
         * 1. No huffman code can be longer than alphabetsize-1.
         * 2. The maximum length of the code also depends on the 
         * number of samples you use to derive your statistics from; 
         * the sequence is as follows (the samples include the fake 
         * samples to give each symbol a nonzero probability!):
         * TODO: look into ceil(log(alphabetsize))
         * http://www.compressconsult.com/huffman/#maxlength
         * https://stackoverflow.com/questions/43809659/get-average-value-of-two-hashmap-using-java-8
         * https://stackoverflow.com/questions/42556953/java8-calculate-average-of-list-of-objects-in-the-map
         */
        public int getMax() {
            int max = 0;
            Object[] keys = codes.values().toArray();
            //keys.ints.forEachOrdered(c-> {if (max < keys[i].toString().length()) max = keys[i].toString().length();});
            //return Arrays.stream(codes.values().toArray());
            for (int i = 0; i < keys.length; i++) if (max < keys[i].toString().length()) max = keys[i].toString().length();
            return max; 
        }
        /**
         * This returns the average codelength of the current huffman tree.
         * 
         * Used for testing.
         * 
         * "To precisely compare the new code with the standard encoding,
         * we can compute the average number of bits/symbol of the codes.
         * The standard coding always uses 2 bits, so obviously the average 
         * number of bits per symbol is also 2"
         * https://www.princeton.edu/~cuff/ele201/kulkarni_text/information.pdf
         * https://softwareengineering.stackexchange.com/questions/237543/how-do-i-find-average-bits-per-symbol-using-huffman-code
         */
        public double getAvg() {
            double sum = 0.0d;
            Object[] keys = codes.values().toArray();
            for (int i = 0; i < keys.length; i++) sum+=(double)keys[i].toString().length();
            return (sum / keys.length);
        }

        public void countStrings(String message) {
            String message = new String(Files.readAllBytes(Paths.get(WarAndPeace)));
            String[] words = message.split("([^\\w\\Q-\\E\\Q'\\E])+");
            String[] seperators = message.split("([\\w\\Q-\\E\\Q'\\E])+");

            String[] seperatorsSeperated = (seperators.toString()).split("");
            ArrayList<String> strings = new ArrayList<String>(22668);
            //I start at 3 to skip the file encoding. This shouldn't work on
            //non encoded txt files. I have to do this because of the special
            //encoding of the file. I looked and most txt files have such 
            //encodings so this is actually quite general.
            for (int i = 0; i < words.length;i++) {
                if (      (message.charAt(3) >= 'A' && s.charAt(3) <= 'Z')
                        ||(message.charAt(3) >= 'a' && s.charAt(3) <= 'z')
                        ||(message.charAt(3) >= '0' && s.charAt(3) <= '9')
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
        HashSet<String> set = new HashSet<>();
        for (int j = 0; j < strings.size(); j++) set.add(strings.get(j));
        Iterator<String> stringItr =  set.iterator();
        while(stringItr.hasNext()){
            String a = stringItr.next();            
            table.put(a,Collections.frequency(strings,a));
        }
    }
}
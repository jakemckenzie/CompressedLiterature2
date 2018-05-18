import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.*;
import java.lang.*;

public class Main {

    /**
     * @param WarAndPeace- This was the required text for compression. It is
     * a long text and showcases the compression technique well. I consider compression
     * of this text to be the baseline to meet. If I can hit this then I will test on
     * progressively harder texts to compress and decompress.
     */

    private static final String WarAndPeace = "WarAndPeace.txt";

    /**
     * @param codes an output file for the codes
     */

    private static final String codes = "./codes.txt";

    /**
     * @param compressed a compressed output file for the codes
     */

    private static final String compressed = "./compressed.txt";

    /**
     * @param decompressed a compressed output file for the codes
     */
    private static final String decompressed = "./decompressed.txt";
    
    /**
     * The driver for the MyHashMap method reads in a file and writes the coded file
     * and the tree codes
     * 
     * @param args The command line arguments
     * 
     *             Hash function | runtime @ 100      |
     *             joaat_hash    | 727.7 miliseconds  | 
     *             bernsteinHash | 770.71 miliseconds | 
     *             djb2          | 723.08 miliseconds | 
     *             FNVHash1      | 666.53 miliseconds |
     */
    public static void main(String[] args) throws IOException {
        long a = System.currentTimeMillis();
        String message = new String(Files.readAllBytes(Paths.get(WarAndPeace)));
        CodingTree c = new CodingTree(message);
        c.codes.stats();
        Files.write(Paths.get(codes), c.codes.toString().getBytes());
        BitSet bs = new BitSet(c.bits.length());
        for (int o = 0; o < c.bits.length(); o++) if (c.bits.charAt(o) == '1') bs.flip(o);
        Files.write(Paths.get(compressed), bs.toByteArray());
        long b = System.currentTimeMillis();
        System.out.println("Runtime: " + (b - a) + " milliseconds");
        int sum = 0;
        for (String i : c.strings) sum+= i.getBytes().length;
        System.out.println("Average byte length: " + ((double)sum)/c.strings.size());
        //Files.write(Paths.get(decompressed),c.decoded.getBytes());

        double compressed = Files.size(Paths.get("compressed.txt"));
        double targetCompressed = Files.size(Paths.get("targetCompressed.txt"));
        double difference = (targetCompressed - compressed);

        System.out.println("Compressed file size: " +  compressed / 1024 + " kilobytes");
        System.out.println("Target compressed file size: " +  targetCompressed / 1024  + " kilobytes");
        System.out.println("Difference in compressed file sizes of my file vs the target: " + difference  + " bytes");


        testMyHashTable();
        testCodingTree();
    }

    public static void testMyHashTable() {
        MyHashTable<String, String> temp = new MyHashTable<String, String>(1 << 15);
        temp.put("yes", "please");
        temp.put("No", "Bugs");
        System.out.println(temp.get("yes") + temp.get("No"));
        temp.put("yes", "What do you mean");
        System.out.println(temp);
    }

    public static void testCodingTree() throws IOException{
        String message1 = new String(Files.readAllBytes(Paths.get("Conan_Beyond_the_Black_River.txt")));
        CodingTree testCodingTree1 = new CodingTree(message1);
        System.out.println(testCodingTree1.codes.toString());

    }

    /**
     * This was used for testing purposes. When I read about the huffman compression
     * entropy kept coming up so I decided to see what the entropy of the files were that
     * I collected for testing. I encluded the entropy of each above in the comments of their file names. 
     * 
     * https://courses.cs.washington.edu/courses/csep521/99sp/lectures/lecture11/sld020.htm
     */
    public static double getEntropy(String s){
        double entropy = 0.0d;
        double probability;
        final int[] frequency = new int[256];
        for (byte b : s.getBytes()) frequency[b & 0xFF]++;
        //Claude Shannon - The theory of information
        for (int f:frequency) {
           probability = (double)f / s.length();
           if (f != 0) entropy -= (probability) * log2(probability);
        }
        return entropy;
    }
    
    public static double log2(double n) {
        return Math.log(n) / Math.log(2);
    }

    /**
     * pg 9 of Maxime Crochemore's Algorithms on Strings. 
     * 
     * fibonacciStrings(1)    1   b
     * fibonacciStrings(2)    1   a
     * fibonacciStrings(3)    2   ab
     * fibonacciStrings(4)    3   aba
     * fibonacciStrings(5)    5   abaab
     * fibonacciStrings(6)    8   abaababa
     * fibonacciStrings(7)    13  abaababaabaab
     * fibonacciStrings(8)    21  abaababaabaababaababa
     * fibonacciStrings(9)    34  abaababaabaababaababaabaababaabaab
     * 
     * The lengths of the strings are exactly the same length as the sequence of fibonacci numbers ie Fn = |fibonacciStrings(n)|
     * yet it has many repeats. This makes it good, in my opinion, for testing purposes
     */
    public static String fibonacciStrings(int N){
        return (N == 0) ? "b" : (N == 1) ? "a" : fibonacciStrings(N - 1) + fibonacciStrings(N - 2);
    }

    public static void testMain() throws IOException {

        
        // TreeMap<String,Integer> tm = new TreeMap<String,Integer>();

        // for (String i:strings) {
        //     Integer j = tm.get(i);
        //     tm.put(i,j == null ? 1 : j + 1);
        // }
    
        // Iterator<String> itrString =  setString.iterator();
        // while(itrString.hasNext()){
        //     String a = itrString.next();            
        //     System.out.println(a+ " :  "+Collections.frequency(strings, a));
        // }



        // for (int i = 0; i < strings.size(); i++) {
        //     System.out.println(strings.get(i));
        // }


        // Set<String> wordSet = new HashSet<>();
        //if (s.charAt(0) == "([\\w\\Q-\\E\\Q'\\E])+")
        // Collections.addAll(wordSet, words);
        // Collections.addAll(wordSet, seperatorsSeperated);
        // System.out.println(wordSet.size());
        // MyHashTable<String, String> temp = new MyHashTable<String, String>(1 << 15);
        // // for (int i = 0; i < wordSet.size(); i++) temp.put(wordSet.);
        // Iterator<String> it = wordSet.iterator();
        // while (it.hasNext()) {
        //     String bob = it.next();
        //     temp.put(bob, bob);
        // }
    }
}
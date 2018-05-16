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
     *             Hash function | runtime @ 100      | runtime @ 1000 
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
        System.out.println("Runtime : " + (b - a) + " miliseconds");
        Files.write(Paths.get(decompressed),c.decoded.getBytes());

        double compressed = Files.size(Paths.get("compressed.txt"));
        double targetCompressed = Files.size(Paths.get("targetCompressed.txt"));
        double difference = (targetCompressed - compressed);

        System.out.println("Compressed file size: " +  compressed + " bytes");
        System.out.println("Target compressed file size: " +  targetCompressed  + " bytes");
        System.out.println("Difference in compressed file sizes of my file vs the target: " + difference  + " bytes");
    }

    public static void testMyHashTable() {
        MyHashTable<String, String> temp = new MyHashTable<String, String>(1 << 15);
        temp.put("yes", "please");
        temp.put("No", "Bugs");
        System.out.println(temp.get("yes") + temp.get("No"));
        temp.put("yes", "What do you mean");
        System.out.println(temp);
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
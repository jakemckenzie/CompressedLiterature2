import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.*;
import java.lang.*;

public class Main {
    private static final String WarAndPeace = "WarAndPeace.txt";
    private static final String codes = "./codes.txt";

    private static final String compressed = "./compressed.txt";

    
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
        // int cap = 100;
        // long[] runtime = new long[cap];
        // for (int i = 0; i < cap; i++) {
        //     Long startTime = System.currentTimeMillis();
        //     testMain();
        //     Long endTime = System.currentTimeMillis();
        //     runtime[i] = (endTime - startTime);
        // }
        // double total = 0;
        // for (long d : runtime) total += (double) d;
        // System.out.println(total / cap + " miliseconds");
        // testMain();
        long a = System.currentTimeMillis();
        String message = new String(Files.readAllBytes(Paths.get(WarAndPeace)));
        //System.out.println("message Done");
        CodingTree c = new CodingTree(message);
        Files.write(Paths.get(codes), c.codes.toString().getBytes());
        BitSet bs = new BitSet(c.bits.length());
        for (int o = 0; o < c.bits.length(); o++) if (c.bits.charAt(o) == '1') bs.flip(o);
        Files.write(Paths.get(compressed), bs.toByteArray());
        long b = System.currentTimeMillis();
        System.out.println(b - a);
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
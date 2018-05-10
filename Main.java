import java.util.*;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.*;
import java.util.Arrays;
import java.util.stream.*;

import java.util.*;
import java.lang.*;
import java.io.*;

public class Main {

    private static final String WarAndPeace = "WarAndPeace.txt";

    /**
     * The driver for the MyHashMap method reads in a file and writes the coded file
     * and the tree codes
     * 
     * @param args The command line arguments
     * 
     * Hash function | runtime @ 100      | runtime @ 1000 
     * joaat_hash    | 780.12 miliseconds | 758.844 miliseconds
     * bernsteinHash | 752.74 miliseconds | 755.924 miliseconds
     * djb2          | 669.31 miliseconds | 694.176 miliseconds
     * FNVHash1      | 673.94 miliseconds | 662.069 miliseconds
     */
    public static void main(String[] args) throws IOException {
        long[] runtime = new long[1000];
        for (int i = 0; i < 1000; i++) {
            Long startTime = System.currentTimeMillis();
            testMain();
            Long endTime = System.currentTimeMillis();
            runtime[i] = (endTime - startTime);
        }
        double total = 0;
        for (long d : runtime) total+= (double)d;
        System.out.println(total / 1000 + " miliseconds");
        }

        // it = wordSet.iterator();
        // while (it.hasNext()) System.out.println(temp.get(it.next()));
        // testMyHashTable();
    

    public static void testMyHashTable() {
        MyHashTable<String, String> temp = new MyHashTable<String, String>(1 << 15);
        temp.put("yes", "please");
        temp.put("No", "Bugs");
        System.out.println(temp.get("yes") + temp.get("No"));
        temp.put("yes", "What do you mean");
        System.out.println(temp);
    }

    public static void testMain() throws IOException {
        
        String s = new String(Files.readAllBytes(Paths.get(WarAndPeace)));
        String[] words = s.split("([^\\w\\Q-\\E\\Q'\\E])+");
        String[] seperators = s.split("([\\w\\Q-\\E\\Q'\\E])+");

        String[] seperators2 = (seperators.toString()).split("");

        Set<String> wordSet = new HashSet<>();

        Collections.addAll(wordSet, words);
        Collections.addAll(wordSet, seperators2);
            //System.out.println(wordSet.size());
        MyHashTable<String, String> temp = new MyHashTable<String, String>(1 << 15);
            // for (int i = 0; i < wordSet.size(); i++) temp.put(wordSet.);
        Iterator<String> it = wordSet.iterator();
        while (it.hasNext()) {
            String bob = it.next();
            temp.put(bob, bob);
        }
    }
}
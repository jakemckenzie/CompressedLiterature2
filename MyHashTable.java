/*
 * MyHashTable.java
 *
 * TCSS 342
 * Assignment 6
 */

import java.util.*;

/**
 * This is a Hash Table class. It stores data as an object
 * with both its key and the data in the object.  The table
 * is typically navigated with hash codes to pooint us to where
 * we need to be in the hash table.
 * @author Bruce Baker and Jake Macaroni
 * @version May 16, 2018
 */

// cd C:\Users\Epimetheus\Documents\GitHub\CompressedLiterature2
// javac *.java -Xlint
public class MyHashTable<K, V> {
      /**
       * This is the amount of occupied buckets in the hash table 
       */
      int myBuckets;
      /**
       * This is the amount of total buckets in the hash table 
       */
      int myCapacity;
      /**
       * This is the hash table 
       */
      ArrayList<ValueData> myTable;
      /**
       * The constructor for a hash table.  initializes capacity amount of spaces
       * to null in an arraylist
       * @param capacity The size of the table
       */
      public MyHashTable(int capacity) {
            myTable = new ArrayList<ValueData>(Collections.nCopies(capacity, null));
            myBuckets = 0;
            myCapacity = capacity;
      }

      // public void put(K searchKey, V newValue) {
      // int t = bernsteinHash(searchKey);
      // int pos = (t < 0) ? (t % myCapacity) + myCapacity : t % myCapacity;
      // int cycle = 0;
      // while (cycle < myCapacity && myValues.get(pos) != null) {
      // pos = (pos + 1) % myCapacity;
      // cycle++;
      // }
      // if (myBuckets == 0 || !containsKey(searchKey)) {
      // myKeys.set(myBuckets, new keyData(searchKey, pos));
      // myBuckets++;
      // }
      // myValues.set(pos, newValue);
      // }

      // public void put(K searchKey, V newValue) {
      // //int t = bernsteinHash(searchKey);
      // //int t = joaat_hash(searchKey);
      // //int t = djb2(searchKey);
      // int t = FNVHash1(searchKey);
      // //int t = randomHash(searchKey);
      // int pos = (t < 0) ? (t % myCapacity) + myCapacity : t % myCapacity;
      // int cycle = 0;

      // if(!containsKey(searchKey)) {
      // while( cycle < myCapacity && myValues.get(pos) != null) {
      // pos = (pos + 1)%myCapacity;
      // cycle++;
      // }
      // myKeys.set(myBuckets, new keyData(searchKey, pos));
      // myBuckets++;
      // myValues.set(pos, newValue);
      // }else {
      // //this part needs work
      // myValues.set(getKeyData(searchKey).myValue, newValue);
      // }

      // }

      /**
       * Places a value into the table based on the hashcode of the key
       * @param searchKey the key of the object
       * @param newValue the value to be associated
       */
      void put(K searchKey, V newValue) {
            //int t = bernsteinHash(searchKey);
            //int t = joaat_hash(searchKey);
            //int t = djb2(searchKey);
            int t = FNVHash1(searchKey);
            //int t = randomHash(searchKey);
            int pos = (t < 0) ? (t % myCapacity) + myCapacity : t % myCapacity;
            int cycle = 0;
               while( cycle < myCapacity && myTable.get(pos) != null) {
                  if(myTable.get(pos).myKey.equals(searchKey)){
                     break;
                  }
                  pos = (pos + 1)%myCapacity;
                  cycle++;
               }
               myBuckets++;
               myTable.set(pos, new ValueData(newValue, searchKey));  
         }
      
      public int randomHash(K searchKey) {
            Random r = new Random(System.currentTimeMillis());
            int t = searchKey.hashCode();
            switch (r.nextInt(4)) {
            case 0:
                  t = bernsteinHash(searchKey);
                  break;
            case 1:
                  t = joaat_hash(searchKey);
                  break;
            case 2:
                  t = djb2(searchKey);
                  break;
            case 3:
                  t = FNVHash1(searchKey);
                  break;
            }
            return t;
      }
      /**
       * This returns an value from our hastable that is associated with the key
       * @param searchKey The key we are using to search the table
       * @return The value associated with the key
       */
      public V get(K searchKey) {
            //int t = bernsteinHash(searchKey);
            //int t = joaat_hash(searchKey);
            //int t = djb2(searchKey);
            int t = FNVHash1(searchKey);
            //int t = randomHash(searchKey);
            int pos = (t < 0) ? (t % myCapacity) + myCapacity : t % myCapacity;
            int cycle = 0;
            V temp;
            while(cycle < myCapacity) {
               if(myTable.get(pos).myKey.equals(searchKey)) {
                  break;
               } else {
                  pos = (pos + 1)% myCapacity;
               }
               cycle++;
            }
            temp = myTable.get(pos).myValue;
            return temp;
         }

      /**
       * Looks for a key in the table, if it is present on the table, we return true
       * @param
       * @return
       */
      public boolean containsKey(K searchKey) {
            //int t = bernsteinHash(searchKey);
            //int t = joaat_hash(searchKey);
            //int t = djb2(searchKey);
            int t = FNVHash1(searchKey);
            //int t = randomHash(searchKey);
            int pos = (t < 0) ? (t % myCapacity) + myCapacity : t % myCapacity;
            int cycle = 0;
            boolean found = false;
            while(cycle < myCapacity) {
                  if(myTable.get(pos) == null) {
                  break;
            }else if(myTable.get(pos).myKey.equals(searchKey)) {
                  found = true;
                  break;
            } else {
                  pos = (pos + 1) % myCapacity;
            }
            cycle++;
            }
            return found;
      }
   /**
    * Returns a hashset of all key values
    * @return all keys in the table
    */
   public HashSet<K> keySet() {
      HashSet<K> temp = new HashSet<K>();
      for(int i = 0; i < myCapacity; i++) {
         if(myTable.get(i) != null) {
            temp.add(myTable.get(i).myKey);
         }
      }
      return temp;
   }
   
   /**
    * Prints the statistics on our hashing and probing methods
    * to the console.
    */
   void stats() {
      System.out.println( "Number of Entries: " + (myBuckets));
      System.out.println( "Number of Buckets : " + myCapacity);
      ArrayList<Integer> histy = new ArrayList<Integer>(myCapacity);
      int bigProbe = 1;
      double averageProbe = 0;
      for(int i = 0; i < myCapacity; i++) {
         if(myTable.get(i) != null) {
            int temp = i - (FNVHash1(myTable.get(i).myKey));
            //System.out.println(temp);
            if(temp < 0) {
               temp = temp + myCapacity;
            }
            if(temp > bigProbe) {
               bigProbe = temp;
            }
            averageProbe += ((double) temp) / myBuckets;
            histy.add(temp);
            
         }
      }
      //System.out.println("BIGGEST PROBE " + bigProbe);
      int[] probeCount = new int[bigProbe + 1];
      for(int i = 0; i < histy.size(); i++) {
         probeCount[histy.get(i)]++;
      }
      
      System.out.println("Histogram of Probes: " + Arrays.toString(probeCount));
      double fill = ((double)myBuckets) / myCapacity;
      System.out.println("Fill Percentage: " + fill);
      System.out.println("Max Linear Probe: " + bigProbe);
      System.out.println("Average Linear Probe: " + averageProbe);
   
   }
   private int hash(K key) {
      int hash = key.hashCode();
      if( hash < 0) { 
               hash = hash % (myCapacity);
               hash += myCapacity;
            }
            hash = hash % (myCapacity);
      return hash;
   }
      public String toString() {
            StringBuilder temp = new StringBuilder();
            temp.append("{");
            int i = 0;
            int bucketCount = 0;
            for( i = 0; i < myCapacity; i++) {
               if(myTable.get(i) != null) {
                  if(bucketCount == 0) {
                        temp.append(myTable.get(i).myKey);
                        temp.append("=");
                        temp.append(myTable.get(i).myValue);
                  } else {
                     temp.append(", ");
                     temp.append(myTable.get(i).myKey);
                     temp.append("=");
                     temp.append(myTable.get(i).myValue);
                  }
                bucketCount++;
               }
            }
            temp.append("}");      
            return temp.toString();
      }
      public int bernsteinHash(K key) {
            int hash = 0;
            int i;
            String s = (String) key;
            for (i = 0; i < s.length(); ++i)
                  hash = 33 * hash + s.charAt(i);
            if( hash < 0) { 
               hash = hash % (myCapacity);
               hash += myCapacity;
            }
            hash = hash % (myCapacity);
            return hash;
      }

      public int joaat_hash(K k) {
            int hash = 0;
            int i = 0;
            byte[] key = k.toString().getBytes();
            while (i != key.length) {
                  hash += (key[i++] & 0xFF);
                  hash += (hash << 0xA);
                  hash ^= (hash >>> 0x6);
            }
            hash += (hash << 0x3);
            hash ^= (hash >>> 0xB);
            hash += (hash << 0xF);
            if( hash < 0) { 
               hash = hash % (myCapacity);
               hash += myCapacity;
            }
            hash = hash % (myCapacity);
            return hash;
      }

      public int djb2(K key) {
            String word = String.valueOf(key);
            int hash = 0;
            for (int i = 0; i < word.length(); i++) {
                  hash = word.charAt(i) + ((hash << 5) - hash);
            }
            if( hash < 0) { 
               hash = hash % (myCapacity);
               hash += myCapacity;
            }
            hash = hash % (myCapacity);
            return hash;
      }

      public int FNVHash1(K key) {
            String data = key.toString();
            final int p = 0x1000193;
            int hash = -0x7EE3623B;
            for (int i = 0; i < data.length(); i++) hash = (hash ^ data.charAt(i)) * p;
            hash ^= hash << 13;
            hash += hash >> 7;
            hash ^= hash << 3;
            hash += hash >> 17;
            hash ^= hash << 5;
            if( hash < 0) { 
               hash = hash % (myCapacity);
               hash += myCapacity;
            }
            hash = hash % (myCapacity);
            return hash;
      }

      private ArrayList<Integer> noRepeatShuffleList(int size) {
            ArrayList<Integer> arr = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                  arr.add(i);
            }
            Collections.shuffle(arr);
            return arr;
      }
      // OOOHHH make a private class for Key values. Each key can have multiple values
      // mapped to it,
      // so each key object needs to keep track of what its pointing to
      // the tostring should print like the map normally does
      /**
       * This is a private class that keeps track
       * of which key is associated to the piece of data
       */
      
      private class ValueData {
            /**
             * The value stored in the table
             */
            V myValue;
            /**
             * The key stored in the table
             */
            K myKey;
            
            /**
             * The constructor for a data node.
             * @param theValue The value to be assigned
             * @param theKey The key to be assigned
             */
            ValueData(V theValue,K theKey) {
               myValue = theValue;
               myKey = theKey;
            }
            
      }      
}
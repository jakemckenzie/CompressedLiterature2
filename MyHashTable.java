import java.util.*;

/**
 * This is a Hash Table class. It stores data as an object with both its key and
 * the data in the object. The table is typically navigated with hash codes to
 * pooint us to where we need to be in the hash table.
 * 
 * @author Bruce Baker and Jake McKenzie
 */
public class MyHashTable<K, V> {
      /**
       * @param myEntryCount This is the amount of entries in the hash table
       */
      int myEntryCount;
      /**
       * @param myBuckets This is the amount of occupied buckets in the hash table
       */
      int myBuckets;
      /**
       * @param myCapacity This is the amount of total buckets in the hash table
       */
      int myCapacity;
      /**
       * @param myTable This is the hash table
       */
      
      ArrayList<ValueData> myTable;
      /**
       * @param probingCount the count of the probes
       */
      int probingCount;
      /**
       * @param largestProbe the largest probe 
       */
      int largestProbe;
      /**
       * @param squaresLookup the first 25 squares used as a lookup
       */
      int[] squaresLookup = {1, 4, 9, 16, 25, 36, 49, 64, 81, 100, 121, 144, 169, 196, 225, 256, 
                           289, 324, 361, 400, 441, 484, 529, 576, 625, 676, 729, 784, 841, 900, 
                           961, 1024, 1089, 1156, 1225, 1296, 1369, 1444, 1521, 1600, 1681, 
                          1764, 1849, 1936, 2025, 2116, 2209, 2304, 2401, 2500, 2601, 2704,
                          2809, 2916, 3025, 3136, 3249, 3364, 3481, 3600, 3721, 3844, 3969,
                          4096, 4225, 4356, 4489, 4624, 4761, 4900, 5041, 5184, 5329, 5476,
                          5625, 5776, 5929, 6084, 6241, 6400, 6561, 6724, 6889, 7056, 7225,
                          7396, 7569, 7744, 7921, 8100, 8281, 8464, 8649, 8836, 9025, 9216,
                          9409, 9604, 9801, 10000};
      /**
       * @param histogram histogram for printing
       */
      public ArrayList<Integer> histogram;
      /**
       * The constructor for a hash table. initializes capacity amount of spaces to
       * null in an arraylist
       * 
       * @param capacity The size of the table
       */
      public MyHashTable(int capacity) {
            myTable = new ArrayList<ValueData>(Collections.nCopies(capacity, null));
            histogram = new ArrayList<Integer>(Collections.nCopies(capacity, 0));
            myEntryCount = 0;
            myBuckets = 0;
            probingCount = 0;
            largestProbe = 0;
            myCapacity = capacity;
      }

      /**
       * Places a value into the table based on the hashcode of the key
       * 
       * @param searchKey the key of the object
       * @param newValue  the value to be associated
       */
      void put(K searchKey, V newValue) {
            //int t = bernsteinHash(searchKey);
            //int t = joaat_hash(searchKey);
            int t = Fowler_Noll_Vo_hash(searchKey);           
            //int t = hash(searchKey);
            int pos = (t < 0) ? (t % myCapacity) + myCapacity : t % myCapacity;
            int cycle = 0, i = 0, j = 0;
            while (cycle < myCapacity && myTable.get(pos) != null) {
                  if (myTable.get(pos).myKey.equals(searchKey)) {
                        break;
                  }
                  pos = (pos + 1) % myCapacity;
                  //pos = quadraticProbe(pos,i,j);
                  i++;
                  j++;
                  pos = absHash(pos);
                  probingCount++;
                  cycle++;
            }
            
            myBuckets++;
            largestProbe = (largestProbe < probingCount) ? probingCount : largestProbe;
            histogram.set(probingCount,1+histogram.get(probingCount));
            myTable.set(pos, new ValueData(newValue, searchKey));
            probingCount = 0;
      }
      /**
       * Gets a value from the table based on the hashcode of the key
       * 
       * @param searchKey the key of the object
       */
      public V get(K searchKey) {
            //int t = bernsteinHash(searchKey);
            //int t = joaat_hash(searchKey);
            int t = Fowler_Noll_Vo_hash(searchKey);
            //int t = hash(searchKey);
            int pos = (t < 0) ? (t % myCapacity) + myCapacity : t % myCapacity;
            int cycle = 0, i = 1, j = 1;
            V temp;
            while (cycle < myCapacity) {
                  if (myTable.get(pos).myKey.equals(searchKey)) {
                        break;
                  } else {
                        pos = (pos + 1) % myCapacity;
                        //pos = quadraticProbe(pos,i,j);
                        i++;
                        j++;
                        pos = absHash(pos);
                  }
                  cycle++;
            }
            temp = myTable.get(pos).myValue;
            return temp;
      }

      /**
       * Looks for a key in the table, if it is present on the table, we return true
       * 
       * @param searchKey the key to be found
       * @return the boolean to whether the key was found
       */

      public boolean containsKey(K searchKey) {
            //int t = bernsteinHash(searchKey);
            //int t = joaat_hash(searchKey);
            int t = Fowler_Noll_Vo_hash(searchKey);
            //int t = hash(searchKey);
            int pos = (t < 0) ? (t % myCapacity) + myCapacity : t % myCapacity;
            int cycle = 0;
            int i = 0, j = 0;
            boolean found = false;
            while (cycle < myCapacity) {
                  if (myTable.get(pos) == null) {
                        break;
                  } else if (myTable.get(pos).myKey.equals(searchKey)) {
                        found = true;
                        break;
                  } else {
                        pos = (pos + 1) % myCapacity;
                        //pos = quadraticProbe(pos,i,j);
                        i++;
                        j++;
                        pos = absHash(pos);
                  }
                  cycle++;
            }
            return found;
      }

      public int quadraticProbe(int pos, int i, int j) {
            return (i > 100) ? (pos + (int)Math.pow(i,j)) % myCapacity : (pos + squaresLookup[i - 1]) % myCapacity;
      }
      /**
        * Returns a hashset of all key values
        * @return all keys in the table
        */
      public HashSet<K> keySet() {
            HashSet<K> temp = new HashSet<K>();
            for (int i = 0; i < myCapacity; i++) if (myTable.get(i) != null) temp.add(myTable.get(i).myKey);
            return temp;
      }
      /**
        * Prints the statistics on our hashing and probing methods
        * to the console.
        */
      public void stats() {
            System.out.println("Number of Entries: " + (myBuckets));
            System.out.println("Number of Buckets : " + myCapacity);
            int[] probeCount = new int[largestProbe + 1];
            for (int i = 0; i < largestProbe + 1; i++) probeCount[i] = histogram.get(i);     
            System.out.println("Histogram of Probes: " + Arrays.toString(probeCount));
            double fill = ((double) myBuckets) / myCapacity;
            System.out.println("Fill Percentage: " + fill);
            System.out.println("Max Probe: " + largestProbe);
            long sum = 0;
            for (int i = 1; i < probeCount.length;i++) sum += i*probeCount[i];
            System.out.println("Average Probe: " + ((double)sum) / myBuckets);
      }
      /**
       * @param key key for hashing
       */
      private int hash(K key) {
            int hash = key.hashCode();
            return absHash(hash);
      }

      public String toString() {
            StringBuilder temp = new StringBuilder();
            temp.append("[");
            int i = 0;
            int bucketCount = 0;
            for (i = 0; i < myCapacity - 1; i++) {
                  
                  if (myTable.get(i) != null) {
                        temp.append("(");
                        if (bucketCount == 0) {
                              temp.append(myTable.get(i).myKey);
                              temp.append(", ");
                              temp.append(myTable.get(i).myValue);
                        } else {
                              
                              temp.append(myTable.get(i).myKey);
                              temp.append(", ");
                              temp.append(myTable.get(i).myValue);
                        }
                        temp.append(")");
                        temp.append(", ");
                        bucketCount++;
                  }
                  
            }
            temp.deleteCharAt(temp.lastIndexOf(","));
            temp.deleteCharAt(temp.lastIndexOf(" "));
            temp.append("]");
            return temp.toString();
      }

      /**
       * "Dan Bernstein created this algorithm and posted it in a newsgroup.
       *  It is known by many as the Chris Torek hash because Chris went a 
       * long way toward popularizing it. Since then it has been used 
       * successfully by many, but despite that the algorithm itself is 
       * not very sound when it comes to avalanche and permutation of the internal state. It has proven very good for small character keys, where it can outperform algorithms that result in a more random distribution:"
       * http://www.eternallyconfuzzled.com/tuts/algorithms/jsw_tut_hashing.aspx
       */
      public int bernsteinHash(K key) {
            int i;
            int hash = 0x1505;
            String s = (String) key;
            for (i = 0; i < s.length(); ++i) hash = 33 * hash + s.charAt(i);
            hash &= 0x7fffffff;
            return absHash(hash);
      }

      /**
       * 
       * https://en.wikipedia.org/wiki/Jenkins_hash_function#one_at_a_time
       */
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
            return absHash(hash);
      }

      /**
       * https://en.wikipedia.org/wiki/Fowler%E2%80%93Noll%E2%80%93Vo_hash_function
       */
      public int Fowler_Noll_Vo_hash(K key) {
            String data = key.toString();
            final int p = 0x1000193;
            int hash = -0x7EE3623B;
            for (int i = 0; i < data.length(); i++) hash = (hash ^ data.charAt(i)) * p;
            hash ^= hash << 13;
            hash += hash >> 7;
            hash ^= hash << 3;
            hash += hash >> 13;
            hash ^= hash << 5;
            
            // hash += hash << 13;
            // hash ^= hash >> 19;
            // hash ^= hash >> 17;
            // hash += hash << 11;


            return absHash(hash);
      }
      /**
       * Used for testing purposes.
       */
      private ArrayList<Integer> noRepeatShuffleList(int size) {
            ArrayList<Integer> arr = new ArrayList<>(size);
            for (int i = 0; i < size; i++) arr.add(i);
            Collections.shuffle(arr);
            return arr;
      }

      public int absHash(int hash) {
            return hash = ((hash < 0) ? (hash % (myCapacity)) + myCapacity : hash) % myCapacity;
      }
      /**
       * This is a private class that keeps track
       * of which key is associated to the piece of data
       */
      private class ValueData {
            /**
             * @param myValue The value stored in the table
             */
            V myValue;
            /**
             * @param myKey The key stored in the table
             */
            K myKey;
            /**
             * The constructor for a data node.
             * @param theValue The value to be assigned
             * @param theKey The key to be assigned
             */
            ValueData(V theValue, K theKey) {
                  myValue = theValue;
                  myKey = theKey;
            }

      }
}
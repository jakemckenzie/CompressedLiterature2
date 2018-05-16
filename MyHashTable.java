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
       * The constructor for a hash table. initializes capacity amount of spaces to
       * null in an arraylist
       * 
       * @param capacity The size of the table
       */
      public MyHashTable(int capacity) {
            myTable = new ArrayList<ValueData>(Collections.nCopies(capacity, null));
            myEntryCount = 0;
            myBuckets = 0;
            myCapacity = capacity;
      }

      /**
       * Places a value into the table based on the hashcode of the key
       * 
       * @param searchKey the key of the object
       * @param newValue  the value to be associated
       */
      void put(K searchKey, V newValue) {
            // int t = bernsteinHash(searchKey);
            // int t = joaat_hash(searchKey);
            // int t = djb2(searchKey);
            int t = Fowler_Noll_Vo_hash(searchKey);
            int pos = (t < 0) ? (t % myCapacity) + myCapacity : t % myCapacity;
            int cycle = 0, i = 0, j = 0;
            while (cycle < myCapacity && myTable.get(pos) != null) {
                  if (myTable.get(pos).myKey.equals(searchKey)) {
                        break;
                  }
                  pos = (pos + (int) Math.pow(++i, ++j)) % myCapacity;
                  pos = absHash(pos);
                  cycle++;
            }
            myBuckets++;
            myTable.set(pos, new ValueData(newValue, searchKey));
      }

      public V get(K searchKey) {
            // int t = bernsteinHash(searchKey);
            // int t = joaat_hash(searchKey);
            // int t = djb2(searchKey);
            int t = Fowler_Noll_Vo_hash(searchKey);
            int pos = (t < 0) ? (t % myCapacity) + myCapacity : t % myCapacity;
            int cycle = 0, i = 0, j = 0;
            V temp;
            while (cycle < myCapacity) {
                  if (myTable.get(pos).myKey.equals(searchKey)) {
                        break;
                  } else {
                        pos = (pos + (int) Math.pow(++i, ++j)) % myCapacity;
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
            // int t = bernsteinHash(searchKey);
            // int t = joaat_hash(searchKey);
            // int t = djb2(searchKey);
            int t = Fowler_Noll_Vo_hash(searchKey);
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
                        pos = (pos + (int) Math.pow(++i, ++j)) % myCapacity;
                        pos = absHash(pos);
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
            ArrayList<Integer> histy = new ArrayList<Integer>(myCapacity);
            int bigProbe = 1;
            double averageProbe = 0;
            for (int i = 0; i < myCapacity; i++) {
                  if (myTable.get(i) != null) {
                        int temp = i - (Fowler_Noll_Vo_hash(myTable.get(i).myKey));
                        temp = (temp < 0) ? temp + myCapacity : temp;
                        bigProbe = (temp > bigProbe) ? temp : bigProbe;
                        averageProbe += ((double) temp) / myBuckets;
                        histy.add(temp);
                  }
            }
            int[] probeCount = new int[bigProbe + 1];
            for (int i = 0; i < histy.size(); i++)
                  probeCount[histy.get(i)]++;
            System.out.println("Histogram of Probes: " + Arrays.toString(probeCount));
            double fill = ((double) myBuckets) / myCapacity;
            System.out.println("Fill Percentage: " + fill);
            System.out.println("Max Linear Probe: " + bigProbe);
            System.out.println("Average Linear Probe: " + averageProbe);

      }

      private int hash(K key) {
            int hash = key.hashCode();
            return absHash(hash);
      }

      public String toString() {
            StringBuilder temp = new StringBuilder();
            temp.append("{");
            int i = 0;
            int bucketCount = 0;
            for (i = 0; i < myCapacity; i++) {
                  if (myTable.get(i) != null) {
                        if (bucketCount == 0) {
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

      /**
       * http://www.eternallyconfuzzled.com/tuts/algorithms/jsw_tut_hashing.aspx
       */
      public int bernsteinHash(K key) {
            int hash = 0;
            int i;
            String s = (String) key;
            for (i = 0; i < s.length(); ++i)
                  hash = 33 * hash + s.charAt(i);
            return absHash(hash);
      }

      /**
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
            // hash = ((hash < 0) ? (hash % (myCapacity)) + myCapacity : hash) % myCapacity;
            return absHash(hash);
      }

      /**
       * http://www.cse.yorku.ca/~oz/hash.html
       */

      public int djb2(K key) {
            String word = String.valueOf(key);
            int hash = 0;
            for (int i = 0; i < word.length(); i++)
                  hash = word.charAt(i) + ((hash << 5) - hash);
            // hash = ((hash < 0) ? (hash % (myCapacity)) + myCapacity : hash) % myCapacity;
            return absHash(hash);
      }

      /**
       * https://en.wikipedia.org/wiki/Fowler%E2%80%93Noll%E2%80%93Vo_hash_function
       */
      public int Fowler_Noll_Vo_hash(K key) {
            String data = key.toString();
            final int p = 0x1000193;
            int hash = -0x7EE3623B;
            for (int i = 0; i < data.length(); i++)
                  hash = (hash ^ data.charAt(i)) * p;
            hash ^= hash << 0xD;
            hash += hash >> 0x7;
            hash ^= hash << 0x3;
            hash += hash >> 0xD;
            hash ^= hash << 0x5;
            return absHash(hash);
      }

      private ArrayList<Integer> noRepeatShuffleList(int size) {
            ArrayList<Integer> arr = new ArrayList<>(size);
            for (int i = 0; i < size; i++)
                  arr.add(i);
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
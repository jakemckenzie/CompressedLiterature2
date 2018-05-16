import java.util.*;

// cd C:\Users\Epimetheus\Documents\GitHub\CompressedLiterature2
// javac *.java -Xlint
public class MyHashTable<K, V> {
      int myEntryCount;
      int myBuckets;
      int myCapacity;
      ArrayList<ValueData> myTable;

      public MyHashTable(int capacity) {
            myTable = new ArrayList<ValueData>(Collections.nCopies(capacity, null));
            myEntryCount = 0;
            myBuckets = 0;
            myCapacity = capacity;
      }

      void put(K searchKey, V newValue) {
            //int t = bernsteinHash(searchKey);
            //int t = joaat_hash(searchKey);
            //int t = djb2(searchKey);
            int t = Fowler_Noll_Vo_hash(searchKey);
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
                  t = Fowler_Noll_Vo_hash(searchKey);
                  break;
            }
            return t;
      }

      public V get(K searchKey) {
            //int t = bernsteinHash(searchKey);
            //int t = joaat_hash(searchKey);
            //int t = djb2(searchKey);
            int t = Fowler_Noll_Vo_hash(searchKey);
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

      
      public boolean containsKey(K searchKey) {
            //int t = bernsteinHash(searchKey);
            //int t = joaat_hash(searchKey);
            //int t = djb2(searchKey);
            int t = Fowler_Noll_Vo_hash(searchKey);
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
   public HashSet<K> keySet() {
      HashSet<K> temp = new HashSet<K>();
      for(int i = 0; i < myCapacity; i++) {
         if(myTable.get(i) != null) {
            temp.add(myTable.get(i).myKey);
         }
      }
      return temp;
   }
   
   
   void stats() {
      System.out.println( "Number of Entries: " + (myBuckets));
      System.out.println( "Number of Buckets : " + myCapacity);
      ArrayList<Integer> histy = new ArrayList<Integer>(myCapacity);
      int bigProbe = 1;
      double averageProbe = 0;
      for(int i = 0; i < myCapacity; i++) {
         if(myTable.get(i) != null) {
            int temp = i - (Fowler_Noll_Vo_hash(myTable.get(i).myKey));
            temp = (temp < 0) ? temp + myCapacity : temp;
            bigProbe = (temp > bigProbe) ? temp : bigProbe;
            averageProbe += ((double) temp) / myBuckets;
            histy.add(temp);
         }
      }
      int[] probeCount = new int[bigProbe + 1];
      for(int i = 0; i < histy.size(); i++) probeCount[histy.get(i)]++;
      System.out.println("Histogram of Probes: " + Arrays.toString(probeCount));
      double fill = ((double)myBuckets) / myCapacity;
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
      /**
       * http://www.eternallyconfuzzled.com/tuts/algorithms/jsw_tut_hashing.aspx
       */
      public int bernsteinHash(K key) {
            int hash = 0;
            int i;
            String s = (String) key;
            for (i = 0; i < s.length(); ++i) hash = 33 * hash + s.charAt(i);
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
            //hash = ((hash < 0) ? (hash % (myCapacity)) +  myCapacity : hash) % myCapacity;
            return absHash(hash);
      }
      /**
       * http://www.cse.yorku.ca/~oz/hash.html
       */
      
      public int djb2(K key) {
            String word = String.valueOf(key);
            int hash = 0;
            for (int i = 0; i < word.length(); i++) hash = word.charAt(i) + ((hash << 5) - hash);
            //hash = ((hash < 0) ? (hash % (myCapacity)) +  myCapacity : hash) % myCapacity;
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
            hash ^= hash << 0xD;
            hash += hash >> 0x7;
            hash ^= hash << 0x3;
            hash += hash >> 0x11;
            hash ^= hash << 0x5;
            return absHash(hash);
      }

      private ArrayList<Integer> noRepeatShuffleList(int size) {
            ArrayList<Integer> arr = new ArrayList<>(size);
            for (int i = 0; i < size; i++) arr.add(i);
            Collections.shuffle(arr);
            return arr;
      }

      public int absHash(int hash) {
            return hash = ((hash < 0) ? (hash % (myCapacity)) +  myCapacity : hash) % myCapacity;
      }
      // OOOHHH make a private class for Key values. Each key can have multiple values
      // mapped to it,
      // so each key object needs to keep track of what its pointing to
      // the tostring should print like the map normally does

      
      private class ValueData {
            V myValue;
            K myKey;
            
            ValueData(V theValue,K theKey) {
               myValue = theValue;
               myKey = theKey;
            }
            
      }      
}
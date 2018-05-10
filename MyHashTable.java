import java.util.*;
// cd C:\Users\Epimetheus\Documents\Data Structures\Assignment4
// javac *.java -Xlint
public class MyHashTable<K, V> {
      int myEntryCount;
      int myBuckets;
      int myCapacity;
      ArrayList<keyData> myKeys;
      ArrayList<V> myValues;

      public MyHashTable(int capacity) {
            myBuckets = capacity;
            myKeys = new ArrayList<keyData>(Collections.nCopies(capacity, null));
            myValues = new ArrayList<V>(Collections.nCopies(capacity, null));
            myEntryCount = 0;
            myBuckets = 0;
            myCapacity = capacity - 1;
      }

      // public void put(K searchKey, V newValue) {
      //       int t = bernsteinHash(searchKey);
      //       int pos = (t < 0) ? (t % myCapacity) + myCapacity : t % myCapacity;
      //       int cycle = 0;
      //       while (cycle < myCapacity && myValues.get(pos) != null) {
      //             pos = (pos + 1) % myCapacity;
      //             cycle++;
      //       }
      //       if (myBuckets == 0 || !containsKey(searchKey)) {
      //             myKeys.set(myBuckets, new keyData(searchKey, pos));
      //             myBuckets++;
      //       }
      //       myValues.set(pos, newValue);
      // }

      public void put(K searchKey, V newValue) {
            //int t = bernsteinHash(searchKey);
            //int t = joaat_hash(searchKey);
            //int t = djb2(searchKey);
            int t = FNVHash1(searchKey);
            int pos = (t < 0) ? (t % myCapacity) + myCapacity : t % myCapacity;
            int cycle = 0;
               
               if(!containsKey(searchKey)) {
                  while( cycle < myCapacity && myValues.get(pos) != null) {
                     pos = (pos + 1)%myCapacity;
                     cycle++;
                  }
                  myKeys.set(myBuckets, new keyData(searchKey, pos));
                  myBuckets++;
                  myValues.set(pos, newValue);  
               }else {
                  //this part needs work
                  myValues.set(getKeyData(searchKey).myValue,  newValue);
               }
                
         }

      public V get(K searchKey) {
            // int t = bernsteinHash(searchKey);
            // int pos = (t < 0) ? (t % myCapacity) + myCapacity : t % myCapacity;
            
            // int cycle = 0;
            keyData myNode = null;
            V temp;
            label:
            for (int i = 0; i < myBuckets; i++) {
                  if ((myKeys.get(i).myKey).equals(searchKey) || myBuckets == 0) {
                        myNode = myKeys.get(i);
                        break label;
                  }
            }
            temp = myValues.get(myNode.myValue);
            return temp;
      }

      public boolean containsKey(K searchKey) {
            boolean flag = false;
            label:
            for (int i = 0; i < myKeys.size(); i++) {
                  if (myKeys.get(i) != null && myKeys.get(i).equals(searchKey)) {
                        flag = true;
                        break label;
                  }
            }
            return flag;
      }

      public String toString() {
            StringBuilder temp = new StringBuilder();
            int i = 0;
            temp.append("{");
            temp.append(myKeys.get(i).myKey.toString());
            temp.append("=");
            temp.append(myValues.get(myKeys.get(i).myValue));
            for (i = 1; i < myBuckets; i++) {
                  temp.append(", ");
                  temp.append(myKeys.get(i).myKey.toString());
                  temp.append("=");
                  temp.append(myValues.get(myKeys.get(i).myValue));
            }
            temp.append("}");
            return temp.toString();
      }

      public int bernsteinHash(K key) {
            int hash = 0;
            int i;
            String s = (String) key;
            for (i = 0; i < s.length(); ++i) hash = 33 * hash + s.charAt(i);
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
            return hash;
        }
      public int djb2(K key) {
            String word = key.toString();
            int hash = 0;
            for (int i = 0; i < word.length(); i++) {
                hash = word.charAt(i) + ((hash << 5) - hash);
            }
            return hash;
      }
      public int FNVHash1(K key) { 
            String data = key.toString();
            final int p = 16777619; 
            int hash = (int) 2166136261L; 
            for (int i = 0; i < data.length(); i++) hash = (hash ^ data.charAt(i)) * p; 
            hash += hash << 13; 
            hash ^= hash >> 7; 
            hash += hash << 3; 
            hash ^= hash >> 17; 
            hash += hash << 5; 
            return hash; 
      }

      private keyData getKeyData(K searchKey) {
            keyData temp = null;
            ArrayList<Integer> random = noRepeatShuffleList(myBuckets);
            label:
            for (int i = 0; i < myBuckets; i++) {
                  if (myKeys.get(random.get(i)).myKey.equals(searchKey)) {
                        temp = myKeys.get(i);
                        break label;
                  }
            }

            return temp;
      }

      public ArrayList<Integer> noRepeatShuffleList(int size) {
            ArrayList<Integer> arr = new ArrayList<>();
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

      private class keyData {
            K myKey;
            int myValue;

            keyData(K theKey) {
                  myKey = theKey;
            }

            keyData(K theKey, int theValue) {
                  myKey = theKey;
                  myValue = theValue;
            }

            public void setValue(int theValue) {
                  myValue = theValue;
            }
      }
}
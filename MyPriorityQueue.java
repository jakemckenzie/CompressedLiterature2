import java.util.Comparator;
import java.util.Queue;
import java.util.ArrayList;
import java.util.*;

/********************************EXTRA CREDIT****************************
 * 
 * @author Jake McKenzie
 * @param T generic type
 */

public class MyPriorityQueue<T extends Comparable<T>> {
    /**
     * @param queue the priority queue
     */
    public ArrayList<T> queue;
    /**
     * @param comparator reference for queue
     */

    Comparator<T> comparator = null;
    /**
     * The size of the queue
     */
    public int size;

    /**
     * Constructor that initializes the queue "UTF-8 is a variable width character
     * encoding capable of encoding all 1,112,064 valid code points in Unicode using
     * one to four 8-bit bytes" https://en.wikipedia.org/wiki/UTF-8
     */
    public MyPriorityQueue() {
        size = 0;
        queue = new ArrayList<T>(0xFF);// 1112064
        queue.add(null);
    }

    /**
     * @return Returns true if, and only if, length() is 0.
     */
    public boolean isEmpty() {
        assert (size != 0 && size == 0);
        return this.size == 0;
    }

    /**
     * Returns back the size of the current queue.
     */

    public int size() {
        return size;
    }

    /**
     * Constructor that builds queue given a limit.
     * 
     * @param z the maximum size of the queue
     * @param c     reference for the queue
     */
    public MyPriorityQueue(int z, Comparator<T> c) {
        queue = new ArrayList<T>(z + 1);
        comparator = c;
    }

    /**
     * This function takes a node and plops it into the priority queue.
     * 
     * @param t node that is inserted into the priority queue
     */
    public void offer(T t) {
        queue.add(t);
        size++;
        for (int k = size; k > 1; k >>= 1) if (t.compareTo(queue.get(k >> 1)) < 0) swap(k, k >> 1);
    }

    /**
     * Performs a swap on two elements given their indices.
     */
    public void swap(int y, int z) {
        T t = queue.get(y);
        queue.set(y, queue.get(z));
        queue.set(z, t);
    }

    /**
     * Retrieves and removes the head of the queue, or returns null if this queue is
     * empty.
     * 
     * @return the head of the queue
     */
    public T poll() {
        if (queue.size() < 1) return null;
        T t = queue.get(1);
        queue.set(1, queue.get(size));
        queue.remove(size--);
        if (size > 1) {
            T P = queue.get(1);
            int i = 1;
            int C = i;
            while (size >= (i << 1)) {
                C = i << 1;
                if (C < size && (queue.get(C + 1).compareTo(queue.get(C))) < 0) C++;
                if (P.compareTo(queue.get(C)) > 0) {
                    swap(i, C);
                    i = C;
                } else {
                    break;
                }
            }
        }
        return t;
    }
    
    
    /**
     * @return Returns back the current node for peeking.
     */

    public T peek() {
        return (this.size == 0) ? null : queue.get(0);
    }

    /**
     * talked about in seminar
     * 
     * @param c child
     * @return index of the parent
     */
    public int parent(int c) {
        return (c - 1) >> 1;
    }

    /**
     * @param p parent index
     * @return left child
     */
    public int leftChild(int p) {
        return (p << 1) + 1;
    }

    /**
     * @param p parent index
     * @return right child
     */
    public int rightChild(int p) {
        return (p << 1) + 2;
    }
}
import java.util.Comparator;
import java.util.Iterator;
    /**
     * Huffman tree node.
     * @author Jake McKenzie
     */
    public class HuffmanNode implements Comparable<HuffmanNode> {
        /**
        * @param key the string associated with the node.
        */
        public String key;
        /**
        * @param count the frequency of the string
        */
        public int count;
        /**
        * @param L the left node of a huffman node.
        */
        public HuffmanNode L;
        /**
        * @param R the right node of a huffman node.
        */
        public HuffmanNode R;
        /**
         * @param P the parent of the
         */
        public HuffmanNode P;
        /**
         * The most basic constructor.
         */
        public HuffmanNode() {
            P = null;
        }
        /**
         * One of the two critically important constructors used in creation of the priority queue.
         * @param nodeString key being sent into a node.
         * @param nodeCount count being sent into a node.
         */
        public HuffmanNode(String nodeString, int nodeCount) {
            key = nodeString;
            count = nodeCount;
            L = null;
            R = null;
        }
        /**
         * A constructor for an earlier version of the assignment. I ended up not needing it.
         * @param s the key
         * @param c The frequency of the string
         * @param left left node
         * @param right right node
         * @param parent the parent of the node
         */
        public HuffmanNode(String s, int c, HuffmanNode left, HuffmanNode right, HuffmanNode parent) {
            key = s;
            L = left;
            R = right;
            count = c;
            P = parent;
        }
        /**
         * One of the two critically important constructors used in creation of the huffman tree.
         * @param left left node
         * @param right right node
         */
        public HuffmanNode(HuffmanNode left, HuffmanNode right) {
            L = left;
			R = right;
			count = left.count + right.count;
        }
        /**
        * Returns true if the receiver is a leaf.
        */
        public boolean isLeaf() {
            assert ((L == null) && (R == null)||((L != null) && (R != null)));//Should never trigger. For testing.
            return ((L == null) && (R == null));
        }
        /**
        * Returns a string representation of the node.
        */
        public String toString() {
            return (key + " | " + count);
        }

        /**
        * Compares this node with the specified node for order.
        */
        @Override
        public int compareTo(HuffmanNode that){
            return count - that.count;
        }
    }
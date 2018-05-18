import java.util.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.*;
import java.lang.*;

/**
 * This is a joint project between Jake McKenzie and Bruce Baker.
 * @author Jake McKenzie and Bruce Baker
 */

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
        System.out.println("Runtime: " + (b - a) + " milliseconds");
        int sum = 0;
        for (String i : c.strings) sum+= i.getBytes().length;
        System.out.println("Average byte length: " + ((double)sum)/c.strings.size());
        
        

        /**
         * (1) Please comment out to test decode
         */
        
        //Files.write(Paths.get(decompressed),c.decoded.getBytes());

        double compressed = Files.size(Paths.get("compressed.txt"));
        double targetCompressed = Files.size(Paths.get("targetCompressed.txt"));
        double difference = (targetCompressed - compressed);
        
        System.out.println("Compressed file size: " +  compressed / 1024 + " kilobytes");
        System.out.println("Target compressed file size: " +  targetCompressed / 1024  + " kilobytes");
        System.out.println("Difference in compressed file sizes of my file vs the target: " + difference  + " bytes");


        
        //Files.write(Paths.get(decompressed),decodes.decoded.getBytes());
        /**
         * (2) Please comment out these functions to run tests
         */
        //testMyHashTable();
        //testCodingTree();
        runDecode();
    }

    public static void testMyHashTable() {
        MyHashTable<String, String> temp = new MyHashTable<String, String>(1 << 15);
        temp.put("yes", "please");
        temp.put("No", "Bugs");
        System.out.println(temp.get("yes") + temp.get("No"));
        temp.put("yes", "What do you mean");
        System.out.println(temp);
    }

    public static void testCodingTree() throws IOException{
        String message1 = new String(Files.readAllBytes(Paths.get("Conan_Beyond_the_Black_River.txt")));
        CodingTree testCodingTree1 = new CodingTree(message1);
        System.out.println(testCodingTree1.codes.toString());

    }

    public static void runDecode() throws IOException{
        long y = System.currentTimeMillis();
        byte[] bytes = Files.readAllBytes(Paths.get("compressed.txt"));
        byte[] smallerBytes = new byte[bytes.length - 2];
        for (int i = 2; i < bytes.length;i++) smallerBytes[i-2] = bytes[i];
        BitSet bits = BitSet.valueOf(bytes);
        //http://www.exampledepot.8waytrips.com/egs/java.util/Bits2Array.html
        //for (int i = 0; i < bytes.length * 8; i++) if ((bytes[bytes.length - i / 8 - 1] & (1 << (i % 8))) > 0) bits.set(i);
        //https://stackoverflow.com/questions/38490760/how-to-print-bitset-as-series-of-bits
        StringBuilder s = new StringBuilder();
        for( int i = 0; i < bits.length();  i++) s.append(bits.get(i) == true ? 1 : 0);
        String codesString = new String(Files.readAllBytes(Paths.get("codes.txt")));
        //Files.write(Paths.get(decompressed),decodes.decoded.getBytes());
        codesString = codesString.substring(1, codesString.length()-1);
        String[] cds = codesString.split("([\\Q, \\E])+");
        String key = "", value = "";
        MyHashTable<String,String> decodes = new MyHashTable<String,String>(1 << 15);
        for (int i = 0; i < cds.length - 1; i++)  {
            if (i % 2 == 0) {
                key = cds[i].substring(1, cds[i].length());
            } else if (i % 2 == 1) {
                value = cds[i].substring(0, cds[i].length()-1);
                decodes.put(key,value);
            }
        }
        CodingTree cTree4decoding = new CodingTree(s.toString(),decodes);
        Files.write(Paths.get(decompressed),cTree4decoding.decoded.getBytes());
        long z = System.currentTimeMillis();

        System.out.println();
        System.out.println(z - y + " milliseconds");
    }

}
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.BitSet;


public class BloomFilter {
    private final BitSet bitSet;
    private final int size;
    private final String[] hashAlgorithms;

    public BloomFilter(int size, String... hashAlgorithms) {
        this.size = size;
        this.bitSet = new BitSet(size);
        this.hashAlgorithms = hashAlgorithms;
    }

    // add: runs the word through each hash function to get indexes, and turns the corresponding bits on
    public void add(String value) {
        for (String algorithm : hashAlgorithms) {
            bitSet.set(getBitIndex(value, algorithm));
        }
    }

    // contains: runs the word through each hash function to get indexes, and checks if these bits are on.
    // If any bit is off, it's certain that the word could not have been added before.
    public boolean contains(String value) {
        for (String algorithm : hashAlgorithms) {
            if (!bitSet.get(getBitIndex(value, algorithm))) {
                return false; // one of the bits is off, so the value is definitely not in the set
            }
        }
        return true; // all bits are on, so the value might be in the set
    }


  @Override
  public String toString() {
      StringBuilder sb = new StringBuilder();
      int lastSetBit = bitSet.length();  // find the last bit that was set
      for (int i = 0; i < lastSetBit; i++) {
          sb.append(bitSet.get(i) ? "1" : "0");
      }
      return sb.toString();
  }

    // getBitIndex: creates a hash of the word using the given algorithm and returns an index in the array
    private int getBitIndex(String value, String algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] digest = md.digest(value.getBytes());
            BigInteger bigInteger = new BigInteger(1, digest);
            return Math.abs(bigInteger.intValue()) % size;
        } catch (Exception e) {
            throw new RuntimeException("Failed to hash value: " + value, e);
        }
    }

}


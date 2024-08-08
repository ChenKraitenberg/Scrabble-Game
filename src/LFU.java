import java.util.*;

class LFU implements CacheReplacementPolicy {
    private final Map<String, Integer> accessCountMap;
    private final int capacity;

    public LFU() {
        this.accessCountMap = new HashMap<>();
        this.capacity = Integer.MAX_VALUE; // Set a default capacity
    }

    public LFU(int capacity) {
        this.accessCountMap = new HashMap<>();
        this.capacity = capacity;
    }

    @Override
    public void add(String word) {
        if (accessCountMap.size() >= capacity) {
            remove();
        }
        accessCountMap.put(word, accessCountMap.getOrDefault(word, 0) + 1);
    }

    @Override
    public String remove() {
        int minCount = Integer.MAX_VALUE;
        String victim = null;
        for (Map.Entry<String, Integer> entry : accessCountMap.entrySet()) {
            if (entry.getValue() < minCount) {
                minCount = entry.getValue();
                victim = entry.getKey();
            }
        }
        accessCountMap.remove(victim);
        return victim;
    }
}
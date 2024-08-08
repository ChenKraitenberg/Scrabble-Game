import java.util.HashMap;
import java.lang.*;
import java.util.Map;


class LRU implements CacheReplacementPolicy {
    private final Map<String, Long> accessTimeMap;
    private long currentTime;
    private final int capacity;

    public LRU() {
        this.accessTimeMap = new HashMap<>();
        this.currentTime = 0;
        this.capacity = Integer.MAX_VALUE; // Set a default capacity
    }
    public LRU(int capacity) {
        this.accessTimeMap = new HashMap<>();
        this.currentTime = 0;
        this.capacity = capacity;
    }

    @Override
    public void add(String word) {
        if (accessTimeMap.size() >= capacity) {
            remove();
        }
        accessTimeMap.put(word, currentTime++);
    }

    @Override
    public String remove() {
        long oldestTime = Long.MAX_VALUE;
        String victim = null;
        for (Map.Entry<String, Long> entry : accessTimeMap.entrySet()) {
            if (entry.getValue() < oldestTime) {
                oldestTime = entry.getValue();
                victim = entry.getKey();
            }
        }
        accessTimeMap.remove(victim);
        return victim;
    }
}


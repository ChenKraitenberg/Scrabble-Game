import java.util.LinkedList;
import java.util.List;

public class CacheManager {
    private final int maxSize;
    private final CacheReplacementPolicy cacheReplacementPolicy;
    private final List<String> cache;

    public CacheManager(int maxSize, CacheReplacementPolicy cacheReplacementPolicy) {
        this.maxSize = maxSize;
        this.cacheReplacementPolicy = cacheReplacementPolicy;
        this.cache = new LinkedList<>();
    }

    public boolean query(String item) {
        boolean found = cache.contains(item);
        if (found) {
            markAsRecentlyUsed(item);
        }
        return found;
    }

    public void add(String item) {
        if (cache.size() >= maxSize) {
            String victim = cacheReplacementPolicy.remove();
            cache.remove(victim);
        }
        cache.add(item);
        cacheReplacementPolicy.add(item);
    }

    private void markAsRecentlyUsed(String item) {
        cache.remove(item);
        cache.add(item);
    }

}
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Dictionary {
    private final CacheManager existingWordsCache;
    private final CacheManager nonExistingWordsCache;
    private final BloomFilter bloomFilter;

    public Dictionary(String... fileNames) {
        existingWordsCache = new CacheManager(400, new LRU());
        nonExistingWordsCache = new CacheManager(100, new LFU());
        bloomFilter = new BloomFilter(256, "MD5", "SHA-1");

        for (String fileName : fileNames) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(fileName));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] words = line.split("\\s+");
                    for (String word : words) {
                        bloomFilter.add(word);
                    }
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

public boolean query(String word) {
    if (existingWordsCache.query(word)) {
        return true;
    } else if (nonExistingWordsCache.query(word)) {
        return false;
    } else {
        boolean isInBloomFilter = bloomFilter.contains(word);
        if (isInBloomFilter) {
            existingWordsCache.add(word);
        } else {
            nonExistingWordsCache.add(word);
        }
        return isInBloomFilter;
    }
}

    public boolean challenge(String lazy) {
        return query(lazy);
    }

}

import java.util.HashMap;
import java.util.Map;

public class DictionaryManager {

    // Singleton instance
    private static final DictionaryManager INSTANCE = new DictionaryManager();

    // Private constructor to prevent instantiation
    private DictionaryManager() {}

    // Map to store dictionaries for each book
    private Map<String, Dictionary> map = new HashMap<>();


    // Method to query a word in the dictionaries of specified books
    public boolean query(String... args) {
        boolean result = false;
        // Extract the word to query
        String word = args[args.length - 1]; //args[args.length - 1], because the last argument in args is the word we query
        for (int i=0;i<args.length-1;i++) {
            String bookName = args[i];

            // If the dictionary for the book doesn't exist, create a new one
            if (!map.containsKey(bookName)) {
                Dictionary newDictionary = new Dictionary(bookName);
                map.put(bookName, newDictionary);
            }
            // Query the word in the dictionary of the book
            if (map.get(bookName).query(word))
                result = true;
        }
        return result;
    }

    // Method to challenge a word in the dictionaries of specified books
    public boolean challenge(String... args) {
        boolean result = false;
        // Extract the word to challenge
        String word = args[args.length - 1]; //args[args.length - 1], because the last argument in args is the word we query
        // Iterate over the book names provided as arguments
        for (int i=0;i<args.length-1;i++) {
            String bookName = args[i];
            // If the dictionary for the book doesn't exist, create a new one
            if (!map.containsKey(bookName)) {
                Dictionary newDictionary = new Dictionary(bookName);
                map.put(bookName, newDictionary);
            }
            // Challenge the word in the dictionary of the book
            if (map.get(bookName).challenge(word))
                result = true;
        }
        return result;
    }

    // Method to get the size of the dictionary manager (num of dictionaries)
    public int getSize() {return map.size();}


    // Method to get the singleton instance of DictionaryManager
    public static DictionaryManager get() {
        return INSTANCE;
    }

}

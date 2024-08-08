import java.util.Arrays;
import java.util.Objects;

public class Tile {
    public final char letter;
    public final int score;

    private Tile(char letter, int score) {
        this.letter = letter;
        this.score = score;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return letter == tile.letter && score == tile.score;
    }

    @Override
    public int hashCode() {
        return Objects.hash(letter, score);
    }

    public char getLetter() {
        return this.letter;
    }


    // The inner class Bag:
    public static class Bag {
        private static final int AMOUNT = 26;
        private static final int[] INITIAL_QUANTITIES = new int[] {9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1};
        private static Bag bag = null;
        private final int[] quantities = Arrays.copyOf(INITIAL_QUANTITIES, AMOUNT);
        private final Tile[] tiles = new Tile[AMOUNT];

        private Bag() {
            initTiles();
        }

        private void initTiles() {
            char[] letters = generateLetters();
            int[] scores = {1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10};

            for (int i = 0; i < tiles.length; i++) {
                tiles[i] = new Tile(letters[i], scores[i]);
            }
        }
        private char[] generateLetters() {
            char[] letters = new char[AMOUNT];
            for (int i = 0; i < AMOUNT; i++) {
                letters[i] = (char) ('A' + i);
            }
            return letters;
        }
        public static Bag getBag() {
            if (bag == null) {
                bag = new Bag();
            }
            return bag;
        }

        public Tile getRand() {
            while (true) {
                int idx = (int) (Math.random() * tiles.length);
                if (quantities[idx] > 0) {
                    quantities[idx]--;
                    return tiles[idx];
                }
            }
        }

        public Tile getTile(char letter) {
            int j = 0;
            for (Tile i : tiles) {
                if (i.letter == letter) {
                    if (quantities[j] > 0) {
                        quantities[j]--;
                        return tiles[j];

                    }
                }
                j++;
            }
            return null;
        }


            public void put (Tile tile)
            {
                int i = tile.letter - 'A';
                if (quantities[i] < this.initialQuantities()[i]) {
                    quantities[i]++;
                }
            }

        private int[] initialQuantities () {
            return Arrays.copyOf(INITIAL_QUANTITIES, AMOUNT);
        }

            public int size ()
            {
                int total = 0;
                for (int quantity : quantities) {
                    total += quantity;
                }
                return total;
            }

            public int[] getQuantities ()
            {
                return quantities.clone();
            }

        }
    }

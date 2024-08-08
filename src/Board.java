import java.util.ArrayList;

public class Board {
    private static final int BOARD_SIZE = 15;
    private static Board instance= null;
    private final Tile[][] board;

    public Board() {
        this.board = new Tile[BOARD_SIZE][BOARD_SIZE];
    }

    int turn = 0;

    public static Board getBoard() {
        if (instance == null) {
            instance = new Board();
        }

        return instance;
    }

    public Tile[][] getTiles() {
        Tile[][] copyBoard = new Tile[15][15];
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                if (board[i][j]==null) {
                    copyBoard[i][j]=null;
                }
                else copyBoard[i][j] = board[i][j];
            }
        }
        return copyBoard;
    }

    public boolean isFirstWord(Word word)
    {
        Tile[] tiles = word.getTiles();
        for (int i = 0; i < BOARD_SIZE; i++)
        {
            for (int j = 0; j < BOARD_SIZE; j++)
            {
                if (board[i][j] != null)
                {
                    boolean found = false;
                    for (Tile tile : tiles)
                    {
                        if (tile != null && board[i][j].getLetter() == tile.getLetter()) {  // Use getter method to access `letter`
                            found = true;
                            break;
                        }
                    }
                    if (!found)
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public char whichLetterLeaning (Word word) {

        Tile[] tiles = word.getTiles();
        int row = word.getRow();
        int col = word.getCol();
        boolean vertical = word.isVertical();

        for (int i = 0; i < tiles.length; i++) {
            if (tiles[i] == null) {
                int currentRow = vertical ? row + i : row;
                int currentCol = vertical ? col : col + i;
                if (currentRow >= 0 && currentRow < board.length &&
                        currentCol >= 0 && currentCol < board[0].length &&
                        board[currentRow][currentCol] != null) {
                    return board[currentRow][currentCol].getLetter();
                }
            }
        }
        // If no leaning letter is found, return a default character, such as '_'
        return '_';
    }

    public boolean boardLegal(Word word) {

        int row = word.getRow();
        int col = word.getCol();
        Tile[] wTiles = word.getTiles();

        // Check if the start position is within the bounds of the board.
        if (row < 0 || row >= board.length || col < 0 || col >= board[0].length) return false;

        // Check that the word is placed within the bounds of the board.
        if (word.isVertical() && (row + wTiles.length > board.length)) return false;
        if (!word.isVertical() && (col + wTiles.length > board[0].length)) return false;

        // Check for the first turn that the word crosses the center tile.
        if (turn == 0) {
            return (word.isVertical() && col == 7 && row <= 7 && (row + wTiles.length - 1) >= 7) ||
                    (!word.isVertical() && row == 7 && col <= 7 && (col + wTiles.length - 1) >= 7);
        }

        return true;
    }

    public boolean dictionaryLegal(Word word) {
        return true;
    }

    private ArrayList<Word> findNewWords(int row, int col, boolean vertical) {
        ArrayList<Word> words = new ArrayList<>();
        int start = vertical ? row : col;
        int end = start;
        int constant = vertical ? col : row;
        int maxIndex = vertical ? board.length : board[0].length;

        while (start > 0 && board[vertical ? start - 1 : constant][vertical ? constant : start - 1] != null) {
            start--;
        }

        while (end < maxIndex && board[vertical ? end : constant][vertical ? constant : end] != null) {
            end++;
        }

        if (end - start > 1) {
            Tile[] newWordTiles = new Tile[end - start];
            for (int k = start; k < end; k++) {
                newWordTiles[k - start] = board[vertical ? k : constant][vertical ? constant : k];
            }
            words.add(new Word(newWordTiles, start, constant, vertical));
        }

        return words;
    }

    public ArrayList<Word> getWords(Word word) {
        if (!boardLegal(word)) return new ArrayList<>();
        int row = word.getRow();
        int col = word.getCol();
        Tile[] wTiles = word.getTiles();

        ArrayList<Word> newWords = new ArrayList<>();

        for (int k = 0; k < wTiles.length; k++) {
            int i = row + (word.isVertical() ? k : 0);
            int j = col + (word.isVertical() ? 0 : k);

            if (board[i][j] == null) {
                board[i][j] = wTiles[k];
                if (word.isVertical()) {
                    newWords.addAll(findNewWords(i, j, false));
                } else {
                    newWords.addAll(findNewWords(i, j, true));
                }
            }
        }
        newWords.add(word);
        return newWords;

    }


    private int getLettersScore(char letter) {
        switch (letter) {
            case 'A':
            case 'E':
            case 'I':
            case 'O':
            case 'U':
            case 'L':
            case 'N':
            case 'S':
            case 'T':
            case 'R':
                return 1;
            case 'D':
            case 'G':
                return 2;
            case 'B':
            case 'C':
            case 'M':
            case 'P':
                return 3;
            case 'F':
            case 'H':
            case 'V':
            case 'W':
            case 'Y':
                return 4;
            case 'K':
                return 5;
            case 'J':
            case 'X':
                return 8;
            case 'Q':
            case 'Z':
                return 10;

            default:
                return 0;
        }
    }

    private static final int[][] LETTER_MULTIPLIERS = {
            // 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10,11,12,13,14
            { 1, 1, 1, 2, 1, 1, 1, 1, 1 ,1 ,1, 2, 1, 1, 1 },
            { 1, 1, 1, 1, 1, 3, 1, 1, 1 ,3 ,1, 1, 1, 1, 1 },
            { 1, 1, 1, 1, 1, 1, 2, 1, 2 ,1 ,1, 1, 1, 1, 1 },
            { 2, 1, 1, 1, 1, 1, 1, 2, 1 ,1 ,1, 1, 1, 1, 2 },
            { 1, 1, 1, 1, 1, 1, 1, 1, 1 ,1 ,1, 1, 1, 1, 1 },
            { 1, 3, 1, 1, 1, 3, 1, 1, 1 ,3 ,1, 1, 1, 3, 1 },
            { 1, 1, 2, 1, 1, 1, 2, 1, 2 ,1 ,1, 1, 2, 1, 1 },
            { 1, 1, 1, 2, 1, 1, 1, 1, 1 ,1 ,1, 2, 1, 1, 1 },
            { 1, 1, 2, 1, 1, 1, 2, 1, 2 ,1 ,1, 1, 2, 1, 1 },
            { 1, 3, 1, 1, 1, 3, 1, 1, 1 ,3 ,1, 1, 1, 3, 1 },
            { 1, 1, 1, 1, 1, 1, 1, 1, 1 ,1 ,1, 1, 1, 1, 1 },
            { 2, 1, 1, 1, 1, 1, 1, 2, 1 ,1 ,1, 1, 1, 1, 2 },
            { 1, 1, 1, 1, 1, 1, 2, 1, 2 ,1 ,1, 1, 1, 1, 1 },
            { 1, 1, 1, 1, 1, 3, 1, 1, 1 ,3 ,1, 1, 1, 1, 1 },
            { 1, 1, 1, 2, 1, 1, 1, 1, 1 ,1 ,1, 2, 1, 1, 1 },

    };

    private static final int[][] WORD_MULTIPLIER = {
            // 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10,11,12,13,14
            {3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3 },
            {1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1 },
            {1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1 },
            {1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1 },
            {1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1 },
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
            {3, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 3 },
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
            {1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 2, 1, 1, 1, 1 },
            {1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 1 },
            {1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1 },
            {1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1 },
            {3, 1, 1, 1, 1, 1, 1, 3, 1, 1, 1, 1, 1, 1, 3 },

    };

    private int getScore(Word word) {
        int sumScoreLetters = 0;
        int sumScoreWord;
        int multiWord =1;
        Tile[] tiles = word.getTiles();
        int wordLength = tiles.length;


        for (int i = 0; i < wordLength; i++) {
            int row = word.getRow();
            int col = word.getCol();
            int currentRow, currentCol;
            if (word.isVertical()) {
                currentRow = row + i;
                currentCol = col;
            }

            else {
                currentRow = row;
                currentCol = col + i;
            }

            int letterMultiplier = LETTER_MULTIPLIERS[currentRow][currentCol];
            int wordMultiplier = WORD_MULTIPLIER[currentRow][currentCol];
            if (tiles[i] != null&&tiles[i].getLetter()!='_') {
                char letter = tiles[i].getLetter();
                int tileScore = getLettersScore(letter);

                sumScoreLetters += tileScore * letterMultiplier;

            }

            if (!isFirstWord(word)) {
                if (currentRow ==7 && currentCol == 7) {
                    wordMultiplier =1;
                }

            }

            if (tiles[i]== null) {
                char letter =whichLetterLeaning(word);
                int tileScore = getLettersScore(letter);

                sumScoreLetters += tileScore * letterMultiplier;
                if (!isFirstWord(word)) {
                    wordMultiplier =1;
                }
            }

            if (wordMultiplier != 1) {
                multiWord = wordMultiplier;

            }

        }
        sumScoreWord = sumScoreLetters*multiWord;

        return sumScoreWord;
    }


    public void placeWordOnBoard(Word word) {
        Tile[] tiles = word.getTiles();
        int row = word.getRow();
        int col = word.getCol();
        boolean vertical = word.isVertical();

        for (int i = 0; i < tiles.length; i++) {
            if (tiles[i] != null) {
                int currentRow = vertical ? row + i : row;
                int currentCol = vertical ? col : col + i;
                board[currentRow][currentCol] = tiles[i];
            }
        }
    }


    public int tryPlaceWord(Word word) {
        if (!boardLegal(word)) {
            return 0;
        }
        // Temporarily place the word to find and score new words
        ArrayList<Word> words = getWords(word);
        int score = 0;

        for (Word w : words) {
            if (!dictionaryLegal(w)) {
                return 0;  // Abort if any word formed is not in the dictionary
            }
            score += getScore(w);
            placeWordOnBoard(w);  // Update the board state with the newly placed word
        }

        turn++;
        return score;
    }

}

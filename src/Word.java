import java.util.Arrays;
import java.util.Objects;
public class Word {
    private final Tile[] tiles;;
    private final int row;
    private final int col;
    private final boolean vertical;

    public Word(Tile[] tiles, int row, int col, boolean vertical) {
        this.tiles = tiles;
        this.row = row;
        this.col = col;
        this.vertical = vertical;
    }

    public Tile[] getTiles() {return tiles;}

    public int getCol() {return col;}

    public int getRow() {return row;}

    public boolean isVertical() {return vertical;}


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;

        else {
            Word someWord = (Word) o;
            return this.col == someWord.getCol() &&
                    this.row == someWord.getRow() &&
                    this.vertical == someWord.isVertical() &&
                    Arrays.equals(tiles, someWord.tiles);
        }
    }

    public int hashCode() {
        int result = Objects.hash(row, col, vertical);
        result = 31 * result + Arrays.hashCode(tiles);
        return result;
    }

}
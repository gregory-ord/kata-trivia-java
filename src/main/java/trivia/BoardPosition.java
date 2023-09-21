package trivia;

/**
 *
 */
public class BoardPosition {

    public final int position;
    public final Category.CategoryName categoryName;

    public BoardPosition( int position, Category.CategoryName categoryName ) {
        this.position = position;
        this.categoryName = categoryName;
    }
}

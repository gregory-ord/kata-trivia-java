package trivia;

public class Player {
    public boolean inPenaltyBox;
    public int numCoins;
    public int positionOnBoard;
    public int number;
    public final String name;

    public Player(String name, int number) {
        inPenaltyBox = false;
        numCoins = 0;
        positionOnBoard = 0;
        this.number = number;
        this.name = name;
    }

    public void move(int roll) {
        positionOnBoard += roll;
        positionOnBoard %= 12;

        System.out.println(name + "'s new location is " + positionOnBoard);
    }
}

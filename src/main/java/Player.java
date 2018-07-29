import java.util.Scanner;

public class Player {
    public int playerHealth;
    public int playerMana;
    public int playerDeck;
    public int playerRune;

    public int hand;

    public Player() {
        playerHealth = 30;
        playerMana = 0;
        playerDeck = 30;
        playerRune = 0;
    }

    public void update(Scanner in)
    {
        int playerHealth = in.nextInt();
        int playerMana = in.nextInt();
        int playerDeck = in.nextInt();
        int playerRune = in.nextInt();
    }
}

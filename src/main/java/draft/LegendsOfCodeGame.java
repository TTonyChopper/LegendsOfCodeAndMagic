package draft;

import java.util.*;

public class LegendsOfCodeGame {

    public Player player;
    public Player opponent;

    public int opponentHand;
    public int cardCount;

    public List<Action> currentActions = new ArrayList<>();

    public void play() {
        Scanner in = new Scanner(System.in);

        player = new Player();
        opponent = new Player();

        // game loop
        while (true) {
            initTurn(in, player, opponent);
            int turnCount = 0;
            if ( turnCount < 30 )
            {
                playDraft();
            }
            else
            {
                playCombat();
            }
        }
    }

    public void initTurn(Scanner in, Player player, Player opponent) {

        player.update(in);
        opponent.update(in);

        opponentHand = in.nextInt();
        cardCount = in.nextInt();

        for (int i = 0; i < cardCount; i++) {
            int cardNumber = in.nextInt();
            int instanceId = in.nextInt();
            int location = in.nextInt();
            int cardType = in.nextInt();
            int cost = in.nextInt();
            int attack = in.nextInt();
            int defense = in.nextInt();
            String abilities = in.next();
            int myHealthChange = in.nextInt();
            int opponentHealthChange = in.nextInt();
            int cardDraw = in.nextInt();
        }
    }

    public void playDraft() {
        System.out.println("PASS");
    }

    public void playCombat() {
        System.out.println("PASS");
    }

    public void sendActions(){
        StringBuffer sb = new StringBuffer();
        for (Action a : currentActions)
        {
            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");
            sb.append(a.prepareSend());
        }
        System.out.println(sb.toString());
    }
}

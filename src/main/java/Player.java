import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Player {
    public static void main(String... args) {
        new LegendsOfCodeGame().play();
    }
}

abstract class Card {
    public DraftInfo draftInfo;

    int cardNumber;
    int instanceId;
    int cardType;

    int cost;
    int attack;
    int defense;

    String abilities;
    int myHealthChange;
    int opponentHealthChange;
    int cardDraw;

    public Card(){}

    Card read(Scanner in, int turnNumber)
    {
        this.cardNumber = in.nextInt();
        this.instanceId = in.nextInt();
        this.draftInfo = new DraftInfo(in.nextInt(), turnNumber);
        this.cardType = in.nextInt();
        this.cost = in.nextInt();
        this.attack = in.nextInt();
        this.defense = in.nextInt();
        this.abilities = in.next();
        this.myHealthChange = in.nextInt();
        this.opponentHealthChange = in.nextInt();
        this.cardDraw = in.nextInt();
        return this;
    }
}

enum TYPE{ATTACKANT, DEFENSIVE, ROUNDED}

class Creature extends Card {
    int efficiency;
    TYPE cardType;

    void analyse()
    {

    }
}

class Action
{
    public static final String ACTION_SEPARATOR = ";";

    enum BASE{
        PICK, SUMMON, ATTACK_CREATURE, ATTACK_PLAYER, PASS
    }

    final BASE base;

    final DraftInfo draftInfo;

    final int[] ids;
    final String comment;

    Action(BASE base, String comment, DraftInfo draftInfo) {

        this.base = base;
        this.draftInfo = draftInfo;

        this.ids = null;
        this.comment = comment;
    }

    Action(BASE base, String comment, int... ids) {

        this.base = base;
        this.draftInfo = null;

        this.ids = ids;
        this.comment = comment;
    }

    public String prepareSend() {
        return toString() + " " + comment + ACTION_SEPARATOR;
    }

    public String toString() {
        switch (base)
        {
            case PICK:
                return "PICK " + draftInfo.positionNumber;
            case SUMMON:
                return "SUMMON " + ids[0];
            case ATTACK_CREATURE:
                return "ATTACK " + ids[0] + " " + ids[1];
            case ATTACK_PLAYER:
                return "ATTACK " + ids[0] + " -1";
            case PASS:
                return "PASS";
            default:
                return "PASS YOLO";
        }
    }

    Action print() {
        System.err.println(toString());
        return this;
    }
}

class PlayerInfo {
    public int playerHealth;
    public int playerMana;
    public int playerDeck;
    public int playerRune;

    public int hand;

    public PlayerInfo() {
        playerHealth = 30;
        playerMana = 0;
        playerDeck = 30;
        playerRune = 0;
    }

    public void update(Scanner in) {
        this.playerHealth = in.nextInt();
        this.playerMana = in.nextInt();
        this.playerDeck = in.nextInt();
        this.playerRune = in.nextInt();
    }
}

class DraftInfo {
    public final int turnNumber;
    public final int positionNumber;

    public DraftInfo(int turnNumber, int positionNumber) {
        this.turnNumber = turnNumber;
        this.positionNumber = positionNumber;
    }
}

class LegendsOfCodeGame {

    public PlayerInfo player;
    public PlayerInfo opponent;

    public int opponentHand;
    public int cardCount;

    public List<Action> currentActions = new ArrayList<>();

    public void play() {
        Scanner in = new Scanner(System.in);

        player = new PlayerInfo();
        opponent = new PlayerInfo();

        int turnCount = 0;

        // game loop
        while (true) {
            initTurn(turnCount, in, player, opponent);

            if (turnCount++ < 30) {
                playDraft();
            } else {
                playCombat();
            }
        }
    }

    public void initTurn(int turnNumber, Scanner in, PlayerInfo player, PlayerInfo opponent) {

        player.update(in);
        opponent.update(in);

        opponentHand = in.nextInt();
        cardCount = in.nextInt();

        for (int i = 0; i < cardCount; i++) {
            Card current = new Creature().read(in, turnNumber);
        }
    }

    public void playDraft() {
        System.out.println("PASS");
    }

    public void playCombat() {
        System.out.println("PASS yolo");
    }

    public void sendActions() {
        StringBuffer sb = new StringBuffer();
        for (Action a : currentActions) {
            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");
            sb.append(a.prepareSend());
        }
        System.out.println(sb.toString());
    }
}

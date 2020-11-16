import java.util.*;

class Player {
    public static void main(String... args) {
        new LegendsOfCodeGame().play();
    }
}

class P{
    static void p(String s) {
        System.err.println(s);
    }
}

abstract class Card {

    boolean isPlayed = false;

    DraftInfo draftInfo;

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

    public Card() {
    }

    Card read(Scanner in, int turnNumber) {
        this.cardNumber = in.nextInt();
        this.instanceId = in.nextInt();
        this.draftInfo = new DraftInfo(turnNumber, in.nextInt());
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

    public String toString() {
        return cardNumber + ":A" + attack + ":D" + defense + ":C" + cost;
    }
}

enum TYPE {ATTACKANT, DEFENSIVE, ROUNDED}

class Creature extends Card {
    int efficiency;
    int playedEfficiency;

    TYPE cardType;

    void analyse() {

    }
}

class Action {
    public static final String ACTION_SEPARATOR = ";";

    enum BASE {
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
        switch (base) {
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

class MyPlayerInfo extends PlayerInfo {
    Map<String, Card> deck = new HashMap<>(30);

    Map<Integer, Card> hand = new HashMap<>();

}

class PlayerInfo {
    public int playerHealth;
    public int playerMana;
    public int playerDeck;
    public int playerRune;

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

    public Card[] draftPicks = new Card[3];

    public List<Action> currentActions = new ArrayList<>();

    public String currentComment = "";

    public Map<Integer, Card> myHand = new TreeMap<>(Collections.reverseOrder());
    public List<Card> myBoard = new ArrayList<>();
    public List<Card> otherBoard = new ArrayList<>();

    void initTurnDraft(int turnNumber, Scanner in, PlayerInfo player, PlayerInfo opponent) {
        initTurn(turnNumber, in, player, opponent);
        for (int i = 0; i < cardCount; i++) {
            Card current = new Creature().read(in, turnNumber);

            draftPicks[i] = current;
            currentComment += "$" + current.cardNumber;
        }
    }

    void initTurnCombat(int turnNumber, Scanner in, PlayerInfo player, PlayerInfo opponent) {
        initTurn(turnNumber, in, player, opponent);
        for (int i = 0; i < cardCount; i++) {
            P.p("before");
            Card current = new Creature().read(in, turnNumber);
            P.p("after");
            P.p(current.toString());
            P.p(""+current.draftInfo.positionNumber);
            if (current.draftInfo.positionNumber == 0)
            {
                P.p(current.toString());
                //My hand
                myHand.put(current.cost, current);
            }else if (current.draftInfo.positionNumber == 1)
            {
                //My board
                myBoard.add(current);
            }else if (current.draftInfo.positionNumber == -1)
            {
                //Other board
                otherBoard.add(current);
            }
        }
    }

    void initTurn(int turnNumber, Scanner in, PlayerInfo player, PlayerInfo opponent) {
        player.update(in);
        opponent.update(in);

        opponentHand = in.nextInt();
        cardCount = in.nextInt();
    }

    public void playDraft() {
        System.out.println("PASS " + currentComment);
        currentComment = "";
    }

    public void playCards() {
        boolean handOver = false;
        while(player.playerMana > 0 && !handOver) {
            int initMana = player.playerMana;
            for(Card card : myHand.values())
            {
                P.p("debug "+card.cost);
                P.p("debug2 "+player.playerMana);
                if (card.cost <= player.playerMana)
                {
                    P.p("debugd ");
                    currentComment = currentComment + "summoned: " + card.cardNumber;
                    currentActions.add(new Action(Action.BASE.SUMMON, currentComment, card.cardNumber));
                    card.isPlayed = true;
                    myHand.remove(card);
                    player.playerMana -= card.cost;
                }
            }
            if (player.playerMana == initMana)
            {
                handOver = true;
            }
            P.p("debug3 "+handOver);
        }
    }

    public void playCombat() {
        for (Card card : myBoard) {
            currentActions.add(new Action(Action.BASE.ATTACK_PLAYER, currentComment, card.cardNumber));
        }
    }

    public void playCombatTurn() {

        playCards();

        playCombat();

        sendActions();
    }

    public void play() {
        Scanner in = new Scanner(System.in);

        player = new PlayerInfo();
        opponent = new PlayerInfo();

        int turnCount = 0;

        // game loop
        while (true) {
            if (turnCount++ < 30) {
                initTurnDraft(turnCount, in, player, opponent);
                playDraft();
            } else {
                initTurnCombat(turnCount, in, player, opponent);
                playCombatTurn();
            }
        }
    }

    public void sendActions() {
        P.p("nbActions " + currentActions.size());
        StringBuffer sb = new StringBuffer();
        for (Action a : currentActions) {
            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");
            sb.append(a.prepareSend());
        }
        System.out.println(sb.toString());
    }
}

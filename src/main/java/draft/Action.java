package draft;

class Action {
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
            case SUMMON:
                return "SUMMON " + ids[0];
            case ATTACK_CREATURE:
                return "ATTACK " + ids[0] + " " + ids[1];
            case ATTACK_PLAYER:
                return "ATTACK " + ids[0] + " -1";
            case PASS:
                return "PASS";
            default:
                return "";
        }
    }

    Action print() {
        System.err.println(toString());
        return this;
    }
}
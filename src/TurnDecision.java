public enum TurnDecision {
    LEFT, STRAIGHT, RIGHT;

    public static TurnDecision random() {
        int r = (int) (Math.random() * 3);
        return TurnDecision.values()[r];
    }
}